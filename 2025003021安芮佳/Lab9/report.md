# Lab9 实验报告
## 1. ViewModel 在 Android 架构中的作用
ViewModel 是 Android Jetpack 组件，主要作用：
- 分离 UI 层和业务逻辑层，降低耦合度
- 持有与 UI 相关的状态，且生命周期独立于 Activity/Fragment（如屏幕旋转时不销毁）
- 提供可测试的业务逻辑，便于单元测试
- 集中管理状态，保证数据一致性

## 2. DessertUiState 设计说明
DessertUiState 是纯数据类，集中封装所有 UI 展示所需状态：
- revenue：当前总收入，用于展示金额
- dessertsSold：已售甜品数量，用于展示销量和分享功能
- currentDessertIndex：当前甜品索引（备用字段）
- currentDessertImageId：当前甜品图片资源 ID，用于展示图片
- currentDessertPrice：当前甜品单价，用于计算收入
默认值与初始状态一致（初始甜品为 cupcake，单价 5 美元），保证应用启动时的初始状态正确。

## 3. DessertViewModel 设计思路
- 状态管理：使用 `mutableStateOf` 包装 `DessertUiState`，Compose 可自动观察状态变化并重组 UI；通过 `private set` 限制外部直接修改状态，保证数据安全性。
- 方法设计：
  - onDessertClicked()：处理核心点击逻辑，计算新收入/销量，更新当前甜品信息，通过 copy 方法创建新状态对象触发重组。
  - determineDessertToShow()：封装甜品升级逻辑，根据销量判断应展示的甜品，从 UI 层移入 ViewModel 后，UI 层无需关注业务规则。

## 4. MainActivity 重构对比
### 重构前
- 包含所有状态变量（revenue、dessertsSold 等），使用 rememberSaveable 保存
- 点击逻辑直接写在 Composable 回调中
- 业务逻辑（determineDessertToShow）与 UI 代码混在一起
- 状态分散，耦合度高

### 重构后
- 无任何状态变量，仅通过 ViewModel 获取 uiState
- 点击逻辑简化为调用 viewModel.onDessertClicked()
- 移除所有业务逻辑，仅保留 UI 展示和事件触发
- 依赖注入 ViewModel，便于测试和复用

## 5. 代码结构区别与感受
重构前：所有代码集中在 MainActivity，代码量大，职责不清晰，修改一个逻辑可能影响整个 UI。
重构后：
- 按职责拆分文件（状态、数据、ViewModel、UI）
- 单一职责原则：ViewModel 管逻辑，UI 管展示
- 代码可读性和可维护性大幅提升，后续修改甜品升级规则只需改 ViewModel，无需动 UI 代码

## 6. 遇到的问题与解决
- 问题1：ViewModel 导入失败 → 解决：确认添加了 lifecycle-viewmodel-compose 依赖并同步 Gradle。
- 问题2：Preview 报错 → 解决：使用 viewModel() 默认参数，Preview 自动使用 mock 实例。
- 问题3：图片资源缺失 → 解决：补充所有甜品图片到 drawable 目录，确保 Datasource 中图片 ID 与资源文件名一致。
- 问题4：屏幕旋转后状态丢失 → 解决：ViewModel 生命周期与 Activity 生命周期解耦，自动保留状态，无需额外处理。