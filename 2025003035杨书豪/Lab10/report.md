# Lab10 实验报告：为 Lunch Tray 添加导航

------

## 1. NavController、NavHost 与 composable() 三者的关系

三者共同构成 Compose Navigation 的核心架构，职责各不相同：

**NavController** 是导航的"大脑"，负责维护返回堆栈（Back Stack）并提供 `navigate()`、`navigateUp()` 等 API。它是状态的持有者，通过 `rememberNavController()` 创建并在重组（recomposition）后保持存活。

**NavHost** 是导航的"容器"，将 `NavController` 与一组路由绑定在一起，并负责在屏幕上显示当前目标页面（destination）。它接收 `navController` 和 `startDestination` 两个必要参数，并通过 DSL 块声明所有可导航的目标。

**composable()** 是 NavHost DSL 中注册单个页面路由的函数，每次调用都把一个路由字符串（`route`）映射到对应的 Composable 函数。当 `NavController` 执行 `navigate("SomePath")` 时，NavHost 就会找到对应的 `composable(route = "SomePath")` 并渲染其内容。

三者的关系可以概括为：**NavController 发出指令 → NavHost 响应并切换 → composable() 提供目标页面内容**。

------

## 2. LunchTrayScreen 枚举类的设计说明

### 为什么使用枚举而不是直接用字符串？

直接使用裸字符串（如 `"Start"`、`"Entree"`）存在以下问题：

- **编译期无法检测拼写错误**：一旦字符串写错，运行时才会崩溃，且错误难以定位。
- **标题资源分散**：需要在多处手动维护路由字符串和对应标题的映射关系，容易遗漏或不一致。
- **可读性差**：代码中出现大量魔法字符串，维护成本高。

使用 `enum class LunchTrayScreen(@StringRes val title: Int)` 的好处：

- **类型安全**：路由名称通过 `.name` 属性自动生成，和枚举值名称始终保持一致，消除了手动输入字符串的风险。
- **集中管理**：每个页面的路由名称和标题资源 ID 都定义在同一个枚举值中，新增或修改页面只需改一处。
- **IDE 支持**：枚举值可以自动补全，重构时 IDE 能追踪所有引用。
- **`valueOf()` 转换方便**：通过 `LunchTrayScreen.valueOf(route)` 可以直接从路由字符串还原枚举值，进而获取标题资源 ID，逻辑简洁。

------

## 3. LunchTrayAppBar 的设计思路

### 动态标题

`LunchTrayAppBar` 接收 `currentScreen: LunchTrayScreen` 参数，通过 `stringResource(currentScreen.title)` 渲染当前页面对应的标题字符串，无需在 AppBar 内部做任何条件判断。

### 返回按钮的显示条件

返回按钮的可见性由 `canNavigateBack: Boolean` 参数控制，该参数在 `LunchTrayApp` 中这样计算：

```kotlin
canNavigateBack = navController.previousBackStackEntry != null
```

**逻辑说明**：

- `previousBackStackEntry` 不为 `null`，说明返回堆栈中存在上一个目标，即当前页面不是起始页，应显示返回箭头。
- `previousBackStackEntry` 为 `null`，说明当前页面是堆栈底部（Start 页面），没有上一页可以返回，隐藏返回箭头。

这种判断方式比直接比较 `currentScreen == LunchTrayScreen.Start` 更加健壮——它依赖于导航状态本身，而非枚举值，即便将来调整起始页面也无需修改 AppBar 逻辑。

------

## 4. 导航流程设计与返回堆栈管理

### 正向导航流程

```
Start → Entree → SideDish → Accompaniment → Checkout
```

每一步通过 `navController.navigate(nextScreen.name)` 实现，默认会将目标页面压入返回堆栈。

### 为什么 Start 页面需要被弹出？

当用户点击 **Start Order** 从 Start 进入 Entree 时，如果不做任何处理，返回堆栈为：

```
[Start] → [Entree]
```

此时用户按系统返回键会回到 Start 页面，再按一次才退出应用——这不符合预期，因为 Start 只是一个入口页面，不应该在进入点餐流程后还保留在堆栈中。

解决方案是在导航到 Entree 时弹出 Start：

```kotlin
navController.navigate(LunchTrayScreen.Entree.name) {
    popUpTo(LunchTrayScreen.Start.name) { inclusive = true }
}
```

`inclusive = true` 表示 Start 自身也从堆栈中移除。弹出后堆栈变为：

```
[Entree]
```

用户按系统返回键时，由于堆栈只剩 Entree（或后续页面），再按返回键即可直接退出应用。

### Cancel 操作的堆栈清理

Cancel 按钮需要从任意页面（Entree / SideDish / Accompaniment / Checkout）回到 Start，并清空中间所有页面，同时重置订单数据：

```kotlin
viewModel.resetOrder()
navController.navigate(LunchTrayScreen.Start.name) {
    popUpTo(navController.graph.startDestinationId) { inclusive = true }
}
```

`popUpTo(graph.startDestinationId) { inclusive = true }` 会弹出整个返回堆栈，然后重新导航到 Start，确保不留下任何中间页面残留。

### Submit 操作

Submit 与 Cancel 的堆栈处理逻辑相同，同样需要清空订单并回到 Start：

```kotlin
viewModel.resetOrder()
navController.navigate(LunchTrayScreen.Start.name) {
    popUpTo(navController.graph.startDestinationId) { inclusive = true }
}
```

------

## 5. 实验中遇到的问题与解决过程

### 问题一：Start 页面仍然出现在系统返回键的堆栈中

**现象**：进入 Entree 页面后按系统返回键，回到了 Start 而非退出应用。

**原因**：最初直接使用 `navController.navigate(LunchTrayScreen.Entree.name)`，未弹出 Start。

**解决**：在 navigate 调用中添加 `popUpTo(LunchTrayScreen.Start.name) { inclusive = true }`，将 Start 从堆栈中移除。

### 问题二：Cancel 后再次进入点餐流程，返回按钮行为异常

**现象**：取消后回到 Start，再次进入 Entree，发现 AppBar 上意外出现了返回按钮。

**原因**：Cancel 时使用的 `popUpTo` 目标不正确，未完全清空堆栈，导致堆栈中残留了旧条目。

**解决**：统一使用 `popUpTo(navController.graph.startDestinationId) { inclusive = true }` 确保清空整个堆栈，再导航到 Start，彻底解决残留问题。

### 问题三：`@StringRes` 引用的字符串资源键名不存在

**现象**：编译报错，找不到 `R.string.choose_side_dish` 等资源。

**原因**：未在 `strings.xml` 中添加对应的字符串资源。

**解决**：在 `res/values/strings.xml` 中补充所有缺失的字符串资源条目后，编译通过。

------

## 总结

本次实验通过为 Lunch Tray 添加 Compose Navigation，完整体验了 `NavController`、`NavHost` 和 `composable()` 的协作方式。返回堆栈的管理是导航设计中最需要仔细考量的部分——合理地使用 `popUpTo` 可以让应用的返回行为符合用户预期，避免出现"幽灵页面"。枚举类的使用也让路由管理更加类型安全和可维护。