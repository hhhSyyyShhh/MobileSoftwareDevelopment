# Lab10 Lunch Tray 导航实验报告

## 一、实验目的
1. 掌握 Jetpack Compose Navigation 组件的基本使用
2. 实现多页面应用的页面跳转与返回堆栈管理
3. 完成 Lunch Tray 点餐应用的完整导航流程
4. 学会使用 NavController、NavHost 实现页面导航

## 二、实验环境
- 开发工具：Android Studio
- 开发语言：Kotlin
- 界面框架：Jetpack Compose
- 依赖：Navigation Compose、ViewModel Compose

## 三、实验内容
1. 创建 LunchTray 项目，搭建基础项目结构
2. 定义页面导航枚举类，管理所有页面路由
3. 实现顶部应用栏，动态显示标题与返回按钮
4. 配置 NavHost 与 NavController，实现页面导航
5. 完成 5 个页面的搭建与跳转逻辑：
   - 开始点餐页面
   - 主菜选择页面
   - 配菜选择页面
   - 饮品选择页面
   - 订单结账页面
6. 实现下一步、取消、提交订单等交互功能

## 四、核心实现
1. 使用枚举类 `LunchTrayScreen` 统一管理页面路由，保证类型安全
2. 通过 `rememberNavController()` 创建导航控制器，管理页面跳转
3. 使用 `NavHost` 作为导航容器，注册所有页面路由
4. 动态控制顶部栏的返回按钮显示逻辑
5. 实现标准的点餐流程导航：Start → Entree → Side → Accompaniment → Checkout

## 五、实验结果
1. 项目成功运行，无报错
2. 所有页面正常显示与跳转
3. 顶部导航栏标题动态变化，返回按钮正常显示与使用
4. 取消、下一步、提交订单功能全部正常
5. 完全满足 Lab10 作业的所有功能与格式要求

## 六、实验总结
本次实验完成了 Lunch Tray 应用的导航功能开发，掌握了 Compose 导航的核心组件使用方法，理解了 NavController、NavHost 的作用，能够独立实现多页面应用的导航逻辑与堆栈管理，顺利完成实验任务。