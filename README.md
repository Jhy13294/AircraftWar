# ✈️ Aircraft War (飞机大战) - Java 面向对象设计实践

本项目是一个基于 Java Swing/AWT 开发的经典 2D 纵轴射击游戏——“飞机大战”。
当前版本为 **实验一** 的阶段性成果，主要完成了游戏的基础框架、面向对象体系设计以及核心物理和道具逻辑。

## 🌟 核心功能与特性 (实验一完成)

- **面向对象架构设计**
    - 提取了飞行物基类 `AbstractFlyingObject`。
    - 派生出 **飞机** (`AbstractAircraft`)、**子弹** (`BaseBullet`)、**道具** (`AbstractProp`) 三大抽象子类，实现了清晰的类层次结构。
- **英雄机与单例模式**
    - 玩家控制的英雄机 (`HeroAircraft`) 采用**单例模式**设计，确保全局唯一，并通过鼠标监听实现流畅的移动控制。
- **丰富的敌机系统**
    - **普通敌机 (`MobEnemy`)**：向下匀速飞行，不发射子弹（生成概率 $$80\%$$）。
    - **精英敌机 (`EliteEnemy`)**：向下飞行，会周期性向下发射子弹，血量更高（生成概率 $$20\%$$）。
- **碰撞与战斗逻辑**
    - 精确的 AABB 碰撞检测机制。
    - 英雄机子弹可对敌机造成伤害，敌机子弹也可对英雄机造成伤害。
    - 飞机实体相撞均会触发损毁判定。
- **道具掉落与增益系统**
    - 精英敌机被击毁时，有一定概率掉落道具。
    - 包含多种道具：加血道具 (`BloodProp`)、火力道具 (`FireProp`)、超级火力道具 (`SuperFireProp`)、炸弹道具 (`BombProp`)。
    - 拾取加血道具可动态恢复英雄机生命值（不超过最大生命值），其他道具拾取后在控制台打印生效日志。

## 🏗️ 核心类图设计 (简述)

```text
AbstractFlyingObject (所有飞行物的基类)
 ├── AbstractAircraft (飞机基类)
 │    ├── HeroAircraft (英雄机)
 │    ├── MobEnemy (普通敌机)
 │    └── EliteEnemy (精英敌机)
 ├── BaseBullet (子弹基类)
 │    ├── HeroBullet (英雄子弹)
 │    └── EnemyBullet (敌机子弹)
 └── AbstractProp (道具基类)
      ├── BloodProp (加血)
      ├── FireProp (火力)
      ├── SuperFireProp (超级火力)
      └── BombProp (炸弹清屏)
