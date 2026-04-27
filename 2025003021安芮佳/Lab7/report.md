# Lab7 实验报告
## 一、应用整体结构说明
本次实现的 Courses App 采用分层设计思想，整体结构清晰，主要分为三个核心部分：

### 1. 数据层
- **Topic.kt**：定义数据类 `Topic`，封装单个课程主题的核心数据（主题名称字符串资源ID、课程数量、主题图片资源ID），是整个应用的数据模型基础。
- **DataSource.kt**：通过 `object` 单例创建静态数据源，集中管理24个课程主题的完整数据列表，避免数据分散管理，提升可维护性。

### 2. UI层
- **TopicCard 组合项**：负责单个课程卡片的UI渲染，封装卡片内图片、文字、图标等元素的布局与样式，实现UI组件的复用。
- **CoursesGridApp 组合项**：基于 `LazyVerticalGrid` 构建两列可滚动网格，遍历 `DataSource` 中的主题数据，逐个渲染 `TopicCard`。
- **MainActivity.kt**：应用入口，配置主题、Scaffold 布局，并加载核心的 `CoursesGridApp` 组合项，完成界面初始化。

### 3. 资源层
- `strings.xml`：集中管理所有课程主题的名称字符串资源，符合Android资源管理规范。
- `drawable` 目录：存放24张主题图片和装饰图标 `ic_grain.xml`（注：代码中临时使用系统图标替代，实际需替换为指定资源）。

## 二、Topic 数据类的字段设计与选择理由
### 字段设计
```kotlin
data class Topic(
    @StringRes val nameResId: Int,
    val courseCount: Int,
    @DrawableRes val imageResId: Int
)
```

### 选择理由
1. **nameResId（@StringRes Int）**：
   - 选择字符串资源ID而非直接存储字符串，符合Android国际化（i18n）规范，便于后续多语言适配；
   - 使用 `@StringRes` 注解，可让Android Studio进行资源合法性检查，避免硬编码字符串导致的维护问题。

2. **courseCount（Int）**：
   - 课程数量为纯数字，无需资源化，直接使用基本数据类型即可满足需求，兼顾性能与简洁性。

3. **imageResId（@DrawableRes Int）**：
   - 选择图片资源ID而非Bitmap或文件路径，利用Android资源系统的缓存和适配机制，支持不同分辨率屏幕的自动适配；
   - `@DrawableRes` 注解可校验资源有效性，减少运行时异常。

## 三、卡片布局实现思路
### 核心组合项选择与嵌套逻辑
单个课程卡片（`TopicCard`）采用**多层嵌套布局**实现，核心组合项及嵌套关系如下：

1. **外层容器：Card**
   - 作为卡片的基础容器，提供阴影（elevation）、圆角等Material Design样式，通过 `fillMaxWidth()` 让卡片占满网格列宽。

2. **一级布局：Row**
   - 卡片内部整体为水平布局，分为“图片区域”和“文字区域”两部分，通过 `padding(16.dp)` 控制卡片内边距，匹配UI规格要求。

3. **图片区域：Box + Image**
   - `Box` 作为图片的容器，通过 `size(68.dp)` + `aspectRatio(1f)` 确保图片为68dp×68dp的正方形，适配不同屏幕；
   - `Image` 组件加载主题图片，`fillMaxSize()` 让图片填满Box容器，`contentDescription` 关联主题名称资源，提升无障碍性。

4. **文字区域：Column + Row**
   - 外层 `Column` 实现主题名称和“图标+课程数”的垂直排列，通过 `padding(start = 16.dp)` 控制与图片的间距，`align(Alignment.CenterVertically)` 让文字区域与图片垂直居中对齐；
   - 主题名称：`Text` 组件使用 `MaterialTheme.typography.bodyMedium` 样式，匹配文字规格；
   - 课程数区域：内层 `Row` 实现“装饰图标+数字”的水平排列，`Spacer(height = 8.dp)` 控制与主题名称的间距，`Spacer(width = 8.dp)` 控制图标与数字的间距；
   - 图标：`Image` 加载装饰图标（代码中临时使用系统图标，需替换为 `ic_grain.xml`），`size(18.dp)` 控制图标大小；
   - 课程数：`Text` 组件使用 `MaterialTheme.typography.labelMedium` 样式，将数字转为字符串展示。

### 关键细节处理
- 使用 `Spacer` 替代 `padding` 实现精准间距控制，避免嵌套层级过多导致的间距叠加问题；
- 图片区域通过 `Box` 包裹，确保图片居中显示且尺寸严格符合68dp×68dp的规格；
- 文字区域垂直居中对齐，提升视觉美观度。

## 四、网格布局实现思路
### LazyVerticalGrid 核心参数配置
`CoursesGridApp` 中使用 `LazyVerticalGrid` 构建两列可滚动网格，关键参数配置及作用如下：

1. **columns = GridCells.Fixed(2)**
   - 指定网格为固定2列布局，直接满足“两列网格”的核心需求。

2. **contentPadding = PaddingValues(8.dp)**
   - 控制网格整体的内边距（上下左右各8dp），实现网格与屏幕边缘的留白，匹配UI规格中“网格整体内边距8dp”的要求。

3. **horizontalArrangement = Arrangement.spacedBy(8.dp)**
   - 控制列之间的水平间距，实现卡片左右间距8dp的规格。

4. **verticalArrangement = Arrangement.spacedBy(8.dp)**
   - 控制行之间的垂直间距，实现卡片上下间距8dp的规格。

5. **items(DataSource.topics) { topic -> TopicCard(topic = topic) }**
   - 遍历 `DataSource` 中的主题列表，为每个主题生成一个 `TopicCard`，实现数据与UI的绑定；
   - `LazyVerticalGrid` 采用懒加载机制，仅渲染屏幕可见区域的卡片，提升滚动性能。

### 布局逻辑补充
- 网格整体通过 `fillMaxSize()` 占满屏幕，确保布局自适应屏幕尺寸；
- 结合 `Scaffold` 的 `innerPadding` 处理系统状态栏/导航栏的边距，避免内容被遮挡。

## 五、遇到的问题与解决过程
### 问题1：图片比例不一致，部分卡片图片变形
- **现象**：直接使用 `size(68.dp)` 设定图片尺寸时，部分非正方形图片出现拉伸变形；
- **解决**：添加 `aspectRatio(1f)` 修饰符，强制图片宽高比为1:1，结合 `fillMaxSize()` 让图片在68dp的Box容器内保持正方形，同时居中显示，解决变形问题。

### 问题2：网格间距不符合规格，卡片与屏幕边缘无留白
- **现象**：仅使用 `Arrangement.spacedBy(8.dp)` 时，卡片之间间距正确，但网格边缘与屏幕无留白；
- **解决**：新增 `contentPadding = PaddingValues(8.dp)`，同时控制网格内边距和卡片间距，满足“网格整体内边距8dp + 卡片间距8dp”的双重要求。

### 问题3：文字区域与图片未垂直居中对齐
- **现象**：文字区域默认居顶显示，与图片视觉上不对齐；
- **解决**：为文字区域的 `Column` 添加 `align(Alignment.CenterVertically)` 修饰符，让文字区域与图片在Row中垂直居中，提升视觉一致性。

### 问题4：装饰图标资源未正确引用
- **现象**：代码中临时使用系统图标 `android.R.drawable.ic_menu_gallery`，未使用指定的 `ic_grain.xml`；
- **解决**：将图标资源替换为 `painterResource(id = R.drawable.ic_grain)`，并确保 `ic_grain.xml` 已放入 `drawable` 目录，匹配实验要求。

### 问题5：预览界面无法正常显示数据
- **现象**：Preview中网格为空，无卡片渲染；
- **解决**：确保 `DataSource.topics` 是公开可访问的列表，且Preview函数中正确加载 `CoursesGridTheme` 和 `CoursesGridApp`，同时检查资源ID是否正确（如图片、字符串资源未缺失）。