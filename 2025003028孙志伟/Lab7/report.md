# Lab7 可滚动课程网格应用 实验报告

## 1. 应用整体结构
- 数据类：Topic，存储课程名称、数量、图片
- 数据源：DataSource 单例，提供全部 24 条课程数据
- UI 层：CourseCard 卡片组件 + CoursesGrid 网格布局
- 入口：MainActivity 调用网格显示界面

## 2. Topic 数据类设计
- 字段：nameRes(字符串资源)、courseCount(数字)、imageRes(图片资源)
- 理由：使用资源ID而非硬编码，适配多语言、便于资源管理，符合Android开发规范

## 3. 卡片布局实现思路
- 外层：Card 提供卡片样式
- 内部：Column 垂直排列 图片 + 文字区域
- 图片：使用 aspectRatio(1f) 实现正方形自适应
- 文字区：16dp内边距，Row 水平放置图标与课程数
- 样式：使用 MaterialTheme 规范字体

## 4. 网格布局实现思路
- 使用 LazyVerticalGrid 实现可滚动网格
- GridCells.Fixed(2) 固定两列
- Arrangement.spacedBy(8.dp) 设置卡片间距
- contentPadding = 8.dp 设置网格整体外边距
- items() 遍历数据源渲染所有卡片

## 5. 遇到的问题与解决
1. 网格无法导入：添加 androidx.compose.foundation.lazy.grid 依赖，同步项目后正常使用网格组件。
2. 图片变形：使用 ContentScale.Crop + aspectRatio(1f) 修复，保证图片1:1正方形显示且不变形。
3. 间距不对：区分 contentPadding（整体边距）和 spacedBy（卡片间距），分别设置合理间距，让布局更整齐。
4. 资源找不到：统一图片为 PNG 格式，文件名全部小写且与代码完全一致，替换损坏的图标与图片文件。
5. 应用闪退：清理项目缓存，重新构建，修复损坏资源，确保所有图片、矢量图可正常加载。
6. 卡片排版错乱：统一内边距与间距，规范图标与文字对齐方式，使整体界面美观、整齐、统一。

## 6. 实验总结
本次实验基于 Jetpack Compose 完成可滚动课程网格应用开发，完成了数据模型封装、数据源统一管理、卡片组件复用与双列网格布局搭建。通过本次实践，熟练掌握了 LazyVerticalGrid 懒加载网格、Card 卡片布局、图片自适应适配、资源引用管理等核心知识点。

实验过程中不断排查图片格式错误、资源缺失、布局错乱、程序崩溃等问题，提升了Android资源配置与UI布局调试能力。整体项目实现了界面整洁美观、数据正常渲染、页面流畅滚动的效果，理解了Compose声明式UI开发思想与组件化设计优势，为后续复杂移动端界面开发积累了实操经验。