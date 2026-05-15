# Lab10 实验报告

## 1. NavController、NavHost、composable() 关系
- NavController：负责页面跳转、返回、管理返回栈。
- NavHost：显示当前页面，管理所有路由。
- composable()：定义每个页面的路径与UI。

简单说：NavHost 容器，composable 是页面，NavController 控制跳转。

## 2. 为什么使用枚举类 LunchTrayScreen
- 避免字符串拼写错误
- 统一管理页面名称和标题
- 代码更安全、易维护
- 方便动态获取当前页面标题

## 3. AppBar 设计思路
- 根据当前页面动态显示标题
- 返回按钮只有在非 Start 页面显示
- 判断条件：navController.previousBackStackEntry != null

## 4. 导航流程与返回栈管理
页面流程：Start → Entree → SideDish → Accompaniment → Checkout

返回栈处理：
- 从 Start 跳转到主菜时，弹出 Start，确保返回键直接退出
- Cancel / Submit 清空所有页面回到 Start
- 保证用户体验干净，不会出现循环返回

## 5. 遇到的问题与解决
1. 项目路径含中文报错 → 移动到纯英文路径
2. 导航跳转混乱 → 正确使用 popUpTo 管理返回栈
3. 返回按钮显示错误 → 通过 previousBackStackEntry 判断
4. 订单没有重置 → Cancel 和 Submit 时调用 viewModel.resetOrder()