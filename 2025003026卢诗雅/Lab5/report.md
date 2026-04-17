# Lab5：创建 Art Space 应用

## 一、应用展示内容
1. 应用主题
ArtSpace 是基于 Android Jetpack Compose 开发的艺术画作展示应用，用于展示经典艺术作品，支持切换查看图片与作品信息。

2. 作品数量
共展示 3 幅经典艺术画作：
- 作品1：《灵魂》，作者：乔治·鲁，1885 年
- 作品2：《绽放》，作者：阿博特·富勒·格拉夫，1877 年
- 作品3：《撑阳伞的女人》，作者：克劳德·莫奈，1875 年

3. 功能介绍
- 显示带画框、阴影、边框的艺术作品图片
- 显示作品名称、作者、创作年份
- 支持 Previous / Next 按钮循环切换作品

---

## 二、界面结构说明
1. 界面区块划分
整个界面分为 3 个区块：
- 艺术作品展示区（图片画框）
- 作品信息描述区（文字简介）
- 按钮控制区（切换功能）

2. 使用的可组合项
- Column：垂直布局，整体页面结构
- Surface：实现画框、阴影、背景效果
- Image：显示艺术作品图片
- Text：显示作品名称、作者、年份
- Row：水平排列按钮
- Button：点击切换作品
- Spacer：设置组件间距

3. 组件嵌套结构
Column(主界面)
├─ ArtworkWall(Surface + Image)
├─ ArtworkDescriptor(Column + Text)
└─ DisplayController(Row + Button)

---

## 三、Compose 状态管理当前作品索引
使用 remember + mutableStateOf 实现状态管理：

var currentArtwork by remember { mutableStateOf(1) }

1. currentArtwork 存储当前显示的作品编号（1、2、3）
2. 状态变化时自动触发 UI 重组
3. 图片与文字信息根据状态同步更新
4. 遵循 Compose 状态驱动 UI 的设计模式

---

## 四、Next / Previous 按钮条件逻辑
1. Next 按钮逻辑
- 当前为第 3 幅 → 回到第 1 幅
- 其他情况 → 序号 +1

2. Previous 按钮逻辑
- 当前为第 1 幅 → 跳转到第 3 幅
- 其他情况 → 序号 -1

3. 实现方式
使用 when 表达式判断，实现循环切换，无越界错误。

---

## 五、遇到的问题与解决过程
1. 图片与画框上下间距不一致
- 原因：内边距设置不统一
- 解决：给 Image 统一添加 padding(32.dp)

2. 作品简介没有背景与边框
- 原因：未设置背景样式
- 解决：使用 background + 圆角 + 内边距美化

3. 按钮点击后界面不更新
- 原因：未使用 Compose 状态变量
- 解决：使用 remember + mutableStateOf 管理索引

4. 布局拥挤、间距不美观
- 原因：缺少 Spacer 和合理边距
- 解决：添加 Spacer 与 padding 优化布局

5. 图片显示比例异常
- 原因：contentScale 设置不当
- 解决：使用 ContentScale.Crop 正常展示

---

## 六、实验总结
通过本次实验，我掌握了：
1. Jetpack Compose 基础 UI 组件的使用
2. Modifier 修饰符实现布局、样式、边距、阴影、边框
3. Compose 状态管理：remember + mutableStateOf
4. 条件分支实现循环切换逻辑
5. 完整 Android 应用的界面与交互开发
6. 常见界面问题排查与解决
