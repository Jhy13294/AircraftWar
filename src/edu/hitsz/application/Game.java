package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.factory.*;
import edu.hitsz.prop.AbstractProp;

// === [新增] 导入 DAO 和时间处理相关的包 ===
import edu.hitsz.dao.PlayerRecord;
import edu.hitsz.dao.RecordDao;
import edu.hitsz.dao.RecordDaoImpl;
import java.text.SimpleDateFormat;
import java.util.Date;

// === [v5新增] 导入音频管理器 ===
import edu.hitsz.music.AudioManager;
// =====================================

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
    private final int bossThreshold = 100;      // 分数阈值：每增加 500 分召唤一次 Boss (测试用100)
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
        // [v5新增] 游戏刚开始，播放基础背景音乐
        AudioManager.playBGM();

        // 定时任务：绘制、对象产生、碰撞判定、及结束判定
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // ==========================================
                // [v3新增] Boss 召唤逻辑
                // ==========================================
                if (score - lastBossScore >= bossThreshold && !bossActive) {
                    System.out.println("警告：Boss 敌机降临！");
                    enemyAircrafts.add(bossEnemyFactory.createEnemy());
                    bossActive = true;
                    lastBossScore += bossThreshold;

                    // [v5新增] Boss 降临，切换专属 BGM
                    AudioManager.stopBGM();
                    AudioManager.playBossBGM();
                }

                enemySpawnCounter++;
                if (enemySpawnCounter >= enemySpawnCycle) {
                    enemySpawnCounter = 0;

                    if (enemyAircrafts.size() < enemyMaxNumber) {
                        double rand = Math.random();
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

                shootAction();
                bulletsMoveAction();
                aircraftsMoveAction();
                crashCheckAction();
                postProcessAction();
                repaint();
                checkResultAction();
            }
        };
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
            if (enemyAircraft.getLocationY() >= Main.WINDOW_HEIGHT) {
                enemyAircraft.vanish();
            }
        }

        for (AbstractProp prop : props) {
            prop.forward();
            if (prop.getLocationY() >= Main.WINDOW_HEIGHT) {
                prop.vanish();
            }
        }
    }

    private void crashCheckAction() {
        // 1. 敌机子弹攻击英雄机
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) continue;
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

                    // [v5新增] 击中敌机，播放击中音效
                    AudioManager.playHitSound();

                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();

                    if (enemyAircraft.notValid()) {
                        if (enemyAircraft instanceof BossEnemy) {
                            score += 50;
                            bossActive = false;
                            System.out.println("Boss 被击毁，掉落高级道具！");
                            dropBossProps(enemyAircraft);

                            // [v5新增] Boss 阵亡，停止 Boss 音乐，恢复普通 BGM
                            AudioManager.stopBossBGM();
                            AudioManager.playBGM();

                        } else {
                            score += 10;
                            if (enemyAircraft instanceof EliteEnemy ||
                                    enemyAircraft instanceof VanguardEnemy ||
                                    enemyAircraft instanceof AceEnemy) {
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

                // [v5新增] 吃到道具音效
                // 如果是炸弹道具，额外播放爆炸音效 (假设炸弹类名包含 "Bomb")
                if (prop.getClass().getSimpleName().contains("Bomb")) {
                    AudioManager.playBombSound();
                } else {
                    AudioManager.playGetSupplySound();
                }

                prop.effect(heroAircraft);
                prop.vanish();
            }
        }
    }

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

    private void dropBossProps(AbstractAircraft boss) {
        int x = boss.getLocationX();
        int y = boss.getLocationY();
        String[] propPool = new String[]{"Blood", "Fire", "SuperFire", "Bomb", "Freeze"};

        for (int i = 0; i < 3; i++) {
            int randomIndex = (int) (Math.random() * propPool.length);
            String propType = propPool[randomIndex];
            props.add(PropFactory.createProp(propType, x + (i - 1) * 30, y));
        }
    }

    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        props.removeIf(AbstractFlyingObject::notValid);
    }

    /**
     * ⭐ 游戏结束检查，触发弹窗、保存成绩、并切换到排行榜
     */
    private void checkResultAction(){
        if (heroAircraft.getHp() <= 0) {
            timer.cancel(); // 停止游戏定时器
            gameOverFlag = true;
            System.out.println("Game Over!");

            // 停止音乐并播放结束音效
            AudioManager.stopBGM();
            AudioManager.stopBossBGM();
            AudioManager.playGameOverSound();

            // 1. 弹出输入框，让玩家输入名字
            String userName = JOptionPane.showInputDialog(
                    Main.cardPanel,
                    "游戏结束！你的最终得分是: " + this.score + "\n请输入你的名字：",
                    "保存成绩",
                    JOptionPane.PLAIN_MESSAGE
            );

            // 如果玩家点了取消或者没输入内容，给个默认名
            if (userName == null || userName.trim().isEmpty()) {
                userName = "Anonymous (匿名)";
            }

            // 2. 将数据保存进 DAO
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
            String currentTime = formatter.format(new Date());
            RecordDao recordDao = new RecordDaoImpl("Easy"); // 暂定存入 Easy 的文件

            PlayerRecord currentRecord = new PlayerRecord(userName, this.score, currentTime);
            recordDao.doAdd(currentRecord);

            // 3. 实例化排行榜界面，并传给它含有最新数据的 DAO
            ScoreBoard scoreBoard = new ScoreBoard(recordDao);

            // 4. 将排行榜面板加入卡片舞台，并“切牌”展示它！
            Main.cardPanel.add(scoreBoard.getMainPanel(), "ScoreBoard");
            Main.cardLayout.show(Main.cardPanel, "ScoreBoard");
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
