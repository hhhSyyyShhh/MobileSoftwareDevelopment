# Lab8 构建超级英雄列表应用 实验报告

## 一、应用整体结构说明
本项目采用**分层模块化结构**，将数据层、界面层、主题层完全分离：
1. **model 层**：存放 Hero 数据类、HeroesRepository 统一管理实体数据与模拟数据。
2. **ui/theme 层**：自定义 Material3 颜色、形状、字体排版、全局主题，统一控制全局 UI 样式。
3. **页面层**
   - `HeroesScreen.kt`：封装英雄列表 `LazyColumn` 和单个列表项 `HeroItem` 可组合函数。
   - `MainActivity.kt`：程序入口，通过 `Scaffold` 整合顶部应用栏与列表主体，承载全局主题。
4. **资源层**：strings 字符串资源、drawable 英雄图片资源、font 自定义 Cabin 字体资源。


## 二、Hero 数据类设计与理由
### 1. 数据类代码
```kotlin
data class Hero(
    @StringRes val nameRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val imageRes: Int
)
```

### 2. 字段说明
- `nameRes`：英雄名称字符串资源 ID
- `descriptionRes`：英雄描述字符串资源 ID
- `imageRes`：英雄头像图片资源 ID

### 3. 设计理由
1. 使用资源 ID 而非硬编码，便于多语言适配与资源统一管理。
2. 添加 @StringRes / @DrawableRes 注解，提升代码类型安全性，避免资源类型错误。
3. 符合 Compose 开发最佳实践，适配资源加载逻辑与全局主题适配。

## 三、HeroesRepository 数据源组织方式
1. 采用 `object` 单例对象作为仓库类，全局唯一、无需实例化，适合静态模拟数据源。
2. 内部定义 `heroes` 只读 List，一次性初始化 6 个 Hero 对象，绑定对应字符串和图片资源。
3. 数据集中托管在 Repository，UI 层只需直接调用 `HeroesRepository.heroes`，实现**数据与视图解耦**。
4. 后续如需改为网络请求、本地数据库，只需修改 Repository 内部实现，UI 层无需改动。

## 四、英雄列表项布局实现思路
1. 外层使用 **Material3 Card** 作为卡片容器，采用主题中等圆角、轻微阴影，符合设计规格。
2. 卡片内部采用 **Row 水平布局**：左侧文字 Column、右侧正方形头像。
3. 左侧 Column 垂直排列：英雄名称 + 英雄描述，分别绑定主题预设文字样式。
4. 右侧使用固定 **72dp 正方形 Box** 包裹 Image，设置 `clip` 裁剪为 8dp 小圆角，`ContentScale.Crop` 居中裁剪填满区域。
5. 严格遵循设计规格：
   - 卡片内边距 16dp
   - 图片大小：72.dp × 72.dp
   - 图片圆角：8.dp
   - 英雄名称使用 `displaySmall`、描述使用 `bodyLarge`

## 五、LazyColumn 列表实现和间距配置
1. 使用 `LazyColumn` 实现高性能可滚动列表，仅加载屏幕可见项，优于普通 Column。
2. 通过 `contentPadding` 设置列表整体四周内边距，避免内容贴边。
3. 通过 `verticalArrangement = Arrangement.spacedBy(8.dp)` 设置列表项之间垂直间距 8dp。
4. 使用 `items(heroes)` 遍历数据源，自动循环构建列表项。
5. 接收 Scaffold 传入的 `innerPadding`，避免列表内容被顶部应用栏遮挡。

## 六、Material 主题配置说明
### 1. 颜色配置
- 自定义浅色/深色两套完整配色方案，系统可根据设置自动切换主题。
- 使用自定义字体，配置常规、粗体两种字重，统一全局文字风格。

### 2. 形状配置
- 定义 small 8dp、medium 16dp 圆角，分别用于图片圆角和卡片圆角，全局统一控件弧度。

### 3. 字体排版
- 引入自定义 Cabin 字体（常规、粗体），封装为 FontFamily。
- 预设三级文字样式：
  - displayLarge：顶部应用栏标题
  - displaySmall：英雄名称
  - bodyLarge：英雄描述
- 全局文本统一使用主题 Typography，不用硬编码字号和字体。

### 4. 全局主题
- 封装 `SuperheroesTheme` 可组合函数，统一注入 colorScheme、typography、shapes。
- 配置状态栏适配，自动根据深浅色模式切换状态栏图标明暗。

## 七、顶部应用栏和状态栏处理说明
### 1. 顶部应用栏
- 使用 `CenterAlignedTopAppBar` 实现居中标题栏。
- 标题使用应用名称字符串资源，字体采用主题 `displayLarge` 大标题样式。
- 借助 `Scaffold` 嵌套顶部栏与列表内容，自动管理内边距。

### 2. 状态栏适配
1. 通过 `SideEffect` 获取当前 Activity 窗口。
2. 动态设置状态栏颜色为主题主色调。
3. 根据深色/浅色主题自动切换状态栏文字明暗，保证文字清晰可读。
4. 实现应用界面与系统状态栏视觉融合，无突兀分割色块。

## 八、遇到的问题与解决过程
1. **资源文件名报错**
   - 问题：字体、图片文件名含大写和空格，编译报错。
   - 解决：统一改为小写+下划线命名，符合 Android 资源命名规范。

2. **找不到 model 包相关类原因**
   - 问题：未创建 Hero 与 HeroesRepository 数据文件。
   - 解决：按照项目结构创建对应文件，补全数据模型代码。

3. **图标资源导入失败原因**
   - 问题：Material 图标库依赖缺失。
   - 解决：使用文字 / Emoji 按钮替代图标，零依赖实现切换功能。


## 九、实验总结
通过本次实验，我熟练掌握了以下核心知识点：
Compose 列表、线性布局、卡片等基础组件的使用方法。
数据类设计与静态数据源（Repository）的封装实现。
Material 3 主题自定义（颜色、字体、形状）配置。
Compose 状态管理与深浅模式切换实现。
Scaffold + TopAppBar 标准化界面结构搭建。
Android 资源文件规范管理与多语言适配思路。

---