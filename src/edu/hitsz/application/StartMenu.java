package edu.hitsz.application;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class StartMenu {
    private JPanel mainPanel;
    private JButton btnEasy;
    private JButton btnNormal;
    private JButton btnHard;

    public StartMenu() {
        // 1. 手动初始化主面板和布局
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout()); // 使用 GridBagLayout 让内容完美居中

        // 2. 创建一个存放按钮的子面板，设置为 3行1列，按钮之间上下间距 30 像素
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 0, 30));

        // 3. 实例化按钮并设置文字
        btnEasy = new JButton("简单模式");
        btnNormal = new JButton("普通模式");
        btnHard = new JButton("困难模式");

        // 稍微美化一下按钮字体
        Font font = new Font("微软雅黑", Font.BOLD, 24);
        btnEasy.setFont(font);
        btnNormal.setFont(font);
        btnHard.setFont(font);

        // 4. 将按钮装入面板
        buttonPanel.add(btnEasy);
        buttonPanel.add(btnNormal);
        buttonPanel.add(btnHard);
        mainPanel.add(buttonPanel);

        // 5. 绑定点击事件 (使用 Lambda 表达式简化代码)
        btnEasy.addActionListener(e -> startGame("Easy", "src/images/bg.jpg"));
        btnNormal.addActionListener(e -> startGame("Normal", "src/images/bg2.jpg")); // 确保你的图片路径正确
        btnHard.addActionListener(e -> startGame("Hard", "src/images/bg3.jpg"));
    }

    private void startGame(String difficulty, String bgImagePath) {
        try {
            // 动态替换背景图片
            ImageManager.BACKGROUND_IMAGE = ImageIO.read(new FileInputStream(bgImagePath));

            System.out.println("选择了难度: " + difficulty);

            // 初始化游戏面板 (这里需要你的 Game 类构造函数能接收 difficulty 字符串，如果没有请稍作修改)
            // 如果你的 Game 构造函数不能传参，暂时先用 new Game() 也可以运行。
            Game game = new Game();

            // 将游戏面板加入卡片布局，并切换到游戏画面
            Main.cardPanel.add(game, "Game");
            Main.cardLayout.show(Main.cardPanel, "Game");

            // 启动游戏主逻辑
            game.action();

        } catch (IOException ex) {
            System.err.println("找不到背景图片，请检查路径: " + bgImagePath);
            ex.printStackTrace();
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
