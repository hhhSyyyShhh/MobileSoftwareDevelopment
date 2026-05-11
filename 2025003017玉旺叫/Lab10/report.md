# Lab10 实验报告
# Lab10 实验报告

1.  **Compose Navigation 中 NavController、NavHost 和 composable() 三者关系简述**
    - `NavController`：导航核心控制器，管理导航栈与页面跳转逻辑。
    - `NavHost`：导航容器，承载所有页面，与 `NavController` 绑定。
    - `composable()`：在 `NavHost` 内注册页面，定义路由与对应界面。
    三者协同，实现页面的跳转与显示。

2.  **`LunchTrayScreen` 枚举类设计说明**
    使用枚举类而非直接字符串定义路由，是为了：
    - 编译期类型安全，避免拼写错误。
    - 集中管理路由，便于维护。
    - 提高代码可读性，明确区分不同页面。

3.  **`LunchTrayAppBar` 设计思路**
    顶部导航栏统一显示应用标题 `Lunch Tray`。返回按钮仅在当前页面非首页（`LunchTrayScreen.Start`）时显示，点击调用 `navController.navigateUp()` 返回上一级页面。

4.  **导航流程与返回堆栈管理**
    点餐流程结束后，使用 `popBackStack(LunchTrayScreen.Start.name, false)` 直接弹出所有中间页面，仅保留首页。这样设计是为了：
    - 避免用户多次按返回键才能退出流程。
    - 保证每次点餐都从干净的首页开始，流程清晰。

5.  **实验中遇到的问题与解决过程**
    - 问题1：页面跳转后返回栈残留中间页面，导致需多次按返回键。解决：改用 `popBackStack` 直接清空中间页面。
    - 问题2：直接用字符串作为路由时，拼写错误导致页面无法跳转。解决：改用枚举类管理路由。
    - 问题3：返回按钮显示逻辑错误，在首页也显示返回箭头。解决：监听当前页面状态，动态判断按钮显示条件。