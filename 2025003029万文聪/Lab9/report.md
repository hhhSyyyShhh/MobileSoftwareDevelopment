# Lab9 实验报告：为 Dessert Clicker 添加 ViewModel

## 一、ViewModel 在 Android 架构中的作用

ViewModel 是 Android 架构组件中负责保存和管理界面相关数据的类。它的生命周期比 Activity 或 Composable 更长，在屏幕旋转等配置变化发生时不会立即销毁，因此可以避免界面状态丢失。

在本实验中，原来的 Dessert Clicker 将收入、销量、当前甜品价格、当前甜品图片以及甜品升级逻辑全部写在 `MainActivity.kt` 的 Composable 中。这样会导致 UI 代码和业务逻辑耦合严重。重构后，ViewModel 负责保存状态和处理点击甜品后的业务逻辑，Composable 只负责展示界面和触发事件，代码结构更清晰，也更符合 Android 推荐的分层思想。

## 二、DessertUiState 数据类字段设计说明

本实验新增了 `DessertUiState` 数据类，用来集中描述界面需要展示的全部状态。它包含以下字段：

| 字段名 | 类型 | 默认值 | 作用 |
|---|---|---|---|
| `revenue` | `Int` | `0` | 记录当前总收入 |
| `dessertsSold` | `Int` | `0` | 记录已经售出的甜品数量 |
| `currentDessertIndex` | `Int` | `0` | 记录当前甜品在甜品列表中的位置 |
| `currentDessertImageId` | `Int` | `R.drawable.cupcake` | 记录当前要显示的甜品图片资源 |
| `currentDessertPrice` | `Int` | `5` | 记录当前甜品的单价 |

这些字段对应原来 `DessertClickerApp()` 中分散定义的多个状态变量。通过 `DessertUiState` 统一管理后，界面只需要读取一个状态对象即可获得全部需要显示的数据。

## 三、DessertViewModel 的设计思路

本实验新增了 `DessertViewModel` 类，并继承自 `ViewModel`。ViewModel 中使用以下方式保存 UI 状态：

```kotlin
var uiState by mutableStateOf(DessertUiState())
    private set
```

这里使用 `mutableStateOf` 是为了让 Compose 能够观察状态变化。当 `uiState` 更新时，相关 Composable 会自动重组并刷新界面。`private set` 保证外部只能读取状态，不能直接修改状态，状态变化必须通过 ViewModel 提供的方法完成。

ViewModel 的核心方法是 `onDessertClicked()`。每次用户点击甜品时，该方法会完成以下操作：

1. 根据当前甜品价格增加总收入；
2. 将已售甜品数量加一；
3. 调用 `determineDessertToShow()` 判断是否需要切换到更高级甜品；
4. 使用 `copy()` 创建新的 `DessertUiState` 对象并更新界面状态。

甜品升级逻辑 `determineDessertToShow()` 被移动到 ViewModel 中，因为它属于应用的业务规则，不属于界面展示代码。这样可以让 `MainActivity.kt` 更专注于 UI 展示。

## 四、MainActivity 重构前后对比分析

### 重构前

重构前，`MainActivity.kt` 中的 `DessertClickerApp()` 直接使用 `rememberSaveable` 和 `mutableStateOf` 管理多个状态变量，例如：

```kotlin
var revenue by rememberSaveable { mutableStateOf(0) }
var dessertsSold by rememberSaveable { mutableStateOf(0) }
var currentDessertPrice by rememberSaveable { mutableStateOf(...) }
var currentDessertImageId by rememberSaveable { mutableStateOf(...) }
```

同时，点击甜品后的收入增加、销量增加、甜品升级判断等逻辑也直接写在 Composable 的点击回调中。这会导致 UI 层代码变得臃肿，业务逻辑和界面代码混在一起。

### 重构后

重构后，`MainActivity.kt` 中通过 `viewModel()` 获取 `DessertViewModel`：

```kotlin
private fun DessertClickerApp(
    viewModel: DessertViewModel = viewModel()
) {
    val uiState = viewModel.uiState
}
```

界面显示的数据都来自 `uiState`，点击事件只调用：

```kotlin
viewModel.onDessertClicked()
```

这样 `MainActivity.kt` 不再直接管理业务状态，也不再包含甜品升级逻辑。Composable 只负责展示收入、销量、甜品图片和分享按钮。

## 五、重构前后代码结构区别和感受

重构前，核心逻辑几乎全部集中在 `MainActivity.kt` 中，虽然代码量不算特别大，但随着功能增加会越来越难维护。重构后，代码结构变成：

```text
MainActivity.kt        负责 Activity 生命周期、Compose UI 和分享功能
DessertViewModel.kt    负责状态管理和点击甜品后的业务逻辑
DessertUiState.kt      负责描述界面状态
Datasource.kt          负责提供甜品数据
Dessert.kt             负责定义甜品数据模型
```

这种结构更加清晰。UI 层只关心“显示什么”和“用户点击后调用什么方法”，ViewModel 层负责“状态如何变化”。这样不仅代码更容易阅读，也方便之后进行测试和扩展。

## 六、遇到的问题与解决过程

1. **问题：`DessertUiState` 中使用 `R.drawable.cupcake` 时需要导入 R 类。**  
   解决方法：在 `DessertUiState.kt` 中添加：
   ```kotlin
   import com.example.dessertclicker.R
   ```

2. **问题：Composable 中原来的状态变量需要全部替换为 `uiState`。**  
   解决方法：将 `revenue`、`dessertsSold`、`currentDessertImageId` 等引用统一替换为 `uiState.revenue`、`uiState.dessertsSold` 和 `uiState.currentDessertImageId`。

3. **问题：原来的 `determineDessertToShow()` 位于 `MainActivity.kt` 中。**  
   解决方法：将该函数移动到 `DessertViewModel` 中，并设置为 `private`，因为它只服务于 ViewModel 内部的业务逻辑。

4. **问题：需要在 Compose 中使用 `viewModel()`。**  
   解决方法：在 `app/build.gradle.kts` 中添加依赖：
   ```kotlin
   implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
   ```
   并在 `MainActivity.kt` 中导入：
   ```kotlin
   import androidx.lifecycle.viewmodel.compose.viewModel
   ```

5. **问题：Gradle 下载可能超时。**  
   解决方法：将 Gradle wrapper 下载地址改为华为云镜像，并将 `networkTimeout` 增大到 `60000`，提高同步成功率。

## 七、实验总结

通过本次实验，我理解了 ViewModel 在 Compose 应用中的作用。ViewModel 可以把状态和业务逻辑从 UI 层中分离出来，避免 Composable 函数承担过多职责。使用 `DessertUiState` 统一管理界面状态后，代码更加清晰；使用 `DessertViewModel` 管理点击事件和甜品升级逻辑后，应用架构更加合理，也更方便后续维护。
