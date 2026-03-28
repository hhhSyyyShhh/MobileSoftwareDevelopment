# Lab2 实验报告：Jetpack Compose 构建名片应用
## 一、名片展示的个人信息
- 个人头像/Logo：Android官方Logo，圆形展示
- 姓名：罗贵媛（主标题文字，大号加粗显示）
- 职位：Android Developer Extraordinaire
- 联系方式：
  - 手机号码：18388621668
  - 社交账号：@luoguiyuan
  - 邮箱地址：3568298347@qq.com

## 二、布局结构 & Composable 说明
### 整体布局
整体采用 `Column` 垂直布局，分为上下两个功能区域：
- 外层：`Column` 填充全屏，设置深青背景色`#073042`，水平居中对齐，垂直方向上下留白
- 上半部分（`CardTop`）：`Column` 嵌套，包含头像、姓名、职位，垂直居中
- 下半部分（`CardBottom`）：`Column` 嵌套，包含3行联系方式，每行用`ContactRow`实现

### 核心Composable函数
1.  **`BusinessCard`**：根布局，串联上下两部分，控制整体背景和对齐
2.  **`CardTop`**：上半部分组件，负责展示头像、姓名、职位
3.  **`CardBottom`**：下半部分组件，负责展示联系方式列表，用`HorizontalDivider`分隔行
4.  **`ContactRow`**：复用组件，封装「图标+文字」的单行联系方式，统一样式

## 三、遇到的问题与解决过程
### 问题1：Material Icons 图标无法使用
- 问题描述：`Icons.Default.Phone`等图标标红，无法识别
- 解决过程：
  1.  检查`build.gradle`，确认已添加Material Icons依赖：
      ```gradle
      implementation "androidx.compose.material:material-icons-core:1.4.3"
      implementation "androidx.compose.material:material-icons-filled:1.4.3"