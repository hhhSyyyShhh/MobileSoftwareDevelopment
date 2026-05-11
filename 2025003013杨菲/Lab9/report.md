# Lab9：为 Dessert Clicker 添加 ViewModel

## 一、ViewModel 在 Android 架构中的作用
ViewModel 是 Android Jetpack 中的架构组件，主要作用是分离 UI 层和业务逻辑层，管理应用的状态（如收入、销量），并在配置更改（如旋转屏幕）时保留状态。它独立于 Activity 的生命周期，不会随着 Activity 销毁而销毁，确保状态不丢失，同时让 UI 层只负责展示界面，业务逻辑集中管理，提升代码的可测试性和可维护性。

## 二、DessertUiState 数据类的字段设计说明
DessertUiState 是 UI 状态的集中管理类，包含 5 个字段，对应应用的所有界面状态：
1. revenue：Int 类型，默认值 0，存储当前应用的总收入；
2. dessertsSold：Int 类型，默认值 0，存储已售出的甜品总数；
3. currentDessertIndex：Int 类型，默认值 0，存储当前甜品在数据源列表中的索引；
4. currentDessertImageId：Int 类型（带 DrawableRes 注解），默认值 R.drawable.cupcake，存储当前显示的甜品图片资源 ID；
5. currentDessertPrice：Int 类型，默认值 5，存储当前甜品的单价。
字段默认值与应用起始状态一致，确保应用启动时显示正确的初始界面。

## 三、DessertViewModel 的设计思路
1. 状态管理：用 mutableStateOf 包装 DessertUiState 实例（uiState），让 Compose 能自动观察状态变化，触发界面重组；同时给 uiState 设置 private set，确保外部只能通过 ViewModel 的方法修改状态，保证状态的安全性和封装性。
2. 数据来源：通过 Datasource.dessertList 获取甜品列表，作为业务逻辑的数据源，与 UI 层分离。
3. 方法设计：
   - onDessertClicked()：处理甜品点击事件，计算新的收入和销量，调用 determineDessertToShow 确定当前应显示的甜品，最后用 copy 方法更新 uiState；
   - determineDessertToShow()：私有方法，根据已售甜品数量，从甜品列表中选择当前应展示的甜品，封装了甜品升级的业务规则，避免与 UI 代码耦合。

## 四、MainActivity 重构前后对比分析
### 重构前
- 所有状态（revenue、dessertsSold 等）都内联在 DessertClickerApp 可组合函数中，用 rememberSaveable 保存；
- 业务逻辑（点击甜品、切换甜品）直接写在 Composable 函数的回调中，与 UI 代码混在一起；
- 辅助函数 determineDessertToShow 作为顶层函数，与 UI 代码放在同一文件，耦合度高；
- 缺点：代码混乱，难以测试，维护成本高，状态管理分散。

### 重构后
- 移除了所有状态变量，通过 ViewModel 获取 uiState，UI 层只负责展示状态；
- 点击事件、甜品切换逻辑全部移到 ViewModel 中，MainActivity 只保留 UI 展示代码；
- 删除了顶层辅助函数，业务逻辑集中在 ViewModel 中；
- 优点：代码结构清晰，UI 与业务逻辑分离，可测试性强，维护方便，状态管理统一。

## 五、重构前后代码结构的区别和感受
### 结构区别
- 重构前：MainActivity 承担了所有职责（UI 展示、状态管理、业务逻辑），代码臃肿；
- 重构后：采用“UI 层（MainActivity）+ ViewModel 层（DessertViewModel）+ 状态类（DessertUiState）”的结构，职责分明，各层各司其职。

### 个人感受
重构前，代码混乱，修改一个逻辑（比如甜品升级规则）需要在 UI 代码中找很久；重构后，业务逻辑集中在 ViewModel 中，修改和维护更方便，而且旋转屏幕时状态不会丢失，解决了之前可能存在的状态丢失问题。同时，封装性更好，外部无法直接修改状态，避免了误操作导致的 bug，也更符合 Android 应用架构的最佳实践。

## 六、遇到的问题与解决过程
1. 问题：添加 ViewModel 依赖后，同步 Gradle 失败；
   解决：检查网络连接，确认依赖包版本正确，重新点击 Sync Now，清除 Gradle 缓存（File → Invalidate Caches...）后再次同步。
2. 问题：ViewModel 中导入 DessertUiState 时报错；
   解决：检查 DessertUiState 的包名是否正确（com.example.dessertclicker.ui），按 Alt+Enter 导入正确的包。
3. 问题：重构后点击甜品，收入和销量不更新；
   解决：检查 onDessertClicked 方法中，是否用 copy 方法更新了 uiState，以及 uiState 是否用 mutableStateOf 包装，确保状态变化能被 Compose 观察到。
4. 问题：旋转屏幕后状态丢失；
   解决：确认 ViewModel 是通过 viewModel() 方法获取的，该方法会自动与 Activity 生命周期绑定，确保配置更改时 ViewModel 不被销毁。