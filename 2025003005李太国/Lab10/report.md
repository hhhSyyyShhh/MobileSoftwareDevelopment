# Lab10 实验报告：为 Lunch Tray 添加导航

## 一、Compose Navigation 核心组件关系

在 Jetpack Compose Navigation 中，`NavController`、`NavHost` 和 `composable()` 三者构成了完整的导航体系：

### 1. NavController
- **作用**：导航控制器，负责管理应用的导航状态和返回堆栈
- **职责**：处理页面跳转、返回、弹出堆栈等操作
- **创建方式**：`rememberNavController()` - 返回一个可观察的导航控制器实例

### 2. NavHost
- **作用**：导航宿主，作为所有可导航页面的容器
- **职责**：根据当前路由动态显示对应的页面
- **参数**：
  - `navController`：绑定的导航控制器
  - `startDestination`：初始显示的页面路由
  - `modifier`：布局修饰符

### 3. composable()
- **作用**：定义单个导航目的地（页面）
- **职责**：将路由字符串映射到具体的 Composable 函数
- **参数**：
  - `route`：路由名称，用于页面间导航
  - `content`：页面的 Composable 实现

### 三者关系总结
```
NavController (管理导航状态)
       │
       ▼
NavHost (宿主容器)
       │
       ├── composable("Start") { StartScreen() }
       ├── composable("Entree") { EntreeScreen() }
       ├── composable("SideDish") { SideDishScreen() }
       ├── composable("Accompaniment") { AccompanimentScreen() }
       └── composable("Checkout") { CheckoutScreen() }
```

## 二、LunchTrayScreen 枚举类设计说明

### 为什么使用枚举而不是直接用字符串？

使用 `enum class` 定义导航页面具有以下优势：

| 特性 | 使用枚举 | 使用字符串 |
|------|----------|------------|
| **类型安全** | 编译时检查，避免拼写错误 | 运行时才发现错误 |
| **可读性** | 语义清晰，如 `LunchTrayScreen.Entree` | 字符串字面量，易混淆 |
| **可维护性** | 统一管理，修改一次即可 | 需要查找所有使用位置 |
| **扩展性** | 可添加属性和方法 | 只能是纯字符串 |
| **IDE 支持** | 自动补全、重命名重构 | 无智能提示 |

### 枚举类设计

```kotlin
enum class LunchTrayScreen(@StringRes val title: Int) {
    Start(R.string.app_name),
    Entree(R.string.choose_entree),
    SideDish(R.string.choose_side_dish),
    Accompaniment(R.string.choose_accompaniment),
    Checkout(R.string.order_checkout)
}
```

**设计要点**：
- 使用 `@StringRes` 注解确保只接受字符串资源 ID
- 每个枚举值关联对应的标题资源，实现动态标题显示
- 使用 `.name` 属性作为路由字符串，保持一致性

## 三、LunchTrayAppBar 设计思路

### 核心功能

1. **动态标题显示**：根据当前页面自动切换标题
2. **条件返回按钮**：只在非 Start 页面显示返回箭头
3. **统一样式**：使用 Material 3 的 `TopAppBar` 组件

### 返回按钮显示条件

```kotlin
val canNavigateBack = navController.previousBackStackEntry != null
```

**判断逻辑**：
- `previousBackStackEntry` 不为 null 表示存在上一页
- Start 页面作为初始页面，返回堆栈为空，`previousBackStackEntry` 为 null
- 进入点餐流程后，后续页面的返回堆栈中存在前一页

### AppBar 实现要点

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayAppBar(
    currentScreen: LunchTrayScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
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

## 四、导航流程设计说明

### 导航流程图

```
Start ──[Start Order]──▶ Entree ──[Next]──▶ SideDish ──[Next]──▶ Accompaniment ──[Next]──▶ Checkout
 ▲                                                              │
 └───────────────────────────[Cancel/Submit]─────────────────────┘
```

### 返回堆栈管理策略

#### 1. Start 页面弹出

```kotlin
navController.navigate(LunchTrayScreen.Entree.name) {
    popUpTo(LunchTrayScreen.Start.name) { inclusive = true }
}
```

**设计原因**：
- 从 Start 进入点餐流程后，用户按系统返回键应退出应用
- 如果不弹出 Start，返回键会回到 Start 页面，导致用户体验不佳
- `inclusive = true` 表示同时弹出 Start 页面本身

#### 2. Cancel 操作

```kotlin
navController.navigate(LunchTrayScreen.Start.name) {
    popUpTo(navController.graph.findStartDestination().id) {
        inclusive = true
    }
}
viewModel.resetOrder()
```

**设计原因**：
- 清空整个返回堆栈，确保回到 Start 后按返回键退出应用
- 调用 `viewModel.resetOrder()` 重置订单状态，避免状态污染
- 使用 `findStartDestination()` 确保正确找到起始页面

#### 3. Submit 操作

与 Cancel 操作类似，但代表订单完成。

### 导航流程详细说明

| 页面 | 操作 | 导航目标 | 返回堆栈变化 |
|------|------|----------|--------------|
| Start | Start Order | Entree | Start 被弹出 |
| Entree | Next | SideDish | Entree → SideDish |
| SideDish | Next | Accompaniment | Entree → SideDish → Accompaniment |
| Accompaniment | Next | Checkout | 完整堆栈 |
| 任意 | Cancel | Start | 清空堆栈 |
| Checkout | Submit | Start | 清空堆栈 |

## 五、实验中遇到的问题与解决过程

### 问题 1：找不到导航依赖

**现象**：`rememberNavController()` 无法识别

**解决**：在 `app/build.gradle.kts` 中添加依赖：
```kotlin
implementation("androidx.navigation:navigation-compose:2.7.7")
```

### 问题 2：路由名称不匹配

**现象**：导航时找不到目标页面

**解决**：确保枚举值名称与路由字符串一致：
```kotlin
// 正确：使用枚举的 .name 属性
navController.navigate(LunchTrayScreen.Entree.name)

// 错误：手动拼写字符串容易出错
navController.navigate("entree")
```

### 问题 3：Start 页面返回按钮显示

**现象**：Start 页面也显示了返回按钮

**解决**：正确判断返回堆栈状态：
```kotlin
val canNavigateBack = navController.previousBackStackEntry != null
```

### 问题 4：订单状态未重置

**现象**：Cancel 后重新开始订单，之前的选择仍然存在

**解决**：在导航回 Start 时调用 `viewModel.resetOrder()`

### 问题 5：Preview 报错

**现象**：`LunchTrayApp()` 的 Preview 无法运行

**解决**：创建独立的 Preview 函数，传入 mock 数据：
```kotlin
@Preview
@Composable
fun StartScreenPreview() {
    LunchTrayTheme {
        StartOrderScreen(onStartOrderButtonClicked = {})
    }
}
```

## 六、总结

通过本次实验，我深入理解了 Jetpack Compose Navigation 的核心概念和使用方法：

1. **导航三要素**：`NavController` 管理状态、`NavHost` 作为容器、`composable()` 定义页面
2. **枚举类优势**：类型安全、可读性强、易于维护
3. **返回堆栈管理**：合理的堆栈操作是良好用户体验的关键
4. **状态同步**：导航时需要同步更新 ViewModel 状态

实验完成后，Lunch Tray 应用实现了完整的多屏导航功能，用户可以顺畅地完成从开始点餐到结账的完整流程。