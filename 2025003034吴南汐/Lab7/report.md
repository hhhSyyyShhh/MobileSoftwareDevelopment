# 课程网格展示应用实验报告
## 一、实验概述
本实验基于Android Jetpack Compose框架开发了一个课程网格展示应用，通过网格布局呈现不同主题的课程卡片，每张卡片包含主题图片、名称及课程数量等信息，旨在掌握Compose中数据驱动UI、布局嵌套及惰性网格的使用方法。

## 二、应用整体结构说明
应用采用“数据层-UI层”分层设计，核心结构分为三部分，各部分职责清晰、解耦性强：
1. **数据类（Model层）**：`Topic.kt` 定义了 `Topic` 数据类，封装单门课程主题的核心数据（名称资源ID、课程数量、图片资源ID），是UI展示的数据载体。
2. **数据源（Data层）**：`DataSource.kt` 定义了单例对象 `DataSource`，内部包含一个 `topics` 列表，预加载所有课程主题的 `Topic` 实例，为UI层提供统一的数据源。
3. **UI层（Compose组合项）**：`MainActivity.kt` 作为应用入口，通过多个可组合函数（`CoursesGridApp`、`TopicCard`）实现UI渲染：
   - `CoursesGridApp`：核心布局容器，负责构建惰性垂直网格；
   - `TopicCard`：单个课程卡片的组合项，接收 `Topic` 数据并渲染卡片内的图片、文字等元素；
   - 预览函数（`CoursesGridPreview`、`TopicCardPreview`）：用于实时预览UI效果，提升开发效率。

整体组织方式遵循“数据驱动UI”原则：数据源提供 `Topic` 列表 → 网格布局遍历列表 → 为每个 `Topic` 实例渲染对应的卡片，实现数据与UI的联动。

## 三、Topic 数据类的字段设计与选择理由
### 字段设计
```kotlin
data class Topic(
    val nameResId: Int,
    val courseCount: Int,
    val imageResId: Int
)
```
- **nameResId**：存文字的资源ID，不用直接写文字，方便改语言。
- **courseCount**：存数字，直接显示课程数量，简单好算。
- **imageResId**：存图片的资源ID，不用加载复杂图片，直接用本地图。

用**data class**是因为它专门用来存数据，自带常用方法，写起来省事。

## 四、卡片布局实现思路
### 核心组合项使用
卡片布局（`TopicCard`）基于Compose的基础布局组合项嵌套实现，核心使用的组合项包括：
- `Card`：作为卡片容器，提供圆角、阴影（本实验设为0dp）等卡片样式；
- `Row`：卡片内部顶层布局，实现“图片+文字”的水平排列；
- `Box`：包裹图片，用于裁剪图片为圆角；
- `Image`：加载课程主题图片和图标（课程数量左侧的颗粒图标）；
- `Column`：文字区域的垂直布局，实现“主题名称+课程数量行”的纵向排列；
- `Spacer`：用于元素间的间距控制；
- `Text`：展示文字内容（主题名称、课程数量）。


## 五、网格布局实现思路（LazyVerticalGrid 参数配置）
用 **LazyVerticalGrid** 做网格，只显示屏幕上看得到的卡片，不卡手机。

主要设置：
- **columns = GridCells.Fixed(2)**：固定2列。
- **padding(8.dp)**：网格整体留边，不贴屏幕。
- **horizontal/vertical Arrangement**：卡片之间留8dp空隙。
- **items(DataSource.topics)**：把数据列表循环生成卡片。

## 六、遇到的问题与解决过程
### 问题1：图片拉伸变形，无法保持1:1比例
- **现象**：不同尺寸的课程主题图片在卡片中显示时，宽高比不一致，导致图片拉伸或压缩。
- **原因**：图片默认填充`Box`容器，但未限制宽高比，仅设置`size(68.dp)`无法保证比例一致。
- **解决过程**：在`Image`的`modifier`中添加`aspectRatio(1f)`，强制图片宽高比为1:1，并通过`alignment = Alignment.Center`使图片居中显示，避免变形。

### 问题2：卡片内文字与图片垂直对齐不一致
- **现象**：部分卡片的文字区域偏上或偏下，与图片未实现垂直居中。
- **原因**：文字区域`Column`未设置与`Row`的对齐方式，仅依赖自身布局导致对齐偏差。
- **解决过程**：为`Column`添加`align(Alignment.CenterVertically)`修饰符，使其在`Row`中垂直居中，保证图片与文字区域的对齐一致性。

### 问题3：网格卡片间距不均匀，边缘卡片贴边
- **现象**：网格左右两侧的卡片紧贴屏幕边缘，卡片之间的间距也不一致。
- **原因**：仅设置了`horizontalArrangement`和`verticalArrangement`控制内部间距，但未给网格整体添加内边距。
- **解决过程**：为`LazyVerticalGrid`的`modifier`添加`padding(8.dp)`，同时保持`contentPadding`为0dp，既避免卡片贴边，又保证内部间距均匀。

### 问题4：预览函数中Topic实例初始化报错
- **现象**：`TopicCardPreview`中直接使用字符串/图片ID时，预览界面报错。
- **原因**：`Topic`数据类的字段为资源ID（`@StringRes`/`@DrawableRes`），预览时需使用正确的资源引用，而非硬编码数值。
- **解决过程**：在预览函数中传入`R.string.architecture`、`R.drawable.architecture`等资源ID，而非自定义数值，符合Compose预览的资源加载规则。

## 七、实验总结
本实验基于Compose实现了课程网格展示应用，核心掌握了以下要点：
1. 数据层与UI层的解耦设计，通过`Topic`数据类和`DataSource`单例实现数据统一管理；
2. Compose布局嵌套的核心逻辑，通过`Row`/`Column`/`Box`的嵌套实现复杂卡片布局；
3. `LazyVerticalGrid`的参数配置与惰性加载特性，提升网格布局的性能和可读性；
4. Android资源管理规范（字符串/图片资源ID）在Compose中的应用，保证多语言和资源复用。