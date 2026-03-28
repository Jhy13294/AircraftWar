package edu.hitsz.aircraft;

/**
 * 所有敌机的抽象父类
 */
public abstract class AbstractEnemy extends AbstractAircraft {
    public AbstractEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }
}
