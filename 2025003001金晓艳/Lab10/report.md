# Lab10 实验报告

## 一、实验目的
1.  熟悉 Android Studio 中 Jetpack Compose Navigation 组件的基本使用
2.  掌握 `NavController`、`NavHost` 与 `composable()` 路由配置的方法
3.  学会实现多页面应用的导航流程与返回栈管理
4.  为 Lunch Tray 点餐应用添加完整的页面导航功能

## 二、实验环境
- 设备：Android 模拟器 / 真机（如 Pixel 7）
- 系统：Android 13（API 33）
- 工具：Android Studio Hedgehog / Iguana

## 三、实验内容与步骤
1.  **创建导航枚举类**
    在 `LunchTrayScreen.kt` 中定义 `LunchTrayScreen` 枚举，包含 5 个页面及其对应的标题资源，实现路由与标题的统一管理。
2.  **初始化导航控制器**
    在 `LunchTrayApp()` 中使用 `rememberNavController()` 创建 `NavController`，并通过 `currentBackStackEntryAsState()` 获取当前页面状态。
3.  **实现顶部应用栏 `LunchTrayAppBar`**
    动态显示当前页面标题，并根据返回栈状态控制返回按钮的显示与隐藏，实现非首页页面的返回功能。
4.  **配置 `NavHost` 导航路由**
    在 `NavHost` 中为 5 个页面配置 `composable()` 路由，实现从 Start → Entree → SideDish → Accompaniment → Checkout 的点餐流程。
5.  **管理返回堆栈**
    在页面跳转时使用 `popUpTo()` 方法，将 Start 页面从返回栈中移除；在 Cancel/Submit 操作时清空返回栈，确保用户回到初始状态。
6.  **运行与验证**
    启动应用，测试页面跳转、返回按钮、Cancel/Submit 功能以及系统返回键的行为，确保导航流程符合实验要求。

## 四、运行结果
- 应用成功启动，显示 Start 页面，无返回按钮
- 点击“Start Order”进入主菜选择页，AppBar 标题自动更新为“Choose Entree”，并显示返回按钮
- 依次点击“Next”可正常导航至配菜、佐餐和结账页面
- 在任意页面点击“Cancel”，可回到 Start 页面并重置订单
- 在结账页面点击“Submit”，订单提交成功并回到 Start 页面
- 从点餐流程中按系统返回键，直接退出应用，不会回到 Start 页面

（运行截图：可在此插入菜单页面和结账页面的截图）

## 五、实验总结
本次实验主要学习了 Jetpack Compose Navigation 组件的使用方法，掌握了多页面应用的导航配置与返回栈管理策略。通过实现 Lunch Tray 点餐应用的完整导航流程，理解了 `NavController`、`NavHost` 与路由配置之间的协作关系，同时也解决了版本兼容和资源配置过程中遇到的问题，为后续多页面 Compose 应用开发打下了基础。