# Lab8：构建超级英雄列表应用 实验报告
## 一、应用整体结构说明
本项目为 Superheroes 超级英雄列表应用，基于 Kotlin + Jetpack Compose 实现，遵循 Material 3 设计规范。项目采用分层结构设计：
- 主入口：`MainActivity.kt`，负责应用初始化、主题加载、搭建 Scaffold 与顶部应用栏。
- 界面层：`HeroesScreen.kt`，实现列表项组件 HeroItem 与可滚动列表 HeroesList。
- 数据层：`model` 包，包含 Hero 数据类与 HeroesRepository 静态数据源。
- 主题层：`ui.theme` 包，统一管理颜色、形状、字体及深浅色主题适配。

## 二、Hero 数据类字段设计与理由
```kotlin
data class Hero(
    @StringRes val nameRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val imageRes: Int
)
```
### 设计思路
使用资源 ID 存储界面内容，符合 Android 资源管理规范，便于维护与国际化。

### 字段说明
- `nameRes`：英雄名称字符串资源 ID
- `descriptionRes`：英雄描述字符串资源 ID
- `imageRes`：英雄图片资源 ID

### 注解作用
`@StringRes` / `@DrawableRes` 用于编译期类型检查，避免传入错误资源。

## 三、HeroesRepository 数据源组织方式
- 使用 `object` 单例类集中管理全部英雄数据，保证全局唯一。
- 内部维护 `heroes` 列表，包含 6 个 Hero 对象。
- 数据与 `strings.xml`、`drawable` 资源绑定，结构清晰、易于扩展。

## 四、英雄列表项布局实现思路
- 外层使用 `Card` 组件，使用主题中等圆角，提升视觉层次。
- 内部使用 `Row` 实现左右布局：左侧文字区域、右侧图片区域。
- 文字区域使用 `Column` 垂直排列英雄名称与描述。
- 图片区域使用 `Box` 固定 72dp×72dp，裁剪 8dp 圆角，图片以 `ContentScale.Crop` 填充。
- 卡片高度 72dp、内容内边距 16dp，完全符合实验布局规格。

## 五、LazyColumn 列表实现和间距配置说明
- 使用 `LazyColumn` 实现懒加载可滚动列表，只渲染屏幕可见项，性能更优。
- `contentPadding = PaddingValues(16.dp)`：设置列表四周内边距。
- `verticalArrangement = Arrangement.spacedBy(8.dp)`：设置列表项之间间距。
- 通过 `items(heroes)` 遍历数据，自动生成所有列表项。

## 六、Material 主题配置说明
- 颜色：在 `Color.kt` 中定义浅色 / 深色两套颜色方案，支持系统主题自动切换。
- 形状：在 `Shape.kt` 中配置 small/medium/large 三级圆角，统一全局风格。
- 字体：在 `Type.kt` 中引入 Cabin 字体，配置 displayLarge、displaySmall、bodyLarge 文字样式。
- 主题整合：在 `Theme.kt` 中将颜色、字体、形状注入 `MaterialTheme`，全局统一使用。

## 七、顶部应用栏和状态栏处理说明
- 顶部应用栏：使用 `Scaffold + TopAppBar` 实现标准结构，标题居中，使用 displayLarge 样式。
- 状态栏适配：开启 edge-to-edge 显示，状态栏设为透明，根据深浅色自动切换文字颜色。
- 通过 `innerPadding` 避免列表内容被顶部应用栏遮挡。

## 八、遇到的问题与解决过程
1. **字体资源报错**
   原因：字体文件名包含大写字母与横杠，不符合 Android 资源命名规则。
   解决：改为小写 + 下划线格式 `cabin_regular.ttf`、`cabin_bold.ttf`，并同步修改代码引用。

2. **代码标红、导入缺失**
   解决：使用 `Alt + Enter` 一键导入，重新构建项目后恢复正常。

3. **资源索引异常**
   解决：执行 `Make Project` 重建索引，红波浪报错消失。

## 九、运行效果验证
- 应用可正常启动，无崩溃、无报错。
- 正确显示 6 个超级英雄，名称、描述、图片匹配无误。
- 列表可上下流畅滚动，懒加载机制正常。
- 浅色 / 深色模式自动切换，界面清晰可读。
- 顶部应用栏、状态栏、字体、主题均符合实验要求。