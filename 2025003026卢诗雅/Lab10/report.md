# Lab10：为 Lunch Tray 添加导航

## 1. Compose Navigation 核心组件关系

- **NavController**：导航控制器，负责管理应用的导航堆栈、处理页面跳转（`navigate()`）、返回（`navigateUp()`/`popBackStack()`）等操作，是导航的核心驱动。
- **NavHost**：导航宿主，作为所有可导航页面的容器，通过 `composable()` 注册路由与页面组件的映射关系。
- **composable()**：路由注册函数，定义“路由名称-页面组件”的对应关系，当导航到指定路由时，会渲染对应的 Composable 组件。

三者关系：`NavController` 接收导航指令，通过 `NavHost` 中注册的 `composable()` 路由映射，完成页面的切换和堆栈管理。

## 2. LunchTrayScreen 枚举类设计说明

使用枚举类而非直接字符串的原因：
1. **类型安全**：枚举值是编译期常量，避免手写字符串导致的拼写错误（如把 "Entree" 写成 "Entrey"）。
2. **语义化**：每个枚举值关联页面标题资源 ID，将“页面标识”和“页面标题”统一管理，便于维护。
3. **可扩展性**：可在枚举中添加更多属性（如页面描述、权限等），比纯字符串更灵活。
4. **代码可读性**：通过 `LunchTrayScreen.Entree.name` 获取路由名称，代码意图更清晰。

## 3. LunchTrayAppBar 设计思路

### 核心功能
- 动态显示当前页面标题：通过传入的 `currentScreen` 枚举值，读取其 `title` 资源 ID 显示标题。
- 条件显示返回按钮：通过 `canNavigateBack` 参数控制返回按钮的显示/隐藏。

### 返回按钮显示条件
`canNavigateBack` 由 `navController.previousBackStackEntry != null` 决定：
- Start 页面是导航起始页，`previousBackStackEntry` 为 null → 不显示返回按钮。
- 其他页面（Entree/SideDish/Accompaniment/Checkout）都有上一级页面 → 显示返回按钮。

### 交互逻辑
返回按钮点击时调用 `navigateUp()`，触发 `NavController` 的默认返回行为（弹出当前页面，回到上一级）。

## 4. 导航流程设计说明

### 基本导航流程
Start → Entree → SideDish → Accompaniment → Checkout → Start

### 返回堆栈管理策略
1. **Start 页面弹出堆栈**：
   点击 Start 页面的“Start Order”进入 Entree 时，使用 `popUpTo(Start) { inclusive = true }` 弹出 Start 页面。
   原因：用户进入点餐流程后，按系统返回键应退出应用，而非回到 Start 页面（符合移动端应用的常规交互逻辑）。

2. **Cancel/Submit 清空堆栈**：
   任意页面点击 Cancel 或 Checkout 点击 Submit 时，使用 `popUpTo(graph.findStartDestination().id) { inclusive = true }` 清空整个堆栈并返回 Start。
   原因：取消/提交订单后，需要重置导航状态，避免用户返回已完成/取消的订单流程页面。

3. **正常下一步导航**：
   Entree→SideDish、SideDish→Accompaniment、Accompaniment→Checkout 采用普通 `navigate()`，保留堆栈层级，支持通过返回按钮逐步回退。

## 5. 实验问题与解决过程

### 问题1：Start 页面点击返回键仍能回到应用
- 现象：进入 Entree 页面后，按系统返回键回到了 Start 页面，而非退出应用。
- 原因：未配置 `popUpTo` 弹出 Start 页面。
- 解决：在 Start→Entree 的导航中添加 `popUpTo(LunchTrayScreen.Start.name) { inclusive = true }`。

### 问题2：Cancel 按钮点击后订单状态未清空
- 现象：Cancel 回到 Start 后，再次进入点餐流程，仍显示上一次的选择。
- 原因：只处理了导航，未调用 `viewModel.resetOrder()`。
- 解决：在所有 Cancel/Submit 回调中，先调用 `viewModel.resetOrder()` 再执行导航。

### 问题3：AppBar 标题不随页面切换更新
- 现象：切换页面后，AppBar 标题仍显示上一页标题。
- 原因：未通过 `currentBackStackEntryAsState()` 监听堆栈变化。
- 解决：使用 `val backStackEntry by navController.currentBackStackEntryAsState()` 实时获取当前路由，更新 `currentScreen`。


## 六、实验总结

本次实验成功为 Lunch Tray 应用实现了完整的多页面导航，掌握了 Compose Navigation 的核心用法，理解了返回堆栈管理的重要性。通过枚举类统一管理路由，实现了类型安全；通过动态 AppBar 提升用户体验；通过合理的堆栈弹出策略，保证了导航逻辑简洁清晰。本次实验为后续复杂多屏应用开发奠定了坚实基础。