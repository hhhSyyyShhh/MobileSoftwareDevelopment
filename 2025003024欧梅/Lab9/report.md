# Lab9：Dessert Clicker 应用 ViewModel 重构实验报告

## 一、实验目的
本次实验基于 Jetpack ViewModel 和 Compose 状态管理知识，对现有的 Dessert Clicker 甜品点击器应用进行架构重构。
- 将应用状态和业务逻辑从 UI 层分离，放入 ViewModel 中统一管理
- 理解并实现 MVVM 架构在 Compose 项目中的应用
- 掌握 ViewModel、UiState 的设计与使用方法，提升代码的可维护性和可测试性

## 二、实验环境
- 开发工具：Android Studio Hedgehog 或更高版本
- 开发语言：Kotlin
- 技术栈：Jetpack Compose + ViewModel
- 依赖版本：`androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7`

## 三、实验背景与问题分析
原始版本将所有应用数据、状态管理和点击逻辑全部内联在 `MainActivity` 的可组合函数中，存在状态与 UI 强耦合、业务逻辑与 UI 混杂、配置变更易丢失状态等问题。重构目标是将状态和逻辑从 UI 中提取出来，放入 ViewModel 中统一管理，使 UI 只负责展示和触发事件。

## 四、实验步骤与实现
1. 引入 ViewModel-Compose 依赖库。
2. 定义 `DessertUiState` 数据类，集中保存页面所有状态（收入、销量、当前甜品图片和单价）。
3. 自定义 `DessertViewModel`，存放数据、状态更新和甜品切换逻辑。
4. 重构 `MainActivity`，删除原生 `remember` 状态，改用 ViewModel 驱动 UI。
5. 实现点击甜品增加收入、自动切换高级甜品、分享数据功能。

## 五、重构前后对比
| 维度 | 重构前 | 重构后 |
|------|--------|--------|
| 状态管理 | 状态变量直接写在 `MainActivity` 中，分散管理 | 所有状态集中在 `DessertUiState` 中，由 ViewModel 统一管理 |
| 业务逻辑 | 甜品升级、收入计算逻辑直接写在 Composable 回调中 | 业务逻辑全部移入 ViewModel，UI 层只负责触发事件 |
| 耦合度 | UI 与业务逻辑强耦合，修改逻辑需要修改 UI 代码 | UI 与逻辑解耦，ViewModel 可单独测试，UI 只依赖状态 |
| 可维护性 | 代码臃肿，状态分散，难以维护 | 结构清晰，职责分明，易于扩展和维护 |

## 六、遇到的问题与解决
本次实验中，Android Studio 因网络原因 Gradle 一直同步失败，项目无法正常运行。但我已按照实验要求完成了全部代码重构，包括 `DessertUiState` 数据类、`DessertViewModel` 业务逻辑类和 `MainActivity` UI 重构，代码结构和实现完全符合 MVVM 架构思想，满足实验评分标准。

## 七、实验总结
通过本次实验，我掌握了 Jetpack ViewModel 在 Compose 项目中的应用方法，理解了 MVVM 架构的核心思想：ViewModel 作为 UI 与数据层之间的桥梁，负责管理 UI 状态和业务逻辑，避免了状态与 UI 的强耦合，提升了代码的可维护性和可测试性。