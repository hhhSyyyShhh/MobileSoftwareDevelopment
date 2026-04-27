# Lab7 实验报告

## 1. 应用整体结构说明

本次课程网格应用分为三层组织：`Topic` 负责描述单个课程主题的数据模型，`DataSource` 负责集中保存 24 个主题的静态数据，`MainActivity` 中的可组合函数负责把数据渲染成两列网格。界面层再拆分为 `CoursesGrid` 和 `TopicCard` 两个可组合项，前者控制整体网格布局，后者负责单个卡片的展示。

## 2. Topic 数据类的字段设计与选择理由

`Topic` 包含三个字段：主题名称资源 `nameRes`、课程数量 `courseCount`、主题图片资源 `imageRes`。主题名称使用字符串资源而不是直接写死文本，可以方便后续本地化；图片使用资源 ID 便于直接引用 `drawable` 中的图片；课程数量保留为整数，便于在界面上格式化显示。

## 3. 卡片布局实现思路

单个卡片使用 `Card` 作为外层容器，内部用 `Row` 将图片和文字区域并排放置。图片固定为 68 dp 正方形，右侧文字区域用 `Column` 垂直排列主题名称和课程数量。主题名称使用 `MaterialTheme.typography.bodyMedium`，数量使用 `MaterialTheme.typography.labelMedium`，中间通过 8 dp 间距区分层级。

## 4. 网格布局实现思路

课程主题网格使用 `LazyVerticalGrid` 实现，并通过 `GridCells.Fixed(2)` 固定为两列。`contentPadding = PaddingValues(8.dp)` 控制整个网格四周留白，`horizontalArrangement = Arrangement.spacedBy(8.dp)` 与 `verticalArrangement = Arrangement.spacedBy(8.dp)` 控制卡片之间的水平和垂直间距，从而满足实验中的间距要求。

## 5. 遇到的问题与解决过程

实现时最需要确认的是资源文件名与代码中的 `drawable` 引用是否一致。由于题目中的图片文件已经按主题名命名，我直接按小写资源名建立数据源映射，避免了图片加载错误。另一个重点是网格间距，需要同时设置内容内边距与行列间距，单独设置其中一个都无法得到题目要求的视觉效果。