package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

public class BombProp extends AbstractProp {

    public BombProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(HeroAircraft heroAircraft) {
        // 炸弹道具生效时，在控制台打印信息
        System.out.println("BombSupply active!");

        // TODO: 如果后续需要实现炸弹清屏功能，可以把清空敌机和敌机子弹的逻辑写在这里，
        // 或者在这里触发一个全局的炸弹事件。
    }
}
