## 1. 应用整体结构
model/Topic.kt：数据类，存储课程名称、数量、图片资源
data/DataSource.kt：单例对象，统一管理 24 条课程静态数据
MainActivity.kt：实现网格布局 + 卡片 UI，是应用入口

## 2. Topic 数据类设计
三个字段：nameRes(字符串资源)、courseCount(课程数)、imageRes(图片资源)
使用@StringRes/@DrawableRes注解，确保资源类型安全，符合 Android 最佳实践

## 3. 卡片布局实现
外层：Card 提供卡片样式
内部：Column 垂直排列 图片 和 文字区域
图片：使用aspectRatio(1f)实现正方形自适应
文字区域：16dp 内边距，包含课程名 + 图标 + 数量行
图标行：Row 水平排列图标与数字，间距 8dp

## 4. 网格布局实现
使用LazyVerticalGrid实现可滚动网格
GridCells.Fixed(2)：固定两列
spacedBy(8.dp)：卡片之间水平 / 垂直间距 8dp
contentPadding=8.dp：网格整体外边距 8dp
items()：遍历数据源渲染所有卡片

## 5. 常见问题与解决
找不到 LazyVerticalGrid：确保使用最新 Compose 版本，导入androidx.compose.foundation.lazy.grid包
图片变形：使用aspectRatio(1f)+ContentScale.Crop
间距不对：区分spacedBy（卡片间距）和contentPadding（网格边距）
运行效果
两列网格展示 24 个课程
卡片正方形图片、文字样式、间距完全符合实验规格
支持垂直滚动，流畅无卡顿