# Lab7 实验报告
## 1. 应用整体结构说明
本次Courses App采用"数据-UI"分离的结构设计，核心分为三层：
- **数据模型层**：`model/Topic.kt`定义课程主题的数据结构，封装名称、课程数、图片资源三个核心字段；
- **数据提供层**：`data/DataSource.kt`通过单例对象集中管理24条静态课程数据，避免数据分散；
- **UI展示层**：`MainActivity.kt`包含两个核心可组合函数——`TopicGridItem`（单个卡片UI）和`CoursesGrid`（两列网格布局），通过Compose的懒加载特性实现可滚动列表。

整体遵循Compose"单向数据流"原则，数据源变更会自动驱动UI刷新，结构低耦合、易维护。

## 2. `Topic` 数据类的字段设计与选择理由
| 字段名 | 类型 | 选择理由 |
|--------|------|----------|
| `name` | `@StringRes Int` | 拒绝硬编码字符串，使用字符串资源ID符合Android资源管理规范，支持多语言适配，且实验要求明确主题名称为字符串资源；`@StringRes`注解可让IDE校验资源合法性，减少运行时错误 |
| `courseCount` | `Int` | 课程数量为纯数字，无格式/本地化需求，使用基本类型`Int`最轻量化，满足展示需求 |
| `imageRes` | `@DrawableRes Int` | 图片资源通过ID引用，统一管理drawable目录下的资源，避免路径硬编码；`@DrawableRes`注解提供编译期校验 |

## 3. 卡片布局实现思路
单个课程卡片采用"嵌套组合项"的方式实现，核心逻辑如下：
1. **外层容器**：`Card`组件提供圆角、阴影，符合Material Design风格；
2. **核心布局**：`Row`将卡片分为左右两部分（图片+文字）；
   - 左侧图片：固定68dp尺寸，`aspectRatio(1f)`保证正方形，`clip`修饰符适配Card圆角；
   - 右侧文字区：`Column`垂直排列主题名称和课程数行，`align(Alignment.CenterVertically)`保证与图片垂直居中；
3. **文字区细节**：
   - 主题名称：使用`bodyMedium`字体，底部8dp留白与下方行分隔；
   - 课程数行：`Row`包裹`Icon`（装饰图标）+`Text`（数字），两者间距8dp，数字使用`labelMedium`字体；
4. **间距控制**：严格遵循实验规格（文字区16dp内边距、名称与数字行8dp间距、图标与数字8dp间距）。

核心嵌套关系：`Card` → `Row` → （`Image` + `Column` → （`Text` + `Row` → （`Icon` + `Text`）））。

## 4. 网格布局实现思路（`LazyVerticalGrid` 参数配置）
`LazyVerticalGrid`是实现可滚动网格的核心，关键参数配置及作用：
| 参数 | 配置值 | 作用 |
|------|--------|------|
| `columns` | `GridCells.Fixed(2)` | 强制网格为2列，满足实验"两列网格"要求 |
| `modifier.padding(8.dp)` | 8dp | 网格整体与屏幕边缘的间距 |
| `horizontalArrangement` | `Arrangement.spacedBy(8.dp)` | 列之间的水平间距（卡片左右间距） |
| `verticalArrangement` | `Arrangement.spacedBy(8.dp)` | 行之间的垂直间距（卡片上下间距） |
| `contentPadding` | `PaddingValues(8.dp)` | 网格内容区域的内边距，补充外层padding，保证边缘卡片间距合规 |
| `items(DataSource.topics)` | 数据源列表 | 懒加载遍历所有主题，为每条数据渲染`TopicGridItem` |

`LazyVerticalGrid`的"懒加载"特性仅渲染可视区域卡片，提升了24条数据的展示性能。

## 5. 遇到的问题与解决过程
### 问题1：图片在不同屏幕下拉伸变形
- 现象：仅设置`size(68.dp)`后，部分屏幕图片非正方形；
- 解决：添加`aspectRatio(1f)`修饰符，强制图片宽高比1:1，同时保留`size(68.dp)`控制最大尺寸。

### 问题2：文字区与图片垂直对齐错位
- 现象：文字区整体偏上，与图片未居中；
- 解决：为文字区`Column`添加`align(Alignment.CenterVertically)`，让`Row`内的图片和文字垂直居中。

### 问题3：网格间距不符合规格
- 现象：仅设置`contentPadding`后，卡片间间距不足8dp；
- 解决：同时使用`horizontalArrangement`/`verticalArrangement`的`spacedBy(8.dp)` + 外层`padding(8.dp)`，实现"外层8dp + 卡片间8dp"的效果。

### 问题4：装饰图标尺寸过大
- 现象：`ic_grain`图标默认尺寸与数字比例不协调；
- 解决：为`Icon`添加`modifier.size(16.dp)`，缩小图标至合适尺寸。