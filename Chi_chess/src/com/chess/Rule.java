package com.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rule {
	private static final int maxX = 9;
	private static final int maxY = 10;
	public static final int MOVE_OK = 0;
	
	/**
	 * @param chessBoard 棋盘
	 * @param selectedChess 选中的棋子
	 * @param moveToPosi 需要移动到的位置
	 * 克隆棋盘形成虚拟的棋盘，之后的所有操作不会影响原棋盘
	 */
	public static int moveStatus(List<Chess> chessBoard, Chess selectedChess, Integer[] moveToPosi){
		
		int status = Action.STATUS_OUTRULE;
		//检测移动位置是否合规
		List<Integer[]> posi = getPosition(chessBoard, selectedChess);
		for (Integer[] coor : posi) {
			if(Arrays.equals(coor, moveToPosi)){
				status = MOVE_OK;
				break;
			}
		}
		
		//检测移动后是否被将军
		if(status == MOVE_OK){
			if(byGeneral(chessBoard, selectedChess, moveToPosi)){
				status = Action.STATUS_GENERAL;
			}
		}
		return status;
	}
	
	/**
	 * @param chessBoard
	 * @param side
	 * @return 判断是否获胜
	 */
	public static boolean isWin(List<Chess> chessBoard, int side){
		boolean isWin = true;
		
		for (Chess chess : chessBoard){
			if(chess.getSide() != side){
				List<Integer[]> posi = getPosition(chessBoard, chess);
				for (Integer[] coor : posi) {
					if(!byGeneral(chessBoard, chess, coor)){
						isWin = false;
					}
				}
			}
		}
		
		return isWin;
	}
	
	/**
	 * @param chessBoard
	 * @param selectedChess
	 * @param moveToPosi
	 * @return 判断移动下一步是否“被”将军
	 */
	private static boolean byGeneral(List<Chess> chessBoard, Chess selectedChess, Integer[] moveToPosi){
		
		List<Chess> cb = new ArrayList<Chess>();
		for (Chess chess : chessBoard) {
			cb.add(chess.clone());
		}
		
		Chess sc = Util.getChessByPosi(cb, new Integer[]{selectedChess.getX(), selectedChess.getY()});
		Chess cs = Util.getChessByPosi(cb, moveToPosi);
		if(cs != null){
			if(cs.getType() == Chess.TYPE_JIANG){
				return false;
			}
			cb.remove(cs);
		}
		
		sc.setX(moveToPosi[0]);
		sc.setY(moveToPosi[1]);
		
		int side = selectedChess.getSide() == Chess.RED?Chess.BLACK:Chess.RED;
		
		return isGeneral(cb, side);
	}
	

	public static boolean isGeneral(List<Chess> cb, int side) {
		
		Chess j1 = null;
		Chess j2 = null;
		for (Chess chess : cb) {
			if(chess.getSide() == side && chess.getType() == Chess.TYPE_JIANG){
				j1 = chess;
				break;
			}
		}
		
		for (Chess chess : cb) {
			if(chess.getSide() != side && chess.getType() == Chess.TYPE_JIANG){
				j2 = chess;
				break;
			}
		}
		
		/*if(j1 == null){
			return true;
		}
		
		if(j2 == null){
			return false;
		}*/
		
		//判断是否两将是否直接照面
		if(j1.getX() == j2.getX()){
			boolean inLine = false;
			Integer min = null;
			Integer max = null;
			
			if(j1.getY() > j2.getY()){
				max = j1.getY();
				min = j2.getY();
			}else{
				max = j2.getY();
				min = j1.getY();
			}
			
			for (int i = min + 1; i < max; i++) {
				Chess cs = Util.getChessByPosi(cb, new Integer[]{j1.getX(), i});
				if(cs != null){
					inLine = false;
					break;
				}else{
					inLine = true;
				}
			}
			
			if(inLine){
				return true;
			}
		}
		
		
		//判断是否有棋子正在将军
		for (Chess chess : cb) {
			if(chess.getSide() == side){
				List<Integer[]> posi = getPosition(cb, chess);
				for (Integer[] coor : posi) {
					if(Arrays.equals(coor, new Integer[]{j2.getX(), j2.getY()})){
						return true;
					}
				}
			}
		}
		return false;
	}

	public static List<Integer[]> getPosition(List<Chess> chessBoard, Chess selChess) {
		List<Chess> cb = new ArrayList<Chess>();
		for (Chess chess : chessBoard) {
			cb.add(chess.clone());
		}
		Chess sc = selChess.clone();
		
		int type = selChess.getType();
		switch (type) {
		case Chess.TYPE_BING:
			return bing(cb, sc);
		case Chess.TYPE_PAO:
			return pao(cb, sc);
		case Chess.TYPE_MA:
			return ma(cb, sc);
		case Chess.TYPE_JU:
			return ju(cb, sc);
		case Chess.TYPE_XIANG:
			return xiang(cb, sc);
		case Chess.TYPE_SHI:
			return shi(cb, sc);
		case Chess.TYPE_JIANG:
			return jiang(cb, sc);
		default:
			break;
		}
		return new ArrayList<>();
	}

	private static List<Integer[]> jiang(List<Chess> cb, Chess sc) {
		
		List<Integer[]> posi = new ArrayList<>();
		posiCheck(posi, new Integer[]{sc.getX(), sc.getY() - 1}, cb, sc, true);
		posiCheck(posi, new Integer[]{sc.getX(), sc.getY() + 1}, cb, sc, true);
		posiCheck(posi, new Integer[]{sc.getX() + 1, sc.getY()}, cb, sc, true);
		posiCheck(posi, new Integer[]{sc.getX() - 1, sc.getY()}, cb, sc, true);
		
		return posi;
	}


	private static List<Integer[]> shi(List<Chess> cb, Chess sc) {
		List<Integer[]> posi = new ArrayList<>();
		posiCheck(posi, new Integer[]{sc.getX() + 1, sc.getY() - 1}, cb, sc, true);
		posiCheck(posi, new Integer[]{sc.getX() + 1, sc.getY() + 1}, cb, sc, true);
		posiCheck(posi, new Integer[]{sc.getX() - 1, sc.getY() + 1}, cb, sc, true);
		posiCheck(posi, new Integer[]{sc.getX() - 1, sc.getY() - 1}, cb, sc, true);
		
		return posi;
	}


	private static List<Integer[]> xiang(List<Chess> cb, Chess sc) {
		List<Integer[]> posi = new ArrayList<>();
		if(Util.getChessByPosi(cb, new Integer[]{sc.getX() + 1, sc.getY() - 1}) == null){
			if((sc.getSide() == Util.playerSide && sc.getY() - 2 > 4) || 
					(sc.getSide() != Util.playerSide && sc.getY() - 2 < 5)){
				posiCheck(posi, new Integer[]{sc.getX() + 2, sc.getY() - 2}, cb, sc);
			}
		}
		
		if(Util.getChessByPosi(cb, new Integer[]{sc.getX() + 1, sc.getY() + 1}) == null){
			if((sc.getSide() == Util.playerSide && sc.getY() + 2 > 4) || 
					(sc.getSide() != Util.playerSide && sc.getY() + 2 < 5)){
				posiCheck(posi, new Integer[]{sc.getX() + 2, sc.getY() + 2}, cb, sc);
			}
		}
		
		if(Util.getChessByPosi(cb, new Integer[]{sc.getX() - 1, sc.getY() + 1}) == null){
			if((sc.getSide() == Util.playerSide && sc.getY() + 2 > 4) || 
					(sc.getSide() != Util.playerSide && sc.getY() + 2 < 5)){
				posiCheck(posi, new Integer[]{sc.getX() - 2, sc.getY() + 2}, cb, sc);
			}
		}
		
		if(Util.getChessByPosi(cb, new Integer[]{sc.getX() - 1, sc.getY() - 1}) == null){
			if((sc.getSide() == Util.playerSide && sc.getY() - 2 > 4) || 
					(sc.getSide() != Util.playerSide && sc.getY() - 2 < 5)){
				posiCheck(posi, new Integer[]{sc.getX() - 2, sc.getY() - 2}, cb, sc);
			}
		}
		return posi;
	}

	private static List<Integer[]> ju(List<Chess> cb, Chess sc) {
		List<Integer[]> posi = new ArrayList<>();
		for (int i = sc.getY() - 1; i >= 0; i--) {
			Chess cs = Util.getChessByPosi(cb, new Integer[]{sc.getX(), i});
			if(cs == null){
				posiCheck(posi, new Integer[]{sc.getX(), i}, cb, sc);
			}else{
				posiCheck(posi, new Integer[]{sc.getX(), i}, cb, sc);
				break;
			}
		}

		for (int i = sc.getY() + 1; i < maxY; i++) {
			Chess cs = Util.getChessByPosi(cb, new Integer[]{sc.getX(), i});
			if(cs == null){
				posiCheck(posi, new Integer[]{sc.getX(), i}, cb, sc);
			}else{
				posiCheck(posi, new Integer[]{sc.getX(), i}, cb, sc);
				break;
			}
		}

		for (int i = sc.getX() - 1; i >= 0; i--) {
			Chess cs = Util.getChessByPosi(cb, new Integer[]{i, sc.getY()});
			if(cs == null){
				posiCheck(posi, new Integer[]{i, sc.getY()}, cb, sc);
			}else{
				posiCheck(posi, new Integer[]{i, sc.getY()}, cb, sc);
				break;
			}
		}

		for (int i = sc.getX() + 1; i < maxY; i++) {
			Chess cs = Util.getChessByPosi(cb, new Integer[]{i, sc.getY()});
			if(cs == null){
				posiCheck(posi, new Integer[]{i, sc.getY()}, cb, sc);
			}else{
				posiCheck(posi, new Integer[]{i, sc.getY()}, cb, sc);
				break;
			}
		}
		
		return posi;
	}


	private static List<Integer[]> ma(List<Chess> cb, Chess sc) {
		List<Integer[]> posi = new ArrayList<>();
		
		if(Util.getChessByPosi(cb, new Integer[]{sc.getX(), sc.getY() - 1}) == null){
			posiCheck(posi, new Integer[]{sc.getX() - 1, sc.getY() - 2}, cb, sc);
			posiCheck(posi, new Integer[]{sc.getX() + 1, sc.getY() - 2}, cb, sc);
		}
		
		if(Util.getChessByPosi(cb, new Integer[]{sc.getX(), sc.getY() + 1}) == null){
			posiCheck(posi, new Integer[]{sc.getX() - 1, sc.getY() + 2}, cb, sc);
			posiCheck(posi, new Integer[]{sc.getX() + 1, sc.getY() + 2}, cb, sc);
		}
		
		if(Util.getChessByPosi(cb, new Integer[]{sc.getX() - 1, sc.getY()}) == null){
			posiCheck(posi, new Integer[]{sc.getX() - 2, sc.getY() - 1}, cb, sc);
			posiCheck(posi, new Integer[]{sc.getX() - 2, sc.getY() + 1}, cb, sc);
		}
		
		if(Util.getChessByPosi(cb, new Integer[]{sc.getX() + 1, sc.getY()}) == null){
			posiCheck(posi, new Integer[]{sc.getX() + 2, sc.getY() - 1}, cb, sc);
			posiCheck(posi, new Integer[]{sc.getX() + 2, sc.getY() + 1}, cb, sc);
		}
			
		return posi;
	}

	/**
	 * @return 炮的行走规则
	 */
	private static List<Integer[]> pao(List<Chess> cb, Chess sc) {
		List<Integer[]> posi = new ArrayList<>();
		for (int i = sc.getY() - 1; i >= 0; i--) {
			Chess cs = Util.getChessByPosi(cb, new Integer[]{sc.getX(), i});
			if(cs == null){
				posiCheck(posi, new Integer[]{sc.getX(), i}, cb, sc);
			}else{
				for (int j = i - 1; j >= 0; j--) {
					cs = Util.getChessByPosi(cb, new Integer[]{sc.getX(), j});
					if(cs != null){
						if(cs.getSide() != sc.getSide()){
							posiCheck(posi, new Integer[]{sc.getX(), j}, cb, sc);
						}
						break;
					}
				}
				break;
			}
		}

		for (int i = sc.getY() + 1; i < maxY; i++) {
			Chess cs = Util.getChessByPosi(cb, new Integer[]{sc.getX(), i});
			if(cs == null){
				posiCheck(posi, new Integer[]{sc.getX(), i}, cb, sc);
			}else{
				for (int j = i + 1; j < maxY; j++) {
					cs = Util.getChessByPosi(cb, new Integer[]{sc.getX(), j});
					if(cs != null){
						if(cs.getSide() != sc.getSide()){
							posiCheck(posi, new Integer[]{sc.getX(), j}, cb, sc);
						}
						break;
					}
				}
				break;
			}
		}

		for (int i = sc.getX() - 1; i >= 0; i--) {
			Chess cs = Util.getChessByPosi(cb, new Integer[]{i, sc.getY()});
			if(cs == null){
				posiCheck(posi, new Integer[]{i, sc.getY()}, cb, sc);
			}else{
				for (int j = i - 1; j >= 0; j--) {
					cs = Util.getChessByPosi(cb, new Integer[]{j, sc.getY()});
					if(cs != null){
						if(cs.getSide() != sc.getSide()){
							posiCheck(posi, new Integer[]{j, sc.getY()}, cb, sc);
						}
						break;
					}
				}
				break;
			}
		}

		for (int i = sc.getX() + 1; i < maxY; i++) {
			Chess cs = Util.getChessByPosi(cb, new Integer[]{i, sc.getY()});
			if(cs == null){
				posiCheck(posi, new Integer[]{i, sc.getY()}, cb, sc);
			}else{
				for (int j = i + 1; j < maxY; j++) {
					cs = Util.getChessByPosi(cb, new Integer[]{j, sc.getY()});
					if(cs != null){
						if(cs.getSide() != sc.getSide()){
							posiCheck(posi, new Integer[]{j, sc.getY()}, cb, sc);
						}
						break;
					}
				}
				break;
			}
		}
		return posi;
	}


	private static List<Integer[]> bing(List<Chess> cb, Chess sc) {
		List<Integer[]> posi = new ArrayList<>();
		
		if((sc.getSide() == Util.playerSide && sc.getY() < 5) || 
				(sc.getSide() != Util.playerSide && sc.getY() > 4)){
			posiCheck(posi, new Integer[]{sc.getX() - 1, sc.getY()}, cb, sc);
			posiCheck(posi, new Integer[]{sc.getX() + 1, sc.getY()}, cb, sc);
		}
		
		if(sc.getSide() == Util.playerSide){
			if(sc.getY() - 1 >= 0){
				posi.add(new Integer[]{sc.getX(), sc.getY() - 1});
				posiCheck(posi, new Integer[]{sc.getX(), sc.getY() - 1}, cb, sc);
			}
		}
		
		if(sc.getSide() != Util.playerSide){
			if(sc.getY() + 1 < Rule.maxY){
				posiCheck(posi, new Integer[]{sc.getX(), sc.getY() + 1}, cb, sc);
			}
		}
		
		return posi;
	}

	private static void posiCheck(List<Integer[]> posi, Integer[] coor, List<Chess> cb, Chess selChess){
		Chess cs = Util.getChessByPosi(cb, coor);
		if(cs == null || cs.getSide() != selChess.getSide()){
			if(coor[0] >= 0 && coor[0] < maxX && 
					coor[1] >= 0 && coor[1] < maxY ){
				posi.add(coor);
			}
		}
	}
	
	private static void posiCheck(List<Integer[]> posi, Integer[] coor, List<Chess> cb, Chess selChess, boolean inPalace){
		Chess cs = Util.getChessByPosi(cb, coor);
		if(cs == null || cs.getSide() != selChess.getSide()){
			if(coor[0] >= 0 && coor[0] < maxX && 
					coor[1] >= 0 && coor[1] < maxY ){
				if(inPalace){
					if(inPalace(coor, selChess)){
						posi.add(coor);
					}
				}else{
					posi.add(coor);
				}
			}
		}
	}
	
	private static boolean inPalace(Integer[] coor, Chess selChess){
		if(selChess.getSide() == Util.playerSide){
			if(coor[0] > 2 && coor[0] < 6 && coor[1] > 6 && coor[1] < maxY){
				return true;
			}
		}else{
			if(coor[0] > 2 && coor[0] < 6 && coor[1] >= 0 && coor[1] < 3){
				return true;
			}
		}
		return false;
	}
}
