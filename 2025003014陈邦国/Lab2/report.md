# Jetpack Compose 电子名片实验报告
姓名：陈邦国
学号：2025003014
开发环境：Android Studio + Kotlin + Jetpack Compose

## 一、名片展示的个人信息
本次电子名片实现展示的个人信息如下：
- 头像 / Logo：自定义 Android 图标（anzhuo.png）
- 姓名：陈邦国
- 职位：Android 开发工程师
- 联系方式：电话 1234567890
- 社交账号：@chenbangguo
- 邮箱：2116321787@qq.com

## 二、布局结构简要说明
整体采用 Jetpack Compose 声明式 UI 开发，通过可组合函数（Composable）构建界面，布局层次清晰、可复用性强。

### 1. 核心 Composable 组件
- Column：垂直布局容器，用于整体页面、上半部分信息、下半部分联系方式的纵向排列
- Row：横向布局容器，用于实现「图标 + 文字」的联系方式行
- Image：加载并展示头像 / Logo 图片资源
- Text：展示姓名、职位、联系方式等文本信息
- Icon：渲染 Material Design 内置矢量图标（电话、分享、邮件）
- Spacer：用于精确控制组件之间的间距
- Modifier：修饰符，用于设置组件大小、内边距、背景色、填充方式、对齐方式等

### 2. 布局嵌套结构
- 最外层容器：Column 铺满全屏，设置深蓝色背景，整体内容垂直居中、水平居中
- 上半部分（CardTop）：Column 嵌套 Image + 两个 Text，依次展示头像、姓名、职位
- 下半部分（CardBottom）：Column 嵌套三个 ContactRow 组件
- 联系方式行（ContactRow）：Row 横向排列 Icon + Text，实现图标与对应文字的组合样式

## 三、遇到的问题与解决过程
### 问题 1：Unresolved reference: drawable 资源找不到
问题描述：代码中使用 R.drawable.anzhuo 时，编译器提示无法找到 drawable 资源
问题原因：未正确导入当前项目的 R 类，包名不匹配导致资源引用失败
解决方法：
- 确认 anzhuo.png 已放置在 app/src/main/res/drawable 目录下，文件名大小写一致
- 在 MainActivity.kt 顶部导入对应包名的 R 类
- 执行 File → Sync Project with Gradle Files 同步项目，使资源被正常识别

### 问题 2：Material Icons 图标无法使用
问题描述：Icons.Default.Phone、Icons.Default.Share 等图标代码报错，提示未定义
问题原因：未导入 Material Icons 相关依赖包
解决方法：
- 在 MainActivity.kt 顶部添加图标导入语句
- 确认模块级 build.gradle 已包含 Compose Material 依赖

### 问题 3：界面排版错乱、组件位置偏移
问题描述：头像、文字未居中显示，组件间距不均匀
问题原因：未正确使用 Modifier 和对齐参数
解决方法：
- 给外层 Column 设置 horizontalAlignment = Alignment.CenterHorizontally 实现水平居中
- 使用 Spacer 统一控制组件间间距，避免硬编码边距
- 通过 Modifier.padding()、Modifier.size() 精确控制组件大小与内边距