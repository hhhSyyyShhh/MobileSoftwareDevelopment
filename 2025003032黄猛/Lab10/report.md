# Lab10 报告 - Lunch Tray 导航实现

## 1. NavController、NavHost 与 composable() 之间的关系

- **NavController**：导航组件的控制器，负责发起导航操作（navigate、navigateUp、popUpTo 等），管理导航返回栈。
- **NavHost**：导航容器，负责承载不同的可组合目的地（`composable()`），并根据 `NavController` 的当前目的地展示对应的 UI。
- **composable()**：在 `NavHost` 中为每个路由注册的目的地构建器，用来返回对应屏幕的 Composable 内容。

简而言之：`NavController` 发出导航命令 → `NavHost` 接收并根据当前路由选择展示对应的 `composable()` 屏幕。

## 2. 使用 `enum class LunchTrayScreen` 的设计说明

- 我使用 `enum class LunchTrayScreen(@StringRes val title: Int)` 把应用中的页面作为强类型枚举来表示，每个枚举值绑定一个字符串资源 ID（页面标题）。
- 这样做的好处：
  - 路由名称与枚举值 `.name` 一致，避免手写字符串导致的拼写错误。
  - 可以把与页面相关的元数据（如标题资源）放在同一个地方，便于维护和本地化（i18n）。
  - 使用枚举能让代码在编译期更容易发现错误（而不是运行时的字符串错误）。

## 3. `LunchTrayAppBar` 的设计思路

- 顶部 AppBar 动态显示当前页面的标题，标题通过 `currentScreen.title` 获取字符串资源。
- 返回按钮的显示条件：当 `navController.previousBackStackEntry != null` 时显示（即当前不是根起始页），这样 `Start` 页面不会显示返回箭头。
- 返回按钮点击调用 `navigateUp()`，依赖 `NavController` 进行标准的返回行为。

这种设计保证了：用户在流程中间页面能返回到上一个页面，而起始页面看起来是应用的入口（没有返回箭头）。

## 4. 导航流程与返回栈管理说明

- 导航流程：Start → Entree → SideDish → Accompaniment → Checkout。
- 关键点在于“进入点餐流程时弹出 Start 页面”，实现方式：从 Start 导航到 Entree 时，使用：

```kotlin
navController.navigate(LunchTrayScreen.Entree.name) {
  popUpTo(LunchTrayScreen.Start.name) { inclusive = true }
}
```

- 这样做的理由：用户从 Start 进入点餐后，按系统返回键应退出应用，而不是回到 Start 页面（Start 是流程的入口，而不是中间页面）。
- 对于 Cancel 和 Submit 操作，统一返回 Start 并清空订单：
  - 使用 `popUpTo(navController.graph.findStartDestination().id) { inclusive = true }` 来清空中间的返回栈，保证用户返回到干净的 Start 状态。
  - 在返回 Start 的同时调用 `viewModel.resetOrder()` 清除订单状态。

## 5. 实验中遇到的问题与解决过程

- 问题：需要保证路由名称与枚举 `.name` 保持一致以避免运行时找不到目的地。
  - 解决：把路由直接使用 `LunchTrayScreen.XXX.name`，并在 `NavHost` 的 `startDestination` 也使用枚举的 `.name`。

- 问题：AppBar 的返回箭头在 Start 页面不应显示。
  - 解决：通过判断 `navController.previousBackStackEntry != null` 决定是否显示返回按钮。

- 其它注意事项：确保各个 `onSelectionChanged` 回调调用 `OrderViewModel` 的相应方法（`updateEntree`、`updateSideDish`、`updateAccompaniment`），并在 Cancel/Submit 时调用 `resetOrder()`。

---

请在 Android Studio 中将 `LunchTrayScreen.kt` 放入项目的主包（与其它 UI/ ViewModel 类相同的包名），然后运行应用以验证：
- Start 页面无返回箭头；
- 点击 Start 进入主菜页面后，AppBar 标题变化且可返回；
- Next / Cancel / Submit 的导航与返回栈行为符合要求。

（本次提交省略截图。）
