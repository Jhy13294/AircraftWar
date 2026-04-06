package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.ScatterShootStrategy;
import edu.hitsz.strategy.StraightShootStrategy;

public class SuperFireProp extends AbstractProp {
    public SuperFireProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(HeroAircraft heroAircraft) {
        System.out.println("SuperFireSupply active! (超级火力：扇形散射！)");

        // 1. 改变英雄机为散射策略，发射 3 颗 (你也可以改成 5 颗试试更爽的弹幕)
        heroAircraft.setShootNum(3);
        heroAircraft.setStrategy(new ScatterShootStrategy());

        // 2. 同样开启线程，5 秒后恢复
        new Thread(() -> {
            try {
                Thread.sleep(5000);

                // 恢复为单发直射
                heroAircraft.setShootNum(1);
                heroAircraft.setStrategy(new StraightShootStrategy());
                System.out.println("SuperFireSupply expired! (超级火力失效！)");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
