# Lab8 实验报告：Superheroes 应用开发

## 1. 应用整体结构说明
本应用采用 Jetpack Compose 构建，整体结构分为三层：
- **UI层**：`MainActivity.kt` 作为入口，使用 `Scaffold` 搭建包含顶部应用栏的页面框架，调用 `HeroesList` 展示列表。
- **组件层**：`HeroesScreen.kt` 包含 `HeroItem` 和 `HeroesList` 两个可组合项，分别实现列表项布局和列表布局。
- **数据层**：`Hero.kt` 为数据类，定义英雄信息；`HeroesRepository.kt` 提供静态数据源。
- **主题层**：`ui/theme/` 下的文件定义了 Material Design 主题，支持浅色/深色模式切换。

## 2. Hero 数据类字段设计与理由
`Hero` 数据类定义了三个字段：
- `nameRes`：英雄名称的字符串资源ID，方便多语言适配。
- `descriptionRes`：英雄描述的字符串资源ID，与名称保持一致的资源管理方式。
- `imageRes`：英雄头像的Drawable资源ID，统一管理图片资源。
使用资源ID而非硬编码字符串/图片，符合Android开发规范，便于后续维护和扩展。

## 3. HeroesRepository 数据源组织方式
`HeroesRepository` 为单例对象，通过 `listOf()` 创建包含6个英雄的静态列表，每个英雄实例引用对应的字符串和图片资源。这种方式结构简单、便于测试，适合本实验的固定数据源场景。

## 4. 英雄列表项布局实现思路
每个列表项使用 `Card` 组件实现卡片效果，内部通过 `Row` 水平布局：
- 左侧 `Column` 垂直排列英雄名称和描述文本。
- 右侧 `Box` 包裹 `Image` 显示英雄头像，设置固定大小和圆角裁剪。
卡片背景色使用 `MaterialTheme.colorScheme.surfaceVariant`，文本颜色使用 `onSurfaceVariant`，自动适配浅色/深色主题。

## 5. LazyColumn 列表实现和间距配置说明
使用 `LazyColumn` 实现高性能列表，配置如下：
- `verticalArrangement = Arrangement.spacedBy(8.dp)`：列表项之间添加8dp的垂直间距。
- `contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 150.dp)`：设置列表内边距，顶部避开顶部应用栏，底部添加额外间距防止被导航栏遮挡。
- 末尾添加 `Spacer` 组件，进一步确保最后一个列表项完整显示。

## 6. Material 主题配置说明，包括颜色、字体和形状
- **颜色**：`Color.kt` 定义了浅色和深色两套主题配色，背景色为深色模式下的深黑色，卡片为深灰绿色，文本为浅灰色，与实验要求效果一致。
- **字体**：`Type.kt` 引入了 `Cabin` 字体，定义了标题和正文的文字样式。
- **形状**：`Shape.kt` 定义了卡片的圆角形状，符合 Material Design 规范。
- `Theme.kt` 中关闭了动态取色，强制使用自定义配色，确保不同设备上显示效果一致。

## 7. 顶部应用栏和状态栏处理说明
- 使用 `CenterAlignedTopAppBar` 实现居中的顶部应用栏，标题使用自定义字体样式。
- 在 `MainActivity` 中调用 `enableEdgeToEdge()` 实现全屏显示，`Scaffold` 的 `innerPadding` 传递给 `HeroesList`，确保列表内容避开顶部应用栏和状态栏，不会被遮挡。

## 8. 遇到的问题与解决过程
- **问题1：列表顶部被顶部应用栏遮挡**：解决方法是在 `Scaffold` 中获取 `innerPadding`，传递给 `HeroesList` 的 `Modifier.padding()`。
- **问题2：列表底部被导航栏遮挡**：解决方法是在 `LazyColumn` 的 `contentPadding` 中添加底部内边距，并在列表末尾添加 `Spacer` 组件。
- **问题3：深色模式下卡片颜色与截图不一致**：解决方法是在 `Theme.kt` 中关闭动态取色，强制使用自定义的深色配色，并将卡片背景设置为 `surfaceVariant`。