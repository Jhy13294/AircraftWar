package edu.hitsz.strategy;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 散射策略：子弹呈扇形发射
 */
public class ScatterShootStrategy implements ShootStrategy {
    @Override
    public List<BaseBullet> shoot(int x, int y, int direction, int power, int shootNum, boolean isHero) {
        List<BaseBullet> res = new LinkedList<>();
        int speedY = direction * 10;

        for (int i = 0; i < shootNum; i++) {
            int bulletX = x + (i * 2 - shootNum + 1) * 10;
            int bulletY = y + direction * 2;

            // 核心逻辑：赋予子弹不同的横向速度
            // 如果 shootNum=3：i=0时横向速度为-4，i=1时为0，i=2时为4
            int speedX = (i * 2 - shootNum + 1) * 2;

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
