package edu.hitsz.application;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    // 1. 定义全局的 卡片布局 和 容纳卡片的面板
    public static final CardLayout cardLayout = new CardLayout(0, 0);
    public static final JPanel cardPanel = new JPanel(cardLayout);

    public static void main(String[] args) {
        System.out.println("Hello hitsz Plane War!");

        // 设置主窗体
        JFrame frame = new JFrame("HitSz Plane War");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 将卡片面板加入主窗体
        frame.add(cardPanel);

        // 2. 初始化 开始菜单

        StartMenu startMenu = new StartMenu();
        cardPanel.add(startMenu.getMainPanel(), "StartMenu");
        cardLayout.show(cardPanel, "StartMenu");


        // 设置窗体居中并显示
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2,
                0, WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setVisible(true);
    }
}
