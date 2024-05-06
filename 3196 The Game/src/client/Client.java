package client;

import constant.Constant;
import core.BlockLoader;
import core.MyFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client extends MyFrame implements ActionListener {

	public BlockLoader loader = new BlockLoader();
	private JDialog resultDialog;
	public static final Dimension FRAME_SIZE = new Dimension(360, 800);
	public static final Dimension RESULT_DIALOG_SIZE = new Dimension((int)(FRAME_SIZE.width/2), (int)(FRAME_SIZE.height/6));
	private JLabel resultLabel;

	@Override
	public void loadFrame() {
		super.loadFrame();
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				loader.keyPressed(e);
			}
		});

	}

	@Override
	public void paint(Graphics g) {
		drawBasic(g);
		drawScore(g);
		if (gameStart) {
			try {
				loader.draw(g);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		} else {
			if (!gameStart & flag)
				gameReset();
		}
	}

	public static boolean gameStart = true;
	public static boolean flag = true;

	public void gameReset() {
		flag = false;
//		int m = JOptionPane.showOptionDialog(null, "Game Fail! ", "Game Fail", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Quit"}, "Cancel");
//		if (m == JOptionPane.YES_OPTION) {
//			gameStart = true;
//			flag = true;
//			loader = new BlockLoader();
//		} else {
//			System.exit(0);
//		}
		createResultDialog();
		gameStart = true;
		flag = true;
		loader = new BlockLoader();
//		setVisible(false);

	}
	private void createResultDialog(){
		resultDialog = new JDialog();
		resultDialog.setTitle("Result");
		resultDialog.setSize(RESULT_DIALOG_SIZE);
		resultDialog.setResizable(false);
		resultDialog.setLocationRelativeTo(this);
		resultDialog.setModal(true);
		resultDialog.setLayout(new GridLayout(3, 1));

		resultLabel = new JLabel();
		resultLabel.setForeground(Color.red);
		resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
		resultLabel.setText("Game over!");


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
			loadFrame();

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
	private void drawScore(Graphics g) {
		g.setColor(Color.RED);
		g.fillRoundRect(Constant.BACK_X, Constant.BACK_Y - 100, 220, 80, 10, 10);
		g.setColor(Color.WHITE);
		g.setFont(new Font("幼圆", Font.BOLD, 30));
		g.drawString("Score" + BlockLoader.score + "", Constant.BACK_X, Constant.BACK_Y - 50);

		g.setColor(Color.GREEN);
		g.fillRoundRect(Constant.BACK_RIGHT_IN_X + Constant.BLOCK_SPACE - 250, Constant.BACK_Y - 100, 250, 80, 10, 10);
		g.setColor(Color.WHITE);
		g.setFont(new Font("幼圆", Font.BOLD, 30));
		g.drawString("Best Score" + BlockLoader.bestScore + "", Constant.BACK_RIGHT_IN_X + Constant.BLOCK_SPACE - 250, Constant.BACK_Y - 50);

	}

	private void drawBasic(Graphics g) {
		g.setColor(Constant.COLOR_BACK);
		g.fillRoundRect(Constant.BACK_X, Constant.BACK_Y, Constant.BACK_WIDTH, Constant.BACK_HEIGHT, 20, 20);
		g.setColor(Constant.COLOR_BLOCK_BACK);
		for (int i = 1; i <= 4; i++) {
			for (int j = 1; j <= 4; j++) {
				int xStart = Constant.BACK_X + Constant.BLOCK_SPACE * j + Constant.BLOCK_WIDTH * (j - 1);
				int yStart = Constant.BACK_Y + Constant.BLOCK_SPACE * i + Constant.BLOCK_WIDTH * (i - 1);
				g.fillRoundRect(xStart, yStart,
						Constant.BLOCK_WIDTH, Constant.BLOCK_WIDTH, 10, 10);
			}
		}
	}

	public static void main(String[] args) {
		new Client().loadFrame();
	}
}
