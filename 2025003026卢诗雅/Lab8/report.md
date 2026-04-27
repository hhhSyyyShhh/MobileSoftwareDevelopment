# Lab8：构建超级英雄列表应用

## 一、应用整体结构说明
本应用基于 Jetpack Compose 和 Material 3 实现，采用标准 Android 项目结构，实现了可滚动的超级英雄列表，支持浅色/深色双模式自动适配，界面布局、字体、颜色均严格遵循实验要求。

- MainActivity.kt：应用入口，使用 Scaffold 搭建页面结构，包含顶部应用栏与英雄列表。
- HeroesScreen.kt：实现英雄列表页面，包含 HeroesScreen、HeroesList、HeroItem 可组合项。
- model/Hero.kt：定义英雄数据类。
- model/HeroesRepository.kt：提供静态数据源。
- ui/theme/Color.kt：定义浅色、深色主题颜色。
- ui/theme/Type.kt：配置 Cabin 字体与文字样式。
- ui/theme/Shape.kt：定义组件圆角形状。
- ui/theme/Theme.kt：主题整合与系统栏适配。

## 二、Hero 数据类字段设计与理由
```kotlin
data class Hero(
    @StringRes val nameRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val imageRes: Int
)
```

- nameRes：存储英雄名称字符串资源 ID。

- descriptionRes：存储英雄描述字符串资源 ID。

- imageRes：存储英雄图片资源 ID。

- 使用资源注解 @StringRes、@DrawableRes 保证类型安全，避免资源引用错误。

- 采用数据类结构，简洁高效，适合列表展示。

## 三、HeroesRepository 数据源组织方式

```kotlin
object HeroesRepository {
    val heroes = listOf(
        Hero(R.string.hero1, R.string.description1, R.drawable.android_superhero1),
        Hero(R.string.hero2, R.string.description2, R.drawable.android_superhero2),
        Hero(R.string.hero3, R.string.description3, R.drawable.android_superhero3),
        Hero(R.string.hero4, R.string.description4, R.drawable.android_superhero4),
        Hero(R.string.hero5, R.string.description5, R.drawable.android_superhero5),
        Hero(R.string.hero6, R.string.description6, R.drawable.android_superhero6)
    )
}
```

- 使用 object 单例模式，全局唯一，便于统一访问。

- 集中管理所有英雄数据，便于维护与扩展。

- 提供不可变列表，数据安全稳定。

- 与 UI 层分离，结构清晰，符合模块化开发思想。

## 四、英雄列表项布局实现思路

- 外层使用 Card 组件，采用主题中等圆角（16dp）。

- 内部使用 Row 实现文字区域与图片水平排列。

- 文字区域使用 Column 垂直放置英雄名称与描述。

- 英雄名称使用 displaySmall 样式，加粗。

- 英雄描述使用 bodyLarge 样式，常规字重。

- 图片大小固定 72dp，圆角 8dp，裁剪方式为 ContentScale.Crop。

- 内容内边距 16dp，文字与图片间距 16dp。

- 卡片背景使用 primaryContainer，文字颜色使用 onPrimaryContainer，自动适配深浅模式。

## 五、LazyColumn 列表实现和间距配置说明

- 使用 LazyColumn 实现高效滚动列表。

- 列表左右内边距 16dp。

- 列表项垂直间距 14dp。

- 通过 items 遍历 HeroesRepository 中的数据，渲染每一项 HeroItem。

- 接收 Scaffold 传入的 innerPadding，避免列表被顶部应用栏遮挡。

## 六、Material 主题配置说明

### 1. 颜色

- 浅色背景：0xFFFDFBFF

- 深色背景：0xFF121318

- 卡片背景：primaryContainer

- 标题文字颜色：onBackground

- 卡片文字颜色：onPrimaryContainer

### 2. 形状

- small：8dp（图片圆角）

- medium：16dp（卡片圆角）

- large：16dp

### 3. 字体与排版

使用 Cabin 字体：

- displayLarge：顶部标题，常规字重，32sp

- displaySmall：英雄名称，加粗，24sp

- bodyLarge：描述文字，常规字重，16sp

### 4. 主题

SuperheroesTheme 自动根据系统模式切换浅色 / 深色主题，并支持 Android 12 及以上动态颜色。

## 七、顶部应用栏和状态栏处理说明

- 顶部应用栏使用 TopAppBar，标题居中显示 “Superheroes”。

- 标题样式为 displayLarge，常规不加粗。

- 标题颜色自动适配深浅模式，浅色黑字，深色白字。

- 启用 edge-to-edge 模式，状态栏与应用背景统一。

- 状态栏图标颜色随主题自动切换，保证清晰可见。

## 八、遇到的问题与解决过程

1. 标题显示不全、被截断
解决：调整字号为 32sp，移除多余内边距。

2. 深色模式文字看不清
解决：使用 onBackground、onPrimaryContainer 自动适配黑白颜色。

3. Cabin 字体不生效
解决：字体文件命名为小写下划线格式：cabin_regular.ttf、cabin_bold.ttf。

4. 图片无圆角
解决：使用  Modifier.clip (RoundedCornerShape (8.dp))。

5. 列表被顶部栏遮挡
解决：将 Scaffold 的 innerPadding 应用到列表。
