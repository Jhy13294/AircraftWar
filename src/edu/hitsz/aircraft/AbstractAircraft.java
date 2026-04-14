package edu.hitsz.aircraft;

import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import java.util.List;

public abstract class AbstractAircraft extends AbstractFlyingObject {
    protected int maxHp; // 记录最大血量
    protected int hp;

    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp; // 初始血量即为最大血量
    }

    public void decreaseHp(int decrease) {
        hp -= decrease;
        if (hp <= 0) {
            hp = 0;
            vanish();
        }
    }

    public int getHp() {
        return hp;
    }

    // --- [新增部分] ---
    public void setHp(int hp) {
        this.hp = hp;
    }

    // 也可以直接提供一个获取最大血量的方法，方便后续逻辑判断
    public int getMaxHp() {
        return maxHp;
    }

    public abstract List<BaseBullet> shoot();
}
