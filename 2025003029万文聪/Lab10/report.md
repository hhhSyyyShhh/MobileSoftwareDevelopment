# Lab10 实验报告：为 Lunch Tray 添加导航

## 1. NavController、NavHost 和 composable() 的关系

在 Jetpack Compose Navigation 中，`NavController` 负责管理应用的导航状态和返回堆栈，例如跳转到某个页面、返回上一页、清理返回栈等。`NavHost` 是导航容器，它接收一个 `NavController`，并通过 `startDestination` 指定应用启动时显示的第一个页面。`composable()` 用来在 `NavHost` 中声明具体的路由，每一个 `composable(route = ...)` 都对应一个可显示的 Compose 页面。

本实验中，`LunchTrayApp()` 创建 `NavController`，然后在 `NavHost` 中配置 `Start`、`Entree`、`SideDish`、`Accompaniment` 和 `Checkout` 五个路由。用户点击按钮时，通过 `navController.navigate()` 切换页面。

## 2. LunchTrayScreen 枚举类设计说明

本实验使用 `enum class LunchTrayScreen(@StringRes val title: Int)` 管理应用内所有页面。每个枚举值既表示一个导航路由，也绑定了对应的标题字符串资源，例如：

- `Start` 对应 `R.string.app_name`
- `Entree` 对应 `R.string.choose_entree`
- `SideDish` 对应 `R.string.choose_side_dish`
- `Accompaniment` 对应 `R.string.choose_accompaniment`
- `Checkout` 对应 `R.string.order_checkout`

相比直接在代码中多次书写字符串路由，枚举类更安全、更统一。如果直接写字符串，容易出现拼写错误，而且修改页面名称时需要在多个位置同步修改。使用枚举后，可以通过 `LunchTrayScreen.Entree.name` 获取路由，通过 `currentScreen.title` 获取标题资源，使导航代码更加清晰。

## 3. LunchTrayAppBar 的设计思路

`LunchTrayAppBar` 使用 `TopAppBar` 显示当前页面标题。标题由 `currentScreen.title` 决定，因此当页面切换时，顶部标题也会自动变化。例如进入主菜选择页面时，标题显示为 “Choose Entree”；进入结账页面时，标题显示为 “Order Checkout”。

返回按钮使用 `Icons.Filled.ArrowBack` 图标。Start 页面是应用首页，不应该显示返回按钮；其他页面属于点餐流程，所以显示返回按钮。本次实现中使用：

```kotlin
canNavigateBack = currentScreen != LunchTrayScreen.Start
```

这样可以保证 Start 页面不显示返回箭头，而 Entree、SideDish、Accompaniment、Checkout 页面都显示返回箭头。

由于实验要求从 Start 进入 Entree 时把 Start 页面弹出返回堆栈，Entree 页面此时没有上一个返回栈条目。因此返回箭头点击时先调用 `navController.navigateUp()`；如果返回失败，说明当前页面已经没有上一页，此时调用 `resetOrderAndNavigateToStart()` 回到 Start 页面并清空订单。

## 4. 导航流程和返回堆栈管理

本实验的导航流程如下：

```text
Start -> Entree -> SideDish -> Accompaniment -> Checkout
```

各页面按钮对应的跳转关系如下：

- Start 页面点击 Start Order，进入 Entree 页面。
- Entree 页面点击 Next，进入 SideDish 页面。
- SideDish 页面点击 Next，进入 Accompaniment 页面。
- Accompaniment 页面点击 Next，进入 Checkout 页面。
- 任意菜单页面点击 Cancel，清空订单并回到 Start 页面。
- Checkout 页面点击 Cancel 或 Submit，清空订单并回到 Start 页面。

从 Start 进入 Entree 时使用：

```kotlin
navController.navigate(LunchTrayScreen.Entree.name) {
    popUpTo(LunchTrayScreen.Start.name) {
        inclusive = true
    }
}
```

这样做的目的是把 Start 页面从返回堆栈中移除。用户进入点餐流程后，如果按系统返回键，应用会退出，而不是回到 Start 页面。这符合实验对返回堆栈的要求。

Cancel 和 Submit 返回 Start 时，调用统一的 `resetOrderAndNavigateToStart()` 方法。该方法先调用 `viewModel.resetOrder()` 清空订单状态，然后导航回 Start，并通过 `popUpTo(LunchTrayScreen.Entree.name) { inclusive = true }` 清理点餐流程中的页面，避免用户取消或提交后还能通过返回键回到旧订单页面。

## 5. 遇到的问题与解决过程

本实验遇到的主要问题是返回按钮显示逻辑和返回堆栈管理之间存在冲突。实验要求从 Start 进入 Entree 后弹出 Start 页面，这样系统返回键可以直接退出应用；但是如果完全依赖 `navController.previousBackStackEntry != null` 来判断是否显示返回按钮，那么 Entree 页面因为没有上一条返回栈记录，就不会显示返回按钮。

解决方法是将返回按钮的显示条件改为当前页面是否为 Start：

```kotlin
canNavigateBack = currentScreen != LunchTrayScreen.Start
```

同时，在返回按钮点击事件中先尝试 `navController.navigateUp()`。如果返回成功，就回到上一页；如果返回失败，说明当前页面没有上一页，例如 Entree 页面，此时将其视为取消点餐，清空订单并回到 Start 页面。

另一个问题是 Cancel 和 Submit 后需要清空订单。如果只导航回 Start，不调用 `viewModel.resetOrder()`，下次点餐时可能还会看到上一次选择的菜品和价格。因此在统一的返回 Start 方法中先重置 ViewModel，再执行导航。

## 6. 实验总结

通过本次实验，我掌握了在 Jetpack Compose 中使用 Navigation 组件构建多页面应用的方法。`NavController` 管理页面跳转和返回堆栈，`NavHost` 负责承载不同页面，`composable()` 声明具体页面路由。通过枚举类统一管理页面路由和标题，可以减少硬编码字符串，提高代码可维护性。同时，本实验也加深了我对返回堆栈管理的理解，尤其是 `popUpTo` 和 `inclusive` 在控制返回行为时的作用。
