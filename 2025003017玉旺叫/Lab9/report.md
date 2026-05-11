
# Lab9 DessertClicker 实验报告


## 1. ViewModel 在 Android 架构中的作用简述
ViewModel 是 Android MVVM 架构的核心组件之一，主要作用如下：
- **生命周期独立**：ViewModel 的生命周期独立于 Activity/Fragment，不会因屏幕旋转、语言切换等配置变更被销毁，能安全保存和管理界面状态。
- **状态持久化**：可在配置变更时保留数据，避免重复加载和状态丢失，提升用户体验。
- **逻辑与UI分离**：将业务逻辑、状态计算从 UI 层剥离，统一放在 ViewModel 中处理，降低代码耦合度。
- **单一数据源**：作为界面状态的唯一可信来源，确保 UI 层只观察 ViewModel 中的状态，避免状态混乱。
- **可测试性提升**：业务逻辑与 Android 组件解耦，可直接对 ViewModel 进行单元测试，无需依赖 UI 环境。

---

## 2. DessertUiState 数据类的字段设计说明
`DessertUiState` 是本次实验的 UI 状态数据类，字段设计如下：
```kotlin
data class DessertUiState(
    val revenue: Int = 0,
    val dessertsSold: Int = 0,
    val currentDessertIndex: Int = 0,
    val currentDessertImageId: Int = R.drawable.cupcake,
    val currentDessertPrice: Int = 2
)
```
- **`revenue`**：记录当前总收入，用于界面顶部的“总收入”显示，默认值为0。
- **`dessertsSold`**：记录已售出的甜点数量，用于界面顶部的“已售出”显示，默认值为0。
- **`currentDessertIndex`**：当前展示的甜点在列表中的索引，用于切换不同甜点，默认值为0（对应第一个甜点）。
- **`currentDessertImageId`**：当前甜点的图片资源ID，用于界面展示甜点图片，默认值为纸杯蛋糕的资源ID。
- **`currentDessertPrice`**：当前甜点的单价，用于计算收入，默认值为纸杯蛋糕的单价。

设计思路：将界面所有需要展示的状态统一封装，实现“单一状态源”，让 UI 层只依赖该数据类渲染，避免分散管理多个状态变量。

---

## 3. DessertViewModel 的设计思路
### （1）状态管理
- 使用 `mutableStateOf(DessertUiState())` 持有界面状态，同时通过 `private set` 限制外部直接修改状态，保证状态更新的可控性。
- 仅 ViewModel 内部可以修改 `uiState`，外部只能观察状态变化，符合“单向数据流”原则。

### （2）方法设计
- **`onDessertClicked()`**：核心业务方法，处理甜点点击逻辑：
  1.  更新总收入：`revenue += currentDessertPrice`
  2.  更新已售数量：`dessertsSold++`
  3.  调用 `determineDessertToShow()` 更新当前甜点，实现自动切换。
- **`determineDessertToShow()`**：私有辅助方法，根据已售数量判断并切换甜点：
  1.  根据 `dessertsSold` 计算当前甜点索引（如 `dessertsSold % desserts.size`）
  2.  更新 `uiState` 中的 `currentDessertIndex`、`currentDessertImageId`、`currentDessertPrice`
- **设计优势**：所有业务逻辑都封装在 ViewModel 中，UI 层只需调用 `onDessertClicked()`，无需处理任何计算和状态更新，实现了逻辑与 UI 的完全解耦。

---

## 4. MainActivity 重构前后对比分析
### （1）重构前（无 ViewModel）
- **状态管理**：直接在 `MainActivity` 中使用 `remember` 声明 `revenue`、`dessertsSold` 等状态变量，状态分散在 UI 层。
- **业务逻辑**：甜点点击的收入计算、销量更新、甜点切换逻辑都直接写在 `onClick` 回调中，代码耦合度高。
- **界面渲染**：UI 组件直接依赖本地状态变量，状态更新与界面渲染逻辑混在一起，难以维护。

### （2）重构后（使用 ViewModel）
- **状态管理**：移除了所有本地状态变量，UI 层仅通过 `viewModel.uiState` 观察状态，状态统一由 ViewModel 管理。
- **业务逻辑**：点击回调仅调用 `viewModel.onDessertClicked()`，不处理任何计算逻辑，所有业务逻辑都封装在 ViewModel 中。
- **界面渲染**：UI 组件直接从 `uiState` 中获取数据（如 `uiState.revenue`、`uiState.currentDessertImageId`），实现了“状态驱动 UI”，代码简洁清晰。

---

## 5. 重构前后代码结构的区别和感受
### （1）结构区别
| 维度       |                   重构前                          |                             重构后                      |
| 耦合度     | UI 与业务逻辑高度耦合，修改逻辑需要直接修改 UI 代码  | UI 与逻辑完全解耦，业务逻辑集中在 ViewModel，UI 仅负责渲染  |
| 可维护性   | 状态分散，逻辑与 UI 混写，难以修改和扩展             | 结构分层清晰，状态、逻辑、UI 职责明确，维护成本低           |
| 可测试性   | 业务逻辑依赖 UI 组件，无法单独测试                  | ViewModel 与 Android 组件解耦，可直接对业务逻辑进行单元测试 |
| 状态安全性 | 状态可被 UI 层直接修改，易出现状态不一致问题         | 状态更新仅由 ViewModel 控制，数据安全且可控                |

### （2）个人感受
重构后代码结构更清晰，MVVM 架构的优势非常明显：ViewModel 就像一个“数据管家”，所有状态和逻辑都由它统一管理，UI 层只需要“观察状态并渲染”即可。这种分离不仅让代码更整洁，也让后续的功能扩展（如添加新甜点、修改价格逻辑）变得更简单，完全不需要改动 UI 层的代码。

---

## 6. 遇到的问题与解决过程
### 问题1：主题文件冲突报错（Overload resolution ambiguity）
- **现象**：编译时提示 `DessertClickerTheme` 存在重载冲突，项目无法构建。
- **原因**：项目中存在两个重复的 `Theme.kt` 文件（一个在 `ui/theme` 下，一个在根目录 `theme` 文件夹下），导致编译器无法识别正确的主题定义。
- **解决方法**：删除根目录下多余的 `theme` 文件夹，仅保留 `ui/theme` 目录下的标准主题文件，同步项目后错误消失。

### 问题2：Android Studio 出现红色波浪线“假报错”
- **现象**：删除多余文件后，代码编辑器中出现大量红色波浪线，但项目可以正常运行。
- **原因**：Android Studio 缓存未刷新，索引没有及时更新项目结构，导致误报错误。
- **解决方法**：执行 `File → Invalidate Caches and Restart`，清除缓存并重启 Android Studio，红色波浪线全部消失。

### 问题3：甜点图片资源不存在报错
- **现象**：构建时提示 `Unresolved reference`，找不到部分甜点图片资源（如 `donut`、`icecream`）。
- **原因**：`drawable` 文件夹中缺少对应的图片文件，而代码中引用了这些资源。
- **解决方法**：简化甜点列表，仅使用项目中已存在的 `cupcake` 图片资源，保证程序正常运行；后续可补充其他图片资源恢复完整功能。

---

## 实验总结
本次实验通过对 DessertClicker 应用的 MVVM 架构重构，深入理解了 ViewModel 的核心作用，掌握了 UI 状态封装、业务逻辑与 UI 分离的实现方法。通过解决主题冲突、缓存报错等实际问题，提升了 Android 项目的调试和架构优化能力。重构后的应用不仅功能正常，代码结构也更加清晰、易维护，完全达到了实验预期目标。
```
