# Lab8 实验报告：Superheroes

## 1. 应用整体结构说明
本实验采用“入口层 + 界面层 + 数据层 + 主题层”结构：

- MainActivity.kt：应用入口，负责 setContent 并加载主题和主界面。
- HeroesScreen.kt：负责页面结构（Scaffold、TopAppBar）和列表 UI（LazyColumn、HeroItem）。
- Hero.kt：定义英雄数据模型。
- HeroesRepository.kt：集中管理静态英雄数据。
- theme/：包含 Color.kt、Shape.kt、Type.kt、Theme.kt，统一管理 Material 3 视觉样式。
- values/strings.xml：集中维护文案资源。

## 2. Hero 数据类字段设计与理由
Hero 数据类字段为：

- nameRes: Int（@StringRes）
- descriptionRes: Int（@StringRes）
- imageRes: Int（@DrawableRes）

设计理由：

- 使用资源 ID 而不是硬编码字符串/图片路径，方便国际化与资源管理。
- 使用 @StringRes/@DrawableRes 注解可在编译期减少资源类型误用。
- 数据结构轻量，适合静态列表渲染。

## 3. HeroesRepository 数据源组织方式
采用 object 单例管理静态列表数据：

- 使用 listOf 一次性定义 6 个 Hero。
- 每个条目绑定 nameRes、descriptionRes、imageRes。
- UI 层只读取仓库数据，不关心资源细节，职责清晰。

## 4. 英雄列表项布局实现思路
HeroItem 使用 Card + Row + Column + Image 实现：

- 外层 Card 提供容器和主题形状。
- Row 水平布局：左侧文字、右侧图片。
- 左侧 Column 展示名称和描述，名称使用 displaySmall，描述使用 bodyLarge。
- 右侧 Image 固定 72dp，RoundedCornerShape(8.dp) 裁剪，并设置 ContentScale.Crop 保证填充。
- 列表项内部统一 16dp 间距，图片与文字保持 16dp 分隔。

## 5. LazyColumn 列表实现和间距配置说明
HeroesList 通过 LazyColumn 渲染仓库中的全部英雄：

- contentPadding = PaddingValues(16.dp)：控制列表四周留白。
- verticalArrangement = Arrangement.spacedBy(8.dp)：控制卡片间距。
- items(heroes) 按数据逐项渲染 HeroItem。

这样可保证滚动性能和视觉间距一致性。

## 6. Material 主题配置说明（颜色、字体、形状）
主题层统一在 SuperheroesTheme 中注入：

- 颜色：Color.kt 定义 Light/Dark 两套 colorScheme，确保浅色与深色模式可读。
- 字体：Type.kt 使用 Cabin 字体族（regular + bold），并配置 displayLarge/displayMedium/displaySmall/bodyLarge。
- 形状：Shape.kt 中 medium/large 设为 16dp，small 设为 8dp，用于卡片和局部组件。

通过 MaterialTheme(colorScheme, typography, shapes) 统一生效。

## 7. 顶部应用栏和状态栏处理说明
页面结构：

- 使用 Scaffold 组织 topBar 与列表内容。
- 顶部栏使用 CenterAlignedTopAppBar，标题为 app_name，样式 displayLarge。
- Scaffold 的 innerPadding 传递给列表，避免内容被顶栏遮挡。

系统栏适配：

- 在 Theme.kt 的 SideEffect 中调用 WindowCompat.setDecorFitsSystemWindows(window, false)。
- 状态栏设为透明，导航栏色与 surface 对齐。
- 根据 darkTheme 动态切换状态栏/导航栏图标明暗，保证可读性。

## 8. 遇到的问题与解决过程
问题 1：图片在列表项中容易被原图尺寸影响布局。
解决：对图片设置固定 size(72.dp) + ContentScale.Crop。

问题 2：深色模式下系统栏图标可读性不足。
解决：在主题 SideEffect 中通过 WindowInsetsControllerCompat 按深浅主题切换图标亮暗。

问题 3：默认主题样式视觉层次不明显。
解决：自定义颜色、Cabin 字体和圆角形状，强化标题和卡片层次。

---

说明：本目录已完成源码、主题与资源组织。运行截图请在 Android Studio 模拟器中执行应用后分别导出浅色与深色截图，命名为 screenshot_light.png 与 screenshot_dark.png。
