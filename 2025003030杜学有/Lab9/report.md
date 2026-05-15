# 实验报告
## 一、ViewModel 在 Android 架构中的作用简述
ViewModel 是 Android Jetpack 组件库中的核心组件之一，主要用于**分离 UI 层与业务逻辑层**，并管理与 UI 相关的状态。其核心作用体现在以下方面：
1. **生命周期感知**：ViewModel 的生命周期独立于 Activity/Fragment 的配置变更（如屏幕旋转、语言切换），可避免因配置变更导致的状态丢失，无需通过 `onSaveInstanceState` 等方式手动保存/恢复数据。
2. **逻辑解耦**：将原本内联在 UI 中的业务逻辑（如状态计算、事件处理）抽离到 ViewModel 中，使 UI 层仅负责展示数据和触发事件，降低代码耦合度。
3. **可测试性提升**：ViewModel 不持有 UI 控件引用，可脱离 Activity/Fragment 独立进行单元测试，无需依赖 Android 运行环境。
4. **状态集中管理**：集中封装 UI 所需的所有状态，避免状态分散在多个 Composable 或 Activity 中，便于维护和状态追踪。

## 二、DessertUiState 数据类的字段设计说明
`DessertUiState` 作为界面状态的“单一数据源”，其字段设计完全匹配 Dessert Clicker 应用的 UI 展示和交互需求，各字段说明如下：

| 字段名                | 类型    | 默认值                | 设计目的                                                                 |
|-----------------------|---------|-----------------------|--------------------------------------------------------------------------|
| `revenue`             | Int     | 0                     | 存储当前总收入，为界面顶部“Revenue”区域提供展示数据                     |
| `dessertsSold`        | Int     | 0                     | 存储已售甜品总数，用于分享功能和甜品升级逻辑判断                         |
| `currentDessertIndex` | Int     | 0                     | 标记当前甜品在数据源列表中的索引，作为甜品升级逻辑的辅助参考             |
| `currentDessertImageId` | Int   | R.drawable.cupcake    | 存储当前展示甜品的图片资源 ID，为界面中央的甜品图片组件提供数据源       |
| `currentDessertPrice` | Int     | 5                     | 存储当前甜品的单价，点击甜品时用于计算收入增量，与初始甜品（cupcake）单价一致 |

设计原则：
- **最小完备性**：仅包含 UI 层所需的核心状态，不冗余、不遗漏；
- **默认值匹配初始状态**：默认值与应用启动时的初始界面状态（展示 cupcake、收入为 0）完全一致；
- **语义清晰**：字段名直接对应业务含义，便于 UI 层和 ViewModel 层理解和使用；
- **使用注解增强可读性**：通过 `@DrawableRes` 注解标记图片资源 ID，提升代码可读性和编译期校验能力。

## 三、DessertViewModel 的设计思路
### 1. 状态管理设计
- **核心状态容器**：使用 `mutableStateOf(DessertUiState())` 封装 UI 状态，利用 Compose 的状态感知特性，当状态变化时自动触发 UI 重组；
- **封装性保障**：通过 `private set` 限制 `uiState` 的外部修改权限，确保所有状态变更只能通过 ViewModel 暴露的方法（如 `onDessertClicked()`）完成，避免状态被随意篡改；
- **不可变更新**：使用 `DessertUiState` 的 `copy()` 方法更新状态，保证每次状态变更都生成新的对象，符合 Compose 状态更新的不可变原则。

### 2. 方法设计
| 方法名                  | 访问修饰符 | 设计目的                                                                 |
|-------------------------|------------|--------------------------------------------------------------------------|
| `onDessertClicked()`    | public     | 处理甜品点击核心逻辑：计算新收入/销量 → 判定应展示的甜品 → 更新 UI 状态   |
| `determineDessertToShow()` | private  | 封装甜品升级规则：根据已售数量遍历甜品列表，返回当前应展示的甜品对象     |

设计逻辑：
1. **业务逻辑内聚**：将原本散落在 MainActivity 中的“点击计算”“甜品升级判断”逻辑全部移入 ViewModel，使 UI 层无需关注逻辑细节；
2. **单一职责**：`onDessertClicked()` 仅负责处理点击事件的核心流程，`determineDessertToShow()` 仅负责甜品升级判断，符合单一职责原则；
3. **数据隔离**：通过 `private val desserts` 持有甜品数据源，避免 UI 层直接访问数据源，降低层与层之间的依赖。

## 四、MainActivity 重构前后对比分析
### 重构前
| 问题点                     | 具体表现                                                                 |
|----------------------------|--------------------------------------------------------------------------|
| 状态分散且耦合              | 直接在 `DessertClickerApp()` 中通过 `rememberSaveable` 定义 `revenue`、`dessertsSold` 等多个独立状态变量 |
| 逻辑与 UI 混编              | 点击回调、`determineDessertToShow()` 函数直接写在 Composable 中，UI 代码与业务逻辑交织 |
| 状态修改无约束              | 可在任意位置直接修改 `revenue`、`dessertsSold` 等状态，易引发不可预期的状态变更 |
| 配置变更易丢失状态          | 依赖 `rememberSaveable` 保存状态，虽能恢复但代码冗余，且状态分散管理成本高 |

### 重构后
| 优化点                     | 具体表现                                                                 |
|----------------------------|--------------------------------------------------------------------------|
| 状态集中管理                | 移除所有 `rememberSaveable` 状态变量，通过 `viewModel().uiState` 获取统一的 UI 状态 |
| 逻辑完全抽离                | 点击回调仅调用 `viewModel.onDessertClicked()`，无任何业务逻辑；删除 `determineDessertToShow()` 顶层函数 |
| 状态修改受约束              | UI 层仅能读取 `uiState`，无法直接修改，所有变更通过 ViewModel 方法完成     |
| 配置变更状态不丢失          | 依赖 ViewModel 的生命周期特性，屏幕旋转后状态自动保留，无需额外代码       |
| UI 职责单一                | 仅负责展示数据（读取 `uiState`）和触发事件（调用 ViewModel 方法），符合“展示层”定位 |

## 五、重构前后代码结构的区别和感受
### 1. 代码结构区别
| 维度         | 重构前                                  | 重构后                                  |
|--------------|-----------------------------------------|-----------------------------------------|
| 文件职责     | MainActivity 包揽“UI + 状态 + 逻辑”      | MainActivity 仅负责 UI；ViewModel 负责状态 + 逻辑；DessertUiState 定义状态结构 |
| 代码行数     | MainActivity 代码量大，包含大量逻辑代码  | MainActivity 代码量减少，仅保留 UI 渲染代码；逻辑代码迁移至 ViewModel |
| 依赖关系     | UI 直接依赖数据源，耦合度高              | UI 仅依赖 ViewModel，ViewModel 依赖数据源，分层清晰 |

### 2. 重构感受
- **可读性提升**：重构后 MainActivity 的代码聚焦于 UI 布局和状态展示，无需在 UI 代码中穿插业务逻辑，阅读时可快速定位核心 UI 结构；
- **可维护性提升**：若需修改甜品升级规则，只需调整 ViewModel 中的 `determineDessertToShow()` 方法，无需改动 UI 代码；若需调整 UI 展示，只需修改 MainActivity，不影响业务逻辑；
- **稳定性提升**：`private set` 限制了状态的随意修改，避免因误操作导致的状态异常，降低了 Bug 出现的概率；
- **理解成本降低**：分层设计使代码职责边界清晰，新接手开发者可快速区分“UI 层”和“逻辑层”，便于协作开发。

## 六、遇到的问题与解决过程
### 问题 1：添加 ViewModel 依赖后，`viewModel()` 函数无法导入
- 现象：在 `app/build.gradle.kts` 中添加 `lifecycle-viewmodel-compose` 依赖后，同步 Gradle 仍提示无法解析 `viewModel()` 函数；
- 原因：依赖版本与项目中已有的 `lifecycle-runtime-ktx` 版本不兼容；
- 解决：统一 lifecycle 相关依赖版本，将 `lifecycle-viewmodel-compose` 版本调整为与 `lifecycle-runtime-ktx` 一致（2.8.7），重新同步 Gradle 后问题解决。

### 问题 2：重构后点击甜品无反应，状态未更新
- 现象：点击甜品图片后，收入和销量未变化，甜品也未升级；
- 原因：在 `onDessertClicked()` 中更新 `uiState` 时，遗漏了 `currentDessertIndex` 字段的更新，导致后续逻辑判断异常；
- 解决：在 `uiState.copy()` 中补充 `currentDessertIndex = dessertToShow` 对应的索引值，重新运行后状态更新正常。

### 问题 3：Preview 函数报错，提示无法获取 ViewModel 实例
- 现象：`@Preview` 注解的 Composable 函数因无法解析 `viewModel()` 而报错；
- 原因：Preview 环境无 Activity/Fragment 生命周期，无法自动创建 ViewModel 实例；
- 解决：为 Preview 函数手动传入 Mock ViewModel 或简化 Preview 代码，仅展示静态 UI：
  ```kotlin
  @Preview
  @Composable
  fun DessertClickerPreview() {
      DessertClickerScreen(
          revenue = 0,
          dessertsSold = 0,
          dessertImageId = R.drawable.cupcake,
          onDessertClicked = {},
          modifier = Modifier.padding(16.dp)
      )
  }
  ```

### 问题 4：旋转屏幕后分享功能展示的数据异常
- 现象：旋转屏幕后点击分享，分享内容中的销量/收入与界面展示不一致；
- 原因：分享函数中仍引用了重构前的局部状态变量，未替换为 `uiState` 中的值；
- 解决：将分享函数的参数从原有局部变量替换为 `uiState.revenue` 和 `uiState.dessertsSold`，确保分享数据与当前状态一致。

## 七、总结
本次实验通过为 Dessert Clicker 应用引入 ViewModel 和 UI 状态数据类，完成了从“UI 与逻辑混编”到“分层解耦”的重构。重构后的应用不仅解决了状态管理混乱、逻辑耦合的问题，还提升了代码的可测试性、可维护性和稳定性。通过本次实验，深刻理解了 ViewModel 在 Android 架构中的核心价值——让 UI 层专注于展示，让 ViewModel 专注于状态和逻辑，这也是现代 Android 开发中“关注点分离”设计原则的核心体现。