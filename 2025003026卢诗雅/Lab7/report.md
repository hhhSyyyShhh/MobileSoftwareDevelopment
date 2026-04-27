# Lab7：构建可滚动课程网格应用

## 一、应用整体结构说明
本项目采用标准的 Android 分层结构，将数据、界面、资源分离：
1. 模型层：Topic.kt 数据类，封装界面所需数据。
2. 数据源层：DataSource.kt 单例对象，提供全部24个课程数据。
3. UI层：MainActivity 中使用 LazyVerticalGrid 实现网格，TopicCard 实现单个卡片。
4. 资源层：drawable 存放图片与图标，strings.xml 存放课程名称。

整体结构清晰、解耦充分，符合 Compose 开发规范。

## 二、Topic 数据类设计
### 字段设计
- topicNameResId：@StringRes Int，存储课程名字符串ID
- courseCount：Int，存储课程数量
- imageResId：@DrawableRes Int，存储课程图片ID

### 设计理由
- 使用资源ID而非硬编码，支持多语言与多分辨率。
- 类型安全，避免资源引用错误。
- 字段精简，完全匹配卡片展示需求。

## 三、卡片布局实现思路
1. 外层使用 Card 提供圆角与阴影。
2. Row 横向排列图片与文字区域，垂直居中。
3. 图片固定 68dp 正方形，1:1比例，居中裁剪。
4. 文字区域使用 Column，内边距16dp。
5. 课程名：bodyMedium，下方间距8dp。
6. 图标+数量：Row 水平排列，间距8dp，字体 labelMedium。
所有尺寸、间距、样式严格遵循实验要求。

## 四、网格布局实现思路
使用 LazyVerticalGrid 实现惰性加载网格：
- columns = GridCells.Fixed(2)：固定两列
- contentPadding = 8.dp：网格整体边距
- vertical/horizontal Arrangement.spacedBy(8.dp)：卡片间距
- items 遍历数据源，渲染 TopicCard

优点：只加载可见项，滚动流畅，性能优秀。

## 五、遇到的问题与解决方法
1. LazyVerticalGrid 导包失败
   解决：导入 androidx.compose.foundation.lazy.grid 包。

2. 图片拉伸变形
   解决：设置固定尺寸 + ContentScale.Crop。

3. 间距混乱
   解决：区分 contentPadding 与 spacedBy，统一设为8dp。

4. 资源加载失败
   解决：确保图片名小写、与字符串名一致。

5. 图标与数字不对齐
   解决：给 Row 设置 verticalAlignment.CenterVertically。

## 六、实验总结
本次实验完整实现了课程网格应用，是对 Jetpack Compose 的综合实战。我掌握了数据类、单例数据源、LazyVerticalGrid 网格、Card/Row/Column 嵌套布局、图片与文字样式等核心知识点。

通过严格按照设计规范实现界面，我理解了 Compose 中修饰符、间距、比例、对齐方式的重要性。同时，在排查导包、资源、布局问题的过程中，提升了独立解决问题的能力。

实验让我体会到声明式 UI 简洁高效的优势，为后续复杂应用开发打下扎实基础。