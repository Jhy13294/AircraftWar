package edu.hitsz.strategy;

import edu.hitsz.bullet.BaseBullet;
import java.util.List;

/**
 * 射击策略接口
 */
public interface ShootStrategy {
    /**
     * 执行射击
     * @param x 飞机的 X 坐标
     * @param y 飞机的 Y 坐标
     * @param direction 射击方向 (1向下，-1向上)
     * @param power 子弹威力
     * @param shootNum 发射子弹数量
     * @param isHero 是否为英雄机 (用于区分产生英雄子弹还是敌机子弹)
     * @return 产生的子弹列表
     */
    List<BaseBullet> shoot(int x, int y, int direction, int power, int shootNum, boolean isHero);
}
