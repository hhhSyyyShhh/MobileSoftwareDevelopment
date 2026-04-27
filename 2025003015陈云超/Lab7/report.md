# Lab7：构建可滚动课程网格应用 实验报告

## 一、应用整体结构说明
本次实验的 Courses App 采用了分层结构设计：
1.  **model 层**：`Topic.kt` 定义数据模型，封装课程主题的所有属性。
2.  **data 层**：`DataSource.kt` 单例对象，集中管理静态课程数据，方便后续扩展。
3.  **UI 层**：
    - `TopicGridItem`：单个课程卡片的可组合项，实现卡片布局与样式。
    - `CoursesGrid`：使用 `LazyVerticalGrid` 构建两列可滚动网格。
    - `MainActivity`：应用入口，调用网格组件展示界面。

## 二、Topic 数据类的字段设计与选择理由
| 字段 | 类型 | 选择理由 |
|------|------|----------|
| `nameRes` | `@StringRes Int` | 使用字符串资源ID，方便多语言适配，符合 Android 资源管理规范。 |
| `courseCount` | `Int` | 课程数量为整数，直接存储数值即可，无需资源引用。 |
| `imageRes` | `@DrawableRes Int` | 使用图片资源ID，方便系统加载图片，适配不同屏幕密度。 |

## 三、卡片布局实现思路
卡片使用 `Card` 作为根容器，内部采用 `Row` 水平布局，分为左右两部分：
1.  **左侧图片**：使用 `Image` 组件，通过 `aspectRatio(1f)` 保证宽高比为 1:1，同时 `size(68.dp)` 限制最小尺寸。
2.  **右侧文字区域**：使用 `Column` 垂直布局，包含主题名称和课程数量行：
    - 主题名称使用 `MaterialTheme.typography.bodyMedium`。
    - 课程数量行使用 `Row` 水平布局，包含 `ic_grain` 图标和数字文本，通过 `Arrangement.spacedBy(8.dp)` 控制间距。
3.  所有内边距和间距严格按照 UI 规格设置，保证和设计图一致。

## 四、网格布局实现思路
使用 `LazyVerticalGrid` 实现两列可滚动网格，关键参数配置如下：
- `columns = GridCells.Fixed(2)`：设置为固定两列布局。
- `contentPadding = PaddingValues(8.dp)`：控制网格整体四周的留白。
- `horizontalArrangement = Arrangement.spacedBy(8.dp)`：控制卡片之间的水平间距。
- `verticalArrangement = Arrangement.spacedBy(8.dp)`：控制卡片之间的垂直间距。
- `items(topics)`：遍历所有课程主题数据，生成对应的卡片组件。

## 五、遇到的问题与解决过程
1.  **图片不显示**：
    - 问题原因：图片文件名和 `DataSource` 里图片没有放到正确的 `drawable` 目录。
    - 解决方法：检查文件名是否为全小写，确认所有图片都在 `res/drawable/` 目录下。
2.  **图片变形**：
    - 问题原因：没有设置 `contentScale`，图片被拉伸变形。
    - 解决方法：给 `Image` 组件添加 `contentScale = ContentScale.Crop`，让图片按比例裁剪填充，保持1:1比例。