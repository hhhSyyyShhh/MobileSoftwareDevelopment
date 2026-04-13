# Android Compose 骰子摇一摇应用实验报告
## 一、实验概述
本实验基于Android Jetpack Compose框架开发**骰子摇一摇（Diceroller）** 应用，实现点击按钮随机生成1-6点骰子、并切换对应骰子图片的功能，同时完成应用界面分析、状态管理、图片切换、断点调试与问题排查等实验任务。

## 二、应用界面结构说明
本应用采用**Compose声明式UI**构建，整体界面为单一页面，层级结构清晰：
1. **根容器**：`DiceRollerApp`作为入口组合函数，直接调用`DiceWithButtonAndImage`核心组件。
2. **核心布局**：`DiceWithButtonAndImage`使用`Column`垂直布局，内部元素水平居中。
3. **界面元素**
    - 骰子图片：`Image`组件，展示当前点数对应的骰子图片。
    - 间距：`Spacer`组件，设置16dp高度，分隔图片与按钮。
    - 功能按钮：`Button`组件，点击触发骰子随机点数生成，文本为“Roll”。
4. **布局约束**：通过`Modifier`实现全屏填充、内容居中，适配边缘到边缘（Edge-to-Edge）显示。

整体界面简洁直观，核心交互区域居中展示，操作便捷。

## 三、Compose状态保存骰子结果的实现
应用通过**Compose状态管理机制**保存并响应骰子点数变化，核心代码逻辑：
1. **状态声明**：使用`remember`+`mutableStateOf`创建可观察状态变量`result`，初始值为1。
    ```kotlin
    var result by remember { mutableStateOf(1) }
    ```
2. **状态作用**：`remember`确保配置变更（如屏幕旋转）时状态不丢失，`mutableStateOf`使`result`成为可观察状态，值改变时自动触发UI重组。
3. **状态更新**：按钮点击事件中，通过`result = (1..6).random()`随机更新状态值，驱动界面刷新。

该方式是Compose推荐的**单向数据流**状态管理方案，状态与UI解耦，刷新高效。

## 四、根据点数切换图片资源的实现
通过**when条件表达式**匹配骰子点数，绑定对应图片资源，步骤如下：
1. **资源映射**：提前在`drawable`目录放入`dice_1`至`dice_6`六张骰子图片。
2. **条件匹配**：根据`result`状态值，选择对应图片资源ID：
    ```kotlin
    val imageResource = when(result){
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }
    ```
3. **图片渲染**：`Image`组件通过`painterResource(imageResource)`加载对应图片，`contentDescription`绑定点数文本，实现图片随点数实时切换。

## 五、断点设置与观察内容
本次调试在Android Studio中设置**3个关键断点**，分别观察状态、资源、交互逻辑：
1. **断点1**：状态声明行`var result by remember { mutableStateOf(1) }`
    - 观察内容：应用启动时`result`初始值是否为1，`remember`是否正常缓存状态。
2. **断点2**：when表达式图片资源赋值行
    - 观察内容：`result`点数与`imageResource`资源ID是否匹配，验证映射逻辑。
3. **断点3**：按钮点击事件`result = (1..6).random()`行
    - 观察内容：点击按钮时`result`是否生成1-6的随机数，状态是否成功更新。

调试结果：断点处变量值与预期完全一致，状态更新、资源匹配无异常。

## 六、Step Into、Step Over、Step Out使用体会
1. **Step Over（单步跳过）**
    - 用途：逐行执行当前函数代码，不进入子函数内部。
    - 体会：适合快速遍历主逻辑，如查看按钮点击后状态更新流程，效率最高。
2. **Step Into（单步跳入）**
    - 用途：进入当前行调用的子函数/系统函数内部。
    - 体会：用于深入`mutableStateOf`、`random()`等底层逻辑，理解Compose状态和随机数生成原理，但日常调试无需频繁使用。
3. **Step Out（单步跳出）**
    - 用途：退出当前子函数，返回上层调用函数。
    - 体会：跳入子函数后，快速回到主逻辑断点，避免冗余调试步骤。

总结：日常UI调试优先用**Step Over**；需深究底层逻辑用**Step Into**；退出子函数用**Step Out**，三者配合可高效定位问题。

## 七、遇到的问题与解决过程
### 问题1：应用启动后骰子图片不显示
- 原因：`Image`组件的`contentDescription`未正确赋值，且图片资源ID拼写错误。
- 解决：将`contentDescription`设为`result.toString()`，核对`drawable`资源ID与代码映射一致。

### 问题2：按钮点击后图片不刷新
- 原因：状态变量未用`mutableStateOf`声明，仅用普通`var`存储，无法触发UI重组。
- 解决：改为`var result by remember { mutableStateOf(1) }`，使状态可观察。

### 问题3：调试断点无法命中
- 原因：Compose预览界面与实际运行进程不匹配，断点附加到错误进程。
- 解决：运行应用后，在Android Studio**Attach Debugger to Android Process**中选择当前应用进程，断点正常生效。

## 八、实验结论
1. **按钮点击图片自动刷新原因**：`result`是Compose可观察状态，点击按钮更新`result`时，Compose自动触发重组，`Image`组件重新加载对应图片，实现界面刷新。
2. **调试变量与界面一致性**：调试器中观察的`result`随机数、`imageResource`资源ID，与界面显示的骰子点数、图片完全匹配，状态管理与图片切换逻辑正确。
3. **实验总结**：Compose的声明式UI、状态管理机制简化了Android开发，断点调试可快速验证逻辑，本次实验成功实现骰子摇一摇功能，掌握了Compose基础开发与调试技能。

---
要不要我帮你把这份报告**整理成标准Word实验报告格式**，直接可提交使用？