package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class BossEnemyFactory implements EnemyFactory {

    @Override
    public AbstractAircraft createEnemy() {
        // 初始 X 坐标设在屏幕中间
        int locationX = Main.WINDOW_WIDTH / 2;
        // 初始 Y 坐标设定在屏幕上方
        int locationY = ImageManager.BOSS_ENEMY_IMAGE.getHeight() / 2 + 50;

        // X轴速度设为 2，Y轴速度必须为 0
        int speedX = 2;
        int speedY = 0;

        // 给 Boss 设定超高血量，比如 500
        int hp = 500;

        return new BossEnemy(locationX, locationY, speedX, speedY, hp);
    }
}
