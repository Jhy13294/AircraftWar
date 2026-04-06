package edu.hitsz.strategy;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 直射策略：子弹垂直向上或向下飞行
 */
public class StraightShootStrategy implements ShootStrategy {
    @Override
    public List<BaseBullet> shoot(int x, int y, int direction, int power, int shootNum, boolean isHero) {
        List<BaseBullet> res = new LinkedList<>();
        // 子弹基础垂直速度
        int speedY = direction * 10;
        int speedX = 0; // 直射，横向速度为 0

        for (int i = 0; i < shootNum; i++) {
            // 计算子弹的 X 坐标，使其关于飞机中心对称分布
            int bulletX = x + (i * 2 - shootNum + 1) * 10;
            int bulletY = y + direction * 2;

            BaseBullet bullet;
            if (isHero) {
                bullet = new HeroBullet(bulletX, bulletY, speedX, speedY, power);
            } else {
                bullet = new EnemyBullet(bulletX, bulletY, speedX, speedY, power);
            }
            res.add(bullet);
        }
        return res;
    }
}
