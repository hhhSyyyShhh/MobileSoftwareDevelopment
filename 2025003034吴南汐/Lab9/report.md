# Android 甜点点击器应用实验报告
## 一、实验概述
本实验基于**Jetpack Compose**与**MVVM架构**实现甜点点击器应用，核心是通过ViewModel分离UI与业务逻辑，使用UiState统一管理界面状态，完成点击甜点累加收益、切换甜点、分享数据等功能，验证Android架构组件在解耦、状态管理与可维护性上的优势。

## 二、ViewModel在Android架构中的作用简述
ViewModel是Android Jetpack中用于**管理UI相关数据**、**分离UI与业务逻辑**的核心组件，主要作用如下：
1. **数据存储与生命周期安全**：独立于Activity/Fragment生命周期，配置变更（如屏幕旋转）时数据不丢失。
2. **业务逻辑封装**：将点击处理、状态计算、数据判断等逻辑从UI层剥离，UI仅负责展示与交互。
3. **状态统一管理**：集中维护界面所需数据，避免多组件数据不一致。
4. **解耦UI与数据**：Activity/Compose只观察状态，不持有业务逻辑，便于测试与维护。

## 三、DessertUiState数据类字段设计说明
DessertUiState是**界面状态数据类**，用于封装所有UI展示所需数据，字段设计如下：
1. **revenue: Int = 0**：记录总收益，初始值为0，点击甜点时累加当前甜点价格。
2. **dessertsSold: Int = 0**：记录已售出甜点数量，初始值为0，每次点击+1。
3. **currentDessertImageId: Int = R.drawable.cupcake**：当前展示甜点的图片资源ID，默认显示纸杯蛋糕。
4. **currentDessertPrice: Int = 5**：当前甜点单价，默认5，随甜点切换自动更新。

设计原则：**不可变数据类**，通过copy()更新状态，保证线程安全与状态可预测。

## 四、DessertViewModel设计思路
### （一）状态管理
1. 采用**mutableStateOf**管理UiState，提供私有set确保状态只能内部修改。
2. 所有界面状态集中在uiState对象，Compose可自动感知状态变化并重组UI。
3. 状态更新使用copy()方法，保留不可变性，避免直接修改原对象。

### （二）方法设计
1. **onDessertClicked()**：核心点击处理方法
    - 读取当前状态，计算新收益与销量。
    - 调用determineDessertToShow()匹配应展示的甜点。
    - 用copy()更新uiState，触发UI刷新。
2. **determineDessertToShow()**：甜点切换逻辑
    - 根据已售数量，按startProductionAmount阈值匹配对应甜点。
    - 遍历甜点列表，满足销量条件则更新展示甜点，实现渐进式解锁。

## 五、MainActivity重构前后对比分析
### （一）重构前（传统写法）
1. 逻辑与UI耦合：Activity内直接处理点击、数据计算、状态存储。
2. 生命周期风险：数据随Activity重建丢失，需手动保存/恢复。
3. 代码臃肿：布局、逻辑、事件处理混杂，难以维护。
4. 无状态管理：数据分散，易出现不一致。

### （二）重构后（MVVM+Compose）
1. 职责单一：Activity仅负责初始化Compose与主题，无业务逻辑。
2. 状态托管：UI状态由ViewModel管理，配置变更不丢失。
3. 响应式UI：Compose观察UiState自动刷新，无需手动更新视图。
4. 可复用性：UI组件与业务逻辑分离，便于单元测试。

## 六、重构前后代码结构区别与感受
### （一）代码结构区别
1. **耦合度**：重构前高耦合，Activity承担全部工作；重构后UI-ViewModel-数据三层分离。
2. **状态管理**：重构前数据散在Activity成员变量；重构后统一用UiState管理。
3. **逻辑位置**：点击、计算、判断逻辑从Activity移入ViewModel。
4. **UI写法**：从XML布局转为Compose声明式UI，更简洁直观。

### （二）开发感受
1. 代码更清晰：职责明确，阅读与修改更轻松。
2. 状态更稳定：无需担心旋转等场景数据丢失。
3. 扩展性更强：新增甜点/修改价格只需调整数据源，不影响UI。
4. 可测试性提升：ViewModel逻辑可单独单元测试，不依赖Android框架。

## 七、遇到的问题与解决过程
### 问题1：TopAppBar颜色设置报错
- 原因：使用旧版TopAppBar颜色API，与新版本Material3不兼容。
- 解决：改用**TopAppBarDefaults.topAppBarColors()**，正确配置containerColor与titleContentColor。

### 问题2：点击甜点后UI不刷新
- 原因：直接修改uiState成员变量，未触发Compose重组。
- 解决：使用**copy()**方法创建新状态对象，保证状态可观察。

### 问题3：分享功能无法跳转
- 原因：未获取正确Context，Intent创建错误。
- 解决：在Compose中用**LocalContext.current**获取上下文，正确构建分享Intent。

### 问题4：甜点切换逻辑异常
- 原因：determineDessertToShow循环判断条件错误，未正确匹配销量阈值。
- 解决：修正循环逻辑，按startProductionAmount从小到大遍历，满足条件则更新展示甜点。

## 八、实验总结
通过本次实验，掌握了**ViewModel**在MVVM架构中的核心作用、**UiState**状态设计规范，以及Jetpack Compose与ViewModel协作开发流程。重构后代码**解耦彻底、状态稳定、易于维护**，验证了Android官方架构在实际开发中的优势，为后续复杂应用开发奠定基础。