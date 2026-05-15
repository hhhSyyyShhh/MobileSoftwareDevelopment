# Lab10 实验报告
## 实验名称
为 Lunch Tray 添加 Jetpack Compose 导航功能

## 一、实验概述
本次实验基于 Jetpack Compose Navigation 组件，为已完成 UI 开发的 Lunch Tray 点餐应用实现完整的页面导航功能。应用包含**开始点餐、选择主菜、选择配菜、选择佐餐、结账**五个页面，实验核心是通过 Navigation 组件建立页面间的跳转逻辑、管理页面返回堆栈、实现顶部导航栏动态更新，最终完成流畅且符合用户习惯的点餐流程交互。

## 二、核心知识点说明
### 1. NavController、NavHost、composable() 三者关系
- **NavController**：导航的**控制中心**，负责管理页面的跳转、返回、返回堆栈的维护，是页面之间通信的核心对象，通过`rememberNavController()`创建并持有。
- **NavHost**：导航的**容器**，作为页面展示的载体，关联 NavController，定义导航的起始页面，所有页面路由都在 NavHost 内部配置。
- **composable()**：导航的**路由配置项**，为每个 Composable 页面绑定唯一路由地址，定义页面进入时的 UI 渲染和交互逻辑。

**整体关系**：NavController 发出导航指令，NavHost 接收指令并根据 composable 配置的路由规则，切换展示对应的页面。

### 2. LunchTrayScreen 枚举类设计说明
本次实验使用枚举类`LunchTrayScreen`定义所有页面，替代直接使用字符串路由，设计优势：
1. **类型安全**：避免手写字符串路由出现拼写错误，编译器会自动校验，减少运行时异常；
2. **统一管理**：将页面路由、标题资源集中管理，代码结构清晰，便于维护和扩展；
3. **便捷获取**：可直接通过枚举值获取页面名称（路由）和标题资源，简化顶部栏标题动态设置逻辑；
4. **可读性高**：代码语义化更强，一眼就能识别所有页面。

### 3. LunchTrayAppBar 设计思路
顶部应用栏核心实现了**动态标题+条件化返回按钮**：
1. **动态标题**：根据当前页面的枚举值，获取对应的字符串资源，实时展示当前页面名称；
2. **返回按钮逻辑**：通过`navController.previousBackStackEntry != null`判断是否存在上一级页面；
3. **显示规则**：Start 页面不显示返回按钮，其余所有页面均显示返回箭头，点击后调用`navigateUp()`返回上一级；
4. **样式统一**：使用 Material3 主题样式，保证应用 UI 风格一致性。

### 4. 导航流程与返回堆栈管理设计
#### 导航流程
严格按照业务流程实现：
`Start → Entree → SideDish → Accompaniment → Checkout → Start`
任意页面点击 Cancel / Checkout 点击 Submit 均返回 Start 页面。

#### 返回堆栈管理（核心重点）
1. **从 Start 进入点餐流程**：使用`popUpTo(LunchTrayScreen.Start.name) { inclusive = true }`，将 Start 页面从返回堆栈中移除。这样用户在点餐流程中按系统返回键，会直接退出应用，而不会回到 Start 页面，符合用户使用习惯；
2. **取消/提交订单**：导航回 Start 页面并清空全部返回堆栈，同时重置 ViewModel 中的订单数据，保证下次点餐数据干净；
3. **逐级 Next**：仅做页面跳转，不修改堆栈，支持通过返回按钮逐级返回。

## 三、实验完成内容
1. 完成`LunchTrayScreen`枚举类定义，包含 5 个页面及对应标题资源；
2. 初始化`NavController`，实现页面状态监听与当前页面识别；
3. 实现`LunchTrayAppBar`顶部栏，支持动态标题和条件返回按钮；
4. 完成`NavHost`全路由配置，实现所有页面的跳转逻辑；
5. 封装公共方法`cancelAndNavigateToStart`，统一处理取消/提交逻辑；
6. 正确管理返回堆栈，满足实验要求的返回行为；
7. 集成 ViewModel，实现订单数据在页面间的同步与重置。

## 四、实验中遇到的问题与解决过程
### 问题1：`findStartDestination()` 和 `inclusive` 代码报错
- **问题描述**：使用`navController.graph.findStartDestination().id`获取起始页面时，代码提示找不到方法/参数报错，无法编译。
- **原因**：`findStartDestination()`需要在导航图完全构建后调用，普通函数中直接使用会导致导航上下文异常；`inclusive`参数语法使用错误。
- **解决方案**：放弃使用`findStartDestination()`，直接使用枚举类的路由名称`LunchTrayScreen.Start.name`作为`popUpTo`参数，语法简洁且完全兼容，报错立即解决。

### 问题2：Start 页面进入点餐流程后，返回键回到 Start 页面而非退出应用
- **问题描述**：未正确配置`popUpTo`参数，返回堆栈保留了 Start 页面。
- **解决方案**：在 Start 跳转到 Entree 的导航逻辑中添加`popUpTo(Start.name) { inclusive = true }`，将 Start 页面移出堆栈。

### 问题3：Cancel 按钮重复代码过多
- **问题描述**：每个页面的 Cancel 逻辑完全一致，代码冗余。
- **解决方案**：封装`cancelAndNavigateToStart`函数，统一处理导航和订单重置，优化代码结构。

### 问题4：顶部栏标题不更新
- **问题描述**：页面跳转后标题没有变化。
- **解决方案**：使用`currentBackStackEntryAsState()`监听返回堆栈变化，实时获取当前页面并更新标题。

## 五、实验验证结果
应用运行后功能完全符合要求：
1. 启动正常，默认显示 Start 页面，无返回按钮；
2. 点击 Start Order 可正常进入主菜页面，标题同步更新；
3. 支持逐级 Next 导航，支持返回按钮逐级返回；
4. 任意页面点击 Cancel 可返回 Start 并清空订单；
5. Checkout 页面点击 Submit 返回 Start 页面；
6. 点餐流程中按系统返回键直接退出应用；
7. 订单数据正常显示，浅色/深色模式界面正常。

## 六、实验总结
本次实验熟练掌握了 Jetpack Compose Navigation 组件的核心用法，理解了 NavController、NavHost、composable 的协作模式，学会了使用枚举类管理路由、动态构建顶部栏、合理管理页面返回堆栈。同时掌握了 Compose 中多页面应用的开发规范，能够独立完成完整的导航流程设计与实现，为后续复杂应用开发打下了基础。