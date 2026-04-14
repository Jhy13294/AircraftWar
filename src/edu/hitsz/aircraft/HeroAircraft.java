package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
// 引入策略接口和直射策略
import edu.hitsz.strategy.ShootStrategy;
import edu.hitsz.strategy.StraightShootStrategy;

import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * 采用单例模式（双重检查锁定 DCL）实现
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    /**
     * 1. 静态私有实例变量
     * 使用 volatile 关键字：
     * - 保证变量在多线程间的可见性
     * - 禁止指令重排序，防止在实例未初始化完成时就被其他线程读取
     */
    private volatile static HeroAircraft instance;

    // --- [v2新增/修改] 策略模式相关属性 ---
    // 默认射击策略：直射
    private ShootStrategy strategy = new StraightShootStrategy();

    // 每次射击发射子弹数量 (默认 1)
    private int shootNum = 1;

    // 子弹威力
    private int power = 30;

    // 子弹射击方向 (向上发射：-1，向下发射：1)
    private int direction = -1;

    /**
     * 2. 私有构造方法
     * 将原本的 public 改为 private，防止外部通过 new 关键字创建多个实例
     */
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    /**
     * 3. 公有静态获取实例方法
     * 采用双重检查锁定 (DCL)
     * @return 全局唯一的英雄机实例
     */
    public static HeroAircraft getInstance(int locationX, int locationY, int speedX, int speedY, int hp) {
        if (instance == null) {
            synchronized (HeroAircraft.class) {
                if (instance == null) {
                    instance = new HeroAircraft(locationX, locationY, speedX, speedY, hp);
                }
            }
        }
        return instance;
    }

    // --- [v2新增] 提供给道具调用的 Setter 方法 ---
    /**
     * 设置射击策略
     */
    public void setStrategy(ShootStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * 设置每次射击的子弹数量
     */
    public void setShootNum(int shootNum) {
        this.shootNum = shootNum;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    @Override
    public List<BaseBullet> shoot() {
        // --- [v2修改] 使用当前的策略执行射击 ---
        // 参数：X坐标，Y坐标，方向，威力，子弹数量，是否为英雄机(true)
        return strategy.shoot(this.getLocationX(), this.getLocationY(), direction, power, shootNum, true);
    }
}
