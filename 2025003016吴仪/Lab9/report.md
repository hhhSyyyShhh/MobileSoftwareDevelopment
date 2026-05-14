# Lab9 实验报告：基于ViewModel重构甜品点击应用
姓名：吴仪
学号：2025003016

## 一、实验目的
1. 理解 ViewModel 组件的作用、生命周期以及在 Jetpack 架构中的地位。
2. 掌握使用 ViewModel 管理 Compose 界面状态的方法。
3. 学会使用 UiState 数据类统一封装界面所有UI状态。
4. 实现业务逻辑与界面代码解耦，遵循 MVVM 分层思想。
5. 熟悉 Gradle 依赖配置、Compose 项目报错排查与项目构建流程。

## 二、实验原理
ViewModel 是 Android Jetpack 提供的架构组件，专门用来存储和管理界面相关数据，独立于 Activity 和 Fragment 的生命周期。
当手机发生配置变更，例如屏幕旋转、语言切换、深色模式切换时，Activity 会重建，但 ViewModel 不会被销毁，内部数据可以自动保留，不用开发者手动保存恢复数据。

在 Compose 项目中，配合 `viewModel()` 可以在组合函数中获取 ViewModel 实例，
通过 `mutableStateOf` 包装状态数据，实现数据变化自动刷新UI，不用手动监听更新。

本实验将项目拆分为四层：
1. 视图层：MainActivity 只负责页面布局、控件展示、事件分发。
2. 状态层：DessertUiState 统一保存收入、销量、图片、价格等所有界面数据。
3. 逻辑层：DessertViewModel 处理点击逻辑、数据计算、状态更新。
4. 数据层：Datasource 提供所有甜品静态数据源。

## 三、代码结构说明
### 1. DessertUiState 数据类
采用 data class 定义，统一封装界面所有状态：
- revenue：累计总收入
- dessertsSold：已售出甜品数量
- currentDessertIndex：当前甜品索引
- currentDessertImageId：甜品图片资源ID
- currentDessertPrice：当前甜品单价

使用默认初始值，保证页面打开时有默认状态；利用 data class 自带 copy 方法，实现不可变状态更新。

### 2. DessertViewModel 核心作用
1. 持有 UI 状态 uiState，使用 Compose 可观察状态，界面自动订阅刷新。
2. 持有甜品数据源列表。
3. onDessertClicked()：处理甜品点击，累加收入、销量，自动切换甜品。
4. determineDessertToShow()：根据已售出数量，自动匹配解锁更贵的甜品。
5. 把业务计算逻辑全部从 Activity 抽离出来，界面只负责显示。

### 3. MainActivity 界面层
重构之后 Activity 不再处理任何计算逻辑，只做：
- 页面主题与入口组合函数
- 顶部导航栏、分享按钮布局
- 甜品图片、文字信息展示
- 点击事件回调给 ViewModel

完全实现界面与业务逻辑分离。

## 四、核心流程分析
1. 程序启动，初始化 DessertUiState 默认状态。
2. Compose 获取 DessertViewModel 实例，订阅 uiState。
3. 用户点击甜品图片，触发 onDessertClicked 方法。
4. ViewModel 自动计算新收入、新销量。
5. 根据销量自动判断切换对应甜品图片与价格。
6. 更新 uiState，Compose 检测到状态变化，自动刷新页面文字和图片。
7. 点击右上角分享按钮，可以调出系统分享，分享销售数据。

## 五、重构前后对比
### 重构前
1. 所有变量、计算逻辑、判断逻辑全部写在 Activity 里。
2. 界面代码和业务逻辑混在一起，臃肿杂乱。
3. 屏幕旋转数据直接丢失，需要额外写保存代码。
4. 不方便后期增加新甜品、修改价格规则。
5. 代码复用性差，无法单独测试业务逻辑。

### 重构后
1. 分层清晰：视图、状态、逻辑、数据完全分开。
2. ViewModel 自动保存数据，屏幕旋转数据不丢失。
3. Activity 代码精简，只负责UI展示。
4. 修改业务规则只需要改 ViewModel，不用动界面代码。
5. 结构规范，符合 Android MVVM 开发规范。

## 六、实验中遇到的问题及解决方法
### 问题1：viewModel() 标红报错
原因：缺少 lifecycle-viewmodel-compose 依赖。
解决：在 app 的 build.gradle.kts 添加对应依赖，同步 Gradle 后报错消失。

### 问题2：R.drawable 资源报红
原因：Gradle 同步失败、项目没有正常生成 R 资源类。
解决：Sync 同步项目，执行 Clean Project 和 Rebuild Project，重新生成资源索引。

### 问题3：运行模拟器提示 Device is offline
原因：模拟器进程卡死，和Android Studio断开连接。
解决：关闭模拟器重新启动，清理后台应用进程后再次运行。

### 问题4：无法在文件夹创建 Kotlin 文件
原因：文件名带空格、后缀错误、系统文件名非法。
解决：新建文本文档，直接命名为正确的 xxx.kt，不添加多余后缀和空格。

### 问题5：FloatingActionButton 里 mini = true 报错
原因：新版 Material3 不再支持 mini 参数。
解决：直接删除 mini=true 一行，编译正常。

## 七、实验总结
通过本次 Lab9 实验，我完整学习并实践了 ViewModel 组件的使用方法，理解了 MVVM 架构分层思想。
学会了用 UiState 统一管理界面状态，把业务逻辑全部放到 ViewModel 中，让 Activity 只专注页面展示。

同时熟悉了 Compose 项目依赖配置、Gradle 同步、资源报错、模拟器异常排查等常见开发问题。
重构后的代码结构清晰、职责分明，数据不会因屏幕旋转丢失，代码可读性、可维护性都大幅提升，为后续开发更复杂的 Android Compose 项目打下了扎实基础。