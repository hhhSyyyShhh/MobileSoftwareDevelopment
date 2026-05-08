# Lab8 超级英雄列表应用实验报告

## 一、应用整体结构
本应用采用 MVVM 简化结构，分为三层：
1. 数据层：Hero 数据类 + HeroesRepository 数据源
2. UI层：HeroItem 列表项 + HeroesList 滚动列表
3. 主题层：自定义颜色、字体、形状、状态栏适配

## 二、Hero 数据类设计
使用 data class 定义三个字段：
- nameRes：字符串资源ID，存储英雄名称
- descriptionRes：字符串资源ID，存储英雄描述
- imageRes：图片资源ID，存储英雄头像
设计理由：使用资源ID解耦数据与UI，适配多语言/多主题。

## 三、HeroesRepository 数据源
使用 object 单例模式集中管理 6 个英雄数据，
优点：全局唯一、无需实例化、数据统一管理。

## 四、列表项布局实现
1. 外层使用 Card 实现圆角卡片
2. Row 横向排列图片+文字
3. 图片固定 72dp，8dp 圆角裁剪
4. Column 垂直展示名称+描述
5. 严格遵循 16dp 内边距、16dp 图文间距

## 五、LazyColumn 列表实现
- contentPadding：16dp 列表四周留白
- verticalArrangement：8dp 列表项间距
- items 函数遍历数据源，实现高效滚动列表

## 六、Material 主题配置
1. 颜色：自定义深浅主题配色，保证对比度
2. 字体：加载 Cabin 自定义字体，设置三级文字样式
3. 形状：medium 圆角 16dp，匹配卡片规格

## 七、顶部栏与状态栏
1. TopAppBar 居中显示应用名称
2. 透明状态栏，适配深浅模式图标颜色
3. Scaffold innerPadding 避免内容被遮挡

## 八、问题与解决
1. 图片溢出：使用 Modifier.clip 裁剪圆角
2. 状态栏断层：设置透明状态栏 + 系统栏适配
3. 字体不生效：检查字体文件名小写+资源引用正确