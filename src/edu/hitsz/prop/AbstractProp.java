package edu.hitsz.prop;

import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.aircraft.HeroAircraft;

public abstract class AbstractProp extends AbstractFlyingObject {
    public AbstractProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    /**
     * 道具生效抽象方法
     * @param heroAircraft 英雄机对象，用于修改其属性
     */
    public abstract void effect(HeroAircraft heroAircraft);
}
