# Lab7：构建可滚动课程网格应用

## 一、应用整体结构说明
本项目采用**分层架构**设计，严格遵循实验要求的代码目录结构，整体分为3个模块：
1.  **数据层**：`Topic.kt` 数据类 + `DataSource.kt` 单例静态数据源，负责管理课程主题数据
2.  **UI组件层**：`TopicCard` 可组合项，实现单个课程卡片的UI布局与样式
3.  **页面入口层**：`MainActivity.kt`，通过`LazyVerticalGrid`构建两列可滚动网格，展示全部卡片

整体遵循Jetpack Compose声明式UI开发规范，数据与UI完全分离，结构清晰易维护。

## 二、Topic数据类字段设计与选择理由
本次设计的`Topic`数据类包含3个字段，完全匹配实验需求：
1.  `topicNameResId: @StringRes Int`：主题名称字符串资源ID
    理由：Android规范推荐使用字符串资源ID而非硬编码字符串，支持多语言国际化，符合官方最佳实践。
2.  `courseCount: Int`：主题对应课程数量
    理由：数字类型直接存储课程计数，无需格式转换，读写高效。
3.  `imageResId: @DrawableRes Int`：主题配图图片资源ID
    理由：使用Drawable资源ID统一管理图片资源，适配Android资源管理机制，便于图片替换与维护。

同时使用`data class`数据类，自动生成equals、hashCode、toString等方法，适合纯数据存储场景。

## 三、卡片布局实现思路
单个课程卡片采用**左右水平Row布局**嵌套内部垂直Column布局，完全匹配实验图纸标注的所有尺寸与间距：
1.  外层使用`Card`组件实现带圆角的卡片容器样式
2.  左侧：`Image`图片组件，固定`68dp*68dp`正方形尺寸，1:1宽高比，裁剪圆角适配卡片风格
3.  右侧：垂直`Column`文字区域，设置上下左右16dp内边距
4.  文字区域第一行：主题名称文本，使用`MaterialTheme.typography.bodyMedium`字体样式，底部距离下一行8dp间距
5.  文字区域第二行：水平`Row`布局，左侧点阵图标`ic_grain`、右侧课程数量数字，图标与数字间距8dp；数字使用`MaterialTheme.typography.labelMedium`字体样式

所有间距、尺寸、字体完全严格按照实验图纸标注实现，与设计稿完全一致。

## 四、网格布局实现思路
网格使用`LazyVerticalGrid`惰性垂直网格组件实现，核心参数配置说明：
1.  列数：`GridCells.Fixed(2)`，固定**2列**网格，完全符合实验要求
2.  网格整体内边距：`contentPadding = 8.dp`，控制网格四周与屏幕边缘的8dp留白
3.  卡片垂直间距：`verticalArrangement = Arrangement.spacedBy(8.dp)`，控制上下卡片间距8dp
4.  卡片水平间距：`horizontalArrangement = Arrangement.spacedBy(8.dp)`，控制左右卡片间距8dp
5.  数据加载：使用`items()`遍历数据源`DataSource.topics`，自动为每条数据生成对应卡片
6.  特性：`LazyVerticalGrid`为惰性加载，只渲染屏幕可见区域内容，支持流畅垂直滚动，性能优于普通Column嵌套布局。

## 五、遇到的问题与解决过程
1.  **图片比例变形，无法保持1:1正方形**
    问题：直接设置宽高dp后，不同屏幕下图片拉伸变形
    解决：使用固定68dp正方形尺寸+`clip`裁剪圆角，严格保持1:1宽高比，同时设置`ContentScale.Crop`保证图片完整显示不变形。

2.  **卡片间距不符合8dp规范，网格内外边距混淆**
    问题：分不清`contentPadding`和`spacedBy`区别，导致网格边缘和卡片之间间距错误
    解决：`contentPadding`控制网格整体和屏幕边缘的内边距，`spacedBy`控制卡片与卡片之间的间距，分别设置8dp，完全匹配实验规格。

3.  **字体样式不符合bodyMedium/labelMedium要求**
    问题：手动设置字号粗细，和设计字体规范不一致
    解决：直接使用MaterialTheme官方主题字体样式`bodyMedium`、`labelMedium`，无需手动定义字号，完全符合设计规范。

4.  **卡片内部间距和图纸16dp/8dp标注不一致**
    解决：严格按照图纸标注，逐行设置padding：文字区域上下左右16dp、标题和图标行8dp间距、图标和数字8dp间距，完全还原设计稿布局。

## 六、实验总结
通过本次Lab7实验，我熟练掌握了Kotlin数据类、单例数据源、Compose网格布局`LazyVerticalGrid`、卡片Card组件、布局嵌套、Material主题字体样式等核心知识点。
独立完成了从数据模型设计、静态数据源编写、单卡片UI还原、网格布局搭建的完整开发流程，理解了惰性网格布局的性能优势，同时学会了严格按照UI设计图纸的尺寸、间距、字体规范还原界面，提升了Android Compose移动端UI开发与文档阅读能力。
