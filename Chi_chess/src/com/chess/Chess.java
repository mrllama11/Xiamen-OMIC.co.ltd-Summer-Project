package com.chess;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Chess implements Cloneable {
	private int side;	//棋子的阵营，红方或者黑方。
	private int type;	//棋子的类型，如车，马，炮。
	private String name;	//棋子名称，如车，马，炮。
	private int score;	//棋子的价值
	
	private int x;	
	private int y;	//棋子在横竖交叉点的位置
	
	private BufferedImage img;
	public final static int SIZE = Util.ChessSize;

	//棋子的阵营
	public final static int RED = 0;
	public final static int BLACK = 1;

	
	//棋子的类型
	public final static int TYPE_JIANG = 0;
	public final static int TYPE_JU = 1;
	public final static int TYPE_MA = 2;
	public final static int TYPE_PAO = 3;
	public final static int TYPE_XIANG = 4;
	public final static int TYPE_SHI = 5;
	public final static int TYPE_BING = 6;
	public static final int DIAM = 50;//棋子直径

	


	
	public Chess(int side, int type, int x, int y) {
		initData(side, type, x, y);
	}
	private Image createImage() {
		BufferedImage bimg = new BufferedImage(DIAM, DIAM, BufferedImage.TYPE_INT_ARGB);//样式：带透明色
		Graphics2D g2d = (Graphics2D) bimg.getGraphics();

		Color fillColor, borderColor, fontColor;//填充颜色，边框颜色，字体颜色
		if (side == 0) {
			fillColor = new Color(255, 239, 188);
			borderColor = new Color(215, 173, 15);
			fontColor = Color.BLACK;
		} else {
			fillColor = new Color(255, 230, 223);
			borderColor = new Color(231, 140, 59);
			fontColor = Color.RED;
		}
		g2d.setColor(fillColor);
		g2d.fillOval(0, 0, DIAM, DIAM);

		g2d.setStroke(new BasicStroke(3.0f));
		g2d.setColor(borderColor);
		g2d.drawOval(1, 1, DIAM - 2, DIAM - 2);

		g2d.setFont(new Font("楷体", Font.BOLD, (int) (38.0 * DIAM / 50)));
		g2d.setColor(fontColor);
		g2d.drawString(name, (int) (DIAM / 2 - 20.0 * DIAM / 50), (int) (DIAM / 2 + 14.0 * DIAM / 50));
		return bimg;
	}
	
	private void initData(int side, int type, int x, int y){
		this.side = side;
		this.type = type;
		this.x = x;
		this.y = y;

		
		switch (this.type) {
		case TYPE_JIANG:
			this.name = "将";
			this.score = 100;
			img = (BufferedImage) createImage();
			break;
			
		case TYPE_JU:
			this.name = "车";
			this.score = 10;
			img = (BufferedImage) createImage();
			break;
			
		case TYPE_PAO:
			this.name = "炮";
			this.score = 6;
			img = (BufferedImage) createImage();
			break;
			
		case TYPE_MA:
			this.name = "马";
			this.score = 6;
			img = (BufferedImage) createImage();
			break;
			
		case TYPE_SHI:
			this.name = "士";
			this.score = 4;
			img = (BufferedImage) createImage();
			break;
			
		case TYPE_XIANG:
			this.name = "象";
			this.score = 4;
			img = (BufferedImage) createImage();
			break;
			
		case TYPE_BING:
			this.name = "兵";
			this.score = 2;
			img = (BufferedImage) createImage();
			break;
			
		default:
			break;
		}
		
		//this.width = this.img.getWidth();
		//this.height = this.img.getHeight();
	}

	public int getSide() {
		return side;
	}

	public void setSide(int side) {
		this.side = side;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

	@Override
	public String toString() {
		return "Chess [side=" + side + ", type=" + type + ", name=" + name + ", score=" + score + ", x=" + x + ", y="
				+ y + "]";
	}
	
	public Chess clone(){
		Chess obj = null;
		try {
			obj = (Chess) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
