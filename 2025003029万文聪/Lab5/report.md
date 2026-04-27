# ArtSpace 应用开发报告

## 一、应用展示内容

### 主题

本应用是基于 **Jetpack Compose** 开发的艺术作品展示类应用（ArtSpace），用于浏览世界经典绘画作品，展示作品的图片、名称、作者与创作年份。

### 作品数量

应用内置 **5 幅** 经典西方绘画作品，包含《戴珍珠耳环的少女》《蒙娜丽莎》《抱银鼠的女子》《加利利海风暴》《星月夜》。

------

## 二、界面结构说明

### 界面区块划分

应用界面垂直分为 **3 个核心功能区块**：

1. **图片展示区**：显示艺术作品图片，带阴影边框美化
2. **作品信息区**：展示作品名称、加粗的作者名、创作年份
3. **操作按钮区**：包含 `Previous`（上一张）和 `Next`（下一张）切换按钮

### 可组合项与嵌套结构

采用 Compose 线性布局实现，层级结构如下：

```
根容器 Column（全屏、居中、外边距）
├─ Spacer：顶部间距
├─ Surface：图片容器（阴影效果）
│   └─ Image：展示作品图片
├─ Spacer：图片与文字间距
├─ Column：文字信息容器（背景色、内边距）
│   ├─ Text：作品名称
│   └─ Text：作者（加粗）+ 年份（富文本）
├─ Spacer：文字与按钮间距
└─ Row：按钮水平容器
    ├─ Button：上一张
    └─ Button：下一张
```

### 核心可组合项

- `Column`/`Row`：垂直 / 水平线性布局
- `Surface`：提供阴影、背景的容器组件
- `Image`：加载本地图片资源
- `Text`：展示文本，支持富文本样式
- `Button`：点击交互组件
- `Spacer`：控制界面间距
- `Modifier`：设置组件尺寸、边距、样式

------

## 三、Compose 状态管理（当前作品索引）

应用使用 **Compose 响应式状态** 管理当前展示的作品索引 `currentIndex`：

```kotlin
// 定义可观察状态，初始值为0（第一张作品）
var currentIndex by remember { mutableStateOf(0) }
```

1. `mutableStateOf(0)`：创建**可观察状态变量**，值改变时自动刷新 UI
2. `remember`：在界面重组时**保存状态**，防止数据丢失重置
3. 状态关联：通过 `artworks[currentIndex]` 获取当前作品数据，驱动图片、文字自动更新

------

## 四、Next / Previous 按钮逻辑说明

### 1. Previous（上一张）

实现**循环切换**：第一张的上一张为最后一张

```kotlin
currentIndex = if (currentIndex > 0) {
    currentIndex - 1        // 非第一张，索引减1
} else {
    artworks.size - 1       // 第一张，跳转到最后一张
}
```

### 2. Next（下一张）

利用**取模运算**实现简洁的循环切换：

```kotlin
currentIndex = (currentIndex + 1) % artworks.size
```

- 最后一张 +1 后取模，自动回到索引 0（第一张）
- 无越界风险，适配任意作品数量

------

## 五、遇到的问题与解决过程

### 问题 1：按钮固定在屏幕最底部，无法调整间距

- 原因：根布局使用 `Arrangement.SpaceBetween`，强制组件上下撑开
- 解决：修改为 `Arrangement.Top`，改为从上到下顺序排列，通过 `Spacer` 自由控制间距

### 问题 2：点击按钮后，界面不更新

- 原因：使用普通变量 `var currentIndex = 0`，Compose 无法感知变量变化
- 解决：使用 `remember { mutableStateOf(0) }` 声明响应式状态

### 问题 3：文字区域无法仅让作者名加粗

- 原因：直接设置 `Text` 加粗会作用于全部文字
- 解决：使用 `buildAnnotatedString` + `SpanStyle` 实现富文本，单独加粗作者

### 问题 4：图片无层次感，界面单调

- 原因：直接使用 `Image` 组件，无装饰效果
- 解决：嵌套 `Surface` 组件，添加 `shadow` 阴影样式

### 问题 5：上一张按钮点击出现索引越界崩溃

- 原因：未处理索引为 0 时的边界逻辑
- 解决：增加 `if-else` 判断，实现循环切换逻辑