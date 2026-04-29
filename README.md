# ✈️ Aircraft War (飞机大战) - Java 面向对象设计实践

本项目是一个基于 Java Swing/AWT 开发的经典 2D 纵轴射击游戏——“飞机大战”。
本项目通过多次实验迭代，逐步完善游戏基础框架，并深入实践**单例模式**、**工厂方法模式**、**简单工厂模式**、**策略模式**、**DAO 模式**、**Swing 界面设计**与**多线程编程**等面向对象设计思想，打造高内聚、低耦合的游戏架构。

---

## 🌟 版本更新日志

### [v1.0] 🛡️ 基础框架与实体设计（实验一）

- 提取飞行物基类 `AbstractFlyingObject`
- 派生出三大抽象子类：
  - `AbstractAircraft`
  - `BaseBullet`
  - `AbstractProp`
- 英雄机 `HeroAircraft` 采用单例模式，保证全局唯一
- 实现英雄机鼠标移动控制
- 初始敌机系统：
  - `MobEnemy`：普通敌机，匀速下落，不发射子弹
  - `EliteEnemy`：精英敌机，血量更高，会周期性发射子弹
- 实现 AABB 碰撞检测机制

### [v2.0] ⚙️ 架构升级与工厂模式（实验二）

- 新增精锐敌机 `AdvancedEnemy`
  - 支持左右移动
  - 支持双排子弹
- 新增王牌敌机 `AceEnemy`
  - 支持左右移动
  - 支持扇形散射
- 引入 `EnemyFactory` 工厂方法模式
  - 为每种敌机建立专属工厂
  - 解耦敌机对象的创建与使用
- 引入 `PropFactory` 简单工厂模式
  - 统一生成不同类型道具
- 新增冰冻道具 `FreezeProp`

### [v3.0] 🔥 Boss 降临与策略模式（实验三）

- 新增 Boss 敌机 `BossEnemy`
  - 当玩家得分每累计满 500 分时自动召唤
  - 屏幕上同一时刻仅存在一只 Boss
  - 具备左右边界反弹移动逻辑
- 引入 `ShootStrategy` 射击策略接口
  - 将飞机开火行为与实体解耦
  - 普通/精英机使用直射策略
  - 王牌机使用散射策略
  - Boss 使用环射策略 `RingShootStrategy`
- Boss 专属掉落机制
  - 击杀 Boss 后从道具池中随机掉落 3 个道具
- 优化内存与性能
  - 越界飞出屏幕的敌机与道具自动销毁

### [v4.0] 💾 数据持久化与质量保证（实验四）

- 引入 DAO 模式
  - 设计 `RecordDao` 接口与 `RecordDaoImpl` 实现类
  - 将排行榜数据读写与游戏业务逻辑解耦
- 实现按难度分文件存储
  - Easy / Normal / Hard 对应不同本地文件
- 实现排行榜自动排序
  - 新增记录后自动按分数降序排列
- 实现动态排行榜系统
  - 游戏结束后自动记录玩家姓名、得分与时间
  - 在控制台打印格式化榜单
- 引入 JUnit5 单元测试
  - 对核心业务逻辑进行测试，提升代码可靠性

### [v5.0] 🎨 Swing 界面与多线程控制（实验五）

- 使用 Java Swing 实现游戏开始前的难度选择界面
- 使用 `CardLayout` 实现多页面切换
- 使用 `JTable` 实现得分排行榜界面
- 游戏结束后自动弹出输入窗口，记录玩家姓名与本局得分
- 支持删除任意历史得分记录
- 使用多线程完善游戏音效控制
  - 背景音乐循环播放
  - Boss 音乐循环播放
  - 子弹击中、炸弹爆炸、道具生效、游戏结束音效
- 使用 `Runnable` 完善两种火力道具的持续时间逻辑
  - 道具生效后自动恢复直射状态

---

## 🏗️ 核心系统设计图谱

### 1. 飞行物实体类群

- `AbstractFlyingObject`：所有飞行物基类
  - `AbstractAircraft`：飞机基类
    - `HeroAircraft`：英雄机（单例）
    - `MobEnemy`：普通敌机
    - `EliteEnemy`：精英敌机
    - `AdvancedEnemy`：精锐敌机
    - `AceEnemy`：王牌敌机
    - `BossEnemy`：Boss 敌机
  - `BaseBullet`：子弹基类
    - `HeroBullet`
    - `EnemyBullet`
  - `AbstractProp`：道具基类
    - `BloodProp`
    - `FireProp`
    - `SuperFireProp`
    - `BombProp`
    - `FreezeProp`

### 2. 设计模式实践群

- **单例模式**
  - `HeroAircraft`
- **工厂方法模式**
  - `EnemyFactory`
  - `MobEnemyFactory`
  - `EliteEnemyFactory`
  - `AdvancedEnemyFactory`
  - `AceEnemyFactory`
  - `BossEnemyFactory`
- **简单工厂模式**
  - `PropFactory`
- **策略模式**
  - `ShootStrategy`
  - `StraightShootStrategy`
  - `ScatterShootStrategy`
  - `RingShootStrategy`
- **DAO 模式**
  - `RecordDao`
  - `RecordDaoImpl`

---

## 🛠️ 技术栈

- Java
- Java Swing
- AWT
- 多线程
- JUnit 5
- 面向对象设计模式

---

## 📁 项目结构

```text
AircraftWar-base1.0/
├── .idea/                    # IntelliJ IDEA 配置文件
├── lib/                      # 第三方库依赖
├── out/                      # 编译输出目录
├── src/                      # 源代码根目录
│   └── edu.hitsz/           # 主包
│       ├── aircraft/        # 飞机实体类（英雄机、敌机、Boss）
│       ├── application/     # 游戏主程序与 UI 界面
│       ├── basic/           # 基础抽象类（飞行物基类等）
│       ├── bullet/          # 子弹实体类
│       ├── dao/             # 数据访问对象（排行榜持久化）
│       ├── factory/         # 工厂类（敌机工厂、道具工厂）
│       ├── music/           # 音频控制（多线程音效管理）
│       ├── prop/            # 道具实体类
│       └── strategy/        # 策略模式（射击策略）
│   ├── images/              # 游戏图片资源
│   └── videos/              # 游戏音频资源（.wav 格式）
├── uml/                      # UML 类图文件
├── AircraftWar-base.iml     # IntelliJ 模块配置
├── README.md                # 项目说明文档
└── record_easy.txt          # 简单难度排行榜数据文件
