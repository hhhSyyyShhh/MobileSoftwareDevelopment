# Lab7 实验报告：课程主题网格应用

## 一、实验目的
实现一个两列可滚动的课程主题展示应用，掌握 Jetpack Compose 网格布局、卡片布局、资源引用、数据模型与数据源的使用。

## 二、应用整体结构说明
本项目采用标准的 MVI 结构，分为三层：
1. **模型层（model）**：Topic 数据类，存储课程主题信息
2. **数据源层（data）**：DataSource 单例，提供所有课程数据
3. **UI 层**：MainActivity 入口 + TopicGridItem 卡片 + CoursesGrid 网格布局

## 三、Topic 数据类设计
数据类包含三个字段：
- nameRes：字符串资源 ID，用于显示主题名称
- courseCount：课程数量
- imageRes：图片资源 ID，用于显示主题图片

使用 @StringRes 和 @DrawableRes 保证资源类型安全。

## 四、卡片布局实现（TopicGridItem）
使用 Card + Row 实现横向布局：
- 左侧固定 68.dp 正方形图片
- 右侧使用 Column 显示标题与课程数
- 图标使用 ic_grain.xml，与课程数量水平排列
- 所有间距、大小严格按照老师要求实现

## 五、网格布局实现（CoursesGrid）
使用 LazyVerticalGrid 实现高性能网格列表：
- 固定两列：GridCells.Fixed(2)
- 卡片间距 8.dp
- 整体边距 8.dp
- 支持滚动，性能优化

## 六、实验遇到的问题与解决
1. LazyVerticalGrid 找不到
   → 解决：添加 compose.foundation 依赖
2. paddingFromBaseline 报错
   → 解决：替换为标准 padding 兼容版本
3. 图片资源加载失败
   → 解决：确保文件名小写、无空格、放入 drawable 文件夹

## 七、实验总结
成功完成课程网格应用，掌握 Compose 网格、卡片、资源、数据模型的基本使用，符合老师所有 UI 与功能要求。