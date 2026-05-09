# Lab9 实验报告：为 Dessert Clicker 添加 ViewModel

## 一、ViewModel 在 Android 架构中的作用

ViewModel 是 Android Jetpack 组件库中的核心组件，主要作用包括：

1. **生命周期感知**：ViewModel 的生命周期独立于 Activity/Fragment，能在配置变更（如屏幕旋转）时保留数据，避免重复加载和状态丢失。

2. **状态管理**：集中管理 UI 相关的状态数据，使 UI 层只负责展示和事件触发，逻辑层负责业务处理。

3. **解耦 UI 与业务逻辑**：将业务逻辑从 Activity/Fragment 中分离出来，提高代码的可测试性和可维护性。

4. **数据共享**：多个 Fragment 可以共享同一个 ViewModel 实例，实现数据通信。

## 二、DessertUiState 数据类的字段设计说明

`DessertUiState` 数据类集中管理了应用的所有 UI 状态：

| 字段 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| `revenue` | Int | 0 | 当前总收入（美元） |
| `dessertsSold` | Int | 0 | 已售出的甜品数量 |
| `currentDessertIndex` | Int | 0 | 当前甜品在列表中的索引 |
| `currentDessertImageId` | Int | R.drawable.cupcake | 当前甜品图片资源 ID |
| `currentDessertPrice` | Int | 5 | 当前甜品单价 |

设计考虑：
- 默认值与应用初始状态一致，确保应用启动时显示正确的初始界面
- 使用 `@DrawableRes` 注解标记图片资源 ID，提高代码安全性
- 将分散的状态变量整合到单一数据类中，便于状态的统一管理和传递

## 三、DessertViewModel 的设计思路

### 3.1 状态管理

ViewModel 使用 `mutableStateOf` 包装 `DessertUiState`：

```kotlin
var uiState by mutableStateOf(DessertUiState())
    private set
```

设计要点：
- `mutableStateOf` 使 Compose 能够观察状态变化并自动重组界面
- `private set` 确保状态只能通过 ViewModel 提供的方法修改，保证封装性

### 3.2 方法设计

**`onDessertClicked()`** - 处理甜品点击事件：
1. 获取当前状态
2. 计算新的收入和销售量
3. 根据销售量确定应展示的甜品
4. 使用 `copy()` 方法更新状态，确保状态对象不可变

**`determineDessertToShow()`** - 根据销量决定展示的甜品：
- 遍历甜品列表，找到符合当前销量的最高级甜品
- 该方法从 MainActivity 移入，实现业务逻辑与 UI 层的分离

## 四、MainActivity 重构前后对比分析

### 重构前（问题）
- 所有状态变量（`revenue`、`dessertsSold`、`currentDessertPrice` 等）直接定义在 Composable 函数中
- 点击逻辑直接写在回调中，与 UI 代码混杂
- `determineDessertToShow()` 作为顶层函数存在，职责不清晰
- 状态使用 `rememberSaveable` 管理，虽然能保存状态，但逻辑分散

### 重构后（改进）
- 通过 `viewModel()` 获取 ViewModel 实例
- 所有状态通过 `uiState` 统一访问
- 点击事件委托给 `viewModel.onDessertClicked()` 处理
- 删除了 `determineDessertToShow()` 顶层函数
- UI 层只负责展示和触发事件，不再包含业务逻辑

## 五、重构前后代码结构的区别和感受

### 代码结构对比

| 重构前 | 重构后 |
|--------|--------|
| 状态和逻辑内联在 `DessertClickerApp()` 中 | 状态在 ViewModel 中，UI 只负责展示 |
| `rememberSaveable` 分散管理状态 | `mutableStateOf` 集中管理状态 |
| 业务逻辑与 UI 代码混杂 | 业务逻辑独立在 ViewModel 中 |
| 难以进行单元测试 | 可单独测试 ViewModel |

### 个人感受

重构后代码结构更加清晰，职责分明。UI 层变得简洁，只关注界面展示，而业务逻辑集中在 ViewModel 中，便于维护和测试。特别是在处理屏幕旋转等配置变更时，ViewModel 能够自动保留状态，用户体验更好。

## 六、遇到的问题与解决过程

### 问题 1：ViewModel 依赖未正确配置

**现象**：`viewModel()` 函数无法导入

**解决**：在 `app/build.gradle.kts` 中添加依赖：
```kotlin
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
```

### 问题 2：Preview 函数报错

**现象**：原有 Preview 函数依赖 `DessertClickerApp(desserts = ...)`，重构后参数变更

**解决**：修改 Preview 函数，直接使用 `DessertClickerScreen` 并传入 mock 数据

### 问题 3：状态更新不触发重组

**现象**：点击甜品后界面没有更新

**解决**：确保使用 `uiState.copy()` 创建新的状态对象，而不是直接修改状态字段

### 问题 4：R 类引用错误

**现象**：`DessertUiState` 中无法直接引用 `R.drawable.cupcake`

**解决**：使用完整限定名 `com.example.dessertclicker.R.drawable.cupcake` 或导入 R 类

## 七、总结

通过本次实验，我深入理解了 ViewModel 在 Android 架构中的重要作用，掌握了如何将状态管理和业务逻辑从 UI 层分离出来。重构后的代码结构更清晰、可测试性更强，为后续的功能扩展和维护打下了良好基础。