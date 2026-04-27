# Lab7 构建可滚动课程网格应用 实验报告

---

## 1. 应用整体结构说明
本应用采用 **Compose UI + 数据模型 + 静态数据源** 的分层结构，代码清晰、职责明确，完全符合 Android 官方推荐的开发规范。

整体结构分为三部分：
1. **数据模型层**：`Topic.kt` 数据类，用于封装课程主题的核心数据。
2. **数据源层**：`DataSource.kt` 单例对象，统一管理所有课程主题静态数据，提供给 UI 层调用。
3. **UI 展示层**：`MainActivity.kt` 作为入口，包含网格布局 `CoursesApp()` 和卡片组件 `TopicCard()`，负责界面渲染与交互。

应用通过 `LazyVerticalGrid` 实现两列可滚动网格，使用 `Card`、`Row`、`Column` 等 Compose 基础组件完成卡片样式设计，整体界面美观、适配性强。

---

## 2. Topic 数据类的字段设计与选择理由
`Topic` 是一个**数据类**，专门用于存储单个课程主题的所有信息，设计如下：
```kotlin
data class Topic(
    @StringRes val nameRes: Int,
    val courseCount: Int,
    @DrawableRes val imageRes: Int
)
```

### 字段说明与设计理由
1. **nameRes: Int（@StringRes）**
   - 存储课程名称对应的字符串资源 ID
   - 优点：支持多语言切换、符合 Android 资源管理规范、避免硬编码字符串。

2. **courseCount: Int**
   - 存储该主题下的课程数量
   - 直接使用整型，简单高效，无需资源引用。

3. **imageRes: Int（@DrawableRes）**
   - 存储课程图片对应的图片资源 ID
   - 优点：方便 Compose 通过 `painterResource` 加载图片，适配不同分辨率设备。

**选择数据类的原因**：数据类专门用于存储数据，自动提供 `equals()`、`toString()` 等方法，非常适合存储列表项数据模型。

---

## 3. 卡片布局实现思路
卡片是应用的核心 UI 单元，通过多层布局嵌套实现设计图要求的样式。

### 使用的组合项
- `Card`：卡片容器，提供圆角和阴影效果。
- `Row`：水平布局，将**图片**和**文字区域**左右排列。
- `Box`：包裹图片，保证图片区域稳定。
- `Column`：垂直布局，将**课程名称**和**课程数量**上下排列。
- `Image`：显示课程图片。
- `Icon`：显示课程数量旁的装饰图标。
- `Text`：显示文字内容。

### 布局实现步骤
1. 最外层使用 `Card` 包裹所有内容。
2. 内部使用 `Row` 分为左右两部分：
   - 左侧：`Box` + `Image`，设置 68dp 正方形，`aspectRatio(1f)` 保证比例不变形。
   - 右侧：`Column` 垂直排列：
     - 上方：课程名称文字，使用 `bodyMedium` 样式，设置 16dp 内边距。
     - 下方：`Row` 水平排列图标 + 课程数量，间距 8dp，使用 `labelMedium` 样式。

所有间距、尺寸、字体样式均严格按照实验要求实现。

---

## 4. 网格布局实现思路
使用 **`LazyVerticalGrid`** 实现可滚动两列网格，这是 Compose 提供的高性能懒加载网格布局。

### 关键参数配置
```kotlin
LazyVerticalGrid(
    columns = GridCells.Fixed(2),     // 固定 2 列
    verticalArrangement = Arrangement.spacedBy(8.dp),    // 卡片垂直间距
    horizontalArrangement = Arrangement.spacedBy(8.dp),  // 卡片水平间距
    modifier = Modifier.padding(8.dp)                    // 网格整体边距
)
```

### 实现说明
- `GridCells.Fixed(2)`：固定显示两列网格，适配手机竖屏。
- `spacedBy(8.dp)`：控制卡片之间的间距。
- `padding(8.dp)`：控制网格与屏幕边缘的距离。
- `items(DataSource.topics)`：遍历数据源，自动为每个课程生成一个卡片。
- 支持**垂直滚动**，只加载屏幕内可见卡片，性能优秀。

---

## 5. 遇到的问题与解决过程
### 问题 1：图片拉伸变形
- 原因：未设置宽高比，图片被强制拉伸。
- 解决：添加 `aspectRatio(1f)` 和 `ContentScale.Crop`，让图片保持正方形并居中裁剪。

### 问题 2：网格间距与设计不符
- 原因：只设置了内边距，没有设置卡片之间的间距。
- 解决：同时使用 `Arrangement.spacedBy`（卡片间距）和 `Modifier.padding`（整体边距）。

### 问题 3：包名错误导致引用失败
- 原因：最初包名为 `com.example.courses`，后改为 `com.example.affirmations`。
- 解决：统一修改所有文件的 package 声明和导入语句，确保资源引用正常。

### 问题 4：图标与文字没有居中对齐
- 原因：未设置垂直对齐方式。
- 解决：给 `Row` 添加 `verticalAlignment = Alignment.CenterVertically`。

---

## 6. 实验总结
1. 成功使用 **数据类** 封装课程数据。
2. 学会使用 **object 单例** 管理静态数据源。
3. 掌握 `LazyVerticalGrid` 实现**两列可滚动网格**。
4. 熟练使用 `Row`、`Column`、`Card`、`Image` 等 Compose 组件。
5. 能够按照设计规范实现间距、字体、比例等 UI 细节。
6. 应用运行稳定，界面与实验要求完全一致，可正常滚动并显示 24 个课程主题。

---