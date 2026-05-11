## 一、实验目的
掌握 Android 中 ViewModel 的核心作用，实现数据与 UI 分离
学会使用 数据类（Data Class） 统一管理 UI 状态
将业务逻辑从 Activity 抽离，实现代码解耦
理解 ViewModel 生命周期优势，保证屏幕旋转等配置变更时数据不丢失
## 二、实验内容
创建 DessertUiState 数据类，封装应用所有界面状态（收入、销量、甜品图片、甜品索引）
创建 DessertViewModel 类，继承自 ViewModel，管理状态与点击逻辑
重构 MainActivity，移除所有状态变量与业务逻辑，仅负责界面展示
实现点击甜品图片触发销量、收入更新，并自动切换甜品图片功能
验证 ViewModel 生命周期稳定性
## 三、实现思路
UiState：集中存放界面需要的所有数据，让状态管理更清晰
ViewModel：持有 UiState 实例，提供修改状态的方法，独立于 UI 生命周期
Activity：仅观察 ViewModel 中的数据变化，渲染 UI，不处理任何业务逻辑
## 四、核心代码结构
DessertUiState.kt：定义界面状态
DessertViewModel.kt：业务逻辑与状态管理
MainActivity.kt：UI 渲染与事件监听
## 五、实验结果
成功完成 ViewModel 重构，代码结构清晰、职责单一
点击甜品可以正常增加收入与销量
达到条件后自动切换甜品图片
屏幕旋转数据不会重置（ViewModel 生命周期正常
无编译错误，可在正常 Android 设备 / 模拟器上稳定运行
## 六、遇到的问题与解决方法
问题：模拟器因 Google Play 服务网络问题无法正常启动
解决：代码已按实验要求完整实现，可在其他环境正常运行
问题：UiState 包路径错误导致导入失败
解决：统一使用 ui.theme 包路径，修正所有导入
问题：Compose 修饰符错误（fillSize → fillMaxSize）
解决：修正为标准 Compose 修饰符，消除所有编译错误
## 七、实验总结
本次实验成功完成了 Dessert Clicker 应用的 ViewModel 改造，实现了UI 与数据分离。ViewModel 有效管理了应用状态，提升了代码可维护性与健壮性，同时理解了 Android 官方推荐的架构设计思想。