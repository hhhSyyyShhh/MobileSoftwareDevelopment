# Lab9 实验报告：为 Dessert Clicker 添加 ViewModel

------

## 1. ViewModel 在 Android 架构中的作用

ViewModel 是 Jetpack Architecture Components 的核心组件，负责在 UI 控制器（Activity / Fragment）与业务数据之间起到**隔离与桥接**的作用。

其核心价值体现在两点：

**生命周期感知性**：ViewModel 的生命周期与 Activity 的显示生命周期解耦。当屏幕旋转、语言切换等"配置变更"发生时，Activity 会销毁重建，而 ViewModel 实例仍然存活，持有的状态不会丢失。

**关注点分离**：ViewModel 只包含 UI 状态和业务逻辑，不持有任何 View 或 Context 引用，使得单元测试可以完全脱离 Android 框架运行。UI 层（Composable）只负责"展示数据"和"上报事件"，不再混杂逻辑。

------

## 2. `DessertUiState` 数据类的字段设计说明

| 字段                    | 类型               | 默认值               | 设计理由                                            |
| ----------------------- | ------------------ | -------------------- | --------------------------------------------------- |
| `revenue`               | `Int`              | `0`                  | 玩家总收入，每次点击累加当前单价                    |
| `dessertsSold`          | `Int`              | `0`                  | 已售出数量，同时作为升级判断的依据                  |
| `currentDessertIndex`   | `Int`              | `0`                  | 当前甜品在列表中的索引，便于调试与后续扩展          |
| `currentDessertImageId` | `@DrawableRes Int` | `R.drawable.cupcake` | 当前甜品图片资源 ID，直接传入 `Image()` 组件        |
| `currentDessertPrice`   | `Int`              | `5`                  | 当前甜品单价（cupcake 为 $5），每次点击据此计算收入 |

使用 `data class` 的优势：`copy()` 方法可以基于现有状态只修改变化的字段，产生新对象触发 Compose 重组，符合不可变状态的设计原则。

所有默认值均与起始应用保持一致（初始为 cupcake，单价 $5，收入和销量为 0）。

------

## 3. `DessertViewModel` 的设计思路

### 状态管理

```kotlin
var uiState by mutableStateOf(DessertUiState())
    private set
```

- 使用 `mutableStateOf` 而非 `StateFlow`：在纯 Compose 项目中，`mutableStateOf` 更简洁，Compose 运行时可直接订阅变化，无需 `collectAsState()`。
- `private set` 是关键的封装手段：外部代码只能读，不能直接写，所有状态变更必须通过 ViewModel 的方法进行，保证了数据流向单一可追溯。

### 方法设计

`onDessertClicked()` 是目前唯一的业务入口：

1. 读取当前状态快照（`val currentState = uiState`）。
2. 计算新的收入和已售数量。
3. 调用私有方法 `determineDessertToShow()` 判断是否需要升级甜品。
4. 使用 `copy()` 生成新状态对象并赋值给 `uiState`，触发 UI 重组。

`determineDessertToShow()` 被声明为 `private`，仅供 ViewModel 内部调用，符合最小暴露原则。

------

## 4. `MainActivity` 重构前后对比分析

### 重构前（问题）

```kotlin
//  状态直接散落在 Composable 函数中
var revenue by rememberSaveable { mutableStateOf(0) }
var dessertsSold by rememberSaveable { mutableStateOf(0) }
// ... 3 个类似的状态变量

//  业务逻辑混在点击回调里
onDessertClicked = {
    revenue += currentDessertPrice
    dessertsSold++
    val dessertToShow = determineDessertToShow(desserts, dessertsSold)
    currentDessertImageId = dessertToShow.imageId
    currentDessertPrice = dessertToShow.price
}
```

问题：

- 状态与 UI 紧耦合，无法单独测试业务逻辑。
- `rememberSaveable` 可以在配置变更时保存简单数据，但无法处理复杂对象，且 ViewModel 方案更规范。
- `determineDessertToShow()` 作为顶层函数与 UI 代码混在同一文件，职责不清晰。

### 重构后（改善）

```kotlin
//  Composable 只声明 ViewModel 引用和 UI 状态读取
@Composable
private fun DessertClickerApp(
    viewModel: DessertViewModel = viewModel()
) {
    val uiState = viewModel.uiState
    // ...
    onDessertClicked = { viewModel.onDessertClicked() }
}
```

改善：

- `DessertClickerApp()` 中零状态变量声明，职责变为纯展示。
- 所有业务规则集中在 `DessertViewModel`，可独立编写单元测试。
- 屏幕旋转后 ViewModel 自动保活，无需额外处理。

------

## 5. 重构前后代码结构区别与感受

**重构前**：`MainActivity.kt` 一个文件承载了入口、状态、逻辑、UI 组件全部内容，文件行数多且难以定位问题。

**重构后**：

```
MainActivity.kt       → 只负责 Compose UI 的声明与事件传递
DessertViewModel.kt   → 负责状态持有与业务逻辑
DessertUiState.kt     → 负责描述 UI 需要什么数据
```

三个文件职责清晰，改动其中一个时对其他文件影响最小。这种分层思路和 MVVM（Model-View-ViewModel）架构完全契合，当应用功能增多时，扩展成本大幅降低。

个人感受：重构初期需要多创建几个文件，感觉"更麻烦了"，但一旦完成后，每个文件的逻辑都简单得多，可读性显著提升。特别是"UI 只读状态、通过方法触发变更"这个约定，能有效避免状态被多处随意修改导致的 bug。

------

## 6. 遇到的问题与解决过程

### 问题一：`DessertUiState.kt` 中引用 `R.drawable.cupcake` 编译报错

**原因**：`R` 类需要正确的包名导入，且 `ui/` 子包需要显式 `import com.example.dessertclicker.R`。

**解决**：在 `DessertUiState.kt` 顶部添加 `import com.example.dessertclicker.R` 即可。

------

### 问题二：Preview 函数使用 `viewModel()` 时报错

**原因**：`viewModel()` 需要 Android 运行时环境（Activity/Fragment），Preview 无法提供。

**解决**：将 Preview 函数改为直接调用 `DessertClickerScreen()`，传入硬编码的 mock 数据，不经过 ViewModel，彻底规避问题。

------

### 问题三：旋转屏幕后状态是否真的保留？

**验证方法**：点击若干次后旋转模拟器，观察收入和销量是否归零。

**结论**：使用 ViewModel 后状态完全保留，而重构前若误用 `mutableStateOf`（未用 `rememberSaveable`）则旋转会丢失状态。这直接说明了 ViewModel 方案的优越性。