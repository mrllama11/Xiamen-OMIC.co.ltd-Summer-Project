package com.chess;

import java.util.*;
import java.util.Map.Entry;

public class AI extends Thread{
	
	private List<Chess> cb;
	private int curSide;
	private int maxLoop = 3000;
	private int loopTime = maxLoop;
	private Long runTimes = 0L;
	
	public AI(List<Chess> chesslist, int curSide) {

		this.cb = Util.copyBorad(chesslist);
		this.curSide = curSide;
	}
	
	@Override
	public void run() {
		super.run();
		
		Integer[][] ai = think(this.cb, this.curSide);
		if(ai != null){
			Chess slChess = Util.getChessByPosi(Util.chesslist, new Integer[]{ai[0][0], ai[0][1]});
			Util.selectedChess = slChess;
			Action.ClickHandler(new Integer[]{ai[1][0], ai[1][1]});
		}
	}
	
	private Integer[][] think(List<Chess> cb, int cp) {
		
		Map<Integer[][], Long> scoreArr = new HashMap<Integer[][], Long>();
		List<Chess> cpBorad = Util.copyBorad(cb);
		
		for (Chess cs : cpBorad) {
			if(cs.getSide() == cp){
				List<Integer[]> posi = Rule.getPosition(cpBorad, cs);
				for (Integer[] coor : posi) {
					
					int status = Rule.moveStatus(cpBorad, cs, coor);
					if(status == Rule.MOVE_OK){
						Integer[][] sk = new Integer[2][2];
						Integer[] selCoor = new Integer[]{cs.getX(), cs.getY()};
						sk[0] = selCoor;
						sk[1] = coor;
						
						Long score = getScore(cpBorad, selCoor, coor, cp);
						
//						System.out.println(Arrays.toString(sk[0])+" "+Arrays.toString(sk[1])+" "+score);
						
						scoreArr.put(sk, score);
						
						this.loopTime = this.maxLoop;
						cpBorad = Util.copyBorad(cb);
					}
				}
			}
		}
		
//		System.out.println(runTimes);
		runTimes = 0L;
		
		Long maxScore = -Long.MAX_VALUE;
		ArrayList<Integer[][]> msArr = new ArrayList<>();
		Iterator<Entry<Integer[][], Long>> it = scoreArr.entrySet().iterator();
		while(it.hasNext()){
			@SuppressWarnings("rawtypes")
			Entry entry = (Entry) it.next();
			Object key = entry.getKey();
			if(scoreArr.get(key) > maxScore){
				msArr.clear();
				msArr.add((Integer[][]) key);
				maxScore = scoreArr.get(key);
			}else if(scoreArr.get(key) == maxScore){
				msArr.add((Integer[][]) key);
			}
		}
		
		for (Integer[][] coor : msArr) {
			String result = Arrays.toString(coor[0]) + "--" + Arrays.toString(coor[1]);
//			System.out.println(Arrays.toString(coor[0])+"--"+Arrays.toString(coor[1]));
		}
		
		return msArr.get(Util.getRandom(0, msArr.size()));
	}
	
	private Long getScore(List<Chess> cb, Integer[] selCoor, Integer[] coor, int curSide) {
		if(loopTime < 0){
			return 0L;
		}
		
		loopTime --;
		runTimes ++;
		Long score = 0L;

		List<Chess> cpBorad = Util.copyBorad(cb);
		
		Chess movChess = Util.getChessByPosi(cpBorad, coor);
		if(movChess != null){
			if(movChess.getType() == Chess.TYPE_JIANG){
				return scoreBySide((long) movChess.getScore(), curSide);
			}
			score += scoreBySide((long) movChess.getScore(), curSide);
			cpBorad.remove(movChess);
		}
		
		Chess selChess = Util.getChessByPosi(cpBorad, selCoor);
		
		selChess.setX(coor[0]);
		selChess.setY(coor[1]);
		
		if(Rule.isWin(cpBorad, curSide)){
			return scoreBySide(10000L, curSide);
		}
		
		/*==================================*/

		List<Chess> copyBorad = Util.copyBorad(cpBorad);
		curSide = curSide == Chess.RED?Chess.BLACK:Chess.RED;
		
		for (Chess cs : copyBorad) {
			if(cs.getSide() == curSide){
				List<Integer[]> posi = Rule.getPosition(copyBorad, cs);
				for (Integer[] coordinate : posi) {
					
					int status = Rule.moveStatus(copyBorad, cs, coor);
					if(status == Rule.MOVE_OK){
						
						Integer[] selCoordinate = new Integer[]{cs.getX(), cs.getY()};
						score += getScore(copyBorad, selCoordinate, coordinate, curSide);
						
						copyBorad = Util.copyBorad(cpBorad);
					}
				}
			}
		}
		
		return score;
	}

	private Long scoreBySide(Long score, int curSide) {
		if(this.curSide == curSide){
			return score;
		}else{
			return (score * -1);
		}
	}
	
	
}
