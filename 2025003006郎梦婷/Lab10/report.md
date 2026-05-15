# Lab10：为 Lunch Tray 添加导航 实验报告

---

## 1. Compose Navigation 核心组件关系简述
在Jetpack Compose Navigation中，三者分工明确：
- `NavController`：**导航控制器**，负责页面跳转、返回、管理返回栈，是整个导航体系的控制中枢。
- `NavHost`：**导航宿主容器**，承载所有可跳转页面，定义初始路由和页面集合。
- `composable()`：**路由注册函数**，将Compose界面与唯一路由地址绑定，实现页面与路由的一一映射。

三者配合工作：`NavHost`通过`composable()`注册所有页面，`NavController`下发跳转指令，由`NavHost`完成页面切换，共同实现多页面应用的统一路由管理。

---

## 2. `LunchTrayScreen` 枚举类设计说明
本次实验使用`enum class`而非硬编码字符串来定义路由，核心优势有三点：
1.  **类型安全**：避免路由名称拼写错误，编译阶段即可发现问题。
2.  **集中管理**：所有页面路由与标题统一存放，便于后期维护和扩展。
3.  **数据绑定**：可直接关联字符串资源ID，实现路由与页面标题一体化配置。

```kotlin
enum class LunchTrayScreen(@StringRes val title: Int) {
    Start(R.string.app_name),
    Entree(R.string.choose_entree),
    SideDish(R.string.choose_side_dish),
    Accompaniment(R.string.choose_accompaniment),
    Checkout(R.string.order_checkout)
}
3. LunchTrayAppBar 设计思路
顶部导航栏核心实现了两大功能：
动态标题：根据当前路由匹配LunchTrayScreen枚举，自动读取字符串资源更新标题。
智能返回按钮：
显示条件：navController.previousBackStackEntry != null
首页（Start页面）无上级页面，自动隐藏返回按钮
其他页面显示返回按钮，点击调用navController.navigateUp()实现页面回退
kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayAppBar(
    currentScreen: LunchTrayScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(Icons.Filled.ArrowBack, "返回")
                }
            }
        }
    )
}
4. 导航流程与返回堆栈管理设计
核心导航流程
Start → Entree → SideDish → Accompaniment → Checkout → Start
返回堆栈管理关键设计
用户从Start页面进入点餐流程后，按系统返回键应直接退出应用，而非退回首页，因此在跳转时添加了popUpTo规则：
kotlin
navController.navigate(LunchTrayScreen.Entree.name) {
    popUpTo(LunchTrayScreen.Start.name) { inclusive = true }
}
作用：将Start页面从返回堆栈中移除
效果：用户进入点餐流程后，无法再退回首页，按物理返回键直接退出应用，符合点餐类 APP 的使用习惯。
同时，Cancel和Submit操作会清空整个返回栈，直接回到Start页面并重置订单。
5. 实验中遇到的问题与解决过程
表格
问题	原因	解决方法
首页错误显示返回按钮	未正确判断页面层级	使用navController.previousBackStackEntry != null判断，首页隐藏按钮
物理返回键可退回Start页面	未配置返回栈清除规则	跳转时添加popUpTo，将Start页面移出返回栈
页面标题无法动态更新	未监听路由状态	使用currentBackStackEntryAsState()监听路由变化，实时匹配枚举
切换页面后订单数据丢失	数据仅存于页面状态中	使用OrderViewModel全局存储订单数据，页面跳转不销毁数据
6. 实验总结
本次实验完整实现了 Lunch Tray 点餐应用的多页面导航功能，熟练掌握了 Compose Navigation 的核心用法。通过枚举路由、自定义导航栏、返回栈管理，实现了规范、易用的导航体验，同时结合 ViewModel 完成了业务数据与导航逻辑的联动，圆满完成实验目标。