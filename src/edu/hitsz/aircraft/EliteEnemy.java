package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet; // 确保导入了敌机子弹类

import java.util.LinkedList;
import java.util.List;

public class EliteEnemy extends AbstractEnemy {

    /** 攻击伤害 */
    private int power = 10;
    /** 子弹射击方向 (向下射击：1，向上射击：-1) */
    private int direction = 1;

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();

        // 计算子弹发射的初始位置（飞机正下方中心）
        int x = this.getLocationX();
        int y = this.getLocationY() + direction * 2;

        // 子弹的水平速度为0，垂直速度略快于飞机本身的下落速度
        int speedX = 0;
        int speedY = this.getSpeedY() + direction * 5;

        BaseBullet bullet = new EnemyBullet(x, y, speedX, speedY, power);
        res.add(bullet);

        return res;
    }
}
