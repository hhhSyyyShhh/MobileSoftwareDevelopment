 Lab7 实验报告：构建可滚动课程网格应用

1. 应用整体结构说明

本应用采用典型的 MVVM 分层架构思想（虽未使用 ViewModel，但层次划分清晰），整体分为三层：

层级 文件/包 职责
数据层 (Model) Topic.kt 定义数据模型，封装课程主题的属性
数据源层 (Data) DataSource.kt 使用 object 单例集中管理静态数据，模拟后端数据供给
UI 展示层 (UI) MainActivity.kt 使用 Jetpack Compose 构建界面，包含网格和卡片组合项

数据流向：DataSource.topics → CoursesGrid() → TopicCard() → 渲染到屏幕。

这种分层的好处是：数据与界面解耦，便于后续扩展（如从网络或数据库获取数据时，只需替换 DataSource 即可，UI 层无需改动）。

---

2. Topic 数据类字段设计与选择理由

在 Topic.kt 中定义了如下数据类：

data class Topic(
    @StringRes val name: Int,
    val availableCourses: Int,
    @DrawableRes val imageRes: Int
)

字段设计理由

字段 类型 设计理由
name @StringRes Int 使用字符串资源 ID 而非硬编码字符串，便于国际化（i18n）和统一维护。@StringRes 注解可在编译期检查传入的是否为合法资源 ID，避免运行时错误。
availableCourses Int 课程数量为纯数值，直接存储整数类型，便于计算和格式化显示。
imageRes @DrawableRes Int 使用图片资源 ID 而非图片 URL 或 Bitmap，因为本实验使用本地静态图片。@DrawableRes 注解同样提供编译期安全检查。

为什么选择数据类 (data class)？

- 自动生成 equals()、hashCode()、toString()、copy() 等方法
- 语法简洁，专注于承载数据
- 与 Compose 的重组机制配合良好（稳定的数据类型有助于性能优化）

---

3. 卡片布局实现思路

布局结构

单个课程卡片采用 "左图右文" 的横向布局，内部层次如下：

Card (卡片容器，提供圆角和阴影)
└── Row (水平排列)
    ├── Image (左侧：课程主题图片，68dp × 68dp)
    └── Column (右侧：文字信息，垂直排列)
        ├── Text (主题名称，bodyMedium)
        ├── Spacer (8dp 垂直间距)
        └── Row (水平排列：图标 + 数字)
            ├── Image (ic_grain 装饰图标)
            ├── Spacer (8dp 水平间距)
            └── Text (课程数量，labelMedium)

关键实现细节

- 图片尺寸控制：使用 Modifier.size(68.dp) 固定宽高，并配合 aspectRatio(1f) 确保图片始终为正方形，避免不同尺寸图片导致卡片高度不一致。
- 内容裁剪：设置 contentScale = ContentScale.Crop，使图片填满指定区域并保持比例，超出部分自动裁剪。
- 文字区域内边距：右侧 Column 使用 padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)，严格按照设计稿留出 16dp 内边距。
- 间距控制：标题与图标行之间使用 Spacer(modifier = Modifier.height(8.dp))，图标与数字之间使用 Spacer(modifier = Modifier.width(8.dp))。

---

4. 网格布局实现思路

使用 LazyVerticalGrid 构建两列可滚动网格：

LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    contentPadding = PaddingValues(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    items(topicList) { topic ->
        TopicCard(topic = topic)
    }
}

参数配置说明

参数 配置 作用
columns GridCells.Fixed(2) 固定显示 2 列，无论屏幕宽度如何，始终维持两列布局
contentPadding PaddingValues(8.dp) 控制整个网格四周的外边距（上、下、左、右各 8dp）
horizontalArrangement Arrangement.spacedBy(8.dp) 控制列与列之间的水平间距为 8dp
verticalArrangement Arrangement.spacedBy(8.dp) 控制行与行之间的垂直间距为 8dp
items() Lambda 表达式 遍历数据列表，为每个 Topic 对象生成一个 TopicCard

为什么使用 LazyVerticalGrid？

- 懒加载：只渲染屏幕可见的卡片，滚动时才创建新项，内存占用低，性能优秀
- 自适应：GridCells.Fixed(2) 会自动计算每列宽度，无需手动指定
- 间距分离：contentPadding 和 spacedBy 职责分离，前者控制网格与屏幕边缘的距离，后者控制卡片之间的距离，叠加后恰好实现设计稿效果

---

5. 遇到的问题与解决过程

问题1：R.drawable.ic_grain 资源无法识别，代码反复报错

现象：R.drawable.ic_grain 时红时正常，Sync Project 后短暂恢复，过一会又变红，甚至整个 R 类都报错。

排查过程：
1. 首先检查 ic_grain.xml 文件，确认已放入 res/drawable/ 目录，文件名无误。
2. 尝试 Build → Clean Project 和 File → Sync Project with Gradle Files，但问题反复出现。
3. 怀疑是 Android Studio 缓存问题，执行 File → Invalidate Caches → Invalidate and Restart，重启后 R 类恢复正常，但 ic_grain 偶尔仍报错。
4. 最终发现是 Android Studio 对 Vector Asset 的索引不稳定，保存文件并重新打开项目后解决。

收获：Android 资源编译依赖 Gradle 的增量构建，遇到资源类问题时，清理缓存和重启往往比反复 Sync 更有效。

---

问题2：卡片内图片与文字的对齐不符合设计稿

现象：早期实现时，图片高度与右侧文字区域高度不一致，导致卡片内部看起来"一边高一边低"。

解决：严格使用 Modifier.size(68.dp) 固定图片尺寸，并在右侧 Column 中统一设置 padding(16.dp)，使文字区域总高度与图片高度（68dp）加上下内边距对齐，视觉上达到平衡。

---

问题3：Compose 函数缺少 @Composable 注解

现象：初次编写 TopicCard 函数时，忘记添加 @Composable 注解，Android Studio 提示函数体内无法使用 Compose 组件。

解决：在函数定义前添加 @Composable 注解，并确保注解与函数之间没有空行，否则编译器可能无法正确关联。

---

6. 实验总结

通过本次实验，我掌握了：
- 使用 data class 设计类型安全的数据模型
- 使用 object 单例管理静态数据集
- 使用 LazyVerticalGrid 和 GridCells.Fixed 构建高性能网格布局
- 使用 Card、Row、Column、Image、Text、Spacer 等组件组合复杂 UI
- 使用 Modifier 精确控制尺寸、间距和排列

最终应用成功展示了 24 个课程主题，以两列网格排列，支持垂直滚动，UI 与实验规格一致。
