# 课程主题网格应用实验报告
## 一、应用整体结构说明
本应用采用分层设计，将数据层与UI层分离，整体结构清晰规范。数据层包含Topic.kt与DataSource.kt，Topic.kt定义Topic数据类，封装课程主题的核心数据；DataSource.kt提供静态数据源DataSource.topics，作为列表的数据源。UI层包含TopicGridItem、CoursesGrid和MainActivity，TopicGridItem是单个课程卡片的组合项，负责展示图片、名称和课程数；CoursesGrid使用LazyVerticalGrid实现两列网格布局，负责列表的整体展示；MainActivity是应用入口，设置主题并加载CoursesGrid组合项。

## 二、Topic数据类的字段设计与选择理由
数据类设计为data class Topic(@StringRes val nameRes: Int, val courseCount: Int, @DrawableRes val imageRes: Int)。其中nameRes: Int使用@StringRes标注字符串资源ID，支持多语言，避免硬编码；courseCount: Int存储课程数量，为纯数字类型，便于直接显示；imageRes: Int使用@DrawableRes标注图片资源ID，保证资源引用安全。这种设计将所有与课程主题相关的数据封装在一起，符合单一职责原则，便于数据源管理和UI组件复用。

## 三、卡片布局实现思路
卡片使用Card组件作为容器，提供圆角、阴影和点击效果，是Material Design的标准卡片组件。内部采用水平Row布局，水平排列图片和文字信息，左侧为Image组件，加载imageRes资源，设置固定宽高比防止拉伸变形；右侧为Column组件，垂直排列两个Text组件，分别显示课程名称和课程数量，文字区域设置padding，保证内容与卡片边缘有合适间距，提升可读性。

## 四、网格布局实现思路
使用LazyVerticalGrid实现高性能网格列表，核心配置为columns = GridCells.Fixed(2)设置固定两列网格，contentPadding设置列表整体内边距避免内容贴边，verticalArrangement与horizontalArrangement分别设置网格项之间的垂直和水平间距，通过items传入数据源，用TopicGridItem渲染每个条目。LazyVerticalGrid仅渲染屏幕可见项，性能优于传统RecyclerView网格布局。

## 五、遇到的问题与解决过程
问题1：图片拉伸变形，原因是未设置Image的宽高比，图片在不同卡片中被强制拉伸，解决方法是使用Modifier.aspectRatio(1f)或固定宽高，保持图片比例一致。问题2：网格项间距不均，原因是同时设置了contentPadding和Arrangement.spacedBy导致内外边距冲突，解决方法是明确区分列表整体contentPadding和项之间的spacedBy，确保间距统一。问题3：数据类型不匹配，原因是直接传递字符串和图片ID时未使用资源注解，编译无提示但运行报错，解决方法是在Topic类中添加@StringRes和@DrawableRes注解，保证资源引用安全。

### 总结
本课程主题网格应用基于Android Jetpack Compose开发，实现了两列垂直网格展示课程主题卡片的功能，采用数据与UI分离的分层架构，使用LazyVerticalGrid实现高性能列表，通过规范的资源注解和布局适配解决了图片变形、间距不均等问题，符合Material3设计规范，是Compose网格布局与卡片组件的典型实践案例。