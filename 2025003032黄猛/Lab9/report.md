# Lab9 实验报告

## 1. ViewModel 的作用

ViewModel 负责保存和管理界面相关状态，并把业务逻辑从 UI 层分离出去。它可以在配置变更时保留状态，避免旋转屏幕后数据丢失，也让代码更容易测试和维护。

## 2. `DessertUiState` 字段设计

- `revenue`：当前总收入，默认 0。
- `dessertsSold`：已售甜品数量，默认 0。
- `currentDessertIndex`：当前甜品索引，默认 0。
- `currentDessertImageId`：当前展示的甜品图片，默认 `R.drawable.cupcake`。
- `currentDessertPrice`：当前甜品单价，默认 5。

这些字段覆盖界面展示和点击计算所需的核心状态，便于用一个数据类统一管理。

## 3. `DessertViewModel` 设计思路

ViewModel 中使用 `mutableStateOf` 持有 `DessertUiState`，Compose 可以自动观察变化并重组界面。点击甜品时只调用 `onDessertClicked()`，在 ViewModel 内统一完成收入累加、销量累加和甜品升级判断。`uiState` 使用 `private set`，保证外部只能读不能直接改。

## 4. `MainActivity` 重构前后对比

重构前，`MainActivity` 里直接保存多个状态变量，还包含点击逻辑和甜品切换逻辑，UI 与业务紧密耦合。重构后，`MainActivity` 只负责创建界面，状态和规则全部放入 `DessertViewModel`，UI 只接收 `uiState` 并触发事件。

## 5. 结构变化与体会

重构后代码层次更清楚：数据状态集中、业务逻辑集中、UI 只做展示。这样以后如果要增加新规则或新页面，只需要优先改 ViewModel，不必在 UI 中来回修改多个状态。

## 6. 遇到的问题与解决过程

主要问题是忘记添加 `lifecycle-viewmodel-compose` 依赖会导致 `viewModel()` 无法导入。另一个问题是 Preview 不能直接依赖 `viewModel()`，所以预览时改为传入固定的 `DessertUiState` 和空回调。截图按要求已跳过。
