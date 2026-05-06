# Lab8 实验报告：Superheroes 超级英雄列表应用
## 一、应用整体结构说明
本应用基于 Android Jetpack Compose 开发，实现可滚动的超级英雄列表，支持浅色/深色主题自动切换、沉浸式状态栏与标准化 Material 3 样式。

### 项目结构
```plaintext
com.example.superheroes/
├── MainActivity.kt          
├── HeroesScreen.kt          
├── model/
│   ├── Hero.kt             
│   └── HeroesRepository.kt  
└── ui/theme/
    ├── Color.kt             
    ├── Type.kt             
    ├── Shape.kt             
    └── Theme.kt            
```

### 核心功能
- 展示 6 个超级英雄的名称、描述、头像图片
- LazyColumn 实现高性能可滚动列表
- 自定义 Material 3 颜色、字体、形状
- 顶部应用栏居中标题 + 深浅色切换
- 全屏沉浸式状态栏适配

## 二、Hero 数据类字段设计与理由
```kotlin
data class Hero(
    @StringRes val nameRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val imageRes: Int
)
```

### 设计说明
1. **使用资源 ID 而非硬编码**
- `@StringRes` 与 `@DrawableRes` 提供编译期类型检查，避免资源错误
- 便于多语言、资源替换与统一管理

2. **字段职责明确**
- `nameRes`：英雄名称字符串资源
- `descriptionRes`：英雄描述字符串资源
- `imageRes`：英雄头像图片资源

3. **data class**
- 轻量数据载体，自动生成 equals、toString、componentN 等方法
- 适配 Compose 数据驱动 UI 模型

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

### 组织思路
1. **单例 object**
- 全局唯一，无需实例化，直接访问数据
- 适合静态本地数据场景

2. **不可变 List**
- 使用 `listOf()` 保证数据不可修改，安全稳定

3. **集中管理**
- 所有英雄数据统一维护，便于扩展、修改与维护

4. **资源驱动**
- 全部从 `strings.xml`、drawable 加载，符合 Android 资源规范

## 四、英雄列表项布局实现思路
列表项采用 `Card + Row + Column + Box` 组合实现，完全对齐实验例图规格。

### 实现结构
1. **Card**
- 作为列表项容器，提供圆角、背景、阴影
- 高度固定 72dp，宽度充满父布局

2. **Row**
- 水平排布：左侧文字 + 右侧图片
- 全局内边距 16dp，文字与图片间距 16dp

3. **Column**
- 垂直排布英雄名称与描述
- 使用 `weight(1f)` 占满剩余空间

4. **Box + Image**
- 固定 72dp 正方形，圆角 8dp
- `ContentScale.Crop` 保证图片填充且不变形

### 布局规范
- 卡片圆角：16dp
- 图片圆角：8dp
- 名称：displaySmall（粗体）
- 描述：bodyLarge（常规）
- 所有间距、尺寸与例图完全一致

## 五、LazyColumn 列表实现和间距配置说明
```kotlin
LazyColumn(
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    items(heroes) { hero ->
        HeroItem(hero = hero)
    }
}
```

### 说明
1. **LazyColumn**
- 惰性加载，只渲染可见项，滚动流畅、性能高

2. **contentPadding**
- 列表整体四周内边距 16dp
- 避免内容紧贴屏幕边缘

3. **spacedBy(8.dp)**
- 列表项之间垂直间距 8dp
- 与实验例图间距规范一致

4. **items(heroes)**
- 遍历数据并自动构建列表项
- 代码简洁、可维护性高

## 六、Material 主题配置说明（颜色、字体、形状）
### 1. 颜色（Color.kt）
- 提供浅色主题 + 深色主题两套配色
- 颜色严格按照实验例图配置
- 背景、卡片、文字、顶部栏自动适配深浅色
- 关闭动态颜色，保证全设备显示一致

### 2. 字体与排版（Type.kt）
- 使用 Cabin 字体（Regular + Bold）
- displayLarge：顶部标题 32sp 粗体
- displaySmall：英雄名称 20sp 粗体
- bodyLarge：英雄描述 16sp 常规
- 行高、字间距遵循 Material 3 规范

### 3. 形状（Shape.kt）
```kotlin
val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),   // 图片圆角
    medium = RoundedCornerShape(16.dp)  // 卡片圆角
)
```
- 统一全局圆角风格
- 与例图视觉效果完全一致

## 七、顶部应用栏和状态栏处理说明
### 顶部应用栏
- 使用 `CenterAlignedTopAppBar` 实现居中标题
- 背景色使用 `primaryContainer`
- 标题样式 `displayLarge`
- 右侧添加深浅主题切换开关

### 状态栏适配
- 启用 `enableEdgeToEdge` 全屏显示
- 状态栏设为透明，界面延伸至状态栏
- 根据深浅主题自动切换状态栏图标明暗
- 无断层、无突兀色块，视觉统一

## 八、遇到的问题与解决过程
1. **SuperheroesTheme 标红无法找到**
- 原因：包名修改后导入路径不匹配
- 解决：统一包名为 `com.example.superheroes`，修正主题导入

2. **Typography / Shapes 标红**
- 原因：未创建对应文件或未导入
- 解决：完成 Shape.kt、Type.kt 并正确导入

3. **字体资源 R.font.cabin_regular 报错**
- 原因：未添加字体文件或命名不规范
- 解决：放入 `cabin_regular.ttf`、`cabin_bold.ttf` 或使用默认字体

4. **深浅色切换不生效 / 颜色错乱**
- 原因：开启动态颜色覆盖自定义主题
- 解决：关闭 `dynamicColor`，使用固定配色

5. **布局比例与例图不一致**
- 原因：边距、高度、圆角设置偏差
- 解决：统一使用 72dp 卡片高度、16dp 内边距、8dp 间距、16dp 卡片圆角

## 九、实验总结
通过本次实验，我掌握了以下内容：
- 使用 Kotlin 数据类与资源 ID 封装列表项数据
- 使用 Repository 单例管理静态数据源
- 使用 LazyColumn 实现高效可滚动列表
- 使用 Compose 标准组件完成复杂列表项布局
- 自定义 Material 3 主题（颜色、字体、形状）
- 实现深浅色主题切换
- 完成顶部应用栏与沉浸式状态栏适配


