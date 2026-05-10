# Lab8 构建超级英雄列表应用实验报告

## 一、应用整体结构说明

本项目采用分层模块化设计，整体结构清晰，功能职责划分明确，主要分为以下几个部分：

1. **Model 层**  
   用于定义数据模型与数据源，包括 `Hero` 数据类以及 `HeroesRepository` 静态仓库对象，统一管理应用中的英雄数据。

2. **UI Theme 层**  
   负责全局主题配置，包括 Material3 的颜色方案、字体排版、形状样式等，实现应用整体界面的统一风格。
3. **页面层**
   - `HeroesScreen.kt`：实现英雄列表页面，封装 `LazyColumn` 列表以及单个英雄卡片组件 `HeroItem`。
   - `MainActivity.kt`：作为应用入口，通过 `Scaffold` 组织顶部应用栏与页面主体内容，并加载全局主题。
4. **资源层**  
   包含字符串资源（strings）、图片资源（drawable）以及自定义字体资源（font），用于统一管理界面文本与素材文件。

整个项目遵循了 Jetpack Compose 中“数据与 UI 分离、组件复用、主题统一管理”的开发思想，提高了代码的可维护性与扩展性。

---

## 二、Hero 数据类设计与实现

### 1. 数据类代码

```kotlin
data class Hero(
    @StringRes val nameRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val imageRes: Int
)
```

### 2. 字段说明

- `nameRes`：英雄名称对应的字符串资源 ID
- `descriptionRes`：英雄描述对应的字符串资源 ID
- `imageRes`：英雄头像图片资源 ID

### 3. 设计理由

1. 使用 `data class` 能够自动生成 `equals()`、`hashCode()`、`toString()` 等方法，适合作为列表实体模型。
2. 通过 `@StringRes` 与 `@DrawableRes` 注解，可以在编译阶段校验资源类型，降低资源传递错误的风险。
3. 数据类中仅保存资源 ID，而不是直接保存字符串或图片对象，符合 Android 的资源管理规范，便于后续实现多语言适配与主题切换。

---

## 三、HeroesRepository 数据源组织方式

1. 使用 `object` 单例模式实现 `HeroesRepository` 仓库类，避免重复实例化，适用于静态模拟数据的管理。
2. 仓库内部定义只读 `heroes` 集合，并在初始化时统一创建多个 `Hero` 对象。
3. UI 层仅通过 `HeroesRepository.heroes` 获取数据，实现了数据层与界面层的解耦。
4. 后续若需要接入网络请求或本地数据库，仅需修改 Repository 内部实现，而无需改动 UI 层代码，具有良好的扩展性。

---

## 四、英雄列表项布局实现思路

1. 外层采用 Material3 的 `Card` 组件作为卡片容器，并结合统一主题设置圆角与阴影效果。
2. 卡片内部使用 `Row` 实现水平布局：
   - 左侧为文本信息区域
   - 右侧为英雄头像区域
3. 左侧通过 `Column` 垂直排列英雄名称与描述文本，并分别应用主题中的文字样式。
4. 右侧头像使用固定大小的 `Box` 包裹 `Image`：
   - 尺寸固定为 `72dp`
   - 使用 `clip` 设置圆角裁剪
   - 使用 `ContentScale.Crop` 保证图片居中裁剪填充
5. 布局严格遵循设计规范：
   - 卡片内边距：`16dp`
   - 文本与图片间距：`16dp`
   - 图片圆角：`8dp`
   - 名称文本使用 `displaySmall`
   - 描述文本使用 `bodyLarge`

---

## 五、LazyColumn 列表实现与间距配置

1. 使用 `LazyColumn` 实现可滚动列表，仅渲染当前可见区域内容，相比普通 `Column` 具有更好的性能。
2. 使用：

```kotlin
contentPadding = PaddingValues(16.dp)
```

设置列表整体边距，使页面布局更加美观。

3. 使用：

```kotlin
verticalArrangement = Arrangement.spacedBy(8.dp)
```

设置列表项之间的垂直间距。

4. 通过 `items(heroes)` 遍历数据源，并复用 `HeroItem` 组件生成列表内容。

5. 接收 `Scaffold` 提供的 `innerPadding`，避免列表内容被顶部应用栏遮挡。

---

## 六、Material3 主题配置说明

### 1. 颜色配置

- 自定义浅色与深色两套 ColorScheme，符合 Material3 设计规范。
- 应用能够根据系统深浅色模式自动切换主题颜色。

### 2. 形状配置

统一定义圆角风格：

- `small = 8dp`
- `medium = 16dp`

分别用于图片与卡片组件，实现整体视觉统一。

### 3. 字体排版

- 引入自定义 Cabin 字体，并封装为 `FontFamily`
- 统一定义 Typography：
  - `displayLarge`：顶部标题
  - `displaySmall`：英雄名称
  - `bodyLarge`：英雄描述
- 全局文本均通过主题 Typography 管理，避免硬编码字体与字号。

### 4. 全局主题封装

通过自定义 `SuperheroesTheme`：

- 统一注入 `colorScheme`
- 配置 `typography`
- 配置 `shapes`

同时实现状态栏颜色与深浅色模式的自动适配。

---

## 七、顶部应用栏与状态栏处理

### 1. 顶部应用栏

- 使用 `CenterAlignedTopAppBar` 构建居中标题栏。
- 标题文本使用应用名称资源，并应用 `displayLarge` 字体样式。
- 借助 `Scaffold` 管理顶部栏与页面主体布局，自动处理内边距。

### 2. 状态栏适配

1. 使用 `SideEffect` 获取当前 Activity 的窗口对象。
2. 动态设置状态栏背景颜色，使其与应用主题保持一致。
3. 根据深浅色模式自动切换状态栏图标颜色，保证内容清晰可见。
4. 实现应用界面与系统状态栏之间的视觉统一。

---

## 八、实验过程中遇到的问题与解决方案

### 1. 资源文件命名错误

**问题：**  
字体与图片文件名包含大写字母或空格，导致项目编译失败。

**解决方案：**  
统一修改为“小写字母 + 下划线”格式，符合 Android 资源命名规范。

---

### 2. 图片显示变形

**问题：**  

不同尺寸图片导致列表卡片高度不统一。

**解决方案：**

- 固定图片容器大小为 `72dp`
- 使用 `ContentScale.Crop`
- 设置统一圆角裁剪

从而保证图片显示效果一致。

---

### 3. 列表内容被顶部栏遮挡

**问题：**  
`LazyColumn` 顶部内容被 `TopAppBar` 覆盖。

**解决方案：**  
将 `Scaffold` 提供的 `innerPadding` 传递给列表组件，实现自动避让。

---

### 4. 深色模式下状态栏文字不可见

**问题：**  
深色主题下状态栏图标与背景颜色对比不足。

**解决方案：**  
使用 `WindowCompat` 动态切换状态栏图标亮暗模式，增强可读性。

---

### 5. 自定义字体未生效

**问题：**  
导入字体后界面字体没有变化。

**解决方案：**

- 正确放置字体文件至 `res/font`
- 在 `Type.kt` 中绑定字体文件与对应字重

最终成功实现全局字体替换。

---

## 九、实验总结

本次实验综合运用了 Kotlin 数据类、Repository 数据管理、Jetpack Compose 布局系统、`LazyColumn` 列表、Material3 自定义主题、资源管理以及状态栏适配等知识点。

通过本次实验，成功实现了一个结构清晰、界面规范的超级英雄列表应用，并完成了深浅色主题自动切换、组件样式统一以及响应式布局等功能。

同时，本次实验进一步加深了对 Jetpack Compose 模块化开发思想的理解，掌握了 Material3 在颜色、字体、形状等方面的完整主题配置流程，也提升了对 Android UI 架构设计与组件化开发的实践能力。