# ✈️ Aircraft War (飞机大战) - Java 面向对象设计实践

本项目是一个基于 Java Swing/AWT 开发的经典 2D 纵轴射击游戏——“飞机大战”。
本项目通过多次实验迭代，逐步完善游戏基础框架，并深入实践**单例模式**、**工厂方法模式**、**简单工厂模式**以及**策略模式**等面向对象设计模式，打造高内聚、低耦合的游戏架构。

---

## 🌟 版本更新日志

### [v1.0] 🛡️ 基础框架与实体设计 (实验一)

* **面向对象架构设计**
    * 提取了飞行物基类 `AbstractFlyingObject`。
    * 派生出 **飞机** (`AbstractAircraft`)、**子弹** (`BaseBullet`)、**道具** (`AbstractProp`) 三大抽象子类，实现了清晰的类层次结构。
* **英雄机与单例模式**
    * 玩家控制的英雄机 (`HeroAircraft`) 采用**单例模式**设计，确保全局唯一，并通过鼠标监听实现流畅的移动控制。
* **初始敌机系统**
    * **普通敌机 (`MobEnemy`)**：向下匀速飞行，不发射子弹（生成概率 $$80\%$$）。
    * **精英敌机 (`EliteEnemy`)**：向下飞行，会周期性向下发射子弹，血量更高（生成概率 $$20\%$$）。
* **核心物理与战斗**
    * 精确的 AABB 碰撞检测机制。实体相撞、子弹命中均会触发相应的扣血或销毁判定。

### [v2.0] ⚙️ 架构升级与工厂模式 (实验二)

<<<<<<< HEAD
### 🏗️ 核心实体与工厂类图设计 (v2 更新版)

#### 1. 飞行物实体类群 (继承结构)
- **`AbstractFlyingObject`** (所有飞行物的基类)
  - **`AbstractAircraft`** (飞机基类)
    - `HeroAircraft` (英雄机 - 单例)
    - `MobEnemy` (普通敌机)
    - `EliteEnemy` (精英敌机)
    - `AdvancedEnemy` (精锐敌机) ⭐ *[v2新增] 支持左右移动与双排子弹*
    - `AceEnemy` (王牌敌机) ⭐ *[v2新增] 支持左右移动与扇形散射*
  - **`BaseBullet`** (子弹基类)
    - `HeroBullet` (英雄子弹)
    - `EnemyBullet` (敌机子弹)
  - **`AbstractProp`** (道具基类)
    - `BloodProp` (加血道具)
    - `FireProp` (火力道具)
    - `SuperFireProp` (超级火力道具)
    - `BombProp` (炸弹道具)
    - `FreezeProp` (冰冻道具) ⭐ *[v2新增] 冰冻敌机效果*

#### 2. 创建者类群 (设计模式)
- **`EnemyFactory`** (敌机工厂接口) ⭐ *[v2新增 - 工厂方法模式]*
  - `MobEnemyFactory` (普通敌机工厂)
  - `EliteEnemyFactory` (精英敌机工厂)
  - `AdvancedEnemyFactory` (精锐敌机工厂)
  - `AceEnemyFactory` (王牌敌机工厂)
- **`PropFactory`** (道具简单工厂类) ⭐ *[v2新增 - 简单工厂模式]*
  - 提供静态方法: `createProp(int type, int x, int y)`
=======
* **敌机生态扩充与工厂方法模式 (Factory Method)**
    * 新增 **精锐敌机 (`AdvancedEnemy`)**（双排子弹）与 **王牌敌机 (`AceEnemy`)**（扇形散射），支持左右移动。
    * 引入 `EnemyFactory` 接口，为每种敌机建立专属工厂，彻底解耦敌机对象的创建与使用。
* **丰富道具与简单工厂模式 (Simple Factory)**
    * 新增 **冰冻道具 (`FreezeProp`)**，并引入 `PropFactory` 根据类型字符串统一生产掉落道具。

### [v3.0] 🔥 Boss 降临与策略模式 (实验三)

* **Boss 敌机系统 (`BossEnemy`)**
    * 引入极具压迫感的 Boss 敌机。当玩家得分每累计满 $$500$$ 分时自动召唤（保证同屏仅存活一只 Boss）。
    * 采用特殊的移动逻辑：悬浮于屏幕上方，并在触碰左右边界时**自动丝滑反弹**。
* **策略模式重构弹道 (Strategy Pattern)**
    * 将飞机的开火行为全面抽象封装为 `ShootStrategy` 接口，解耦了飞行器实体与具体射击逻辑。
    * 实现了 Boss 专属的 **环射策略 (`RingShootStrategy`)**，单次爆发生态下发射 $$20$$ 颗子弹，形成无死角弹幕压制。
* **Boss 专属豪华掉落机制**
    * 击杀 Boss 敌机后不仅奖励高额分数，还会触发专属掉落逻辑：从全道具池中随机抽取并在 Boss 坠毁位置**并排散开掉落 $$3$$ 个道具**，视觉与收益拉满。
* **内存与性能优化**
    * 越界飞出屏幕的敌机与道具会自动销毁，优化内存占用，彻底解决了旧版本游戏后期停止刷新敌机的 Bug。

---
## ✈️ Aircraft War v4.0 - 数据持久化与质量保证

本项目迎来第四次重大迭代！本次更新专注于**数据访问解耦**与**核心逻辑可靠性**。

### 🌟 核心更新内容

* 💾 **引入 DAO 模式 (数据访问对象)**
  * 设计并实现了 `RecordDao` 接口与 `RecordDaoImpl` 实现类，彻底剥离了游戏业务逻辑与底层文件 I/O 操作。
  * **数据隔离**：实现了按游戏难度（Easy, Normal, Hard）分发不同的本地 `.txt` 存储文件，避免数据互相干扰。
  * **自动排序**：利用 `Comparable` 接口，在每次插入新记录后自动完成降序排列，时间复杂度为 $$O(N \log N)$$。

* 📊 **动态排行榜系统**
  * 游戏结束时（英雄机坠毁），系统会自动记录当前玩家的得分与精确到分钟的系统时间。
  * 在控制台实时打印格式化的榜单信息（包含：名次、玩家名、得分、时间）。

* 🧪 **JUnit5 自动化单元测试**
  * 引入了 JUnit5 测试框架，搭建了专门的 `test` 目录。
  * 针对核心实体类 `HeroAircraft` 编写了严谨的测试用例，涵盖了至少 $$3$$ 个核心业务方法，利用 `@Test` 注解与断言机制 (`assertEquals` 等) 确保了扣血、碰撞等关键逻辑的绝对正确性。

## 🏗️ 核心系统设计图谱 (截至 v3.0)

### 1. 飞行物实体类群 (继承结构)

* **`AbstractFlyingObject`** (所有飞行物的基类)
    * **`AbstractAircraft`** (飞机基类)
        * `HeroAircraft` (英雄机 - 单例)
        * `MobEnemy` (普通敌机)
        * `EliteEnemy` (精英敌机)
        * `AdvancedEnemy` (精锐敌机)
        * `AceEnemy` (王牌敌机)
        * `BossEnemy` (头目敌机) ⭐ *[v3新增] 左右移动、高血量、专属环射*
    * **`BaseBullet`** (子弹基类)
        * `HeroBullet` (英雄子弹)
        * `EnemyBullet` (敌机子弹)
    * **`AbstractProp`** (道具基类)
        * `BloodProp` (加血) / `FireProp` (火力) / `SuperFireProp` (超级火力) / `BombProp` (炸弹) / `FreezeProp` (冰冻)

### 2. 设计模式实践群

* **【工厂方法模式】 `EnemyFactory` 及其实现类**
    * `MobEnemyFactory` / `EliteEnemyFactory` / `AdvancedEnemyFactory` / `AceEnemyFactory`
    * `BossEnemyFactory` ⭐ *[v3新增] 负责配置 Boss 初始坐标、生命值与悬浮属性*
* **【简单工厂模式】 `PropFactory`**
    * 提供静态方法: `createProp(String type, int x, int y)` 生成不同道具。
* **【策略模式】 `ShootStrategy` (射击策略接口)** ⭐ *[v3核心重构]*
    * `StraightShootStrategy` (直射策略 - 供普通/精英机使用)
    * `ScatterShootStrategy` (散射策略 - 供王牌机使用)
    * `RingShootStrategy` (环射策略) ⭐ *[v3新增] Boss 专属，360 度环射子弹计算与生成*
* **【单例模式】 `HeroAircraft`**
    * 全局唯一，通过 `getInstance()` 控制获取。

