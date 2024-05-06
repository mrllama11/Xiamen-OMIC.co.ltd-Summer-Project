package com.chess;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class Util {
	// 图片
	public static Map<String, BufferedImage> imgs;
	//棋子的尺寸
	public static Integer ChessSize = 50;
	// 棋格坐标
	public static int[][][] posi = new int[9][10][2];
	//当前棋盘的所有棋子
	public static List<Chess> chesslist = new ArrayList<>();
	//选中的棋子
	public static Chess selectedChess = null;
	//是否获胜
	public static boolean STATUS_WIN = false;
	//当前玩家
	public static int curPlayer = Chess.RED;
	//玩家的阵营
	public static int playerSide = Chess.RED;
	//可移动位置
	public static List<Integer[]> movePosi = new ArrayList<>();
	
	static {
		/********************** 获取所有图片 *************************/
		imgs = new HashMap<String, BufferedImage>();
		File[] files = new File("E:\\year 2\\sem 2\\Java_intern\\group_project\\Chi_chess\\img").listFiles();
		for (File file : files) {
			if (file.isFile()) {
				try {
					imgs.put(file.getName(), ImageIO.read(file));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/********************** 获取棋格坐标 *************************/
		int posiT = 21;
		int posiL = 20;
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 10; y++) {
				posi[x][y][0] = posiT + (int)(x * 63.5);
				posi[x][y][1] = posiL + (int)(y * 63.5);
			}
		}

		/********************** 摆棋子 *************************/
		initChess();
	}
	
	private static void initChess(){
		int color = playerSide;
		// 红将
		chesslist.add(new Chess(color, Chess.TYPE_JIANG, 4, 9));
		// 红车
		chesslist.add(new Chess(color, Chess.TYPE_JU, 0, 9));
		chesslist.add(new Chess(color, Chess.TYPE_JU, 8, 9));
		// 红马
		chesslist.add(new Chess(color, Chess.TYPE_MA, 1, 9));
		chesslist.add(new Chess(color, Chess.TYPE_MA, 7, 9));
		// 红炮
		chesslist.add(new Chess(color, Chess.TYPE_PAO, 1, 7));
		chesslist.add(new Chess(color, Chess.TYPE_PAO, 7, 7));
		// 红士
		chesslist.add(new Chess(color, Chess.TYPE_SHI, 3, 9));
		chesslist.add(new Chess(color, Chess.TYPE_SHI, 5, 9));
		// 红象
		chesslist.add(new Chess(color, Chess.TYPE_XIANG, 2, 9));
		chesslist.add(new Chess(color, Chess.TYPE_XIANG, 6, 9));
		// 红兵
		chesslist.add(new Chess(color, Chess.TYPE_BING, 0, 6));
		chesslist.add(new Chess(color, Chess.TYPE_BING, 2, 6));
		chesslist.add(new Chess(color, Chess.TYPE_BING, 4, 6));
		chesslist.add(new Chess(color, Chess.TYPE_BING, 6, 6));
		chesslist.add(new Chess(color, Chess.TYPE_BING, 8, 6));

		color = playerSide == Chess.RED ? Chess.BLACK : Chess.RED;
		// 黑将
		chesslist.add(new Chess(color, Chess.TYPE_JIANG, 4, 0));
		// 黑车
		chesslist.add(new Chess(color, Chess.TYPE_JU, 0, 0));
		chesslist.add(new Chess(color, Chess.TYPE_JU, 8, 0));
		// 黑马
		chesslist.add(new Chess(color, Chess.TYPE_MA, 1, 0));
		chesslist.add(new Chess(color, Chess.TYPE_MA, 7, 0));
		// 黑炮
		chesslist.add(new Chess(color, Chess.TYPE_PAO, 1, 2));
		chesslist.add(new Chess(color, Chess.TYPE_PAO, 7, 2));
		// 红士
		chesslist.add(new Chess(color, Chess.TYPE_SHI, 3, 0));
		chesslist.add(new Chess(color, Chess.TYPE_SHI, 5, 0));
		// 黑象
		chesslist.add(new Chess(color, Chess.TYPE_XIANG, 2, 0));
		chesslist.add(new Chess(color, Chess.TYPE_XIANG, 6, 0));
		// 黑兵
		chesslist.add(new Chess(color, Chess.TYPE_BING, 0, 3));
		chesslist.add(new Chess(color, Chess.TYPE_BING, 2, 3));
		chesslist.add(new Chess(color, Chess.TYPE_BING, 4, 3));
		chesslist.add(new Chess(color, Chess.TYPE_BING, 6, 3));
		chesslist.add(new Chess(color, Chess.TYPE_BING, 8, 3));
	}
	
	public static void swichSide(){
		curPlayer = curPlayer == Chess.RED ? Chess.BLACK : Chess.RED;
	}

	public static Chess getChessByPosi(List<Chess> cb, Integer[] clickPosi) {
		if(clickPosi == null)
			return null;
		
		int x = clickPosi[0];
		int y = clickPosi[1];
		
		for (Chess chess : cb) {
			if(chess.getX() == x && chess.getY() == y){
				return chess;
			}
		}
		
		return null;
	}
	
	public static Map<?, ?> mapCopyDeep(Map<?, ?> data){
		Map<Object, Object> reData = null;
		if(data != null){
			reData = new HashMap<>();
			Iterator<?> it = data.entrySet().iterator();
			while(it.hasNext()){
				@SuppressWarnings("rawtypes")
				Entry  entry = (Entry) it.next();
				Object key = entry.getKey();
				reData.put(key, data.get(key));
			}
		}
		
		return reData;
	}

	public static Integer[] getClickPosi(int x, int y) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 10; j++) {
				int posX = Util.posi[i][j][0];
				int posY = Util.posi[i][j][1];
				
				if(x >= posX && x <= posX + Chess.SIZE){
					if(y >= posY && y <= posY + Chess.SIZE){
						return new Integer[]{i, j};
					}
				}
			}
		}
		return null;
	}

	public static List<Chess> copyBorad(List<Chess> cb){
		List<Chess> chessborad = new ArrayList<>();

		for (Chess chess : cb) {
			chessborad.add(chess.clone());
		}
		
		return chessborad;
	}
	
	static public int getRandom(int min, int max){
		return (new Random().nextInt(max)%(max-min+1)+min);
	}
}
