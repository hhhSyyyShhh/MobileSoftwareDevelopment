# Lab8：构建超级英雄列表应用实验报告

## 一、实验目的

本实验基于 Jetpack Compose 与 Material 3，实现一个具有现代化界面的 Superheroes 超级英雄列表应用。实验主要目标包括：

1. 学习使用 Kotlin 数据类组织应用数据
2. 学习使用 Repository 管理静态数据源
3. 学习 Compose 中 LazyColumn 的使用
4. 学习 Card、Row、Column、Image 等布局组件
5. 学习 Material 3 自定义主题配置
6. 学习浅色与深色主题适配
7. 学习 Scaffold 与顶部应用栏实现
8. 学习状态栏 edge-to-edge 显示适配

通过本实验，进一步掌握 Compose 声明式 UI 开发流程与 Android 现代化界面设计方法。

------

# 二、项目整体结构

本项目采用分层结构组织代码，将界面、数据与主题分离，提升了代码可维护性与可读性。

项目主要结构如下：

```text
com.example.activity
│
├── MainActivity.kt
├── HeroesScreen.kt
│
├── model
│   ├── Hero.kt
│   └── HeroesRepository.kt
│
└── ui
    └── theme
        ├── Color.kt
        ├── Shape.kt
        ├── Theme.kt
        └── Type.kt
```

其中：

- MainActivity.kt 作为应用入口
- HeroesScreen.kt 负责列表与列表项 UI
- model 文件夹负责数据管理
- theme 文件夹负责 Material 主题配置

这种结构符合现代 Android 开发规范。

------

# 三、Hero 数据类设计

为了统一描述超级英雄数据，本实验使用 Kotlin 数据类 Hero 表示单个列表项。

代码如下：

```kotlin
data class Hero(
    @StringRes val nameRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val imageRes: Int
)
```

字段说明：

| 字段           | 作用               |
| -------------- | ------------------ |
| nameRes        | 英雄名称字符串资源 |
| descriptionRes | 英雄说明字符串资源 |
| imageRes       | 英雄图片资源       |

设计原因：

1. 使用 data class 可以自动生成 equals、copy、toString 等函数
2. 使用资源 ID 可以统一管理字符串和图片
3. 使用 @StringRes 与 @DrawableRes 可以增强类型安全
4. 后期扩展数据时更方便维护

------

# 四、HeroesRepository 数据源实现

为了集中管理应用中的全部英雄数据，本实验创建了 HeroesRepository 对象。

实现方式如下：

```kotlin
object HeroesRepository {
    val heroes = listOf(...)
}
```

Repository 中保存了六个 Hero 对象。

每个对象包含：

- 英雄名称
- 英雄描述
- 英雄图片

设计优点：

1. 数据与 UI 解耦
2. 后续更换数据源更加方便
3. 代码结构更清晰
4. 避免 MainActivity 代码过于复杂

应用运行时，LazyColumn 从 Repository 中读取数据并动态生成列表。

------

# 五、英雄列表项布局实现

单个英雄列表项使用 HeroItem() 组合函数实现。

主要使用：

- Card
- Row
- Column
- Image
- Text

布局结构如下：

```text
Card
 └── Row
      ├── Column
      │    ├── Hero Name
      │    └── Hero Description
      └── Image
```

实现思路：

1. 使用 Card 实现圆角卡片效果
2. 使用 Row 让文字与图片水平排列
3. 使用 Column 实现名称与描述垂直排列
4. 使用 Modifier.weight(1f) 让文字区域自动占满剩余空间
5. 使用 Image 显示英雄图片
6. 使用 clip(RoundedCornerShape()) 实现图片圆角
7. 使用 ContentScale.Crop 保证图片填充效果

关键代码：

```kotlin
Image(
    painter = painterResource(hero.imageRes),
    contentDescription = null,
    modifier = Modifier
        .size(72.dp)
        .clip(RoundedCornerShape(8.dp)),
    contentScale = ContentScale.Crop
)
```

最终实现了与官方规格较为一致的卡片布局。

------

# 六、LazyColumn 列表实现

本实验使用 LazyColumn 构建可滚动英雄列表。

代码如下：

```kotlin
LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
)
```

实现特点：

1. LazyColumn 只渲染当前可见项
2. 可以有效优化内存与性能
3. 支持大量列表数据
4. 更适合现代 Compose UI

其中：

- contentPadding 控制整体边距
- spacedBy 控制列表项间距

列表数据通过：

```kotlin
items(heroes)
```

动态生成。

相比传统 RecyclerView，Compose 的 LazyColumn 实现更加简洁直观。

------

# 七、Material 3 主题配置

为了实现更现代化的视觉风格，本实验自定义了：

- 颜色
- 字体
- 形状

## 1. 颜色配置

在 Color.kt 中定义浅色与深色主题颜色。

浅色模式：

- 使用浅绿色与奶白色
- 提升整体柔和感

深色模式：

- 使用深灰背景
- 提高夜间阅读舒适性

------

## 2. 字体配置

本实验使用 Cabin 字体。

字体资源：

```text
cabin_regular.ttf
cabin_bold.ttf
```

在 Type.kt 中配置：

```kotlin
val Cabin = FontFamily(
    Font(R.font.cabin_regular, FontWeight.Normal),
    Font(R.font.cabin_bold, FontWeight.Bold)
)
```

并应用于：

- displayLarge
- displaySmall
- bodyLarge

使应用整体风格更加统一。

------

## 3. 形状配置

在 Shape.kt 中统一配置圆角：

```kotlin
val Shapes = Shapes(
    medium = RoundedCornerShape(16.dp)
)
```

用于：

- Card
- Material 组件

提升了界面现代感。

------

# 八、顶部应用栏实现

本实验使用 Scaffold + CenterAlignedTopAppBar 构建应用结构。

代码如下：

```kotlin
Scaffold(
    topBar = {
        CenterAlignedTopAppBar(...)
    }
)
```

实现效果：

1. 标题居中显示
2. 自动适配状态栏
3. 内容区域不会被顶部栏遮挡

标题使用：

```kotlin
stringResource(R.string.app_name)
```

实现了资源统一管理。

------

# 九、状态栏与导航栏适配

为了实现 edge-to-edge 显示，本实验在 Theme.kt 中进行了系统栏处理。

主要实现：

```kotlin
WindowCompat.setDecorFitsSystemWindows(window, false)
```

并设置：

```kotlin
window.statusBarColor = android.graphics.Color.TRANSPARENT
```

同时根据浅色与深色主题切换状态栏图标颜色：

```kotlin
controller.isAppearanceLightStatusBars = !darkTheme
```

实现效果：

1. 状态栏与应用背景更加融合
2. 深浅模式下图标均清晰可见
3. 提升整体沉浸感

------

# 十、实验中遇到的问题与解决方法

## 1. Theme 名称不一致

问题：

```text
Unresolved reference 'ActivityTheme'
```

原因：

MainActivity 中调用的主题函数名称与 Theme.kt 中定义的不一致。

解决：

统一修改为：

```kotlin
SuperheroesTheme()
```

------

## 2. 字体资源无法识别

问题：

```text
resource font/Cabin-Bold not found
```

原因：

Android 资源文件不能包含大写字母与横线。

解决：

将文件改名为：

```text
cabin_bold.ttf
cabin_regular.ttf
```

------

## 3. Scaffold 括号嵌套错误

问题：

```text
No value passed for parameter 'content'
```

原因：

Compose lambda 括号未正确闭合。

解决：

重新整理 Scaffold 与 TopAppBar 的括号结构。

------

## 4. 图片无法显示

原因：

图片未放入 drawable 目录。

解决：

将资源文件统一放入：

```text
res/drawable/
```

------

# 十一、实验总结

通过本实验，我进一步掌握了：

1. Jetpack Compose 声明式 UI 开发
2. LazyColumn 可滚动列表实现
3. Material 3 自定义主题
4. Kotlin 数据类设计
5. Repository 数据管理
6. Scaffold 页面结构
7. 深色模式适配
8. Android 资源管理规范

相比传统 XML + RecyclerView 开发方式，Compose 的开发效率更高，代码更加简洁，同时也更容易实现现代化 UI 效果。

本实验增强了我对 Android 现代开发框架的理解，也为后续学习更复杂的 Compose 动画与状态管理打下了基础。
