# Lab10 实验报告

## 1. NavController、NavHost、composable() 关系
- NavController：导航控制器，负责页面跳转、返回、管理返回堆栈。
- NavHost：导航容器，承载所有页面，指定起始页面。
- composable()：定义每一个页面的路由，绑定页面与路径。

三者关系：NavController 指挥跳转，NavHost 提供容器，composable 注册页面。

## 2. LunchTrayScreen 枚举类设计说明
使用枚举而不是字符串的原因：
- 类型安全，避免拼写错误
- 统一管理页面和标题
- 代码更易维护、可读性高
- 方便获取页面标题资源

## 3. LunchTrayAppBar 设计思路
- 显示当前页面标题：通过 currentScreen.title 获取字符串资源
- 返回按钮显示条件：判断 navController.previousBackStackEntry 是否为空
- Start 页面没有返回按钮，其他页面自动显示
- 返回按钮点击执行 navigateUp() 返回上一页

## 4. 导航流程与返回堆栈管理
导航流程：
Start → Entree → SideDish → Accompaniment → Checkout → Start

返回堆栈关键处理：
- 从 Start 跳转到 Entree 时，popUpTo Start 并 inclusive=true，把 Start 弹出堆栈
- 按返回键直接退出应用，不会回到 Start
- Cancel/Submit 时清空堆栈并回到 Start，保证流程干净

## 5. 实验遇到的问题与解决
1. 问题：返回按钮一直不显示
   解决：使用 currentBackStackEntryAsState() 监听路由变化

2. 问题：按返回键回到 Start 页面
   解决：跳转时使用 popUpTo 把 Start 从堆栈移除

3. 问题：Cancel 后订单没有清空
   解决：导航同时调用 viewModel.resetOrder()