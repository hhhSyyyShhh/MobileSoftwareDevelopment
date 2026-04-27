# Lab7 实验报告

## 1. 应用整体结构说明

本应用是一个基于 Jetpack Compose 开发的课程主题列表应用，整体结构分为以下三层：

1.  **数据层**：
    - `Topic.kt`：数据模型类，定义了课程主题的基本字段，包含课程名称ID、课程数量和图片ID。
    - `DataSource.kt`：数据源单例，通过 `listOf()` 静态创建了包含全部24个课程主题的数据集合，为UI层提供数据支持。
2.  **UI组件层**：
    - `TopicGridItem`：可复用的卡片组件，负责单个课程主题的布局和显示，是整个应用的核心UI单元。
    - `CoursesGrid`：网格布局组件，使用 `LazyVerticalGrid` 实现了两列可滚动的列表，负责承载所有 `TopicGridItem`。
3.  **入口层**：
    - `MainActivity.kt`：应用的主入口，在 `onCreate` 方法中设置内容视图，加载 `CoursesGrid` 组件作为根布局。

---

## 2. `Topic` 数据类的字段设计与选择理由

`Topic` 数据类定义如下：
```kotlin
data class Topic(
    @StringRes val nameId: Int,
    val courseCount: Int,
    @DrawableRes val imageId: Int
)
```

- `@StringRes val nameId: Int`：存储课程名称的字符串资源ID。使用资源ID而非硬编码字符串，便于多语言适配和资源管理。
- `val courseCount: Int`：存储课程的数量，为纯数字类型，直接表示数量值。
- `@DrawableRes val imageId: Int`：存储课程图片的drawable资源ID，通过资源ID引用图片，保证资源的统一管理。

---

## 3. 卡片布局实现思路

卡片布局使用了 Compose 的组合项嵌套实现，具体结构如下：

1.  **根布局**：使用 `Card` 作为根容器，提供了圆角、阴影和点击效果，符合Material Design规范。
2.  **水平布局 `Row`**：
    - 左侧：使用 `Image` 组件展示课程图片，通过 `Modifier.size(68.dp).aspectRatio(1f)` 控制图片大小和宽高比，保证图片自适应。
    - 右侧：使用 `Column` 作为垂直容器，包含课程名称和课程数量两部分内容。
3.  **垂直布局 `Column`**：
    - 上方：使用 `Text` 组件显示课程名称，通过 `stringResource(id = topic.nameId)` 读取字符串资源。
    - 下方：使用 `Row` 实现图标+文本的水平布局，包含一个 `Icon`（课程数量图标）和一个 `Text`（课程数量），并通过 `verticalAlignment = Alignment.CenterVertically` 实现垂直居中。

---

## 4. 网格布局实现思路（`LazyVerticalGrid` 参数配置说明）

网格布局通过 `LazyVerticalGrid` 实现，核心参数配置如下：

```kotlin
LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    contentPadding = PaddingValues(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = modifier
) {
    items(DataSource.topics) { topic ->
        TopicGridItem(topic = topic)
    }
}
```

- `columns = GridCells.Fixed(2)`：设置网格为两列布局，固定列数为2。
- `contentPadding = PaddingValues(8.dp)`：为整个网格设置四周的内边距，避免卡片内容紧贴屏幕边缘。
- `horizontalArrangement = Arrangement.spacedBy(8.dp)`：设置相邻卡片之间的水平间距为8dp。
- `verticalArrangement = Arrangement.spacedBy(8.dp)`：设置相邻卡片之间的垂直间距为8dp。
- `items(DataSource.topics)`：通过 `items` 方法遍历 `DataSource.topics` 集合，为每个数据项创建一个 `TopicGridItem` 组件。

---

## 5. 遇到的问题与解决过程

1.  **问题1：`ic_grain` 图标找不到报错**
    - 原因：`drawable` 文件夹中缺少 `ic_grain.xml` 文件，导致 `painterResource(id = R.drawable.ic_grain)` 无法找到资源。
    - 解决：通过 `New -> Vector Asset` 创建 `ic_grain.xml` 文件，并将官方提供的图标代码替换进去，问题解决。
2.  **问题2：`Arrangement` 无法识别报错**
    - 原因：缺少 `LazyVerticalGrid` 中 `Arrangement` 的导入包。
    - 解决：在 `MainActivity.kt` 顶部添加 `import androidx.compose.foundation.lazy.grid.arrangement.Arrangement`，报错消失。
3.  **问题3：图片自适应效果差**
    - 原因：图片使用固定宽高比，在不同屏幕尺寸下会变形。
    - 解决：为 `Image` 组件添加 `.aspectRatio(1f)` 修饰符，让图片以1:1的宽高比自适应显示，同时保持不变形。
4.  **问题4：卡片间距不统一**
    - 原因：未设置 `contentPadding` 和 `Arrangement.spacedBy`，导致卡片紧贴屏幕边缘或间距不一致。
    - 解决：同时配置 `contentPadding` 和 `horizontal/verticalArrangement`，实现了和效果图一致的网格间距效果。

---

## 实验总结

本次实验通过实现课程主题列表应用，深入理解了 Jetpack Compose 中数据类、数据源、组合项和网格布局的使用方法。通过本次实验，掌握了以下核心技能：
- 如何使用数据类和数据源组织静态数据。
- 如何通过嵌套组合项实现复杂的卡片布局。
- 如何使用 `LazyVerticalGrid` 实现可滚动的网格列表，并配置间距和内边距。
- 如何处理资源文件和解决编译报错。

