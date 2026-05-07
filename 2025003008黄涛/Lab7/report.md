# **Lab7 构建可滚动课程网格应用 实验报告**

### 1. 应用整体结构说明

本应用采用 **Compose UI + 数据模型 + 静态数据源** 的分层架构，代码结构清晰、职责明确，完全符合 Android 官方推荐的现代开发规范。

整体分为以下三层：

- **数据模型层**：Topic.kt 数据类，封装单个课程主题的核心数据。
- **数据源层**：DataSource.kt 单例对象，统一管理所有课程主题的静态数据。
- **UI 展示层**：MainActivity.kt 作为入口，包含网格布局 CoursesApp() 和卡片组件 TopicCard()，负责界面渲染与用户交互。

应用使用 LazyVerticalGrid 实现两列可滚动网格，结合 Card、Row、Column 等基础组件构建卡片样式，界面美观且适配性良好。

### 2. Topic 数据类的字段设计与选择理由

Topic 是用于描述单个课程主题的数据类，设计如下：

Kotlin

```
data class Topic(
    @StringRes val nameRes: Int,
    val courseCount: Int,
    @DrawableRes val imageRes: Int
)
```

**字段说明与设计理由**：

1. **nameRes: Int（@StringRes）** 存储课程名称的字符串资源 ID。 **优点**：支持多语言、符合 Android 资源管理规范，避免硬编码字符串。
2. **courseCount: Int** 存储该主题下的课程数量。 **优点**：直接使用基本类型，简单高效，无需资源引用。
3. **imageRes: Int（@DrawableRes）** 存储课程图片的资源 ID。 **优点**：便于 Compose 使用 painterResource() 加载图片，支持不同分辨率设备适配。

**选择数据类的原因**：数据类专为存储不可变数据设计，自动生成 equals()、hashCode()、toString() 和 copy() 方法，非常适合列表项模型。

### 3. 卡片布局实现思路

卡片是应用的核心 UI 单元，通过多层 Compose 组件嵌套实现所需样式。

**主要使用的组件**：

- Card：提供圆角和阴影的卡片容器
- Row：水平布局（图片 + 文字区域）
- Box：包裹图片区域
- Column：垂直布局（名称 + 课程数量）
- Image、Icon、Text：具体内容展示

**布局实现步骤**：

1. 最外层使用 Card 包裹整体内容。

2. 内部通过 

   Row

    分左右两部分：

   - **左侧**：Box + Image，固定 68dp 正方形区域，使用 aspectRatio(1f) 保持比例。

   - 右侧

     ：

     Column

      垂直排列课程名称和数量信息。

     - 上方：课程名称（bodyMedium 样式，16dp 内边距）
     - 下方：Row 水平排列图标与课程数量（labelMedium 样式，间距 8dp）

所有间距、尺寸和字体样式均严格按照实验设计要求实现。

### 4. 网格布局实现思路

使用 LazyVerticalGrid 实现高性能的两列可滚动网格布局。

**核心代码配置**：

Kotlin

```
LazyVerticalGrid(
    columns = GridCells.Fixed(2),                    // 固定两列
    verticalArrangement = Arrangement.spacedBy(8.dp),   // 垂直间距
    horizontalArrangement = Arrangement.spacedBy(8.dp), // 水平间距
    modifier = Modifier.padding(8.dp)                   // 整体边距
) {
    items(DataSource.topics) { topic ->
        TopicCard(topic)
    }
}
```

**实现说明**：

- GridCells.Fixed(2)：固定两列，适合手机竖屏显示。
- spacedBy(8.dp)：精确控制卡片间距。
- items(DataSource.topics)：遍历数据源，自动生成卡片。
- 懒加载机制，仅加载屏幕可见项，滚动性能优秀。

### 5. 遇到的问题与解决过程

**问题 1：图片拉伸变形** 原因：未设置宽高比。 解决：在 Image 外层 Box 添加 aspectRatio(1f) 并使用 ContentScale.Crop。

**问题 2：网格间距与设计稿不符** 原因：仅设置了整体 padding，未处理卡片间距。 解决：同时使用 Arrangement.spacedBy()（卡片间距）和 Modifier.padding()（屏幕边距）。

**问题 3：包名修改后引用失败** 原因：包名从 com.example.courses 改为 com.example.affirmations 后未同步修改。 解决：统一更新所有文件的 package 声明和导入路径。

**问题 4：图标与文字未垂直居中** 原因：Row 默认对齐方式为 Top。 解决：添加 verticalAlignment = Alignment.CenterVertically。

### 6. 实验总结

通过本次实验，我成功掌握了以下知识与技能：

1. 使用 Kotlin 数据类规范封装列表项数据模型。
2. 利用 object 单例对象管理静态数据源。
3. 熟练运用 LazyVerticalGrid 实现高性能两列可滚动网格。
4. 灵活组合 Row、Column、Card、Box 等 Compose 基础组件。
5. 严格按照设计规范控制间距、字体样式、图片比例等 UI 细节。
6. 应用运行稳定，界面效果与实验要求完全一致，支持流畅滚动并正确显示 24 个课程主题。