# Lab8 实验报告：Superheroes

## 1. 应用整体结构说明
本实验采用“数据层 + UI 层 + 主题层”的方式组织代码。
- `Hero` 负责描述单个超级英雄的数据结构。
- `HeroesRepository` 负责集中管理静态数据源。
- `HeroesScreen` 负责列表和列表项 UI。
- `MainActivity` 负责启动应用并加载主题。
- `ui/theme` 负责颜色、字体、形状和系统栏样式。

这种拆分方式的好处是职责清晰，后续维护和修改都更方便。

## 2. Hero 数据类字段设计与理由
`Hero` 包含三个字段：
- `nameRes: Int`：英雄名称字符串资源
- `descriptionRes: Int`：英雄说明字符串资源
- `imageRes: Int`：英雄图片资源

使用资源 ID 而不是直接写字符串和图片对象的好处是：
1. 便于统一管理文案和图片。
2. 支持国际化和资源替换。
3. 通过 `@StringRes` 与 `@DrawableRes` 可以减少传参错误。

## 3. HeroesRepository 数据源组织方式
`HeroesRepository` 使用 `object` 单例保存英雄列表。
这样做可以让所有静态数据集中存放，UI 层只需要读取 `heroes` 即可。
当英雄文案或图片发生变化时，只需要修改仓库数据，不需要修改界面代码。

## 4. 英雄列表项布局实现思路
单个英雄列表项使用 `Card + Row + Column + Image` 组合：
- `Card` 提供卡片背景和圆角
- `Row` 让文字与图片横向排列
- `Column` 让名称和说明纵向排列
- `Image` 显示右侧头像

图片使用固定尺寸，并通过 `clip(RoundedCornerShape(8.dp))` 实现圆角裁剪。

## 5. LazyColumn 列表实现和间距配置说明
列表使用 `LazyColumn` 实现，因为它适合展示可滚动的大量条目，并且只会绘制屏幕可见部分，性能更好。
使用 `contentPadding = PaddingValues(16.dp)` 设置列表整体边距。
使用 `Arrangement.spacedBy(8.dp)` 设置每个卡片之间的间距。

## 6. Material 主题配置说明
本实验自定义了颜色、字体和形状：
- 颜色：分别定义浅色和深色主题，保证不同模式下都清晰可读。
- 字体：使用 Cabin 字体，通过 `FontFamily` 和 `Typography` 统一管理。
- 形状：通过 `Shapes` 设置卡片圆角，使界面更加统一。

## 7. 顶部应用栏和状态栏处理说明
顶部应用栏使用 `CenterAlignedTopAppBar`，标题居中显示。
在主题中使用 `WindowCompat.setDecorFitsSystemWindows(window, false)` 和 `SideEffect` 处理 edge-to-edge。
同时设置状态栏和导航栏颜色为透明，并根据深浅色模式切换系统栏图标颜色。

## 8. 遇到的问题与解决过程
1. 资源文件命名必须小写下划线，否则无法编译。
2. 图片如果不设置固定尺寸，会把列表项撑得过高。
3. 顶部栏如果没有使用居中样式，标题不容易满足实验要求。
4. 使用 `Modifier.padding(innerPadding)` 可以避免内容被顶部栏遮挡。
5. 字体文件必须放在 `res/font` 下，并在主题排版中引用。