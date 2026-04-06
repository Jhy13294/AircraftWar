package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.AceEnemy;
import edu.hitsz.application.Main;

public class AceEnemyFactory implements EnemyFactory {
    @Override
    public AbstractAircraft createEnemy() {
        int x = (int) (Math.random() * Main.WINDOW_WIDTH);
        int y = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05);
        // 王牌机横向移动可以更快一点
        int speedX = Math.random() > 0.5 ? 5 : -5;
        int speedY = 4;
        int hp = 90; // 王牌机血量最厚
        return new AceEnemy(x, y, speedX, speedY, hp);
    }
}
