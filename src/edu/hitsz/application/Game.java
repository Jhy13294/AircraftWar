package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.factory.*;
import edu.hitsz.prop.AbstractProp;

// 引入我们新建的工厂类

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * 游戏主面板，游戏启动
 * @author hitsz
 */
public class Game extends JPanel {

    private int backGroundTop = 0;

    //调度器, 用于定时任务调度
    private final Timer timer;
    //时间间隔(ms)，控制刷新频率
    private final int timeInterval = 40;

    private final HeroAircraft heroAircraft;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;

    //屏幕中出现的敌机最大数量
    private final int enemyMaxNumber = 5;

    //敌机生成周期
    protected double enemySpawnCycle  =  20;
    private int enemySpawnCounter = 0;

    //英雄机和敌机射击周期
    protected double shootCycle = 20;
    private int shootCounter = 0;

    //当前玩家分数
    private int score = 0;

    private final List<AbstractProp> props; // 存储当前屏幕上的道具
    //游戏结束标志
    private boolean gameOverFlag = false;

    // ==========================================
    // [重构] 实例化敌机工厂 (工厂方法模式)
    // ==========================================
    private final EnemyFactory mobEnemyFactory = new MobEnemyFactory();
    private final EnemyFactory eliteEnemyFactory = new EliteEnemyFactory();
    // [新增] 精锐与王牌敌机工厂
    private final EnemyFactory vanguardEnemyFactory = new VanguardEnemyFactory();
    private final EnemyFactory aceEnemyFactory = new AceEnemyFactory();

    public Game() {
        // 使用单例工厂方法获取实例
        heroAircraft = HeroAircraft.getInstance(Main.WINDOW_WIDTH / 2, Main.WINDOW_HEIGHT - 100, 0, 0, 100);
        props = new LinkedList<>();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

        this.timer = new Timer("game-action-timer", true);
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {

        // 定时任务：绘制、对象产生、碰撞判定、及结束判定
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                enemySpawnCounter++;
                if (enemySpawnCounter >= enemySpawnCycle) {
                    enemySpawnCounter = 0;

                    if (enemyAircrafts.size() < enemyMaxNumber) {
                        double rand = Math.random();
                        // 设定出现概率：王牌 10%，精锐 20%，精英 30%，普通 40%
                        if (rand < 0.1) {
                            enemyAircrafts.add(aceEnemyFactory.createEnemy());
                        } else if (rand < 0.3) {
                            enemyAircrafts.add(vanguardEnemyFactory.createEnemy());
                        } else if (rand < 0.6) {
                            enemyAircrafts.add(eliteEnemyFactory.createEnemy());
                        } else {
                            enemyAircrafts.add(mobEnemyFactory.createEnemy());
                        }
                    }
                }

                // 飞机发射子弹
                shootAction();
                // 子弹移动
                bulletsMoveAction();
                // 飞机移动
                aircraftsMoveAction();
                // 撞击检测
                crashCheckAction();
                // 后处理
                postProcessAction();
                // 重绘界面
                repaint();
                // 游戏结束检查
                checkResultAction();
            }
        };
        // 以固定延迟时间进行执行：本次任务执行完成后，延迟 timeInterval 再执行下一次
        timer.schedule(task, 0, timeInterval);

    }

    //***********************
    //      Action 各部分
    //***********************

    private void shootAction() {
        shootCounter++;
        if (shootCounter >= shootCycle) {
            shootCounter = 0;
            //英雄机射击
            heroBullets.addAll(heroAircraft.shoot());

            // 遍历所有敌机，调用其 shoot 方法
            for (AbstractAircraft enemy : enemyAircrafts) {
                enemyBullets.addAll(enemy.shoot());
            }
        }
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
        for (AbstractProp prop : props) {
            prop.forward();
        }
    }


    /**
     * 碰撞检测
     */
    private void crashCheckAction() {
        // 1. 敌机子弹攻击英雄机
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 2. 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) continue;
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) continue;
                if (enemyAircraft.crash(bullet)) {
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();

                    // 敌机坠毁掉落道具及加分
                    if (enemyAircraft.notValid()) {
                        score += 10;

                        // 判定哪些敌机会掉落道具：精英、精锐、王牌都有几率掉落
                        if (enemyAircraft instanceof EliteEnemy ||
                                enemyAircraft instanceof VanguardEnemy ||
                                enemyAircraft instanceof AceEnemy) {

                            // 设定 50% 的概率掉落道具
                            if (Math.random() < 0.5) {
                                dropProp(enemyAircraft);
                            }
                        }
                    }
                }
            }
        }

        // 3. 英雄机拾取道具检测
        for (AbstractProp prop : props) {
            if (prop.notValid()) continue;
            if (heroAircraft.crash(prop)) {
                prop.effect(heroAircraft); // 生效
                prop.vanish();             // 消失
            }
        }
    }

    // ==========================================
    // [重构] 掉落逻辑使用简单工厂模式，并区分敌机类型
    // ==========================================
    private void dropProp(AbstractAircraft enemy) {
        int x = enemy.getLocationX();
        int y = enemy.getLocationY();

        // 声明一个道具池数组
        String[] propPool = null;

        // 根据敌机类型，设定不同的道具掉落池
        if (enemy instanceof AceEnemy) {
            // 王牌敌机：5 种道具全覆盖
            propPool = new String[]{"Blood", "Fire", "SuperFire", "Bomb", "Freeze"};
        } else if (enemy instanceof VanguardEnemy) {
            // 精锐敌机：4 种道具（没有冰冻）
            propPool = new String[]{"Blood", "Fire", "SuperFire", "Bomb"};
        } else if (enemy instanceof EliteEnemy) {
            // 精英敌机：基础 3 种道具
            propPool = new String[]{"Blood", "Fire", "SuperFire"};
        }

        // 从池子中随机抽取一种道具，并让工厂生产
        if (propPool != null) {
            int randomIndex = (int) (Math.random() * propPool.length);
            String propType = propPool[randomIndex];
            props.add(PropFactory.createProp(propType, x, y));
        }
    }


    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid); // 清理越界或被拾取的道具
    }

    private void checkResultAction(){
        if (heroAircraft.getHp() <= 0) {
            timer.cancel();
            gameOverFlag = true;
            System.out.println("Game Over!");
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, props); // [绘制道具]
        paintImageWithPositionRevised(g, enemyAircrafts);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        paintScoreAndLife(g);
    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.isEmpty()) {
            return;
        }
        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE: " + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE: " + this.heroAircraft.getHp(), x, y);
    }
}
