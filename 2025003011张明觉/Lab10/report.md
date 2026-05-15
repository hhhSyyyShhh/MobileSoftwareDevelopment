\# Lab10 实验报告：为 Lunch Tray 添加导航

\## 一、实验基本信息

\- 实验名称：Jetpack Compose Navigation 导航实现
\- 项目名称：Lunch Tray 点餐应用
\- 开发环境：Android Studio + Kotlin + Jetpack Compose

\## 二、核心知识点说明

\### 1. NavController、NavHost、composable() 三者关系简述
\- \*\*NavController\*\*：导航控制器，负责页面跳转、返回堆栈管理、监听页面变化。
\- \*\*NavHost\*\*：导航容器，用来显示当前所在页面，连接 NavController 与所有页面。
\- \*\*composable()\*\*：用来注册每一个页面路由，告诉 NavHost 哪个路径对应哪个页面。
\*\*总结\*\*：NavController 发出导航指令 → NavHost 接收指令 → 根据 composable 注册的路由显示对应页面。
\---

\### 2. LunchTrayScreen 枚举类设计说明
本项目使用枚举类 `LunchTrayScreen` 管理所有页面：
\- Start
\- Entree
\- SideDish
\- Accompaniment
\- Checkout

\*\*为什么使用枚举而不直接用字符串？\*\*
1\. \*\*类型安全\*\*：避免字符串拼写错误，编译器会检查。
2\. \*\*便于维护\*\*：页面统一管理，修改方便。
3\. \*\*可携带数据\*\*：可以直接绑定页面标题 string 资源。
4\. \*\*代码更清晰\*\*：可读性远高于魔法字符串。
\---

\### 3. LunchTrayAppBar 设计思路
AppBar 包含：
\- 动态标题：根据当前页面切换
\- 返回按钮：根据是否能返回显示/隐藏
\*\*返回按钮显示条件\*\*：
\- 当 `navController.previousBackStackEntry != null` 时显示返回按钮
\- 在 Start 页面不显示返回按钮

\*\*设计思路\*\*：
通过监听当前路由，动态设置标题；根据返回堆栈是否为空判断是否展示返回箭头，保证界面行为符合用户直觉。
\---
\### 4. 导航流程与返回堆栈管理说明
导航流程：
Start → 主菜 → 配菜 → 饮品 → 结账
\*\*返回堆栈管理重点\*\*：
在点击 \*\*Cancel\*\* 或 \*\*Submit\*\* 时：
使用 `popBackStack(Screen.START.name, false)`
直接回到 \*\*Start 页面并清空中间所有页面\*\*。
\*\*为什么要把 Start 之外的页面弹出？\*\*
\- 防止用户点返回键又回到点餐页面
\- 保证订单结束后状态重置
\- 符合正常 APP 使用逻辑
\- 避免ViewModel状态残留导致错误
\---


\## 三、实验遇到的问题与解决过程
\### 问题1：TopBar 标题不会跟着页面变化
\- 原因：TopBar 写死为 Screen.START
\- 解决：通过 `currentBackStackEntryAsState()` 获取当前路由，动态设置标题。

\### 问题2：导航参数不匹配报错
\- 原因：页面参数名不统一（onNext / onCancel）
\- 解决：统一所有页面参数格式，保证路由调用正确。

\### 问题3：Checkout 页面价格计算错误
\- 原因：数值相加逻辑错误、UI 显示错乱
\- 解决：重新计算 subtotal → tax → total，并正确显示菜品名称与价格。

\### 问题4：返回键逻辑混乱，回到上一个订单页面
\- 原因：返回堆栈没有正确弹出
\- 解决：使用 `popBackStack` 回到 Start，同时重置 ViewModel。
\---

\## 四、实验总结
本次实验完成了：
\- 多页面 Navigation 导航
\- ViewModel 状态管理
\- 动态 TopBar 与返回按钮
\- 正确的返回堆栈管理
\- 订单选择、价格计算、取消与提交功能

程序符合标准作业结构，可正常运行、无报错、逻辑完整。

