package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.factory.*;
import edu.hitsz.prop.AbstractProp;

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

    // ==========================================
    // [v3新增] Boss 机制相关变量
    // ==========================================
    private final int bossThreshold = 100;      // 分数阈值：每增加 500 分召唤一次 Boss
    private int lastBossScore = 0;              // 记录上次召唤 Boss 时的分数
    private boolean bossActive = false;         // 标志位：当前屏幕上是否有 Boss

    private final List<AbstractProp> props; // 存储当前屏幕上的道具
    //游戏结束标志
    private boolean gameOverFlag = false;

    // ==========================================
    // [重构] 实例化敌机工厂 (工厂方法模式)
    // ==========================================
    private final EnemyFactory mobEnemyFactory = new MobEnemyFactory();
    private final EnemyFactory eliteEnemyFactory = new EliteEnemyFactory();
    private final EnemyFactory vanguardEnemyFactory = new VanguardEnemyFactory();
    private final EnemyFactory aceEnemyFactory = new AceEnemyFactory();
    // [v3新增] Boss敌机工厂
    private final EnemyFactory bossEnemyFactory = new BossEnemyFactory();

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

                // ==========================================
                // [v3新增] Boss 召唤逻辑
                // ==========================================
                // 如果当前分数达到阈值，并且当前没有Boss存活，则召唤 Boss
                if (score - lastBossScore >= bossThreshold && !bossActive) {
                    System.out.println("警告：Boss 敌机降临！");
                    enemyAircrafts.add(bossEnemyFactory.createEnemy());
                    bossActive = true;
                    // 更新记录的分数，防止在同一分数段重复生成
                    lastBossScore += bossThreshold;
                }

                enemySpawnCounter++;
                if (enemySpawnCounter >= enemySpawnCycle) {
                    enemySpawnCounter = 0;

                    if (enemyAircrafts.size() < enemyMaxNumber) {
                        double rand = Math.random();
                        // 设定出现概率：王牌 5%，精锐 15%，精英 30%，普通 50%
                        if (rand < 0.05) {
                            enemyAircrafts.add(aceEnemyFactory.createEnemy());
                        } else if (rand < 0.20) {
                            enemyAircrafts.add(vanguardEnemyFactory.createEnemy());
                        } else if (rand < 0.50) {
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
        // 以固定延迟时间进行执行
        timer.schedule(task, 0, timeInterval);
    }

    //***********************
    //      Action 各部分
    //***********************

    private void shootAction() {
        shootCounter++;
        if (shootCounter >= shootCycle) {
            shootCounter = 0;
            heroBullets.addAll(heroAircraft.shoot());

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
            // [v3修复Bug] 越界检测：如果敌机飞出屏幕底端，让其销毁，释放敌机生成名额
            // 注意：Boss 敌机会在上面左右横跳，所以它的 Y 坐标不会触发这个销毁
            if (enemyAircraft.getLocationY() >= Main.WINDOW_HEIGHT) {
                enemyAircraft.vanish();
            }
        }

        for (AbstractProp prop : props) {
            prop.forward();
            // [v3修复Bug] 道具同理，飞出屏幕底端也自动销毁，防止占用内存
            if (prop.getLocationY() >= Main.WINDOW_HEIGHT) {
                prop.vanish();
            }
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

                        // ==========================================
                        // [修改] 区分 Boss 死亡和普通敌机死亡
                        // ==========================================
                        if (enemyAircraft instanceof BossEnemy) {
                            score += 50; // 击败 Boss 奖励分数
                            bossActive = false; // 重置 Boss 标志，允许未来再次生成
                            System.out.println("Boss 被击毁，掉落高级道具！");
                            dropBossProps(enemyAircraft); // Boss 特殊掉落机制（3个道具）
                        } else {
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
    // 道具掉落逻辑
    // ==========================================
    private void dropProp(AbstractAircraft enemy) {
        int x = enemy.getLocationX();
        int y = enemy.getLocationY();

        String[] propPool = null;

        if (enemy instanceof AceEnemy) {
            propPool = new String[]{"Blood", "Fire", "SuperFire", "Bomb", "Freeze"};
        } else if (enemy instanceof VanguardEnemy) {
            propPool = new String[]{"Blood", "Fire", "SuperFire", "Bomb"};
        } else if (enemy instanceof EliteEnemy) {
            propPool = new String[]{"Blood", "Fire", "SuperFire"};
        }

        if (propPool != null) {
            int randomIndex = (int) (Math.random() * propPool.length);
            String propType = propPool[randomIndex];
            props.add(PropFactory.createProp(propType, x, y));
        }
    }

    // ==========================================
    // [新增] Boss 死亡掉落 3 个道具的专属方法
    // ==========================================
    private void dropBossProps(AbstractAircraft boss) {
        int x = boss.getLocationX();
        int y = boss.getLocationY();
        // Boss 专属豪华掉落池
        String[] propPool = new String[]{"Blood", "Fire", "SuperFire", "Bomb", "Freeze"};

        // 循环生成 3 个道具
        for (int i = 0; i < 3; i++) {
            int randomIndex = (int) (Math.random() * propPool.length);
            String propType = propPool[randomIndex];
            // x + (i - 1) * 30 是为了让三个道具稍微分散开，不会完全重叠在一起
            props.add(PropFactory.createProp(propType, x + (i - 1) * 30, y));
        }
    }


    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
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
        paintImageWithPositionRevised(g, props);
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
