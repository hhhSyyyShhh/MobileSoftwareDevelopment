# Lab9 为 Dessert Clicker 添加 ViewModel 实验报告
姓名：柳航
学号：2025003022
实验日期：2026-05-09

## 一、ViewModel 在 Android 架构中的作用
ViewModel 是 Jetpack 架构组件之一，专门用于管理界面相关的数据和业务逻辑。它的生命周期独立于 Activity 和 Compose 页面，当手机发生屏幕旋转、配置变更时，Activity 会重新创建，但 ViewModel 不会被销毁，可以自动保存页面数据，避免状态重置。
同时 ViewModel 可以把业务逻辑从 UI 界面中分离出来，让 UI 只负责界面展示和事件触发，逻辑统一放在 ViewModel 中，代码结构更清晰、便于维护和测试。

## 二、DessertUiState 数据类的字段设计说明
本次实验创建了 DessertUiState 数据类，用来集中管理页面所有 UI 状态，包含字段如下：
- revenue：记录当前总收入
- dessertsSold：记录已售出甜品总数
- currentDessertIndex：当前甜品在数据源列表中的索引
- currentDessertImageId：当前展示甜品的图片资源 ID
- currentDessertPrice：当前甜品的单价

把所有分散的状态统一封装在一个 UiState 类中，实现单一数据源，方便统一管理和更新界面状态。

## 三、DessertViewModel 设计思路
1. 使用 mutableStateOf 包装 UiState，让 Compose 可以自动监听状态变化，自动刷新界面。
2. 对 uiState 设置 private set，外部只能读取状态，不能直接修改，只能通过 ViewModel 提供的方法更新，保证数据安全。
3. 将原本在 MainActivity 中的 determineDessertToShow 甜品判断逻辑移入 ViewModel，和 UI 彻底解耦。
4. 提供 onDessertClicked 方法供 UI 调用，点击甜品后自动计算收入、销量，并根据销量自动切换对应甜品。

## 四、MainActivity 重构前后对比分析
### 重构前
1. 所有页面状态全部写在 MainActivity 内部，使用 rememberSaveable 单独定义。
2. 甜品判断逻辑、点击计算逻辑全部写在 UI 点击回调中。
3. UI 界面代码和业务逻辑混在一起，耦合度高，结构混乱，不方便后期修改。

### 重构后
1. 删除了所有 rememberSaveable 定义的本地状态，全部改用 ViewModel 中的 uiState 获取数据。
2. 页面只负责布局展示，点击事件仅调用 ViewModel 的方法，不做任何计算逻辑。
3. 移除了 MainActivity 中多余的顶层工具函数，业务逻辑全部迁移到 ViewModel。
4. 代码分工明确，UI 管展示，ViewModel 管状态和逻辑，结构整洁规范。

## 五、重构前后代码结构区别和感受
重构前整个项目所有状态、计算、判断逻辑全部堆积在 MainActivity 中，代码臃肿杂乱，一旦需要修改规则就要改动界面代码。
重构后采用 ViewModel + UiState 架构，状态统一管理，业务逻辑独立存放，UI 只负责渲染页面。整体代码分层清晰，符合安卓开发规范，后期维护、修改功能都更简单，同时屏幕旋转不会丢失数据，体验更好。

## 六、遇到的问题与解决过程
1. 问题：DessertUiState 中引用 R.drawable 报错找不到 R 文件。
解决：导入当前项目包名下的 R 类，资源引用恢复正常。
2. 问题：Compose 组件提示未导入、标红报错。
解决：补充缺失的 Compose Material3 相关导入语句，同步项目后恢复正常。
3. 问题：重构后预览界面报错。
解决：预览报错不影响模拟器正常运行，忽略预览提示即可。
4. 问题：替换状态后界面数据不更新。
解决：检查是否完全替换为 uiState 字段、是否删除旧的本地状态变量，修正后正常运行。