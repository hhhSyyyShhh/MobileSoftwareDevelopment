# Lab7 Superheroes 实验报告

## 1. 应用整体结构说明

本应用采用 MVVM 架构模式，使用 Jetpack Compose 构建 UI。主要包含以下模块：

```
app/src/main/java/com/example/hwang/
├── MainActivity.kt           # 应用入口，组合 Scaffold 和列表
├── HeroesScreen.kt          # 英雄列表和列表项组合函数
├── model/
│   ├── Hero.kt              # 英雄数据类
│   └── HeroesRepository.kt  # 英雄静态数据源
└── ui/theme/
    ├── Color.kt            # 自定义颜色
    ├── Shape.kt            # 自定义形状
    ├── Theme.kt           # Material 主题和系统栏处理
    └── Type.kt            # Cabin 字体与文字样式
```

## 2. Hero 数据类字段设计与理由

```kotlin
data class Hero(
    @StringRes val nameRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val imageRes: Int
)
```

**字段说明：**
- `nameRes`: 英雄名称字符串资源 ID，使用 `@StringRes` 注解确保传入有效的字符串资源
- `descriptionRes`: 英雄描述字符串资源 ID，便于国际化管理和复用
- `imageRes`: 英雄图片资源 ID，使用 `@DrawableRes` 注解确保传入有效的图片资源

**设计理由：** 使用资源 ID 而非直接字符串，便于应用国际化、主题切换和资源替换，同时注解可以提供编译时类型检查。

## 3. HeroesRepository 数据源组织方式

使用单例对象 `HeroesRepository` 集中管理所有英雄数据：

```kotlin
object HeroesRepository {
    val heroes = listOf(
        Hero(nameRes = R.string.hero1, descriptionRes = R.string.description1, imageRes = R.drawable.android_superhero1),
        // ... 共6个英雄
    )
}
```

**优点：**
- 单一数据源，便于维护和修改
- 延迟初始化，节省内存
- 全局可访问，无需传递数据

## 4. 英雄列表项布局实现思路

使用 `Card` 组件包裹内容，水平排列图片和文字：

```kotlin
Card(shape = MaterialTheme.shapes.medium) {
    Row(modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = stringResource(hero.nameRes), style = MaterialTheme.typography.displaySmall)
            Text(text = stringResource(hero.descriptionRes), style = MaterialTheme.typography.bodyLarge)
        }
        Image(painter = painterResource(hero.imageRes), contentScale = ContentScale.Crop)
    }
}
```

**关键点：**
- 使用 `weight(1f)` 让文字区域占满剩余空间
- 图片使用 `Box` + `clip(RoundedCornerShape(8.dp))` 实现圆角
- `ContentScale.Crop` 让图片填满容器

## 5. LazyColumn 列表实现和间距配置

```kotlin
LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    items(heroes) { hero -> HeroItem(hero = hero) }
}
```

**间距配置：**
- `contentPadding = PaddingValues(16.dp)`: 列表四周留白 16dp
- `verticalArrangement = Arrangement.spacedBy(8.dp)`: 相邻列表项间距 8dp
- 结合规格图的 16dp 内边距和 8dp 间距要求

## 6. Material 主题配置

### 颜色 (Color.kt)
自定义了完整的 Material 3 颜色方案，包括：
- 主色：绿色系 (#006C4C)
- 辅助色：灰绿色系 (#4D6357)
- 背景色：浅色/深色模式区分
- 表面变体色用于卡片背景

### 字体 (Type.kt)
使用 Cabin 字体族：
```kotlin
val Cabin = FontFamily(
    Font(R.font.cabin_regular, FontWeight.Normal),
    Font(R.font.cabin_bold, FontWeight.Bold)
)

val Typography = Typography(
    displayLarge = TextStyle(fontFamily = Cabin, fontWeight = FontWeight.Bold, fontSize = 57.sp),
    displaySmall = TextStyle(fontFamily = Cabin, fontWeight = FontWeight.Bold, fontSize = 36.sp),
    bodyLarge = TextStyle(fontFamily = Cabin, fontWeight = FontWeight.Normal, fontSize = 16.sp),
    // ...
)
```

### 形状 (Shape.kt)
```kotlin
val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(16.dp)
)
```

## 7. 顶部应用栏和状态栏处理

### 顶部应用栏
使用 `Scaffold` + `CenterAlignedTopAppBar`：

```kotlin
Scaffold(
    topBar = {
        CenterAlignedTopAppBar(
            title = { Text(text = stringResource(R.string.app_name), style = MaterialTheme.typography.displayLarge) }
        )
    }
) { innerPadding -> HeroesList(modifier = Modifier.padding(innerPadding)) }
```

### 状态栏处理
在 Theme.kt 中使用 `SideEffect` 处理：

```kotlin
val view = LocalView.current
if (!view.isInEditMode) {
    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = colorScheme.primary.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
    }
}
```

- 状态栏颜色跟随主题主色
- 根据深色/浅色模式调整状态栏图标颜色

## 8. 遇到的问题与解决过程

1. **旧项目文件冲突**
   - 问题：原项目包含旧的 DataSource.kt、Topic.kt 等文件导致编译错误
   - 解决：删除不再使用的旧文件

2. **实验性 API 警告**
   - 问题：CenterAlignedTopAppBar 是实验性 API
   - 解决：添加 @OptIn(ExperimentalMaterial3Api::class) 注解

3. **Image 导入问题**
   - 问题：使用了完整路径 `androidx.compose.foundation.Image`
   - 解决：添加正确的 import 语句

4. **字体文件缺失**
   - 问题：需要 Cabin 字体文件
   - 解决：从 Lab8 原始资源复制字体文件到 font 目录

## 总结

本次实验成功实现了 Superheroes 列表应用，包含：
- 6 个超级英雄数据
- 自定义 Material 3 主题（颜色、字体、形状）
- 可滚动的英雄列表
- 顶部应用栏
- 浅色/深色主题支持