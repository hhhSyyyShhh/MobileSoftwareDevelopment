# Lab10 为 Lunch Tray 添加导航 实验报告

## 一、实验目的
本次实验基于 Jetpack Compose Navigation 组件，为已有的 Lunch Tray 点餐应用实现完整的多页面导航功能。通过本次实验，掌握导航枚举、页面路由、顶部应用栏、返回堆栈管理等核心知识点，能够独立完成一个具备完整页面跳转逻辑的 Compose 多页面应用。

## 二、实验环境
- 操作系统：Windows 10/11
- 开发工具：Android Studio
- 开发语言：Kotlin
- 核心框架：Jetpack Compose + Navigation Compose

## 三、实验原理与知识点
### 1. Navigation 三大核心组件
- **NavController**：页面跳转控制器，负责管理页面的前进、后退、路由跳转与返回堆栈。
- **NavHost**：页面容器，用于展示当前路由对应的 Composable 页面。
- **composable()**：路由注册方法，为每个页面绑定唯一路径，实现页面与路径的映射。

### 2. 枚举类管理页面
使用 enum class 定义所有页面，能够统一管理页面标题、路径，避免字符串硬编码，提高代码安全性与可读性。

### 3. TopAppBar 动态标题与返回按钮
根据当前页面动态切换标题，并根据返回堆栈判断是否显示返回箭头，提升用户体验。

### 4. 返回堆栈管理
通过 popUpTo 与 inclusive 参数控制返回栈，确保从 Start 页面进入流程后，按返回键可直接退出应用，而不会回到首页。

## 四、实验步骤
1. 打开老师提供的 Lunch Tray 起始项目。
2. 在 LunchTrayScreen.kt 中创建导航枚举类 LunchTrayScreen。
3. 实现动态标题顶部栏 LunchTrayAppBar。
4. 初始化 NavController，配置 NavHost 与所有页面路由。
5. 实现页面跳转逻辑：Start → Entree → SideDish → Accompaniment → Checkout。
6. 实现 Cancel 与 Submit 逻辑，返回首页并清空订单。
7. 运行项目测试导航流程、返回逻辑、标题切换是否正常。
8. 截图并完成实验报告。

## 五、实验结果
- 应用成功运行，五个页面导航流程完整。
- 顶部标题可根据页面自动切换。
- 返回按钮只在非首页显示，点击可正确返回上一页。
- Next 按钮可按顺序跳转页面。
- Cancel / Submit 按钮均可返回首页并清空订单。
- 返回堆栈管理正确，从首页进入流程后按返回键可直接退出应用。
- 界面显示正常，无崩溃与异常。

## 六、实验分析与总结
本次实验完成了 Compose 多页面导航的全部核心功能。通过实现枚举、导航控制器、AppBar、路由配置，理解了 Compose 页面跳转的底层逻辑。返回堆栈管理是本次实验的重点，合理配置 popUpTo 能够避免页面重复与返回异常。整体项目结构清晰，代码规范，完全符合实验要求。

## 七、实验遇到的问题及解决方法
1. **问题**：返回按钮在首页也会显示。
   **解决**：通过判断 navController.previousBackStackEntry 是否为空来控制返回按钮显示。

2. **问题**：从首页跳转后按返回键回到首页。
   **解决**：使用 popUpTo 弹出 Start 页面，使返回键直接退出应用。

3. **问题**：页面标题不更新。
   **解决**：使用 currentBackStackEntryAsState 监听路由变化，动态获取当前页面枚举并更新标题。