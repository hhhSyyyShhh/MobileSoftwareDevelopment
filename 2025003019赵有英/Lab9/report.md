# Lab9 实验报告
学号：2025003019
姓名：赵有英

## 一、ViewModel 在 Android 架构中的作用
ViewModel 是专门用来管理界面数据和业务逻辑的组件，它的生命周期比界面更长，旋转屏幕、切换横竖屏时数据不会丢失。
它可以将 UI 代码与逻辑代码分离，让代码更清晰、更容易测试、更容易维护。

## 二、DessertUiState 数据类字段设计
DessertUiState 用于统一管理界面所有状态：
- revenue：当前总收入
- dessertsSold：已卖出甜品数量
- currentDessertIndex：当前甜品索引
- currentDessertImageId：当前甜品图片
- currentDessertPrice：当前甜品价格

所有界面展示数据都集中在这里，便于观察和管理。

## 三、DessertViewModel 设计思路
1. 使用 ViewModel 存储所有状态和逻辑
2. 通过 mutableStateOf(DessertUiState()) 让 Compose 自动监听状态变化
3. 把点击逻辑 onDessertClicked() 放在 ViewModel 中
4. 把判断甜品切换的逻辑 determineDessertToShow 放在 ViewModel
5. 使用 private set 保护数据，外部只能调用方法不能直接修改

## 四、MainActivity 重构前后对比
重构前：
- 所有变量、逻辑、状态都写在 UI 里
- 代码混乱、耦合度高、难以测试

重构后：
- UI 只负责显示界面
- 所有状态和逻辑都在 ViewModel
- 结构清晰、职责单一、屏幕旋转不丢失数据

## 五、重构前后代码结构区别
重构前：UI 和逻辑混在一起
重构后：遵循单一职责原则，逻辑与界面分离，符合 Jetpack 架构规范。

## 六、遇到的问题与解决
1. 找不到 build.gradle.kts 文件 → 找到 app 模块下的文件
2. 依赖未添加 → 添加 viewmodel-compose 依赖
3. 状态报错 → 按照教程删除旧状态，使用 uiState 替代
4. 函数参数错误 → 按照教程修改 DessertClickerApp 参数