# Lab7 可滚动课程网格应用实验报告

## 1. 实验目标
- 掌握 Compose 中 `LazyVerticalGrid` 网格布局的使用，实现两列可滚动列表；
- 理解数据类+单例数据源的设计模式，实现数据与UI分离；
- 严格按照UI规格实现组件布局，掌握间距、尺寸、样式的精准控制。

## 2. 界面结构设计
### （1）单个课程卡片
- 布局：左侧68dp×68dp图片 + 右侧文字区域（垂直排列主题名、水平排列图标+课程数）；
- 间距：文字区域内边距16dp，主题名与课程数间距8dp，图标与数字间距8dp；
- 样式：主题名使用bodyMedium，课程数使用labelMedium，卡片圆角8dp。

### （2）网格布局
- 列数：固定两列（`GridCells.Fixed(2)`）；
- 间距：网格整体内边距8dp，卡片水平/垂直间距均为8dp；
- 滚动：基于 `LazyVerticalGrid` 实现垂直可滚动，仅加载可见项，提升性能。

## 3. 核心代码逻辑
### （1）数据层
- `Topic` 数据类：封装主题名称（字符串资源ID）、课程数量、图片资源ID，解耦硬编码；
- `DataSource` 单例类：集中管理24个课程主题数据，便于维护和扩展。

### （2）UI层
- `TopicCard` 可组合函数：复用单个卡片布局，接收 `Topic` 对象渲染内容；
- `CoursesGridApp` 可组合函数：通过 `LazyVerticalGrid` + `items` 遍历数据源，批量渲染卡片。

## 4. 遇到的问题与解决
- 问题1：图片尺寸不匹配68dp×68dp → 解决：使用 `Modifier.size(68.dp)` 强制固定尺寸，配合 `clip` 裁剪圆角；
- 问题2：网格间距不符合要求 → 解决：通过 `horizontalArrangement`/`verticalArrangement` 设置卡片间距，`modifier.padding` 设置网格内边距；
- 问题3：文字样式不匹配 → 解决：使用 `MaterialTheme.typography` 中的bodyMedium/labelMedium统一样式；
- 问题4：编译报错 `The file name must end with .xml or .png` → 解决：删除drawable目录下无后缀的README文件，清理缓存后重建项目；
- 问题5：包名不匹配报 `Unresolved reference` → 解决：统一所有文件包名为 `com.example.artspace`，修正导入语句路径。

## 5. 实验总结
本次实验掌握了 Compose 网格布局的核心用法，理解了“数据驱动UI”的设计思想，能够严格按照UI规格实现精准的布局控制。同时解决了资源引用、包名匹配、缓存清理等常见开发问题，为复杂UI开发打下基础，也提升了排查编译报错的能力。