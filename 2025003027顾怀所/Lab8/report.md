# Lab8：构建超级英雄列表应用

## 一、应用整体结构说明
本项目采用分层架构设计，整体分为4个核心模块：
1.  **数据层**：`Hero.kt` 数据类 + `HeroesRepository.kt` 单例静态数据源，统一管理英雄数据
2.  **UI组件层**：`HeroesScreen.kt`，包含单个英雄卡片`HeroItem`和可滚动列表`HeroesList`
3.  **主题层**：`Color.kt`/`Shape.kt`/`Type.kt`/`Theme.kt`，实现自定义Material3主题
4.  **入口层**：`MainActivity.kt`，通过`Scaffold`整合顶部应用栏和列表，构建完整应用结构

整体遵循Jetpack Compose声明式UI开发规范，数据与UI完全分离，结构清晰，符合Android最佳实践。

## 二、Hero数据类字段设计与理由
本次设计的`Hero`数据类包含3个字段：
1.  `nameRes: @StringRes Int`：英雄名称字符串资源ID
    理由：使用资源ID而非硬编码字符串，支持多语言国际化，符合Android资源管理规范。
2.  `descriptionRes: @StringRes Int`：英雄说明字符串资源ID
    理由：同上，便于后续修改和本地化，避免硬编码维护成本。
3.  `imageRes: @DrawableRes Int`：英雄头像图片资源ID
    理由：使用Drawable资源ID统一管理图片，适配Android资源管理机制，便于图片替换与维护。

使用`data class`数据类，自动生成equals、hashCode、toString等方法，适合纯数据存储场景。

## 三、HeroesRepository数据源组织方式
使用`object`单例模式实现数据源，统一管理全部6个英雄数据：
- 将所有英雄数据集中存放在`heroes`列表中，避免数据分散
- 每个英雄实例通过字符串和图片资源ID初始化，数据与UI分离
- 单例模式保证应用内数据全局唯一，便于在列表中直接调用，无需重复创建实例

## 四、英雄列表项布局实现思路
单个英雄卡片采用**左右水平Row布局**，完全匹配实验图纸的尺寸与间距：
1.  外层使用`Card`组件，应用`MaterialTheme.shapes.medium`圆角
2.  左侧：垂直`Column`布局，包含英雄名称（`displaySmall`样式）和英雄说明（`bodyLarge`样式）
3.  右侧：72dp正方形图片，使用`Box`容器+`clip`裁剪8dp圆角，`ContentScale.Crop`保证图片不变形
4.  间距控制：卡片内边距16dp，文字与图片间距16dp，列表项之间间距8dp，完全符合UI规格

## 五、LazyColumn列表实现和间距配置说明
使用`LazyColumn`实现可滚动列表，核心配置：
- `contentPadding = PaddingValues(16.dp)`：控制列表四周与屏幕边缘的16dp留白
- `verticalArrangement = Arrangement.spacedBy(8.dp)`：控制相邻卡片之间的8dp间距
- `items(heroes)`：遍历数据源，为每个英雄生成对应的`HeroItem`
- `LazyColumn`为惰性加载，仅渲染屏幕可见区域内容，性能优于普通Column布局，适合长列表场景

## 六、Material主题配置说明
1.  **颜色**：使用Material Theme Builder生成的自定义配色，支持浅色/深色模式，主色调为暖黄色，适配超级英雄主题
2.  **形状**：自定义`Shapes`，设置卡片圆角为16dp，图片圆角为8dp，保持视觉一致性
3.  **排版**：引入Cabin字体，自定义`displayLarge`/`displaySmall`/`bodyLarge`样式，分别用于顶部栏标题、英雄名称和说明文字，提升应用整体风格
4.  **主题**：`SuperheroesTheme`整合颜色、排版、形状，支持系统深色/浅色模式切换，并在`SideEffect`中配置状态栏颜色，实现edge-to-edge效果

## 七、顶部应用栏和状态栏处理说明
1.  **顶部应用栏**：使用`CenterAlignedTopAppBar`，标题为`Superheroes`，居中显示，使用`displayLarge`字体样式，风格统一
2.  **状态栏适配**：通过`WindowCompat.setDecorFitsSystemWindows`配置无边框显示，根据浅色/深色模式动态设置状态栏颜色和图标颜色，避免系统栏与应用界面断层，提升视觉完整性

## 八、遇到的问题与解决过程
1.  **图片尺寸和圆角不符合要求**
    解决：使用固定72dp正方形尺寸+`clip(RoundedCornerShape(8.dp))`裁剪，同时设置`ContentScale.Crop`保证图片按比例填充不变形。

2.  **列表间距混乱，卡片重叠**
    解决：`LazyColumn`中`contentPadding`控制整体留白，`spacedBy`控制卡片间距，分别设置16dp和8dp，完全匹配实验规格。

3.  **自定义字体无法加载**
    解决：将字体文件放入`res/font/`目录，使用小写命名，在`Type.kt`中正确引用资源ID，避免大小写错误。

4.  **深色模式下文字不清晰**
    解决：使用Material Theme Builder生成适配的深色配色，确保文字与背景对比度足够，同时在主题中配置状态栏图标颜色随模式变化。

## 九、实验总结
通过本次Lab8实验，我熟练掌握了Kotlin数据类、单例数据源、Compose可滚动列表、自定义Material3主题、顶部应用栏和状态栏适配等核心知识点。
完成了从数据模型设计、静态数据源编写、单个卡片UI还原、列表布局搭建、主题配置的完整开发流程，理解了惰性列表的性能优势，同时学会了如何自定义颜色、字体和形状打造专属应用风格，提升了Android Compose移动端UI开发与主题设计能力。
