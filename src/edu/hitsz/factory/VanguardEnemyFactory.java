package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.VanguardEnemy;
import edu.hitsz.application.Main;

public class VanguardEnemyFactory implements EnemyFactory {
    @Override
    public AbstractAircraft createEnemy() {
        int x = (int) (Math.random() * Main.WINDOW_WIDTH);
        int y = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05);
        // 随机给一个初始的横向速度：向左或向右
        int speedX = Math.random() > 0.5 ? 3 : -3;
        int speedY = 5;
        int hp = 60; // 精锐机血量
        return new VanguardEnemy(x, y, speedX, speedY, hp);
    }
}
