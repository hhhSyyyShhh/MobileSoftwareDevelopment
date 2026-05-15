# Lab10 实验报告：为 Lunch Tray 添加导航

## 1. NavController、NavHost、composable() 关系简述
- **NavController**：导航控制器，负责管理页面跳转、返回堆栈、监听当前页面，是导航的“大脑”。
- **NavHost**：导航容器，承载所有页面，指定起始页面，管理页面切换显示。
- **composable()**：路由配置，为每个页面绑定路由名称，定义页面内容与跳转逻辑。
三者关系：NavController 控制 NavHost 显示哪个 composable 页面。

## 2. LunchTrayScreen 枚举类设计说明
使用枚举而不是字符串的原因：
- **类型安全**：避免手写字符串路由出错；
- **统一管理**：所有页面集中定义，便于维护；
- **自带标题**：每个页面绑定对应标题资源，实现动态 AppBar；
- **可读性强**：代码更清晰，便于团队协作。

枚举包含 5 个页面：Start、Entree、SideDish、Accompaniment、Checkout。

## 3. LunchTrayAppBar 设计思路
- 动态显示当前页面标题，通过枚举获取字符串资源；
- 返回按钮判断条件：`navController.previousBackStackEntry != null`；
- Start 页面无返回按钮，其他页面自动显示；
- 返回按钮点击执行 `navigateUp()` 返回上一页。

## 4. 导航流程与返回堆栈设计
导航流程：
Start → Entree → SideDish → Accompaniment → Checkout → Start

返回堆栈关键设计：
- 从 Start 进入 Entree 时，**弹出 Start 页面**，按返回键直接退出应用；
- Cancel / Submit 按钮会清空整个返回堆栈并回到 Start，同时重置订单；
- 确保用户不会陷入无限返回循环。

## 5. 遇到的问题与解决
1. **返回按钮一直显示**
   解决：通过判断 previousBackStackEntry 是否为空控制显示。
2. **按返回键回到 Start 页面**
   解决：导航时使用 popUpTo 将 Start 从堆栈中移除。
3. **Cancel 后订单未清空**
   解决：导航同时调用 viewModel.resetOrder()。
4. **页面标题不更新**
   解决：使用 currentBackStackEntryAsState() 监听路由变化。