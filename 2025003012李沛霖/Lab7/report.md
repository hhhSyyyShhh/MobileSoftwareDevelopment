# Lab7 构建可滚动课程网格应用 实验报告
## 一、应用整体结构说明
本应用采用标准的 Android Jetpack Compose 架构，整体分为三层：
1. **数据模型层（model）**：使用 Topic 数据类封装课程信息。
2. **数据源层（data）**：使用 object 单例 DataSource 统一管理所有课程静态数据。
3. **UI 展示层**：通过 TopicGridItem 实现单个卡片，CoursesGrid 实现两列网格布局，MainActivity 作为入口调用。

## 二、Topic 数据类的字段设计与选择理由
- **nameRes：@StringRes Int**：存储课程名称字符串资源 ID，便于国际化与资源管理。
- **courses：Int**：存储该主题下的课程数量，为纯数字类型。
- **imageRes：@DrawableRes Int**：存储课程配图资源 ID，统一加载图片。
设计理由：使用资源 ID 而非硬编码，符合 Android 资源使用规范，提升应用可维护性与扩展性。

## 三、卡片布局实现思路
1. 外层使用 **Card** 实现卡片样式与阴影效果。
2. 内部使用 **Column** 垂直排列：上方图片 + 下方文字区域。
3. 图片使用 **fillMaxWidth + aspectRatio(1f)** 保证 1:1 正方形比例。
4. 文字区域使用 **padding(16.dp)** 实现内边距。
5. 底部使用 **Row** 水平摆放图标与课程数量，间距 8.dp。
6. 文字分别使用 **bodyMedium** 和 **labelMedium** 样式，符合设计规范。

## 四、网格布局实现思路
使用 **LazyVerticalGrid** 实现可滚动两列网格：
- **columns = GridCells.Fixed(2)**：固定显示 2 列。
- **contentPadding = PaddingValues(8.dp)**：网格整体外边距 8dp。
- **verticalArrangement / horizontalArrangement** 均设置 spacedBy(8.dp)：卡片之间间距 8dp。
- 通过 items 遍历数据源，渲染所有课程卡片。

## 五、遇到的问题与解决过程
1. **问题**：LazyVerticalGrid 报错找不到。
   **解决**：确认导入 androidx.compose.foundation.lazy.grid 包。
2. **问题**：图片无法显示/资源找不到。
   **解决**：确保图片名称全小写，与代码中保持一致，放入 drawable 目录。
3. **问题**：布局间距不对齐。
   **解决**：严格按照要求设置 padding 与 spacedBy，使用 aspectRatio 固定图片比例。
4. **问题**：文字样式不正确。
   **解决**：使用 MaterialTheme.typography 中对应的样式。

## 六、实验总结
本次实验成功完成了可滚动课程网格应用的开发，掌握了 Compose 网格布局、数据类、单例数据源、卡片布局等核心技能，能够独立使用 LazyVerticalGrid 实现多列列表，符合实验所有 UI 与功能要求。