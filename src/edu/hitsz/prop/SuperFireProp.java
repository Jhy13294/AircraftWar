package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.RingShootStrategy;
import edu.hitsz.strategy.StraightShootStrategy;

public class SuperFireProp extends AbstractProp {
    public SuperFireProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(HeroAircraft heroAircraft) {
        System.out.println("SuperFireSupply active! (超级火力道具生效：360度环射！)");

        // 1. 改变英雄机为环射策略，发射 20 颗子弹
        heroAircraft.setShootNum(20);
        heroAircraft.setStrategy(new RingShootStrategy());

        // 2. 开启线程，5 秒后自动恢复（提前完成了实验五的要求，非常棒！）
        new Thread(() -> {
            try {
                Thread.sleep(5000);

                // 恢复为单发直射
                heroAircraft.setShootNum(1);
                heroAircraft.setStrategy(new StraightShootStrategy());
                System.out.println("SuperFireSupply expired! (超级火力道具失效，恢复直射！)");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
