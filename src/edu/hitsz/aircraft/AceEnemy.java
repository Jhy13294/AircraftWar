package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
// 引入策略
import edu.hitsz.strategy.ShootStrategy;
import edu.hitsz.strategy.ScatterShootStrategy;

import java.util.List;

public class AceEnemy extends AbstractAircraft {

    // 声明并初始化“散射”策略
    private ShootStrategy strategy = new ScatterShootStrategy();

    public AceEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();
        if (this.getLocationX() <= 0) {
            this.speedX = Math.abs(this.speedX);
        } else if (this.getLocationX() >= Main.WINDOW_WIDTH) {
            this.speedX = -Math.abs(this.speedX);
        }
        if (this.getLocationY() >= Main.WINDOW_HEIGHT) {
            this.vanish();
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        // 调用策略进行射击：向下(1), 伤害15, 发射数量3, 是敌机(false)
        return strategy.shoot(this.getLocationX(), this.getLocationY(), 1, 15, 3, false);
    }
}
