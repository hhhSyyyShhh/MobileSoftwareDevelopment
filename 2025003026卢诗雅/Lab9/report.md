# Lab9：为 Dessert Clicker 添加 ViewModel

## 一、实验概述

本次实验基于 Jetpack ViewModel 与 Compose 状态管理知识，对 **Dessert Clicker** 甜品点击游戏进行架构重构。原项目将数据、状态、逻辑全部写在 MainActivity 内，UI 与业务高度耦合。重构目标是分离 UI 层与数据逻辑层，使用 ViewModel 统一管理状态，提升代码可维护性、可测试性与生命周期稳定性。

## 二、ViewModel 在 Android 架构中的作用

1. **状态托管**：独立于 UI 生命周期管理应用数据，屏幕旋转、语言切换等配置变化时状态不丢失。

2. **逻辑分离**：将业务计算、数据处理、状态更新逻辑从 UI 中抽离，UI 只负责展示与事件触发。

3. **生命周期安全**：ViewModel 生命周期绑定 Activity/Fragment，比 Composable 更长，避免内存泄漏与重复初始化。

4. **数据共享**：可在同一页面的多个 Composable 间共享状态，减少状态层层传递。

5. **便于测试**：逻辑集中在 ViewModel，可单独进行单元测试，无需依赖 Android 框架。

## 三、DessertUiState 数据类设计说明

DessertUiState 用于**统一描述界面所需的全部数据**，是 UI 唯一依赖的数据来源。

|字段名|类型|默认值|作用|
|---|---|---|---|
|revenue|Int|0|总收入|
|dessertsSold|Int|0|已售甜品数量|
|currentDessertIndex|Int|0|当前甜品索引|
|currentDessertImageId|DrawableRes|R.drawable.cupcake|当前甜品图片|
|currentDessertPrice|Int|5|当前甜品单价|

设计原则：

- 只保留界面展示需要的数据，不包含业务逻辑。

- 使用 data class，方便用 copy () 更新状态。

- 提供合理默认值，保证界面初始状态正确。

## 四、DessertViewModel 设计思路

### 1. 结构设计

- 继承 ViewModel 类，享受生命周期管理能力。

- 用 **mutableStateOf(DessertUiState())** 持有状态，Compose 可自动观测重组。

- 使用 private set 限制外部修改，保证状态只能通过 ViewModel 方法更新。

### 2. 核心成员

- **uiState**：对外暴露的界面状态，只读。

- **desserts**：从 Datasource 加载甜品列表，私有不可变。

### 3. 核心方法

- **onDessertClicked()**：处理点击事件，更新收入与销量，调用 determineDessertToShow 判断是否升级甜品，最后用 copy () 更新 uiState。

- **determineDessertToShow()**：根据销量判断当前应显示的甜品，属于纯业务逻辑，移入 ViewModel。

## 五、MainActivity 重构前后对比

### 重构前

- 所有状态（revenue、dessertsSold 等）用 rememberSaveable 写在 Composable 内。

- 点击逻辑直接写在点击回调里，耦合严重。

- determineDessertToShow 作为顶层函数与 UI 混在一起。

- UI 负责展示、状态管理、逻辑计算，职责混乱。

### 重构后

- 无任何状态变量，全部从 viewModel.uiState 获取。

- 点击事件只调用 viewModel.onDessertClicked ()，不处理任何逻辑。

- 移除 determineDessertToShow 函数。

- UI 只做两件事：展示数据、发射用户事件，职责清晰。

- 代码更短、更易读、更容易维护与修改。

## 六、重构前后代码结构区别与感受

### 区别

- **耦合度**：重构前高耦合，重构后低耦合，UI 与逻辑彻底分离。

- **可维护性**：重构后修改逻辑只需改 ViewModel，不影响 UI。

- **可测试性**：ViewModel 可单独测试，无需运行应用。

- **稳定性**：屏幕旋转状态不丢失，体验更稳定。

- **可读性**：状态集中管理，代码流程更清晰。

### 感受

通过重构明显感受到 **单一职责原则** 的优势。UI 只关心界面，ViewModel 只关心数据与逻辑，代码结构更干净，后期加功能、改逻辑都非常轻松，也更符合官方推荐的 Compose 最佳实践。

## 七、遇到的问题与解决方法

1. **问题**：导入 viewModel () 时报错找不到方法。
**解决**：在 build.gradle.kts 添加 lifecycle-viewmodel-compose 依赖并同步。

2. **问题**：uiState 更新后界面不刷新。
**解决**：确保使用 mutableStateOf 包装，并且用 copy () 生成新对象，不要直接修改字段。

3. **问题**：Preview 报错。
**解决**：Preview 中可直接传入默认 ViewModel，或简化 Preview 代码。

4. **问题**：甜品升级逻辑错误。
**解决**：将 determineDessertToShow 完整移入 ViewModel 并保持逻辑不变，按销量顺序遍历判断。

## 八、实验总结

本次实验成功完成了 Dessert Clicker 的 ViewModel 架构重构，实现了 UI 与业务逻辑的分离。掌握了 UiState 设计、ViewModel 状态管理、Compose 与 ViewModel 协作的核心知识点，理解了 Android 官方推荐的**状态向下流、事件向上传**的架构模式，代码质量与应用稳定性都得到明显提升。

