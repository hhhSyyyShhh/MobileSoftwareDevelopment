# Lab7 可滚动课程网格应用 实验报告

## 1. 应用整体结构说明
本应用采用 **数据层 + UI层** 分离的结构设计，层次清晰、职责明确：

- **数据类（Topic.kt）**：用于封装单个课程主题的信息，是整个应用的数据模型。
- **数据源（DataSource.kt）**：使用单例 object 统一管理所有24个课程数据，提供给界面使用，与UI完全解耦。
- **UI组合项**：
  - CoursesApp：根组合项，负责展示整体网格布局。
  - TopicCard：单个课程卡片组合项，负责展示图片、主题名称、课程数量。
  - LazyVerticalGrid：实现可滚动的两列网格列表。

数据流向：数据源 → 网格布局 → 循环渲染卡片，结构简洁规范。

---

## 2. Topic 数据类的字段设计与选择理由
Topic 是一个数据类，包含3个字段：

1. **nameRes：@StringRes Int**
   存储课程名称对应的字符串资源ID，
   理由：使用资源ID而不是硬编码字符串，符合 Android 开发规范，支持多语言，更安全规范。

2. **courseCount：Int**
   存储该主题下的课程数量，
   理由：课程数量是纯数字，使用 Int 类型最直接、高效。

3. **imageRes：@DrawableRes Int**
   存储课程图片的资源ID，
   理由：通过资源ID加载图片，便于管理 drawable 资源，代码更规范。

三个字段完整表示一张课程卡片所需的全部信息，结构简洁、功能完整。

---

## 3. 卡片布局实现思路（组合项 + 嵌套结构）
卡片使用 **Material3 + Compose 标准布局组件** 实现：

1. **Card**：最外层容器，提供圆角和卡片阴影效果。
2. **Row**：横向布局，将卡片分为 **左侧图片 + 右侧文字** 两部分。
3. **Image**：显示课程图片，设置固定大小 68dp × 68dp，保持正方形比例。
4. **Column**：右侧文字区域垂直布局，内部放两个部分：
   - 第一部分：Text 显示课程名称（bodyMedium 样式）
   - 第二部分：Row 水平放置图标 + 课程数量（labelMedium 样式）

布局嵌套结构：
Card → Row → Image + Column → Text + Row(Image + Text)

所有间距、内边距、尺寸严格按照实验要求实现，界面整齐规范。

---

## 4. 网格布局实现思路（LazyVerticalGrid 参数说明）
使用 **LazyVerticalGrid** 实现可滚动两列网格：

关键参数：
1. **columns = GridCells.Fixed(2)**
   固定显示 **2列** 网格。

2. **contentPadding = PaddingValues(8.dp)**
   给整个网格设置 **四周8dp的内边距**，让网格不贴紧屏幕边缘。

3. **horizontalArrangement = Arrangement.spacedBy(8.dp)**
   卡片之间 **水平间距 8dp**。

4. **verticalArrangement = Arrangement.spacedBy(8.dp)**
   卡片之间 **垂直间距 8dp**。

LazyVerticalGrid 自带懒加载和滚动能力，数据多时不会卡顿，性能高效。

---

## 5. 遇到的问题与解决过程
1. **问题：代码报错 Unresolved reference: ic_grain**
   原因：没有将 ic_grain.xml 图标文件放入 drawable 文件夹。
   解决：把 ic_grain.xml 复制到 res/drawable，错误消失。

2. **问题：图片加载不出来/闪退**
   原因：图片文件名与代码不一致，或未放入 drawable。
   解决：将24张课程图片全部放入 drawable，并保证文件名小写一致。

3. **问题：卡片布局混乱、间距不对**
   原因：padding 和 spacing 数值设置错误。
   解决：按照实验要求统一设置 8dp、16dp 间距，界面恢复正常。

4. **问题：LazyVerticalGrid 无法使用**
   原因：缺少依赖或未导包。
   解决：使用最新版 Compose，自动包含 foundation 库，即可正常使用网格。

---

## 实验总结
本次实验成功完成了可滚动课程网格应用，掌握了数据类、数据源、LazyVerticalGrid、卡片布局、图片与文字排版等技能，界面规范、功能完整，达到实验全部要求。