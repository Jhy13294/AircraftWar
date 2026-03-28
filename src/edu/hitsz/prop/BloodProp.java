package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

public class BloodProp extends AbstractProp {
    public BloodProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void effect(HeroAircraft heroAircraft) {
        // 恢复 20 点血量，但不超过初始最大血量 100
        int currentHp = heroAircraft.getHp();
        int increase = 20;
        heroAircraft.setHp(Math.min(currentHp + increase, 100));
        System.out.println("BloodSupply active!");
    }
}