package edu.hitsz.factory;

import edu.hitsz.prop.*;

/**
 * 道具工厂（简单工厂模式）
 */
public class PropFactory {

    /**
     * 根据道具类型创建道具
     * @param propType 道具类型，如 "Blood", "Fire", "SuperFire", "Bomb", "Freeze"
     * @param x 掉落位置 X
     * @param y 掉落位置 Y
     * @return 对应的道具对象
     */
    public static AbstractProp createProp(String propType, int x, int y) {
        // 道具默认向下掉落，速度设置在这统一管理
        int speedX = 0;
        int speedY = 5;

        switch (propType) {
            case "Blood":
                return new BloodProp(x, y, speedX, speedY);
            case "Fire":
                return new FireProp(x, y, speedX, speedY);
            case "SuperFire":
                return new SuperFireProp(x, y, speedX, speedY);
            case "Bomb":
                return new BombProp(x, y, speedX, speedY);
            case "Freeze":
                return new FreezeProp(x, y, speedX, speedY);
            default:
                System.out.println("未知的道具类型：" + propType);
                return null;
        }
    }
}
