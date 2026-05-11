# Lab9：为 Dessert Clicker 添加 ViewModel

# Lab9 实验报告：为 Dessert Clicker 添加 ViewModel

## 1. ViewModel 在 Android 架构中的作用简述
ViewModel 是 Android Jetpack 组件库的核心组件之一，主要作用是：
- **生命周期感知**：ViewModel 独立于 Activity/Fragment 的配置变更（如屏幕旋转），可持久化保存数据，避免重建时丢失状态；
- **分离关注点**：将 UI 状态和业务逻辑从 UI 层（Activity/Composable）抽离，使 UI 仅负责展示和事件触发，ViewModel 负责状态管理和逻辑处理；
- **可测试性**：ViewModel 不依赖 UI 上下文，可单独编写单元测试验证业务逻辑；
- **数据共享**：可在 Fragment 之间或 Activity 与 Fragment 之间共享数据，降低组件间耦合。

## 2. DessertUiState 数据类的字段设计说明
DessertUiState 用于集中管理界面所需的所有状态，字段设计遵循「最小必要」和「单一数据源」原则：
| 字段 | 类型 | 默认值 | 设计目的 |
|------|------|--------|----------|
| revenue | Int | 0 | 存储当前总收入，是核心业务指标，需实时展示 |
| dessertsSold | Int | 0 | 存储已售甜品总数，用于计算解锁高级甜品的阈值 |
| currentDessertIndex | Int | 0 | 记录当前甜品在数据源列表中的索引，备用字段（本次重构后主要用 imageId/price 直接展示） |
| currentDessertImageId | Int | R.drawable.cupcake | 当前展示的甜品图片资源 ID，直接用于 UI 渲染 |
| currentDessertPrice | Int | 5 | 当前甜品单价，用于计算点击后的收入增长 |

默认值与应用初始状态一致（初始甜品为纸杯蛋糕，单价 5 美元，收入/销量为 0），保证应用启动时的初始状态正确。

## 3. DessertViewModel 的设计思路
### 3.1 状态管理
- 使用 `mutableStateOf` 包装 `DessertUiState`：Compose 可观察该状态变化，自动触发 UI 重组；
- 给 `uiState` 加 `private set` 修饰符：禁止外部直接修改状态，仅允许通过 ViewModel 暴露的方法（如 `onDessertClicked`）修改，保证状态变更的可控性和可追溯性。

### 3.2 方法设计
- `onDessertClicked()`：核心交互方法，处理甜品点击事件：
  1. 获取当前状态快照；
  2. 计算点击后的新收入和新销量；
  3. 调用 `determineDessertToShow` 确定应展示的新甜品；
  4. 通过 `copy` 方法创建新的 `DessertUiState` 实例，更新状态（不可变数据模式，保证状态变更可追踪）；
- `determineDessertToShow()`：私有方法，封装「根据销量解锁甜品」的业务规则，从 MainActivity 迁移而来，使 UI 层无需关注甜品升级逻辑。

## 4. MainActivity 重构前后对比分析
### 重构前
- 状态分散：`revenue`、`dessertsSold` 等多个 `rememberSaveable` 状态变量散落在 `DessertClickerApp` 中；
- 逻辑耦合：点击事件处理、甜品升级逻辑直接写在 Composable 回调中；
- 代码冗余：`determineDessertToShow` 作为顶层函数与 UI 代码混在一起；
- 状态易失：依赖 `rememberSaveable` 保存状态，虽能应对配置变更，但代码侵入性强。

### 重构后
- 状态集中：所有 UI 状态收敛到 `DessertUiState`，通过 ViewModel 统一管理；
- 逻辑解耦：UI 层仅通过 `viewModel.onDessertClicked()` 触发事件，不关心具体逻辑；
- 代码简洁：删除了所有 `rememberSaveable` 状态变量和顶层业务函数，UI 代码仅负责渲染；
- 生命周期安全：ViewModel 自动处理配置变更，无需手动保存状态。

## 5. 重构前后代码结构的区别和感受
### 结构区别
| 重构前 | 重构后 |
|--------|--------|
| 所有状态和逻辑在 MainActivity 中 | 状态 → DessertUiState，逻辑 → DessertViewModel，UI → MainActivity |
| Composable 包含业务逻辑 | Composable 仅接收状态和事件回调 |
| 依赖 rememberSaveable 保存状态 | 依赖 ViewModel 生命周期保存状态 |

### 主观感受
- 代码职责更清晰：UI 层只做「展示」，ViewModel 只做「逻辑」，符合「单一职责原则」；
- 可维护性提升：后续修改甜品升级规则时，只需改 ViewModel，无需触碰 UI 代码；
- 扩展性更好：如需添加数据持久化（如 Room），可直接在 ViewModel 中集成，不影响 UI；
- 调试更简单：状态变更全部通过 ViewModel 方法触发，可在方法内加日志追踪状态变化。

## 6. 遇到的问题与解决过程
### 问题1：ViewModel 依赖导入失败
- 现象：`viewModel()` 函数无法识别；
- 原因：未添加 `lifecycle-viewmodel-compose` 依赖；
- 解决：在 `app/build.gradle.kts` 中添加 `implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")`，同步 Gradle 后解决。

### 问题2：Preview 函数报错
- 现象：重构后 Preview 因依赖 ViewModel 无法渲染；
- 原因：Preview 环境无法自动创建 ViewModel 实例；
- 解决：简化 Preview 函数，直接传入模拟的状态参数（如固定 revenue=100、dessertsSold=20），不依赖 ViewModel。

### 问题3：状态更新后 UI 不重组
- 现象：点击甜品后收入未变化；
- 原因：最初误将 `uiState` 声明为 `val` 且未用 `mutableStateOf` 包装；
- 解决：修正为 `var uiState by mutableStateOf(DessertUiState())`，保证状态变更可被 Compose 观察。

### 问题4：分享功能数据错误
- 现象：分享文本中的销量/收入始终为 0；
- 原因：分享时仍引用旧的局部变量，未改为 `uiState.dessertsSold`/`uiState.revenue`；
- 解决：替换分享函数中的参数为 ViewModel 管理的状态。

