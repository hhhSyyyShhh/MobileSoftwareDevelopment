# Lab9：为 Dessert Clicker 添加 ViewModel

## 实验背景

本次实验基于 Jetpack ViewModel 和 Compose 状态管理知识，对现有的 Dessert Clicker 甜品点击器应用进行架构重构。

Dessert Clicker 是一款点击售卖甜品的游戏应用：点击屏幕中的甜品图片可获得收入，收入积累到一定数量后自动解锁更高级的甜品。当前版本的 Dessert Clicker 将所有应用数据、状态管理和点击逻辑全部内联在 `MainActivity` 的可组合函数中。

本次实验的目标是将这些状态和逻辑从 UI 中提取出来，放入 `ViewModel` 中统一管理，使代码结构更清晰、可测试性更强。

**viewmodel_state_reference**为参考文件夹,是课上讲的例子,提交代码时无需提交此文件夹内的任何文件!

---

## 前提条件

- 已掌握 Compose 布局基础和状态管理
- 已学习 [ViewModel and State in Compose](https://developer.android.com/codelabs/basic-android-kotlin-compose-viewmodel-and-state?hl=zh-cn) 相关内容
- 了解 `remember`、`mutableStateOf` 和 `rememberSaveable` 的用法
- 熟悉 `data class` 和 Compose 中 `by` 委托语法

---

## 实验目标

完成本实验后，你应能够：

- 理解为什么需要将应用逻辑从 UI 层分离
- 创建 `UiState` 数据类来集中描述界面所需的所有数据
- 使用 `ViewModel` 管理应用状态和用户交互逻辑
- 在 Compose 中使用 `viewModel()` 获取 ViewModel 实例
- 重构现有应用，使 UI 只负责展示和触发事件，状态和逻辑集中在 ViewModel 中
- 编写实验报告说明重构过程和设计思路

---

## 所需资源

### 起始代码

本目录中的 `basic-android-kotlin-compose-training-dessert-clicker/` 是 Dessert Clicker 的起始项目代码。请在 Android Studio 中打开该目录即可开始实验。

起始代码结构概述：

| 文件 | 说明 |
|------|------|
| `MainActivity.kt` | 应用入口，包含全部 UI 和业务逻辑 |
| `model/Dessert.kt` | 甜品数据类 |
| `data/Datasource.kt` | 甜品列表数据源 |
| `ui/theme/Color.kt` | 主题颜色定义 |
| `ui/theme/Theme.kt` | Material 3 主题配置 |

> **注意：** 所有起始分支中只保留了 `main` 分支。如果发现任何包含完整答案的分支，说明起始代码有误。

---

## 起始代码现状分析

当前 `MainActivity.kt` 中存在以下问题：

1. **所有应用状态直接内联在 `DessertClickerApp()` 可组合函数中**，包括 `revenue`、`dessertsSold`、`currentDessertIndex`、`currentDessertPrice`、`currentDessertImageId`
2. **点击逻辑直接写在 Composable 函数内部**，点击甜品时直接在回调中更新多个状态
3. **辅助函数 `determineDessertToShow()`** 作为顶层函数与 UI 代码混在一起
4. **分享功能 `shareSoldDessertsInformation()`** 也直接与 Composable 写在同一文件中

这种结构导致 UI 与业务逻辑耦合严重，不利于测试和后期维护。

---

## 实验任务

### 任务一：打开起始项目

在 Android Studio 中打开本目录下的 `basic-android-kotlin-compose-training-dessert-clicker/` 项目。

等待 Gradle 同步完成后，运行应用确认可正常工作：
- 点击甜品图片，收入增加
- 收入达到一定阈值后，甜品自动升级
- 顶部应用栏可分享销售数据

---

### 任务二：添加 ViewModel 依赖项

#### 目标

在项目中添加 `lifecycle-viewmodel-compose` 依赖，使 Compose 可以使用 ViewModel。

#### 步骤

1. 在项目级 `build.gradle.kts` 的 `ext` 块中添加版本号。由于项目使用 Kotlin DSL，需要将版本号变量添加到合适位置。一种简单的方式是直接在 `app/build.gradle.kts` 中添加依赖。

2. 在 `app/build.gradle.kts` 的 `dependencies` 块中添加以下依赖：

```kotlin
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
```

> **说明：** 起始代码中已有 `lifecycle-runtime-ktx` 依赖，新增的 `lifecycle-viewmodel-compose` 提供了 `viewModel()` 组合函数，可在 Composable 中获取 ViewModel 实例。

3. 点击 **Sync Now** 同步项目。

---

### 任务三：创建 UI 状态数据类

#### 目标

创建一个数据类，将当前 `MainActivity` 的 `DessertClickerApp()` 中分散的状态变量集中管理。

#### 分析当前状态

当前 `DessertClickerApp()` 组合函数中包含以下状态：

| 状态变量 | 类型 | 含义 |
|----------|------|------|
| `revenue` | `Int` | 当前总收入 |
| `dessertsSold` | `Int` | 已售甜品总数 |
| `currentDessertIndex` | `Int` | 当前甜品在列表中的索引 |
| `currentDessertPrice` | `Int` | 当前甜品单价 |
| `currentDessertImageId` | `Int` | 当前甜品图片资源 ID |

#### 要求

在 `ui/` 目录下创建 `DessertUiState.kt` 文件，定义 UI 状态数据类：

```kotlin
package com.example.dessertclicker.ui

import androidx.annotation.DrawableRes

data class DessertUiState(
    val revenue: Int = 0,
    val dessertsSold: Int = 0,
    val currentDessertIndex: Int = 0,
    @DrawableRes val currentDessertImageId: Int = R.drawable.cupcake,
    val currentDessertPrice: Int = 5
)
```

#### 字段说明

| 字段 | 默认值 | 含义 |
|------|--------|------|
| `revenue` | `0` | 总收入（美元） |
| `dessertsSold` | `0` | 已售出的甜品数量 |
| `currentDessertIndex` | `0` | 当前甜品在 `Datasource.dessertList` 中的索引 |
| `currentDessertImageId` | `R.drawable.cupcake` | 当前甜品图片（起始为 cupcake） |
| `currentDessertPrice` | `5` | 当前甜品单价（cupcake 单价为 $5） |

> **提示：** 默认值应与起始状态一致：初始甜品为 cupcake，单价 $5，收入和销量为 0。

---

### 任务四：创建 DessertViewModel

#### 目标

创建 ViewModel 类来管理应用状态和所有业务逻辑。

#### 要求

在项目根包路径（`com.example.dessertclicker`）下创建 `DessertViewModel.kt`：

```kotlin
package com.example.dessertclicker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert
import com.example.dessertclicker.ui.DessertUiState

class DessertViewModel : ViewModel() {

    /** 通过 mutableStateOf 持有 UI 状态，Compose 会自动观察变化 */
    var uiState by mutableStateOf(DessertUiState())
        private set

    /** 甜品列表数据 */
    private val desserts = Datasource.dessertList

    /**
     * 处理甜品点击事件
     * 更新时间后的销售量、收入，并判断是否应切换甜品
     */
    fun onDessertClicked() {
        val currentState = uiState

        // 计算新的收入和销售量
        val newRevenue = currentState.revenue + currentState.currentDessertPrice
        val newDessertsSold = currentState.dessertsSold + 1

        // 根据销售量确定应展示的甜品
        val dessertToShow = determineDessertToShow(newDessertsSold)

        // 更新 UI 状态
        uiState = currentState.copy(
            revenue = newRevenue,
            dessertsSold = newDessertsSold,
            currentDessertImageId = dessertToShow.imageId,
            currentDessertPrice = dessertToShow.price
        )
    }

    /**
     * 根据已售数量决定当前应展示的甜品
     */
    private fun determineDessertToShow(dessertsSold: Int): Dessert {
        var dessertToShow = desserts.first()
        for (dessert in desserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                break
            }
        }
        return dessertToShow
    }
}
```

#### 设计要点

1. **`uiState` 使用 `mutableStateOf` 包装**：Compose 可以观察其变化并自动重组界面
2. **`private set`**：外部只能读取状态，修改只能通过 ViewModel 的方法（如 `onDessertClicked()`）完成
3. **`determineDessertToShow()` 从 MainActivity 移入 ViewModel**：该逻辑属于业务规则，不应留在 UI 层
4. **使用 `copy()`** 更新 `DessertUiState`：保证每次点击创建新的状态对象，触发 Compose 重组

---

### 任务五：重构 MainActivity

#### 目标

移除 `MainActivity` 中的状态变量和业务逻辑，改用 ViewModel 驱动 UI。

#### 步骤

**1. 添加 ViewModel 依赖注入**

在 `DessertClickerApp()` 组合函数中通过 `viewModel()` 获取 ViewModel 实例：

```kotlin
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
private fun DessertClickerApp(
    desserts: List<Dessert>,
    viewModel: DessertViewModel = viewModel()
) {
    val uiState = viewModel.uiState
    // ...
}
```

> **提示：** `viewModel()` 是 `lifecycle-viewmodel-compose` 库提供的组合函数，它会返回与当前 Activity/Fragment 生命周期绑定的 ViewModel 实例。

**2. 删除原有的状态变量**

移除以下代码：

```kotlin
// ❌ 删除这些
var revenue by rememberSaveable { mutableStateOf(0) }
var dessertsSold by rememberSaveable { mutableStateOf(0) }
val currentDessertIndex by rememberSaveable { mutableStateOf(0) }
var currentDessertPrice by rememberSaveable { mutableStateOf(desserts[currentDessertIndex].price) }
var currentDessertImageId by rememberSaveable { mutableStateOf(desserts[currentDessertIndex].imageId) }
```

**3. 替换所有状态引用**

| 原引用 | 替换为 |
|--------|--------|
| `revenue` | `uiState.revenue` |
| `dessertsSold` | `uiState.dessertsSold` |
| `currentDessertImageId` | `uiState.currentDessertImageId` |

**4. 替换点击逻辑**

将原来的点击回调：

```kotlin
onDessertClicked = {
    revenue += currentDessertPrice
    dessertsSold++
    val dessertToShow = determineDessertToShow(desserts, dessertsSold)
    currentDessertImageId = dessertToShow.imageId
    currentDessertPrice = dessertToShow.price
}
```

替换为：

```kotlin
onDessertClicked = {
    viewModel.onDessertClicked()
}
```

**5. 分享功能**

分享功能可以保留在 `DessertClickerApp()` 中（它属于 UI 行为），将 `revenue` 和 `dessertsSold` 替换为 `uiState.revenue` 和 `uiState.dessertsSold`：

```kotlin
shareSoldDessertsInformation(
    intentContext = intentContext,
    dessertsSold = uiState.dessertsSold,
    revenue = uiState.revenue
)
```

**6. 删除 `determineDessertToShow()` 顶层函数**

该函数已移入 ViewModel，从 `MainActivity.kt` 中删除。

#### 重构后的 `DessertClickerApp()` 概要

```kotlin
@Composable
private fun DessertClickerApp(
    viewModel: DessertViewModel = viewModel()
) {
    val uiState = viewModel.uiState

    Scaffold(
        topBar = {
            // ... 分享功能使用 uiState.revenue 和 uiState.dessertsSold
        }
    ) { contentPadding ->
        DessertClickerScreen(
            revenue = uiState.revenue,
            dessertsSold = uiState.dessertsSold,
            dessertImageId = uiState.currentDessertImageId,
            onDessertClicked = { viewModel.onDessertClicked() },
            modifier = Modifier.padding(contentPadding)
        )
    }
}
```

> **注意：** 当你使用 `viewModel()` 默认参数时，Preview 仍需要显式传入 mock 数据。可以保留或简化 Preview 函数。

---

### 任务六：运行并验证

在模拟器或真机上运行应用，检查以下内容：

- 应用可以正常启动，不崩溃
- 点击甜品图片，收入正确增加
- 已售甜品数量正确累计
- 收入达到相应阈值时甜品自动升级（如 cupcake → donut → eclair → ... → oreo）
- 分享功能正常工作
- 旋转屏幕后状态不丢失（ViewModel 的生命周期绑定保证了这一点）
- 浅色和深色模式下界面正常显示

截图请使用 Android Studio 或模拟器内置截图功能，**严禁使用手机拍屏幕**。

---

## 代码结构参考

重构后项目核心文件结构：

```text
app/
└── src/
    └── main/
        └── java/com/example/dessertclicker/
            ├── MainActivity.kt              # 应用入口，仅保留 Compose UI 代码
            ├── DessertViewModel.kt          # ViewModel，管理 UI 状态和业务逻辑
            ├── model/
            │   └── Dessert.kt               # 甜品数据类
            ├── data/
            │   └── Datasource.kt            # 甜品列表数据源
            └── ui/
                ├── DessertUiState.kt        # UI 状态数据类
                └── theme/
                    ├── Color.kt             # 自定义颜色
                    └── Theme.kt             # Material 主题
```

---

## 提交要求

在自己的文件夹下新建 `Lab9/` 目录，提交以下文件：

```text
学号姓名/
└── Lab9/
    ├── MainActivity.kt             # 重构后的主界面
    ├── DessertViewModel.kt         # ViewModel 实现
    ├── DessertUiState.kt           # UI 状态数据类
    ├── screenshot_clicking.png     # 点击过程中运行截图
    ├── screenshot_after.png        # 点击若干次后的截图
    └── report.md                   # 实验报告
```

> **注意：不要提交整个项目代码。** 只提交上述核心源码文件、截图和报告。

`report.md` 需包含：

1. ViewModel 在 Android 架构中的作用简述
2. `DessertUiState` 数据类的字段设计说明
3. `DessertViewModel` 的设计思路，包括状态管理和方法设计
4. `MainActivity` 重构前后对比分析
5. 重构前后代码结构的区别和感受
6. 遇到的问题与解决过程

---

## 验收标准

满足以下条件可视为完成实验：

- 应用可正常运行，点击甜品后收入和销量正确更新
- 甜品随销量自动升级，升级规则正确
- 分享功能正常工作
- 创建了 `DessertUiState` 数据类，包含所有必要的 UI 状态字段
- 创建了 `DessertViewModel`，使用 `mutableStateOf` 管理状态
- `uiState` 使用 `private set`，修改只能通过 ViewModel 方法
- `determineDessertToShow()` 逻辑已从 MainActivity 移入 ViewModel
- `MainActivity` 中不再包含 `rememberSaveable` / `mutableStateOf` 状态变量
- `MainActivity` 中不再包含业务逻辑（`determineDessertToShow` 等）
- 旋转屏幕后 ViewModel 保持状态不丢失
- 报告中能清晰说明设计思路和重构过程

---

## 提示

- 同步 Gradle 依赖后确认 `viewModel()` 函数可正常导入
- `DessertUiState` 中的默认值应与起始应用状态一致
- ViewModel 中的 `private set` 是重要的封装手段，不要遗忘
- 重构时可采取渐进方式：先创建 `DessertUiState` → 再创建 `DessertViewModel` → 最后修改 `MainActivity`
- 重构时保留 `MainActivity` 中的原有 Composable 函数（`DessertClickerScreen`、`TransactionInfo` 等），只修改 `DessertClickerApp()` 中的状态管理和点击逻辑
- 如果 Preview 报错，可以传入 mock ViewModel 或暂时注释掉 Preview
- 分享功能 `shareSoldDessertsInformation()` 可以保留在 MainActivity 中，它涉及 `Context` 和 `Intent`，属于平台相关 UI 行为

---

## 截止时间

**2026-05-18**，届时关于 Lab9 的 PR 请求将不会被合并。

---

