# Lab9 Dessert Clicker ViewModel 实验报告

---

## ViewModel 在 Android 架构中的作用简述
ViewModel 是 Jetpack 核心架构组件，独立于 Activity/Fragment 生命周期；屏幕旋转、配置变更时不会销毁，**自动保留页面状态**，避免数据重置。
主要作用：
1.  分离 UI 与业务逻辑，把计算、判断、数据逻辑从界面代码抽离；
2.  统一管理页面所有状态，作为唯一可信数据源；
3.  生命周期安全，配置变更自动保存状态；
4.  不依赖 View/Context，方便单元测试，代码可维护性更高；
5.  配合 Compose 实现**单向数据流**：状态向下、事件向上。

---

## DessertUiState 数据类的字段设计说明
使用数据类集中存放页面所有 UI 状态，避免状态散乱，统一管理：
- `revenue`：总收入金额，每次点击累加；
- `dessertsSold`：已卖出甜品总数，用于判断甜品升级阈值；
- `currentDessertIndex`：当前甜品在列表中的下标；
- `currentDessertImageId`：当前展示甜品图片资源 ID；
- `currentDessertPrice`：当前甜品单价，用于计算收入。

所有状态统一封装在一个类中，UI 只读取这个类，结构清晰、便于维护。

---

## DessertViewModel 的设计思路
### 状态管理设计
1.  用 `mutableStateOf` 包装 UiState，Compose 可自动监听状态变化、自动刷新界面；
2.  `private set` 限制外部只能读不能改，只能由 ViewModel 内部更新，保证单向数据流；
3.  通过 data class 的 `copy()` 方式更新状态，不修改原对象，符合 Compose 不可变状态思想。

### 方法设计
1.  `onDessertClicked()`：对外暴露的点击事件入口，处理销量、收入累加，自动判断切换甜品，更新 UI 状态；
2.  `determineDessertToShow()`：私有内部方法，根据已售数量匹配对应等级甜品，封装业务规则，不和 UI 耦合。

整体思路：**UI 只发事件，ViewModel 只管逻辑和状态**。

---

## MainActivity 重构前后对比分析
1.  **状态方面**
    重构前：用多个 `rememberSaveable` 分散存状态，散落在界面代码里，难管理、屏幕旋转易乱。
    重构后：所有状态统一从 ViewModel 的 UiState 获取，集中可控，配置变更不丢失。

2.  **逻辑方面**
    重构前：收入计算、甜品判断逻辑直接写在 UI 点击事件里，UI 和业务逻辑混在一起。
    重构后：所有业务逻辑全部移入 ViewModel，MainActivity 只负责写界面、传点击事件，不做任何计算判断。

3.  **代码职责**
    重构前：一个 Activity 包揽界面、状态、业务逻辑、数据判断，臃肿耦合。
    重构后：分层清晰，UI 层只做展示，ViewModel 做逻辑与状态。

---

## 重构前后代码结构的区别和感受
### 结构区别
- 重构前：所有代码都在 MainActivity，没有分层，模型、数据、逻辑、UI 全部混杂；
- 重构后：拆分出 `Dessert.kt` 模型、`Datasource.kt` 数据源、`DessertUiState.kt` 状态类、`DessertViewModel.kt` 业务层，结构分层规范。

### 个人感受
重构后代码条理更清晰，各司其职；后续改价格、改图片、改升级规则不用动 UI 代码，只改 ViewModel 和数据源即可；同时解决了屏幕旋转数据重置问题，更符合 Android 正规开发架构，更容易读懂和后续扩展。

---

## 遇到的问题与解决过程
1.  **导入报错 Unresolved reference Preview / viewModel / compose**
    - 原因：缺少 Compose、ViewModel 相关依赖。
    - 解决：在 `build.gradle.kts` 补全 `compose-bom`、`ui-tooling-preview`、`lifecycle-viewmodel-compose` 依赖，同步 Gradle 后恢复正常。

2.  **所有甜品只显示同一张图片**
    - 原因：给四个甜品配置了同一个图片资源。
    - 解决：在 `Datasource` 里为每个甜品分别绑定不同 `drawable` 图片 ID，达到对应销量自动切换图片。

3.  **TopAppBar 实验 API 警告**
    - 原因：Material3 控件属于实验 API。
    - 解决：添加 `@OptIn(ExperimentalMaterial3Api::class)` 注解，消除警告正常运行。

4.  **屏幕旋转后数据重置**
    - 原因：状态写在 UI 局部 `remember` 中。
    - 解决：改用 ViewModel 托管状态，利用其独立生命周期，旋转后数据保留不丢失。