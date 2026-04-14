package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.strategy.RingShootStrategy;
import edu.hitsz.strategy.ShootStrategy;

import java.util.List;
import java.util.LinkedList;

public class BossEnemy extends AbstractAircraft {

    // 1. 声明 Boss 独有的属性和策略
    private int shootNum = 20;     // 单次发射20颗子弹
    private int power = 20;        // 子弹威力
    private int direction = 1;     // 方向向下方 (虽然环射是360度，但接口参数需要)
    private ShootStrategy strategy = new RingShootStrategy(); // 默认环射策略

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    // 2. 必须实现(重写)父类的抽象方法 shoot()
    @Override
    public List<BaseBullet> shoot() {
        // 调用策略的 shoot 方法产生子弹
        // 最后一个参数传 false，代表这是敌机的子弹（会生成 EnemyBullet）
        return strategy.shoot(this.locationX, this.locationY, this.direction, this.power, this.shootNum, false);
    }

    @Override
    public void forward() {
        super.forward();

        int halfWidth = 80;

        if (locationX <= halfWidth) {
            // 撞到左侧视觉边缘
            speedX = Math.abs(speedX);
        } else if (locationX >= Main.WINDOW_WIDTH - halfWidth) {
            // 撞到右侧视觉边缘
            speedX = -Math.abs(speedX);
        }
    }

}
