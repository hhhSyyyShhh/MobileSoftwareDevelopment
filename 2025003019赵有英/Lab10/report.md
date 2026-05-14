# Lab10 实验报告：Lunch Tray 多屏导航实现

## 一、实验概述
本次实验基于 Jetpack Compose Navigation 组件，为 Lunch Tray 点餐应用实现了完整的多页面导航功能。项目起始代码已实现所有页面 UI 和订单状态管理，本次实验核心任务为搭建导航框架，实现页面间的路由跳转、返回堆栈管理与动态应用栏。

---

## 二、核心组件关系简述
Compose Navigation 中，`NavController`、`NavHost` 和 `composable()` 三者是导航功能的核心，关系如下：

1. **`NavController`**：导航控制器，是导航逻辑的核心。它负责管理应用的返回堆栈，执行页面跳转、返回、出栈等操作，是整个导航系统的“控制中枢”。
2. **`NavHost`**：导航宿主，是导航内容的容器。它会根据 `NavController` 的当前路由，自动切换并显示对应的页面内容，是页面切换的“载体”。
3. **`composable()`**：路由注册函数，用于在 `NavHost` 内部定义路由与页面的映射关系。每个 `composable()` 调用都将一个路由字符串与一个可组合页面绑定，构建出完整的导航图。

三者协同工作：`composable()` 定义了所有可用的页面与路由；`NavController` 发出导航指令；`NavHost` 根据指令更新显示对应的页面。

---

## 三、`LunchTrayScreen` 枚举类设计说明
本次实验使用 `enum class LunchTrayScreen` 来定义所有页面的路由与标题，而非直接使用字符串，主要原因如下：

1. **类型安全，避免拼写错误**：使用枚举类后，所有路由名称都是预定义的常量，IDE 会提供自动补全，若拼写错误会在编译期直接报错，而使用字符串可能导致运行时崩溃。
2. **集中管理，便于维护**：所有页面的路由和标题资源都统一在枚举类中定义，修改或添加页面时只需修改枚举类，无需在多处修改字符串，减少维护成本。
3. **关联附加属性**：枚举类可以为每个页面附加额外属性，如本次实验中关联了 `@StringRes` 类型的标题资源 ID，方便后续动态更新应用栏标题。

---

## 四、`LunchTrayAppBar` 设计思路
`LunchTrayAppBar` 是一个可复用的动态顶部导航栏，设计思路如下：

1. **动态标题显示**：通过传入的 `currentScreen` 参数，调用 `stringResource(currentScreen.title)` 动态获取并显示当前页面的标题，无需手动在每个页面设置标题。
2. **返回按钮的显示控制**：通过 `canNavigateBack` 参数判断是否显示返回按钮。当 `navController.previousBackStackEntry != null` 时，说明当前页面不是首页，存在可返回的上一级页面，此时显示返回按钮；首页则不显示，避免用户无操作可回。
3. **返回按钮点击逻辑**：点击返回按钮时调用 `navigateUp()`，由 `NavController` 自动处理返回逻辑，无需手动管理返回堆栈。

---

## 五、导航流程与返回堆栈管理说明
本次实验的导航流程为：`Start → Entree → SideDish → Accompaniment → Checkout`，返回堆栈管理的关键设计如下：

1. **正向流程的堆栈管理**：当用户从 `Start` 页面点击“开始点餐”进入 `Entree` 页面时，使用 `popUpTo(LunchTrayScreen.Start.name) { inclusive = true }` 将 `Start` 页面从返回堆栈中弹出。这样做的目的是：用户进入点餐流程后，按系统返回键不会回到 `Start` 页面，而是直接退出应用，符合用户使用习惯。
2. **取消/提交订单的堆栈管理**：在任意页面点击 `Cancel` 或 `Submit` 按钮时，使用 `popBackStack(LunchTrayScreen.Start.name, inclusive = false)` 直接清空所有中间页面，回到 `Start` 页面，并调用 `viewModel.resetOrder()` 重置订单状态。这样可以避免用户多次取消订单后，返回堆栈中残留大量无效页面，导致应用内存占用过高。

---

## 六、实验中遇到的问题与解决过程
1. **问题一：Unresolved reference: 'R'**
   - 原因：代码中使用了字符串资源 ID，但未导入应用的 `R` 类。
   - 解决方法：在文件顶部添加 `import com.example.lunchtray.R`，让编译器能够找到资源文件。

2. **问题二：Cannot access 'var inclusive: Boolean': it is private**
   - 原因：使用了旧版的 `popUpTo` 写法，新版本 Navigation 中该属性已私有化。
   - 解决方法：改用 `navController.popBackStack(route, inclusive)` 直接管理返回堆栈，无需在 `navigate()` 中配置。

3. **问题三：MainActivity 中 Unresolved reference: 'LunchTrayApp'**
   - 原因：`LunchTrayScreen.kt` 中存在报错，导致 `LunchTrayApp` 函数无法被编译，`MainActivity` 无法找到该函数。
   - 解决方法：修复 `LunchTrayScreen.kt` 中的所有报错，确保代码无红色波浪线，再重新编译项目。

4. **问题四：导航图标弃用警告**
   - 原因：使用了已弃用的 `Icons.Filled.ArrowBack`。
   - 解决方法：改用新版本推荐的 `Icons.AutoMirrored.Filled.ArrowBack`，该图标可自动适配 RTL 布局。

---

## 七、实验总结
通过本次实验，我掌握了 Jetpack Compose Navigation 的核心用法，理解了 `NavController`、`NavHost` 和 `composable()` 三者的关系，学会了使用枚举类管理路由、实现动态应用栏和管理返回堆栈。实验过程中遇到的问题，也让我对 Compose Navigation 的版本适配和错误排查有了更深入的理解，为后续开发复杂多页面应用打下了基础。