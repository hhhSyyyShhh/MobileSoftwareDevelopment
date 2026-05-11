# Lab9 实验报告

## 一、ViewModel 在 Android 架构中的作用
ViewModel 是 Google 推荐的架构组件，主要作用：
1. 管理 UI 相关数据，与界面分离
2. 生命周期安全，屏幕旋转、配置变化时数据不丢失
3. 集中处理业务逻辑，让 UI 只负责展示
4. 便于单元测试，降低耦合

## 二、DessertUiState 字段设计
- revenue：总收入
- dessertsSold：已售数量
- currentDessertIndex：当前甜品索引
- currentDessertImageId：当前图片
- currentDessertPrice：当前单价
所有字段集中管理，界面只读取这一个对象。

## 三、DessertViewModel 设计思路
1. 使用 mutableStateOf(DessertUiState()) 管理状态
2. 对外暴露 uiState（只读）
3. 提供 onDessertClicked() 供界面调用
4. determineDessertToShow 作为内部业务逻辑
5. 使用 copy() 更新状态，触发 Compose 重组

## 四、MainActivity 重构前后对比
重构前：
- 所有状态写在 Composable 内
- 业务逻辑与 UI 混在一起
- 代码臃肿、难以维护

重构后：
- 无任何状态变量
- 无业务逻辑
- 只负责展示与触发事件
- 结构清晰、可维护、可测试

## 五、重构感受
ViewModel 让数据与 UI 分离，代码更清晰，逻辑更集中，旋转屏幕不会丢失数据，符合现代 Android 开发最佳实践。

## 六、遇到的问题与解决
1. 导入 viewModel() 失败 → 添加 lifecycle-viewmodel-compose 依赖
2. 状态无法更新 → 确保使用 copy() 生成新对象
3. 点击不生效 → 检查是否调用 viewModel.onDessertClicked()
全部顺利解决。