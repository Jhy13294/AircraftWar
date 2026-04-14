package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.prop.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 综合管理图片的加载，访问
 * 提供图片的静态访问方法
 * @author hitsz
 */
public class ImageManager {

    /**
     * 类名-图片 映射，存储各基类的图片 <br>
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    private static final Map<String, BufferedImage> CLASSNAME_IMAGE_MAP = new HashMap<>();

    public static BufferedImage BACKGROUND_IMAGE;
    public static BufferedImage HERO_IMAGE;
    public static BufferedImage HERO_BULLET_IMAGE;
    public static BufferedImage ENEMY_BULLET_IMAGE;
    public static BufferedImage MOB_ENEMY_IMAGE;

    public static BufferedImage ELITE_ENEMY_IMAGE; // [新增] 精英敌机图片变量
    public static BufferedImage VANGUARD_ENEMY_IMAGE;
    public static BufferedImage ACE_ENEMY_IMAGE;
    public static BufferedImage BOSS_ENEMY_IMAGE; //[V3] 新增

    public static BufferedImage BLOOD_PROP_IMAGE;
    public static BufferedImage FIRE_PROP_IMAGE;
    public static BufferedImage SUPER_FIRE_PROP_IMAGE;
    public static BufferedImage FREEZE_PROP_IMAGE;
    public static BufferedImage BOMB_PROP_IMAGE;


    static {
        try {

            BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg.jpg"));

            HERO_IMAGE = ImageIO.read(new FileInputStream("src/images/hero.png"));
            MOB_ENEMY_IMAGE = ImageIO.read(new FileInputStream("src/images/mob.png"));
            ELITE_ENEMY_IMAGE = ImageIO.read(new FileInputStream("src/images/elite.png")); // [v1新增] 加载精英敌机图片
            VANGUARD_ENEMY_IMAGE = ImageIO.read(new FileInputStream("src/images/elitePlus.png"));// [v2新增] 加载精锐敌机图片
            ACE_ENEMY_IMAGE = ImageIO.read(new FileInputStream("src/images/elitePro.png"));// [v2新增] 加载王牌敌机图片
            BOSS_ENEMY_IMAGE = ImageIO.read(new FileInputStream("src/images/boss.png")); //[v3新增] 加载boss敌机图片

            HERO_BULLET_IMAGE = ImageIO.read(new FileInputStream("src/images/bullet_hero.png"));
            ENEMY_BULLET_IMAGE = ImageIO.read(new FileInputStream("src/images/bullet_enemy.png"));

            CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), HERO_IMAGE);
            CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), MOB_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ELITE_ENEMY_IMAGE); // [新增] 注册精英敌机映射
            CLASSNAME_IMAGE_MAP.put(VanguardEnemy.class.getName(), VANGUARD_ENEMY_IMAGE);// [v2新增] 注册精锐敌机映射
            CLASSNAME_IMAGE_MAP.put(AceEnemy.class.getName(), ACE_ENEMY_IMAGE);// [v2新增] 注册王牌敌机映射
            CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(),BOSS_ENEMY_IMAGE);//[v3新增] 注册BOSS敌机映射


            CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), HERO_BULLET_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ENEMY_BULLET_IMAGE);

            BLOOD_PROP_IMAGE = ImageIO.read(new FileInputStream("src/images/prop_blood.png"));
            FIRE_PROP_IMAGE = ImageIO.read(new FileInputStream("src/images/prop_bullet.png"));
            SUPER_FIRE_PROP_IMAGE = ImageIO.read(new FileInputStream("src/images/prop_bulletPlus.png"));
            FREEZE_PROP_IMAGE = ImageIO.read(new FileInputStream("src/images/prop_freeze.png"));
            BOMB_PROP_IMAGE = ImageIO.read(new FileInputStream("src/images/prop_bomb.png"));


            CLASSNAME_IMAGE_MAP.put(BloodProp.class.getName(), BLOOD_PROP_IMAGE);
            CLASSNAME_IMAGE_MAP.put(FireProp.class.getName(), FIRE_PROP_IMAGE);
            CLASSNAME_IMAGE_MAP.put(SuperFireProp.class.getName(), SUPER_FIRE_PROP_IMAGE);
            CLASSNAME_IMAGE_MAP.put(FreezeProp.class.getName(), FREEZE_PROP_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BombProp.class.getName(), BOMB_PROP_IMAGE);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static BufferedImage get(String className){
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    public static BufferedImage get(Object obj){
        if (obj == null){
            return null;
        }
        return get(obj.getClass().getName());
    }

}
