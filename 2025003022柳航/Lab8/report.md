# Lab8 实验报告：构建超级英雄列表应用

## 一、实验概述
本次实验基于 Jetpack Compose 实现一款超级英雄列表应用，综合运用数据类、静态数据源、自定义 Material 3 主题、LazyColumn 列表、顶部应用栏、状态栏适配等知识，完成符合设计规范的完整界面。

## 二、应用整体结构说明
应用采用标准分层结构：
1. **数据层**：Hero 数据类 + HeroesRepository 数据源
2. **UI 层**：HeroesScreen 包含列表与列表项卡片
3. **主题层**：自定义颜色、字体、形状，支持浅色/深色模式
4. **框架层**：MainActivity 使用 Scaffold 搭建整体结构

## 三、Hero 数据类设计
设计三个字段，全部使用资源 ID 保证安全：
- nameRes：英雄名称字符串资源
- descriptionRes：英雄描述字符串资源
- imageRes：英雄图片资源
使用 @StringRes 和 @DrawableRes 注解实现类型安全。

## 四、HeroesRepository 数据源设计
使用 object 单例集中管理 6 个超级英雄数据，统一从 strings.xml 和 drawable 读取资源，便于维护与扩展，是 Android 标准静态数据管理方式。

## 五、列表项布局实现思路
- 外层使用 Card 卡片，使用主题中等圆角
- 内部使用 Row 横向排列：左侧文字、右侧图片
- 文字部分使用 Column 显示名称与描述
- 图片固定 72dp，裁剪 8dp 圆角
- 间距严格按照 16dp 内边距与 16dp 图文间距实现

## 六、LazyColumn 列表实现
- 使用 LazyColumn 实现高性能滚动列表
- contentPadding = 16.dp 设置整体边距
- verticalArrangement.spacedBy(8.dp) 设置卡片间距
- 使用 items 遍历数据源渲染列表项

## 七、Material 3 主题配置
1. **颜色**：自定义浅色/深色两套配色体系
2. **字体**：引入 Cabin 字体，设置 displayLarge、displaySmall、bodyLarge
3. **形状**：统一圆角规格，small=8dp，medium=16dp，large=16dp
4. **主题**：在 SuperheroesTheme 中统一组合颜色、字体、形状

## 八、顶部栏与状态栏适配
- 使用 CenterAlignedTopAppBar 实现居中标题
- 标题使用 displayLarge 样式
- 通过 SideEffect 配置状态栏颜色
- 自动适配深浅模式状态栏图标颜色

## 九、遇到的问题与解决
1. 缺少 layout 导入导致 fillMaxSize 报错
   → 解决：添加完整的 foundation.layout 导入
2. 字体文件找不到
   → 解决：创建 res/font 文件夹，文件名全小写
3. 深色模式文字看不清
   → 解决：正确配置系统栏图标风格

## 十、实验总结
成功完成 Superheroes 应用，掌握 Compose 列表、卡片、主题、资源管理、状态栏适配等核心技能，界面完全符合实验要求。