# Lab9 实验报告：为 Dessert Clicker 添加 ViewModel

## 1. ViewModel 在 Android 架构中的作用简述

ViewModel 是 Android Jetpack 架构组件之一，专门用于存储和管理与 UI 相关的数据。ViewModel 的核心作用包括：

1. **状态管理与生命周期感知**：ViewModel 与 Activity/Fragment 的生命周期绑定，能够在配置更改（如屏幕旋转、键盘弹出）时保持数据不丢失。相比于直接在 Activity 中使用 `remember` 或 `mutableStateOf`，ViewModel 可以在整个应用生命周期内保持状态。

2. **UI 与业务逻辑分离**：ViewModel 承担了原来写在 UI 层（如 Composable 函数）中的业务逻辑，使得 UI 层只负责展示数据和响应用户交互，提高了代码的可测试性和可维护性。

3. **单向数据流**：通过 ViewModel 管理状态，UI 层只能观察状态的变化（只读），而不能直接修改状态。状态的修改必须通过 ViewModel 暴露的方法进行，这保证了数据流的可控性。

4. **降低耦合度**：ViewModel 不持有 UI 组件的引用（如 Activity、Fragment），因此不受 UI 组件生命周期的影响，减少了内存泄漏的风险。

---

## 2. DessertUiState 数据类的字段设计说明

`DessertUiState` 是集中管理 Dessert Clicker 应用所有界面状态的数据类，包含以下字段：

| 字段名 | 类型 | 默认值 | 说明 |
|--------|------|--------|------|
| `revenue` | `Int` | `0` | 当前总收入（美元），初始值为 0 |
| `dessertsSold` | `Int` | `0` | 已售出的甜品总数，初始值为 0 |
| `currentDessertIndex` | `Int` | `0` | 当前甜品在 `Datasource.dessertList` 中的索引 |
| `currentDessertImageId` | `@DrawableRes Int` | `R.drawable.cupcake` | 当前甜品图片的资源 ID |
| `currentDessertPrice` | `Int` | `5` | 当前甜品的单价（cupcake 为 $5） |

**设计原则**：
- 使用 `data class` 定义，便于复制和比较
- 所有字段提供默认值，与应用初始状态一致
- `@DrawableRes` 注解确保资源 ID 的类型安全
- 字段设计覆盖了 UI 展示所需的所有数据

---

## 3. DessertViewModel 的设计思路

### 3.1 状态管理设计

```kotlin
var uiState by mutableStateOf(DessertUiState())
    private set
```

- 使用 `mutableStateOf` 包装 `DessertUiState`，使 Compose 能够观察状态变化并在状态改变时自动重组 UI
- `private set` 保证外部只能读取状态（`uiState`），修改只能通过 ViewModel 的公共方法（`onDessertClicked()`）进行，实现了良好的封装

### 3.2 方法设计

**`onDessertClicked()` 方法**：
- 负责处理用户点击甜品的逻辑
- 每次点击：收入增加当前甜品价格、销量 +1、判断是否需要升级甜品
- 使用 `copy()` 创建新的 `UiState` 对象，确保 Compose 能检测到状态变化

**`determineDessertToShow()` 方法**：
- 根据已售甜品数量确定当前应展示的甜品
- 该逻辑属于业务规则而非 UI 展示，保留在 ViewModel 中是正确的职责划分
- 使用循环遍历甜品列表，找到第一个 `dessertsSold >= startProductionAmount` 的甜品

### 3.3 设计优势

1. **单一职责**：ViewModel 专注于管理状态和业务逻辑
2. **可测试性**：业务逻辑在 ViewModel 中，可以独立于 UI 进行单元测试
3. **生命周期安全**：ViewModel 会自动管理状态的生命周期，屏幕旋转不丢失数据

---

## 4. MainActivity 重构前后对比分析

### 4.1 重构前的问题

原 `MainActivity.kt` 中的 `DessertClickerApp()` Composable 函数存在以下问题：

```kotlin
// ❌ 重构前的代码问题
var revenue by rememberSaveable { mutableStateOf(0) }
var dessertsSold by rememberSaveable { mutableStateOf(0) }
val currentDessertIndex by rememberSaveable { mutableStateOf(0) }
var currentDessertPrice by rememberSaveable { mutableStateOf(desserts[currentDessertIndex].price) }
var currentDessertImageId by rememberSaveable { mutableStateOf(desserts[currentDessertIndex].imageId) }
```

1. **状态分散**：5 个状态变量分散在函数内部
2. **逻辑耦合**：点击逻辑直接写在 Composable 回调中
3. **职责混乱**：`determineDessertToShow()` 业务逻辑与 UI 代码混在一起

### 4.2 重构后的改进

```kotlin
// ✅ 重构后的代码
@Composable
private fun DessertClickerApp(
    viewModel: DessertViewModel = viewModel()
) {
    val uiState = viewModel.uiState

    // ... UI 只负责展示
}
```

1. **状态集中**：所有 UI 状态在 `DessertUiState` 中统一管理
2. **逻辑分离**：点击逻辑移到 ViewModel 的 `onDessertClicked()` 方法
3. **职责清晰**：UI 层只负责展示，ViewModel 负责状态和逻辑

### 4.3 关键变化

| 项目 | 重构前 | 重构后 |
|------|--------|--------|
| 状态管理 | `rememberSaveable` + `mutableStateOf` | ViewModel + `mutableStateOf` |
| 状态位置 | 分散在 Composable 中 | 集中在 `DessertUiState` |
| 业务逻辑 | 写在 Composable 回调内 | 在 ViewModel 的方法中 |
| `determineDessertToShow()` | 顶层函数在 MainActivity.kt | 私有方法在 ViewModel.kt |
| 屏幕旋转 | 依赖 `rememberSaveable` 保持 | ViewModel 生命周期自动保持 |
| 可测试性 | 低（逻辑与 UI 耦合） | 高（逻辑独立于 UI） |

---

## 5. 重构前后代码结构的区别和感受

### 5.1 文件结构变化

**重构前**：
```
MainActivity.kt（包含所有逻辑）
├── 状态变量（5个）
├── determineDessertToShow()
├── shareSoldDessertsInformation()
└── DessertClickerApp()
    └── DessertClickerScreen()
        └── TransactionInfo()
```

**重构后**：
```
MainActivity.kt（仅 UI 代码）
├── shareSoldDessertsInformation()
├── DessertClickerApp()
├── DessertClickerScreen()
└── TransactionInfo()

DessertViewModel.kt（业务逻辑）
├── uiState
├── onDessertClicked()
└── determineDessertToShow()

DessertUiState.kt（UI 状态）
└── data class DessertUiState
```

### 5.2 重构感受

1. **代码更清晰**：业务逻辑从 UI 代码中分离后，每个组件的职责更加明确
2. **易于维护**：修改业务逻辑时只需关注 ViewModel，不需要在复杂的 UI 代码中查找
3. **便于测试**：可以独立对 ViewModel 进行单元测试，不依赖 Android 环境
4. **生命周期管理更安全**：ViewModel 自动处理配置变更，无需担心数据丢失
5. **学习曲线**：需要理解 `mutableStateOf` 与 Compose 的观察机制，以及 ViewModel 的生命周期

### 5.3 MVVM 架构优势体会

通过本次重构，深刻体会到了 MVVM（Model-View-ViewModel）架构的优势：
- **Model（数据层）**：Dessert、Datasource
- **View（视图层）**：MainActivity、Composable 函数
- **ViewModel（业务逻辑层）**：DessertViewModel、DessertUiState

三层各司其职，通过状态单向流动连接，使得应用结构清晰、易于扩展。

---

## 6. 遇到的问题与解决过程

### 6.1 依赖添加问题

**问题**：初次添加 ViewModel 依赖时不确定版本号。

**解决**：参考实验文档中的版本号 `2.8.7`，与项目中已有的 `lifecycle-runtime-ktx` 版本保持一致。

### 6.2 导入问题

**问题**：使用 `viewModel()` 函数时提示无法找到。

**解决**：确认已添加 `implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")` 依赖，并在代码中添加正确的导入：
```kotlin
import androidx.lifecycle.viewmodel.compose.viewModel
```

### 6.3 Preview 预览问题

**问题**：重构后 Preview 函数需要传入 ViewModel 实例。

**解决**：由于 `DessertClickerApp()` 使用了默认参数 `viewModel = viewModel()`，Preview 可以直接调用无需传参，Compose 会自动提供预览用的 ViewModel。

### 6.4 状态更新不触发 UI 重组

**问题**：最初尝试直接修改 `uiState` 的属性，没有触发 UI 更新。

**解决**：正确使用 `copy()` 方法创建新的 `UiState` 对象：
```kotlin
uiState = currentState.copy(
    revenue = newRevenue,
    dessertsSold = newDessertsSold,
    // ...
)
```

这是因为 Compose 只检测对象引用的变化，使用 `copy()` 可以创建新对象，确保 Compose 能够观察到变化。

---

## 总结

本次实验成功完成了 Dessert Clicker 应用的 ViewModel 重构，达到了以下目标：

1. ✅ 创建了 `DessertUiState` 数据类集中管理 UI 状态
2. ✅ 创建了 `DessertViewModel` 管理业务逻辑
3. ✅ 重构了 `MainActivity` 使其只负责 UI 展示
4. ✅ 添加了 ViewModel 依赖并正确配置
5. ✅ 理解了 MVVM 架构的优势和实现方式

重构后的代码结构清晰、职责分明、易于测试和维护，符合现代 Android 开发最佳实践。
