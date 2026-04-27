# Lab7 实验报告

## 1. 应用整体结构
本项目采用标准的 Android 分层结构：
- model/Topic.kt：数据模型类，存储课程信息
- data/DataSource.kt：单例对象，提供所有课程数据
- MainActivity.kt：主界面，使用 Compose 实现 UI 布局
- drawable：存放图片与图标资源
- values/strings.xml：字符串资源

## 2. Topic 数据类设计理由
Topic 包含三个字段：
- nameRes：字符串资源 ID，便于国际化
- courseCount：课程数量
- imageRes：图片资源 ID
使用资源 ID 而非硬编码，符合 Android 最佳实践。

## 3. 卡片布局实现思路
- 使用 Card 作为容器
- 使用 Row 实现“图片左、文字右”结构
- 图片使用 size(68.dp) + aspectRatio(1f) 保证正方形
- 文字区域使用 padding(16.dp)
- 标题与数量间距 8dp
- 图标与数字间距 8dp
- 字体使用题目指定的 bodyMedium 和 labelMedium

## 4. 网格布局实现思路
- 使用 LazyVerticalGrid 实现网格列表
- columns = GridCells.Fixed(2) 固定两列
- verticalArrangement / horizontalArrangement.spacedBy(8.dp) 设置卡片间距
- Modifier.padding(8.dp) 设置整体边距

## 5. 遇到的问题与解决过程
- 报错：Unresolved reference Topic
  解决：确保包路径导入正确，结构为 model/Topic.kt
- 图标缺失报错
  解决：添加 ic_grain.xml 到 drawable
- 布局不符合要求
  解决：严格按照题目尺寸、间距、字体实现