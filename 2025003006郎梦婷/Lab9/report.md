# Lab9 实验报告
## 一、ViewModel 在 Android 架构中的作用
ViewModel 是用于**分离 UI 与业务逻辑**的核心组件，生命周期独立于屏幕旋转等配置变更，专门负责：
1. 存储和管理 UI 状态
2. 处理业务逻辑与数据操作
3. 避免数据丢失与内存泄漏
4. 让 UI 层只负责展示与事件触发

## 二、DessertUiState 字段设计说明
- revenue：总收入
- dessertsSold：已售甜品数量
- currentDessertIndex：当前甜品索引
- currentDessertImageId：当前甜品图片
- currentDessertPrice：当前甜品单价
所有字段集中管理，让 UI 只需读取一个对象即可获取全部状态。

## 三、DessertViewModel 设计思路
1. 使用 mutableStateOf 包装 DessertUiState，使 Compose 自动监听状态变化
2. private set 保证外部无法直接修改状态，只能调用公开方法
3. 将 determineDessertToShow 业务逻辑移入 ViewModel
4. onDessertClicked 统一处理点击事件，更新状态

## 四、MainActivity 重构前后对比
- 重构前：所有状态、逻辑、函数都写在 UI 中，耦合严重
- 重构后：UI 仅负责展示，ViewModel 管理所有状态与逻辑
- 移除所有 rememberSaveable、mutableStateOf
- 点击事件仅调用 ViewModel 方法

## 五、重构感受
代码结构更清晰、职责更单一，ViewModel 让逻辑可测试、可维护，屏幕旋转状态不丢失，开发体验大幅提升。

## 六、遇到的问题与解决
1. 问题：viewModel() 无法导入
   解决：添加 lifecycle-viewmodel-compose 依赖
2. 问题：状态不更新
   解决：使用 data class copy() 生成新状态对象