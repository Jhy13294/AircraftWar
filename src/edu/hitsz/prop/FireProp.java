package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.StraightShootStrategy;

public class FireProp extends AbstractProp {
    public FireProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(HeroAircraft heroAircraft) {
        System.out.println("FireSupply active! (火力升级：三排直射！)");

        // 1. 改变英雄机的射击策略和子弹数量
        heroAircraft.setShootNum(3);
        heroAircraft.setStrategy(new StraightShootStrategy());

        // 2. 开启一个后台线程，用于 5 秒后恢复默认状态
        new Thread(() -> {
            try {
                // 线程休眠 5000 毫秒 (即 5 秒)
                Thread.sleep(5000);

                // 时间到，恢复为单发直射
                heroAircraft.setShootNum(1);
                heroAircraft.setStrategy(new StraightShootStrategy());
                System.out.println("FireSupply expired! (火力道具失效！)");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
