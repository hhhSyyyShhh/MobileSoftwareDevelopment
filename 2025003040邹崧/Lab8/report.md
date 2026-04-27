# Lab8 Superheroes英雄列表应用实验报告

## 1. 实验目标
- 掌握Compose中自定义字体的集成与全局配置；
- 实现基于LazyColumn的可滚动英雄列表，掌握卡片式布局；
- 适配Material 3主题（浅色/深色模式）和沉浸式状态栏；
- 理解数据类+单例数据源的设计模式，实现数据与UI解耦。

## 2. 核心技术点
### （1）自定义字体集成
- 在res/font目录导入Cabin字体文件；
- 通过FontFamily封装字体，在Typography中全局替换默认字体；
- 不同文字层级（标题/描述）使用不同字重（Bold/Normal）。

### （2）可滚动列表实现
- 使用LazyColumn实现懒加载列表，仅渲染可见项；
- 通过contentPadding设置列表内边距，verticalArrangement设置项间距；
- 单个卡片基于Card+Row+Column+Image组合实现，适配尺寸/圆角要求。

### （3）主题适配
- 区分浅色/深色颜色方案，自动识别系统主题切换；
- 配置沉浸式状态栏，统一状态栏与主题主色；
- 通过MaterialTheme统一管理颜色、形状、排版，保证风格一致。

## 3. 遇到的问题与解决
- 问题1：Cabin字体不生效 → 解决：检查字体文件命名（小写+下划线），确保Typography中正确关联FontFamily；
- 问题2：列表被顶部栏遮挡 → 解决：使用Scaffold的innerPadding适配内容区边距；
- 问题3：深色模式颜色显示异常 → 解决：完善DarkColorScheme的onPrimary/onSurface等颜色配置。

## 4. 实验总结
本次实验掌握了Compose自定义字体集成、可滚动列表、Material 3主题适配的核心用法，理解了“数据驱动UI”的设计思想。通过单例数据源封装数据，保证了代码的可维护性；通过全局主题配置，实现了界面风格的统一。实验过程中解决了字体、布局、主题适配等常见问题，提升了Compose UI开发能力。