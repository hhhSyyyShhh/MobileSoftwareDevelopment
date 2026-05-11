# Lab9 实验报告
为 Dessert Clicker 添加 ViewModel

## 一、ViewModel 在 Android 架构中的作用
ViewModel 是 Jetpack 组件中负责管理界面数据与业务逻辑的核心类，它的主要作用如下：
1. 独立于 UI 生命周期，屏幕旋转、配置更改时数据不会丢失。
2. 统一集中管理 UI 状态，避免数据散落在各个 Composable 中。
3. 将业务逻辑与 UI 展示分离，让 Activity/Composable 只负责显示和交互。
4. 提高代码可测试性，逻辑可单独单元测试，不依赖 Android 框架。
5. 实现单一数据源原则，保证界面状态一致性。

## 二、DessertUiState 数据类的字段设计说明
DessertUiState 用于集中存放界面所需的所有状态，字段设计如下：
- revenue：当前总收入，初始值 0。
- dessertsSold：已售出甜品数量，初始值 0。
- currentDessertIndex：当前甜品在列表中的索引，初始值 0。
- currentDessertImageId：当前甜品图片资源 ID，初始为 cupcake。
- currentDessertPrice：当前甜品单价，初始为 5。

所有字段均为不可变 val，通过 copy() 更新，确保状态可预测、可追踪。

## 三、DessertViewModel 的设计思路
1. 继承 ViewModel，让系统管理其生命周期，屏幕旋转不重建。
2. 使用 mutableStateOf 包装 DessertUiState，使 Compose 可观察状态变化。
3. 使用 private set 限制外部修改，保证状态只能通过内部函数更新。
4. 将甜品列表 Datasource.dessertList 作为内部数据来源。
5. 提供 onDessertClicked() 处理点击业务：
   - 计算新收入与销量
   - 调用 determineDessertToShow() 判断应显示的甜品
   - 使用 copy() 更新状态并触发界面刷新
6. 将 determineDessertToShow() 从 UI 移入 ViewModel，属于业务规则。

## 四、MainActivity 重构前后对比分析
### 重构前
- 所有状态（revenue、dessertsSold、index、price、imageId）直接写在 Composable 内。
- 点击逻辑与 UI 耦合，代码混乱难以维护。
- determineDessertToShow 作为顶层函数与 UI 混在一起。
- 旋转屏幕会丢失所有数据。

### 重构后
- UI 只负责展示，不持有任何状态、不处理任何业务。
- 所有数据来自 viewModel.uiState。
- 点击事件仅调用 viewModel.onDessertClicked()。
- 代码结构清晰，易于阅读、测试、扩展。
- 旋转屏幕状态不丢失。

## 五、重构前后代码结构的区别和感受
重构前代码全部写在 MainActivity 一个文件里，逻辑与 UI 混杂，修改一处容易影响多处。
重构后遵循单一职责原则：
- UiState：管理界面数据
- ViewModel：管理业务逻辑
- MainActivity：只做 UI 渲染

明显感受到代码更干净、更安全、更易维护，也更符合 Android 官方推荐的架构模式。

## 六、遇到的问题与解决过程
1. 包结构冲突：ui 与 ui.theme 无法共存
   解决：将主题文件统一放入 ui 包，调整包名与导入，消除冲突。

2. Theme.kt 出现 Typography 爆红
   解决：移除 MaterialTheme 中的 typography 参数，使用系统默认值。

3. stringResource 爆红
   解决：在 strings.xml 补全实验要求的所有字符串资源。

4. ViewModel 无法导入 DessertUiState
   解决：确认包路径正确，添加 import com.example.dessertclicker.ui.DessertUiState。

5. 界面点击无反应
   解决：检查 ViewModel 状态更新逻辑，确保使用 copy() 并正确替换图片与价格。

## 七、实验总结
本次实验成功完成了 Dessert Clicker 的架构重构，掌握了：
- UiState 数据类的设计与使用
- ViewModel 的状态管理与业务封装
- Compose 与 ViewModel 结合的最佳实践
- 单一职责、关注点分离的架构思想
- 旋转不丢失数据的状态保存方式

代码完全符合实验要求，可正常运行、点击升级、分享、屏幕旋转状态保持。