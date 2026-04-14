package edu.hitsz.strategy;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 环射策略：以发机为圆心，向360度四周均匀发射子弹
 */
public class RingShootStrategy implements ShootStrategy {

    @Override
    public List<BaseBullet> shoot(int x, int y, int direction, int power, int shootNum, boolean isHero) {
        List<BaseBullet> res = new LinkedList<>();

        // 设定子弹向外扩散的基准速度
        // (环射不受 direction 参数控制，因为它向四面八方飞)
        int baseSpeed = 8;

        // 如果 shootNum 小于等于 0，直接返回空列表以防除以 0 报错
        if (shootNum <= 0) {
            return res;
        }

        for (int i = 0; i < shootNum; i++) {
            // 1. 计算当前子弹发射的角度（弧度制：2 * PI 代表 360 度）
            double angle = i * (2 * Math.PI / shootNum);

            // 2. 利用三角函数计算 X 轴和 Y 轴的速度分量
            // 使用 Math.round 四舍五入，让子弹分布更均匀
            int speedX = (int) Math.round(baseSpeed * Math.cos(angle));
            int speedY = (int) Math.round(baseSpeed * Math.sin(angle));

            // 3. 根据 isHero 判断生成英雄子弹还是敌机子弹
            BaseBullet bullet;
            if (isHero) {
                // 英雄机发出的环射子弹
                bullet = new HeroBullet(x, y, speedX, speedY, power);
            } else {
                // Boss 敌机发出的环射子弹
                bullet = new EnemyBullet(x, y, speedX, speedY, power);
            }
            res.add(bullet);
        }
        return res;
    }
}
