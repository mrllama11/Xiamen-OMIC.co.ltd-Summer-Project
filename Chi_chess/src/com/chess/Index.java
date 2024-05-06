package com.chess;

import client.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class Index extends JFrame {
	private static final long serialVersionUID = 1L;
	Screen screen = new Screen();
	private int width = 600;
	private int height = 750;
	private final int CELL_W = 63;//单元格宽度
	private final int OFFSET_X = 50, OFFSET_Y = 50;//棋盘左上角偏移量
	private final int TOTAL_W = OFFSET_X + CELL_W * 8 + 230;//画布总宽度
	private final int TOTAL_H = OFFSET_Y + CELL_W * 9 + 80;//画布总高度
	public boolean isRedTurn;//回合判断
	public int timeCnt;
	public int chessState;//对局状态 0：未开始  1：对局中 2：结束
	private JButton startBtn;
	public Timer timer;
	public Image imgbuf;//更新缓冲图像
	private Graphics gbuf;
	private JDialog resultDialog;
	public static final Dimension FRAME_SIZE = new Dimension(360, 800);
	public static final Dimension RESULT_DIALOG_SIZE = new Dimension((int)(FRAME_SIZE.width/2), (int)(FRAME_SIZE.height/6));
	private JLabel resultLabel;

	public Index() {
		this.setSize(this.width, this.height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(false);
		this.add(screen);
		this.setTitle(" 中国象棋");

		screen.addMouseListener(new MyMouseAdapter());

	}

	class MyMouseAdapter extends MouseAdapter {
		@Override
		public void mouseReleased(MouseEvent e) {
			Action.ClickHandler(Util.getClickPosi(e.getX(), e.getY()));

			screen.repaint();

			//AI计算位置
			if (Util.curPlayer != Util.playerSide) {
				new AI(Util.chesslist, Util.curPlayer).run();
			}
		}
	}

	class Screen extends JPanel implements ActionListener{
		private static final long serialVersionUID = 1L;


		public void initButton() {
			startBtn = new JButton("开始游戏");
			startBtn.setFont(new Font("", Font.BOLD, 18));
			startBtn.setBounds(TOTAL_W - 170, 220, 110, 50);
			startBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (chessState != 1) {
						chessState = 1;//进入对局
						isRedTurn = true;//红色方先手
						resetTimer();
						startBtn.setText("结束游戏");
					} else {
						chessState = 0;
						cancelTimer();


						startBtn.setText("开始游戏");
					}
//					update();
				}

			});
			add(startBtn);
		}
		public void createResultDialog(){
			resultDialog = new JDialog();
			resultDialog.setTitle("Result");
			resultDialog.setSize(RESULT_DIALOG_SIZE);
			resultDialog.setResizable(false);
			resultDialog.setLocationRelativeTo(this);
			resultDialog.setModal(true);
			resultDialog.setLayout(new GridLayout(3, 1));
			if (isRedTurn)
			{
				resultLabel = new JLabel();
				resultLabel.setForeground(Color.red);
				resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
				resultLabel.setText("红方胜出!");
			}
			else
			{
				resultLabel = new JLabel();
				resultLabel.setForeground(Color.red);
				resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
				resultLabel.setText("黑方胜出!");
			}

			JButton restartButton = new JButton("Restart");
			restartButton.setForeground(Color.black);
			restartButton.addActionListener(this);

			JButton quitButton = new JButton("Quit");
			quitButton.setForeground(Color.black);
			quitButton.addActionListener(this);

			resultDialog.add(resultLabel);
			resultDialog.add(restartButton);
			resultDialog.add(quitButton);
			resultDialog.setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if(command.equals("Reset") || command.equals("Restart")){
				new Index();
				dispose();

				if(command.equals("Restart")){
					resultDialog.setVisible(false);
					resultDialog.dispose();
				}
			}
			else if(command.equals("Quit")){
				resultDialog.dispose();
				dispose();

			}
		}

		public void update() {
			boolean isRedTurn = false;
			if (imgbuf == null) {
				imgbuf = createImage(this.getSize().width, this.getSize().height);
				gbuf = imgbuf.getGraphics();
			}
			gbuf.setColor(getBackground());
			gbuf.fillRect(0, 0, this.getSize().width, this.getSize().height);
			paint(gbuf);//在缓冲图像上绘制
			this.getGraphics().drawImage(imgbuf, 0, 0, this);
			if (chessState == 2) {
				cancelTimer();
				createResultDialog();
			}
		}

		public void cancelTimer() {
			if (timer != null) {
				timer.cancel();
				timer = null;
			}
		}

		public void resetTimer() {
			cancelTimer();
			timer = new Timer();
			//每秒执行一次
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					timeCnt--;
					if (timeCnt == 0) {
						isRedTurn = !isRedTurn;
						chessState = 2;
					}
					update();
				}
			}, 1000, 1000);
			timeCnt = 30;//定时30s
		}

		private Image createChessBoard() {
			BufferedImage bimg = new BufferedImage(CELL_W * 8, CELL_W * 9, BufferedImage.TYPE_INT_ARGB);//样式：带透明色
			Graphics2D g2d = (Graphics2D) bimg.getGraphics();
			setBackground(new Color(206, 230, 214));

			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke(2.0f));
			//画横线
			for (int j = 1; j < 9; j++) {
				g2d.drawLine(0, CELL_W * j, CELL_W * 8, CELL_W * j);
			}
			//画竖线
			for (int i = 1; i < 8; i++) {
				g2d.drawLine(CELL_W * i, 0, CELL_W * i, CELL_W * 4);
				g2d.drawLine(CELL_W * i, CELL_W * 5, CELL_W * i, CELL_W * 9);
			}
			//边框
			g2d.drawRect(1, 1, CELL_W * 8 - 2, CELL_W * 9 - 2);
			//九宫格斜线
			g2d.drawLine(CELL_W * 3, 0, CELL_W * 5, CELL_W * 2);
			g2d.drawLine(CELL_W * 3, CELL_W * 2, CELL_W * 5, 0);
			g2d.drawLine(CELL_W * 3, CELL_W * 7, CELL_W * 5, CELL_W * 9);
			g2d.drawLine(CELL_W * 3, CELL_W * 9, CELL_W * 5, CELL_W * 7);

			g2d.setColor(Color.BLACK);
			g2d.setFont(new Font("隶书", Font.BOLD, (int) (45.0 * CELL_W / 60)));
			g2d.drawString("楚" + " 河", (int) (CELL_W * 4 - 180.0 * CELL_W / 60), (int) (CELL_W * 4.5 + 15.0 * CELL_W / 60));
			g2d.drawString("漢" + " 界", (int) (CELL_W * 4 + 60.0 * CELL_W / 60), (int) (CELL_W * 4.5 + 15.0 * CELL_W / 60));

			JButton quitButton = new JButton("Quit");
			quitButton.setBounds(400, 650, 100, 50);
			quitButton.setForeground(Color.black);
			quitButton.addActionListener(e -> button(e));
			this.add(quitButton);
			return bimg;
		}

		@Override
		public void paint(Graphics g){
			super.paint(g);
			Graphics2D g2d = (Graphics2D) g;

			//画棋盘
			g2d.drawImage(createChessBoard(), OFFSET_X, OFFSET_Y, null);
			//棋盘外边框
			g2d.setStroke(new BasicStroke(4.0f));
			g2d.drawRect(OFFSET_X - 5, OFFSET_Y - 5, createChessBoard().getWidth(this) + 10, createChessBoard().getHeight(this) + 10);

			for (Chess chess : Util.chesslist) {
				g2d.drawImage(chess.getImg(), Util.posi[chess.getX()][chess.getY()][0], Util.posi[chess.getX()][chess.getY()][1], Chess.SIZE, Chess.SIZE, this);
			}

			//画出可移动位置
			g2d.setColor(Color.RED);
			for (Integer[] coo : Util.movePosi) {
				g2d.fillOval(Util.posi[coo[0]][coo[1]][0] + 15, Util.posi[coo[0]][coo[1]][1] + 15, 19, 19);
			}
		}
		private void button(ActionEvent e) {
			// TODO add your code here
			dispose();
		}

	}
	public static void main(String[] args) {
			new Index();
	}

}
