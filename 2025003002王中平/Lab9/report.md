# Lab9：为 Dessert Clicker 添加 ViewModel 实验报告
## 一、实验概述
### 1. 实验背景
本次实验基于 Jetpack ViewModel 和 Compose 状态管理知识，对甜品点击器（Dessert Clicker）应用进行架构重构。原应用将所有数据、状态管理和业务逻辑全部内联在`MainActivity`的可组合函数中，UI 与业务逻辑高度耦合，代码可维护性、可测试性差。

### 2. 实验目标
1. 理解 ViewModel 在 Android 架构中的核心作用，实现 UI 层与业务逻辑层分离；
2. 创建`DessertUiState`数据类统一管理界面状态；
3. 编写`DessertViewModel`封装状态和业务逻辑；
4. 重构`MainActivity`，仅保留 UI 展示与事件触发功能；
5. 验证应用功能完整性，屏幕旋转后状态不丢失。



## 二、ViewModel 在 Android 架构中的作用
ViewModel 是 Jetpack 架构组件的核心之一，主要作用如下：
1. **状态管理与生命周期安全**：ViewModel 独立于 UI 生命周期，屏幕旋转、配置变更时不会被销毁，自动保存应用状态，解决了配置变更导致数据丢失的问题；
2. **业务逻辑与 UI 分离**：将数据处理、状态更新、业务规则从 UI 层剥离，UI 仅负责展示数据和响应用户操作，降低代码耦合度；
3. **数据共享**：可在同一个界面的多个可组合函数、Fragment 之间共享数据，避免数据传递混乱；
4. **提升可测试性**：业务逻辑集中在 ViewModel 中，不依赖 Android 框架类，便于单元测试；
5. **状态封装**：通过私有 setter 限制外部直接修改状态，保证状态变更的可控性。

## 三、DessertUiState 数据类字段设计说明
`DessertUiState`是 UI 状态数据类，用于集中封装界面展示所需的所有状态数据，替代原分散的状态变量，代码位于`ui/DessertUiState.kt`。

### 字段设计
| 字段名                | 类型  | 默认值                | 作用说明                     |
| --------------------- | ----- | --------------------- | ---------------------------- |
| revenue               | Int   | 0                     | 记录用户总收入               |
| dessertsSold          | Int   | 0                     | 记录已售出甜品总数量         |
| currentDessertIndex   | Int   | 0                     | 当前甜品在数据源列表中的索引 |
| currentDessertImageId | Int   | R.drawable.cupcake    | 当前甜品的图片资源 ID        |
| currentDessertPrice   | Int   | 5                     | 当前甜品的单价               |

### 设计思路
1. 所有字段均提供默认值，与应用初始状态一致（初始甜品为纸杯蛋糕，单价 5，收入和销量为 0）；
2. 使用`@DrawableRes`注解标注图片资源 ID，提升代码可读性和安全性；
3. 采用数据类结构，方便通过`copy()`方法更新状态，触发 Compose 界面重组。

## 四、DessertViewModel 设计思路
`DessertViewModel`继承自`ViewModel`，是应用的状态管理和业务逻辑核心，代码位于项目根包下。

### 1. 核心成员变量
```kotlin
// 对外只读、对内可修改的 UI 状态
var uiState by mutableStateOf(DessertUiState())
    private set

// 甜品数据源
private val desserts = Datasource.dessertList
```
- 使用`mutableStateOf`包装状态，使 Compose 能够观察状态变化并自动重组界面；
- `private set`实现状态封装，外部只能读取状态，无法直接修改，保证状态安全。

### 2. 核心方法设计
1. **onDessertClicked()**
   处理甜品点击事件，是对外暴露的唯一状态修改入口：
   - 计算新的收入和销量；
   - 调用`determineDessertToShow`判断当前应展示的甜品；
   - 通过`copy()`方法创建新的状态对象，更新 UI 状态。

2. **determineDessertToShow()**
   私有业务逻辑方法，根据已售甜品数量判断解锁的高级甜品，从 UI 层移入 ViewModel，纯业务逻辑与 UI 完全解耦。

### 3. 设计优势
- 单一职责：只负责状态管理和业务逻辑，不涉及任何 UI 代码；
- 状态可控：所有状态变更都通过固定方法执行，避免非法修改；
- 生命周期安全：配置变更时状态自动保留，无需额外处理。

## 五、MainActivity 重构前后对比分析
### 1. 重构前（原始代码）
1. **状态分散**：在`DessertClickerApp`可组合函数中定义`revenue`、`dessertsSold`等多个`rememberSaveable`状态变量；
2. **逻辑耦合**：点击事件、甜品切换逻辑直接写在 UI 回调中；
3. **代码混乱**：业务函数`determineDessertToShow`、分享功能与 UI 代码混合；
4. **维护困难**：修改业务逻辑需要修改 UI 代码，易引发 bug，配置变更状态易丢失。

### 2. 重构后（优化代码）
1. **无状态变量**：删除所有`rememberSaveable`/`mutableStateOf`状态变量，所有状态从`viewModel.uiState`获取；
2. **无业务逻辑**：移除`determineDessertToShow`函数，点击事件仅调用`viewModel.onDessertClicked()`；
3. **UI 职责单一**：仅负责界面展示、用户交互触发和分享功能（UI 行为）；
4. **依赖注入**：通过`viewModel()`组合函数获取 ViewModel 实例，自动绑定生命周期；
5. **状态安全**：屏幕旋转后收入、销量、甜品等级全部保留，无数据丢失。

### 3. 核心代码变化
- 原点击逻辑：直接修改本地状态变量；
- 新点击逻辑：`viewModel.onDessertClicked()`，完全委托 ViewModel 处理；
- 原状态引用：直接使用本地变量；
- 新状态引用：统一使用`uiState.xxx`。

## 六、重构前后代码结构区别与感受
### 1. 代码结构区别
| 维度                | 重构前                          | 重构后                                  |
| ------------------- | ------------------------------- | --------------------------------------- |
| 分层结构            | 单层架构（UI=逻辑）| MVVM 分层架构（UI-ViewModel-Model）|
| 状态管理            | 分散在 Composable 中            | 集中在 ViewModel 中                      |
| 业务逻辑            | 与 UI 代码混合                  | 完全封装在 ViewModel 中                  |
| 可维护性            | 差，修改困难                    | 高，逻辑与 UI 独立修改                   |
| 可测试性            | 低，依赖 Android 框架           | 高，ViewModel 可单独单元测试             |
| 配置变更处理        | 需手动保存状态                  | 自动保存，无需处理                       |

### 2. 个人感受
1. 重构后代码**结构清晰、职责明确**，阅读和修改代码时能快速区分 UI 和逻辑部分；
2. ViewModel 极大简化了状态管理，无需手动处理屏幕旋转等配置变更，开发效率显著提升；
3. 状态封装让代码更安全，避免了外部随意修改状态导致的 bug；
4. 深刻理解了**关注点分离**的架构思想，UI 只做 UI 该做的事，逻辑交给专门的组件处理。

## 七、实验过程中遇到的问题与解决方法
### 问题 1：导入`viewModel()`函数失败
- **原因**：未添加`lifecycle-viewmodel-compose`依赖，或依赖同步失败；
- **解决方法**：在`app/build.gradle.kts`中添加依赖，点击 Sync Now 完成同步，正确导入`androidx.lifecycle.viewmodel.compose.viewModel`。

### 问题 2：状态更新后界面不重组
- **原因**：未使用`mutableStateOf`包装`DessertUiState`，或直接修改状态对象而非创建新对象；
- **解决方法**：使用`mutableStateOf`初始化状态，通过`copy()`方法更新状态，保证每次状态变更生成新对象。

### 问题 3：Preview 预览报错
- **原因**：预览环境无法自动创建 ViewModel 实例；
- **解决方法**：在预览函数中手动传入`DessertViewModel()`实例，保证预览正常显示。

### 问题 4：屏幕旋转后状态丢失
- **原因**：未正确使用 ViewModel，状态仍保存在 UI 层；
- **解决方法**：彻底删除 UI 层所有状态变量，全部依赖 ViewModel 提供的状态，利用 ViewModel 生命周期特性自动保存数据。

## 八、实验验证结果
应用重构后完成以下验证，全部功能正常：
1. 点击甜品图片，收入和销量正确累加；
2. 销量达到阈值后，自动解锁更高级的甜品；
3. 顶部应用栏分享功能正常工作；
4. 旋转屏幕后，收入、销量、甜品等级全部保留；
5. 浅色/深色模式界面显示正常，无崩溃、无异常。

## 九、实验总结
本次实验通过引入 ViewModel 完成了 Dessert Clicker 应用的架构重构，成功实现了 UI 层与业务逻辑层的分离。通过`DessertUiState`统一管理界面状态，`DessertViewModel`封装业务逻辑，让代码结构更清晰、可维护性和可测试性大幅提升。

通过本次实验，我掌握了 Compose 中 ViewModel 的使用方式、状态管理的最佳实践，深刻理解了 Android 官方推荐的 MVVM 架构思想，为后续开发高质量 Android 应用奠定了基础。