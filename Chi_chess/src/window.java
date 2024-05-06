import com.chess.Index;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class window extends JFrame {
    public window() throws HeadlessException {
        this.setSize(800, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        JButton game1Button = new JButton("开始游戏");
        game1Button.setBounds(250, 500, 230, 130);
        game1Button.setBackground(Color.YELLOW);
        this.add(game1Button);
        game1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: 处理点击游戏1按钮的逻辑
                new Index();
                dispose();
            }
        });
        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        // 使用 JTextArea 显示文字
        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true); // 设置自动换行
        textArea.setWrapStyleWord(true); // 设置按单词换行
        textArea.setBackground(new Color(206, 230, 214)); // 设置背景颜色
        textArea.setText("1.棋子行棋规则帅/将移动范围：只能在九宫内移动\n" +
                "移动规则：每一步只可以水平或垂直移动一点\n" +
                "特殊规则：帅和将不准在同一直线上直接对面（中间无棋子），如一方已先占据位置，则另一方必须回避，否则就算输\n\n" +
                "2.士移动范围：只能在九宫内移动\n" +
                "移动规则：每一步只可以沿对角线方向移动一点\n\n" +
                "3.相/象移动范围：河界的一侧移动规则\n" +
                "移动规则：每一步只可以沿对角线方向移动两点\n" +
                "可使用汉字中的田字形象地表述：田字格的对角线，俗称相（象）走田字。\n" +
                "当相（象）行走路线中，即田字中心有棋子时（无论己方或是对方棋子），则不允许走过去，俗称：塞相（象）眼。\n\n" +
                "4.马移动范围：任何位置移动规则\n" +
                "每一步只可以水平或垂直移动一点，再按对角线方面向左或者右移动\n" +
                "可使用汉字中的日字来形容马的行走方式，俗称：马走日字（斜对角线）\n" +
                "当马行走时，第一步直行或横行处有别的棋子（无论己方或是对方棋子）挡住，则不许走过去，俗称：蹩马腿。\n\n" +
                "5.车移动范围：任何位置移动规则\n" +
                "可以水平或垂直方向移动任意个无阻碍的点\n\n" +
                "6.炮/砲移动范围：任何位置移动规则\n" +
                "移动起来和车很相似，但它必须跳过一个棋子来吃掉对方棋子\n\n" +
                "7.兵/卒移动范围：任何位置移动规则\n" +
                "过河界前，每步只能向前移动一点\n" +
                "过河界后，增加了向左右移动的能力，兵（卒）不允许向后移动\n" +
                "\n" +
                "唯一例外的是炮的吃棋方法，比较特殊，需要中间隔有棋子（无论是己方或对方棋子）才能吃掉对方的棋子。");
        panel.add(textArea, BorderLayout.CENTER);
        // 添加面板到主窗口
        this.getContentPane().add(panel);
        this.setVisible(true);
    }
    public static void main(String[] args) {
        new window();
    }
}