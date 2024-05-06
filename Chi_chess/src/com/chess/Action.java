package com.chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Action {

	Integer[] clickPosi;
	public static final int STATUS_EAT = 0;
	public static final int STATUS_MOVE = 1;
	public static final int STATUS_SELECT = 2;
	public static final int STATUS_OUTRULE = 3;
	public static final int STATUS_GENERAL = 4;
	private JDialog resultDialog;
	public static final Dimension FRAME_SIZE = new Dimension(360, 800);
	public static final Dimension RESULT_DIALOG_SIZE = new Dimension((int)(FRAME_SIZE.width/2), (int)(FRAME_SIZE.height/6));
	private JLabel resultLabel;

	public static void ClickHandler(Integer[] clickPosi) {
		if(clickPosi != null){
			int actionStatus = getAction(clickPosi);
			String moveInfo = "";
			switch (actionStatus) {
			case STATUS_EAT:
				moveInfo = "吃子";
				break;
			case STATUS_MOVE:
				moveInfo = "走子";
				break;
			case STATUS_SELECT:
				moveInfo = "选中";
				break;
			case STATUS_OUTRULE:
				moveInfo = "移动位置无效";
				break;
			case STATUS_GENERAL:
				moveInfo = "被将军，需要变着。";
				break;
			default:
				moveInfo = "行为无效";
				break;
			}
//			System.out.println(moveInfo);
			
			if(actionStatus == STATUS_SELECT){
				Util.movePosi.clear();
				Util.movePosi = Rule.getPosition(Util.chesslist, Util.selectedChess);
			}
			
			if(actionStatus == STATUS_EAT || actionStatus == STATUS_MOVE){
				if(Rule.isWin(Util.chesslist, Util.curPlayer)){
//					System.out.println(Util.curPlayer+" 玩家获胜！");
				}
				else if(Rule.isGeneral(Util.chesslist, Util.selectedChess.getSide())){
					String sideStr = Util.selectedChess.getSide() == Chess.RED?"红方":"黑方";
//					System.out.println(sideStr+"将军！");
				}
				
				Util.selectedChess = null;
				Util.movePosi.clear();

				//走子或者吃子之后需要切换当前玩家
				Util.swichSide();
			}
		}else{
			//无效的点击位置
		}
	}

	private static Integer getAction(Integer[] clickPosi) {
		Chess chess = Util.getChessByPosi(Util.chesslist, clickPosi);
		if(Util.selectedChess == null){
			//选子
			if(chess != null && chess.getSide() == Util.curPlayer){
				Util.selectedChess = chess;
				return STATUS_SELECT;
			}
		}else{
			//走子
			if(chess == null){
				int moveStatus = Rule.moveStatus(Util.chesslist, Util.selectedChess, clickPosi);
				if(moveStatus == Rule.MOVE_OK){
					Util.selectedChess.setX(clickPosi[0]);
					Util.selectedChess.setY(clickPosi[1]);
					
					return STATUS_MOVE;
				}else{
					return moveStatus;
				}
			}
			//吃子
			else if(chess != null && chess.getSide() != Util.selectedChess.getSide()){
				int moveStatus = Rule.moveStatus(Util.chesslist, Util.selectedChess, clickPosi);
				if(moveStatus == Rule.MOVE_OK){
					Util.chesslist.remove(chess);
					Util.selectedChess.setX(clickPosi[0]);
					Util.selectedChess.setY(clickPosi[1]);
					
					return STATUS_EAT;
				}else{
					return moveStatus;
				}
			}
			//选子
			else{
				Util.selectedChess = chess;
				return STATUS_SELECT;
			}
		}
		return -1;
	}
}
