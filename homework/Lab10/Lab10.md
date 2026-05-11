# Lab10：为 Lunch Tray 添加导航

## 实验背景

本次实验基于 Jetpack Compose 中的 Navigation 组件，为现有的 Lunch Tray（午餐托盘）点餐应用添加多屏导航功能。

Lunch Tray 是一款交互式午餐点餐应用，包含 5 个页面：开始点餐、选择主菜、选择配菜、选择佐餐、结账。当前起始代码中各个页面的 UI 已经实现，但应用缺少页面之间的导航路由——换句话说，各个屏幕是"孤立"的，用户无法从一个屏幕跳转到另一个屏幕。

本次实验的目标是在现有 UI 基础上，添加 Compose Navigation 组件，构建完整的页面导航流。

**navigation_reference** 为参考文件夹，是课上讲的例子，提交代码时无需提交此文件夹内的任何文件！

---

## 前提条件

- 已掌握 Compose 布局基础和状态管理
- 已学习 [Use Jetpack Navigation in Compose](https://developer.android.com/codelabs/basic-android-kotlin-compose-navigation?hl=zh-cn) 相关内容
- 了解 `NavController`、`NavHost` 和 `composable()` 路由的基本用法
- 了解 Compose 中的 `Scaffold` 和 `TopAppBar` 用法
- 了解 Kotlin 中 `enum class` 和 `sealed class` 的基本概念

---

## 实验目标

完成本实验后，你应能够：

- 使用 `enum class` 定义应用内的各个导航页面
- 创建并初始化 `NavController`，管理返回堆栈
- 构建包含动态标题和返回按钮的 `AppBar`
- 使用 `NavHost` 配置页面之间的导航路由
- 正确管理返回堆栈，避免意外的页面返回行为
- 在 Compose 中构建完整的多屏导航应用
- 编写实验报告说明导航设计和实现思路

---

## 所需资源

### 起始代码

本目录中的 `basic-android-kotlin-compose-training-lunch-tray/` 是 Lunch Tray 的起始项目代码。请在 Android Studio 中打开该目录即可开始实验。

起始代码结构概述：

| 文件 | 说明 |
|------|------|
| `MainActivity.kt` | 应用入口，调用 `LunchTrayApp()` |
| `LunchTrayScreen.kt` | **核心文件，包含 TODO 标记，需要你来完成导航代码** |
| `model/MenuItem.kt` | 菜品数据类（主菜、配菜、佐餐） |
| `model/OrderUiState.kt` | 订单 UI 状态数据类 |
| `datasource/DataSource.kt` | 菜品列表数据源 |
| `ui/StartOrderScreen.kt` | 开始点餐页面 |
| `ui/EntreeMenuScreen.kt` | 主菜选择页面 |
| `ui/SideDishMenuScreen.kt` | 配菜选择页面 |
| `ui/AccompanimentMenuScreen.kt` | 佐餐选择页面 |
| `ui/CheckoutScreen.kt` | 结账页面 |
| `ui/BaseMenuScreen.kt` | 菜单页面的通用 UI 组件 |
| `ui/OrderViewModel.kt` | 订单 ViewModel（已实现，管理订单状态） |
| `ui/theme/` | 主题颜色、字体和 Material 3 主题配置 |

---

## 起始代码现状分析

当前 `LunchTrayScreen.kt` 中存在以下问题：

1. **缺少导航枚举类定义**：需要创建一个 `enum class` 来定义所有页面的名称和标题
2. **缺少导航控制器**：没有 `NavController` 来管理页面跳转和返回堆栈
3. **缺少 AppBar**：`Scaffold` 的 `topBar` 参数为空，没有顶部应用栏
4. **缺少导航路由**：`NavHost` 未配置，各个页面的 composable 路由未定义

项目的 UI 页面和 ViewModel 已经完整实现：
- `StartOrderScreen` 有 "Start Order" 按钮，点击后应跳转到主菜选择页
- 各菜单页面（`EntreeMenuScreen`、`SideDishMenuScreen`、`AccompanimentMenuScreen`）通过 `BaseMenuScreen` 提供了 Cancel / Next 按钮
- `CheckoutScreen` 提供 Cancel / Submit 按钮，显示订单汇总

---

## 实验任务

### 任务一：打开起始项目

在 Android Studio 中打开本目录下的 `basic-android-kotlin-compose-training-lunch-tray/` 项目。

等待 Gradle 同步完成后，浏览项目文件结构，了解各个 Composable 函数的职责和已有代码的组织方式。

---

### 任务二：创建导航枚举类

#### 目标

在 `LunchTrayScreen.kt` 中创建一个 `enum class` 来定义 Lunch Tray 的各个导航页面。

#### 要求

每个枚举值需要关联一个标题字符串（使用 `@StringRes` 注解引用字符串资源）：

| 枚举值 | 标题资源 | 屏幕说明 |
|--------|----------|----------|
| `Start` | `R.string.app_name` | 开始点餐页面 |
| `Entree` | `R.string.choose_entree` | 主菜选择页面 |
| `SideDish` | `R.string.choose_side_dish` | 配菜选择页面 |
| `Accompaniment` | `R.string.choose_accompaniment` | 佐餐选择页面 |
| `Checkout` | `R.string.order_checkout` | 结账页面 |

#### 提示

枚举类需要包含一个 `title` 属性，类型为 `Int`（字符串资源 ID）。使用 `@StringRes` 注解确保只接受字符串资源 ID：

```kotlin
import androidx.annotation.StringRes

enum class LunchTrayScreen(@StringRes val title: Int) {
    Start(R.string.app_name),
    // ... 其余枚举值
}
```

---

### 任务三：创建导航控制器并初始化

#### 目标

在 `LunchTrayApp()` 中创建 `NavController` 实例，并初始化返回堆栈条目和当前页面名称。

#### 步骤

1. 在 `LunchTrayApp()` 函数中，调用 `rememberNavController()` 创建导航控制器：

```kotlin
val navController = rememberNavController()
```

2. 通过 `navController` 获取当前返回堆栈条目：

```kotlin
val backStackEntry by navController.currentBackStackEntryAsState()
```

3. 根据当前路由确定当前页面的名称。如果返回堆栈条目存在，取其 `destination.route`；否则默认为 `Start` 页面名称：

```kotlin
val currentScreen = LunchTrayScreen.valueOf(
    backStackEntry?.destination?.route ?: LunchTrayScreen.Start.name
)
```

> **说明：** `LunchTrayScreen.valueOf(name)` 会根据路由名称找到对应的枚举值，从而获取该页面的标题。

---

### 任务四：创建 AppBar

#### 目标

在 `LunchTrayScreen.kt` 中创建一个 `@Composable` 函数，用于 `Scaffold` 的 `topBar`，显示当前页面的标题和返回按钮。

#### 要求

1. **显示当前页面的标题**：根据传入的 `currentScreen` 参数，动态显示对应标题
2. **返回按钮**：使用 `Icons.Filled.ArrowBack` 图标，点击时调用 `navController.navigateUp()`
3. **返回按钮的显示条件**：当 `navController.previousBackStackEntry != null` 时显示返回按钮（即当前页面不是 `Start` 页面时）
4. **不要**在 `Start` 页面上显示返回按钮

#### 代码结构

```kotlin
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayAppBar(
    currentScreen: LunchTrayScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}
```

---

### 任务五：配置导航宿主（NavHost）

#### 目标

在 `Scaffold` 的 `content` 区域使用 `NavHost` 配置所有页面的导航路由。

#### 导航流程

| 起始页面 | 触发操作 | 目标页面 |
|----------|----------|----------|
| Start | 点击 **Start Order** 按钮 | Entree |
| Entree | 点击 **Next** 按钮 | SideDish |
| SideDish | 点击 **Next** 按钮 | Accompaniment |
| Accompaniment | 点击 **Next** 按钮 | Checkout |
| Checkout | 点击 **Submit** 按钮 | Start |
| **任意页面** | 点击 **Cancel** 按钮 | Start |

#### 关键要求

1. **Start 页面应从返回堆栈中弹出**：当用户从 Start 进入点餐流程后，按系统返回键应退出应用，而不是返回到 Start 页面。使用以下方式导航：

```kotlin
navController.navigate(LunchTrayScreen.Entree.name) {
    popUpTo(LunchTrayScreen.Start.name) { inclusive = true }
}
```

2. **取消操作**（Cancel 按钮）应从任意页面回到 Start，同样需要弹出中间页面：

```kotlin
navController.navigate(LunchTrayScreen.Start.name) {
    popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
}
```

或者更简单地清除整个返回堆栈返回 Start。

3. **每个菜单页面**需要传入 `onCancelButtonClicked` 和 `onNextButtonClicked` 回调
4. **每个菜单页面**需要传入 `onSelectionChanged` 回调来更新 ViewModel 中的订单数据
5. **Checkout 页面**需要传入 `orderUiState` 和 `onCancelButtonClicked` / `onNextButtonClicked` 回调

#### 完整 NavHost 参考

```kotlin
val uiState by viewModel.uiState.collectAsState()

NavHost(
    navController = navController,
    startDestination = LunchTrayScreen.Start.name,
    modifier = Modifier.padding(innerPadding)
) {
    composable(route = LunchTrayScreen.Start.name) {
        StartOrderScreen(
            onStartOrderButtonClicked = {
                navController.navigate(LunchTrayScreen.Entree.name)
            }
        )
    }
    composable(route = LunchTrayScreen.Entree.name) {
        EntreeMenuScreen(
            options = DataSource.entreeMenuItems,
            onCancelButtonClicked = {
                // 取消：返回 Start 并清空返回堆栈
                navController.navigate(LunchTrayScreen.Start.name) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                }
                viewModel.resetOrder()
            },
            onNextButtonClicked = {
                navController.navigate(LunchTrayScreen.SideDish.name)
            },
            onSelectionChanged = { viewModel.updateEntree(it) }
        )
    }
    // ... 其余页面类似
}
```

---

### 任务六：运行并验证

在模拟器或真机上运行应用，检查以下内容：

- 应用可以正常启动，显示 Start 页面
- Start 页面**没有**返回按钮
- 点击 **Start Order** 进入主菜选择页，AppBar 标题变为 "Choose Entree"
- 主菜页面出现返回箭头，点击返回箭头回到上一页
- 依次点击 Next 可以正常导航：Entree → SideDish → Accompaniment → Checkout
- 在任意菜单页面点击 **Cancel** 可以回到 Start 页面
- 在 Checkout 页面点击 **Submit** 可以提交订单并回到 Start 页面
- 从 Start 进入点餐流程后，按系统返回键应退出应用（不会回到 Start）
- 订单汇总信息正确显示（主菜、配菜、佐餐、小计、税、总计）
- 浅色和深色模式下界面正常显示

截图请使用 Android Studio 或模拟器内置截图功能，**严禁使用手机拍屏幕**。

---

## 代码结构参考

完成后的核心代码文件结构：

```text
app/
└── src/
    └── main/
        └── java/com/example/lunchtray/
            ├── MainActivity.kt              # 应用入口
            ├── LunchTrayScreen.kt           # 导航核心：枚举、AppBar、NavHost（本次实验重点）
            ├── model/
            │   ├── MenuItem.kt              # 菜品数据类（sealed class）
            │   └── OrderUiState.kt          # 订单 UI 状态
            ├── datasource/
            │   └── DataSource.kt            # 菜品列表数据源
            └── ui/
                ├── StartOrderScreen.kt      # 开始点餐页面
                ├── EntreeMenuScreen.kt      # 主菜选择页面
                ├── SideDishMenuScreen.kt    # 配菜选择页面
                ├── AccompanimentMenuScreen.kt # 佐餐选择页面
                ├── CheckoutScreen.kt        # 结账页面
                ├── BaseMenuScreen.kt        # 通用菜单 UI 组件
                ├── OrderViewModel.kt        # 订单 ViewModel
                └── theme/
                    ├── Color.kt             # 自定义颜色
                    ├── Theme.kt             # Material 主题
                    └── Type.kt              # 字体样式
```

---

## 提交要求

在自己的文件夹下新建 `Lab10/` 目录，提交以下文件：

```text
学号姓名/
└── Lab10/
    ├── LunchTrayScreen.kt          # 完成后的导航核心代码
    ├── screenshot_navigation.png   # 导航过程中任意菜单页面的截图
    ├── screenshot_checkout.png     # 结账页面的截图
    └── report.md                   # 实验报告
```

> **注意：不要提交整个项目代码。** 只提交上述核心源码文件、截图和报告。

`report.md` 需包含：

1. Compose Navigation 中 `NavController`、`NavHost` 和 `composable()` 三者之间的关系简述
2. `LunchTrayScreen` 枚举类的设计说明（为什么使用枚举而不是直接用字符串）
3. `LunchTrayAppBar` 的设计思路，包括返回按钮的显示条件判断
4. 导航流程的设计说明，特别是返回堆栈管理的考虑（为什么 Start 页面需要被弹出）
5. 实验中遇到的问题与解决过程

---

## 验收标准

满足以下条件可视为完成实验：

- 应用可正常运行，页面间导航流畅
- 创建了 `LunchTrayScreen` 枚举类，包含 5 个页面及其对应的标题
- 使用 `rememberNavController()` 创建了 `NavController`
- 创建了 `LunchTrayAppBar`，动态显示当前页面标题
- Start 页面不显示返回按钮，其他页面正确显示返回箭头
- 返回按钮点击后能正确返回上一页
- 使用 `NavHost` 配置了所有页面的 composable 路由
- 导航流程符合实验要求（Start → Entree → SideDish → Accompaniment → Checkout）
- Cancel 按钮从任意页面正确回到 Start 页面
- Start 页面在进入点餐流程后被正确弹出返回堆栈
- 报告中能清晰说明导航设计思路和返回堆栈管理策略

---

## 提示

- 导航依赖项 `androidx.navigation:navigation-compose` 已在起始代码的 `build.gradle.kts` 中配置好
- `LunchTrayScreen` 枚举使用 `.name` 属性作为路由字符串，需要确保枚举值名称和路由名称一致
- 在菜单页面中，`onSelectionChanged` 需要调用 ViewModel 中对应的方法（`updateEntree`、`updateSideDish`、`updateAccompaniment`）
- Cancel 时除了导航回 Start，还需要调用 `viewModel.resetOrder()` 清空订单
- Submit 时同样需要导航回 Start 并清空订单
- 如果 Preview 报错，可以传入 mock 导航状态或暂时注释掉 Preview
- 使用 `navController.currentBackStackEntryAsState()` 来获取当前返回堆栈条目，从而判断是否显示返回按钮

---

## 截止时间

**2026-05-18**，届时关于 Lab10 的 PR 请求将不会被合并。

---
