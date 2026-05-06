# Lab7 实验报告：构建可滚动课程网格应用（Courses App）

------

## 一、应用整体结构说明

本项目采用典型的 **数据层 + UI 层分离结构**，整体架构如下：

### 1. 数据层（Model + DataSource）

- **Topic.kt（数据类）**
    - 定义课程主题的数据结构
    - 包含主题名称、课程数量、图片资源三个字段
- **DataSource.kt（数据源）**
    - 使用 `object` 单例模式集中管理静态数据
    - 提供 `List<Topic>` 作为 UI 数据输入

------

### 2. UI 层（Compose）

- **MainActivity.kt**
    - 应用入口
    - 调用 `CoursesGrid()` 渲染主界面
- **CoursesGrid()**
    - 使用 `LazyVerticalGrid` 构建两列网格
    - 负责整体布局与滚动
- **TopicGridItem()**
    - 单个卡片组件
    - 展示图片 + 文字 + 图标

------

### 3. 资源层（Resources）

- `strings.xml`：提供主题名称
- `drawable/`：提供图片和 `ic_grain.xml` 图标

------

## 二、Topic 数据类设计

```kotlin
data class Topic(
    @StringRes val name: Int,
    val courseCount: Int,
    @DrawableRes val imageRes: Int
)
```

### 字段设计说明：

| 字段        | 类型               | 设计理由                          |
| ----------- | ------------------ | --------------------------------- |
| name        | `@StringRes Int`   | 使用资源 ID，支持多语言和统一管理 |
| courseCount | `Int`              | 表示课程数量，属于纯数据          |
| imageRes    | `@DrawableRes Int` | 图片资源 ID，便于 Compose 加载    |

### 设计优势：

- **类型安全**（使用注解约束资源类型）
- **解耦 UI 与数据**
- **符合 Android 资源管理规范**

------

## 三、卡片布局实现思路

### 1. 组件结构

卡片采用如下嵌套结构：

```text
Card
 └── Row
      ├── Image
      └── Column
           ├── Text（标题）
           └── Row（图标 + 数字）
```

------

### 2. 关键布局组件

| 组件     | 作用         |
| -------- | ------------ |
| `Card`   | 提供卡片容器 |
| `Row`    | 实现左右布局 |
| `Column` | 实现上下排列 |
| `Image`  | 显示主题图片 |
| `Text`   | 显示文字     |
| `Icon`   | 显示装饰图标 |

------

### 3. 布局实现关键点

#### （1）图片尺寸控制

```kotlin
Modifier.size(68.dp)
```

保证图片为 1:1 正方形。

------

#### （2）内边距控制

```kotlin
Modifier.padding(16.dp)
```

确保符合 UI 规范。

------

#### （3）间距控制

```kotlin
Spacer(modifier = Modifier.height(8.dp))
Spacer(modifier = Modifier.width(8.dp))
```

------

#### （4）对齐方式

```kotlin
Row(verticalAlignment = Alignment.CenterVertically)
```

确保图标与文字垂直居中对齐。

------

### 4. 样式控制

```kotlin
MaterialTheme.typography.bodyMedium
MaterialTheme.typography.labelMedium
```

符合 Material Design 规范。

------

## 四、网格布局实现思路

### 1. 使用 LazyVerticalGrid

```kotlin
LazyVerticalGrid(
    columns = GridCells.Fixed(2)
)
```

------

### 2. 参数说明

#### （1）列数控制

```kotlin
columns = GridCells.Fixed(2)
```

👉 固定两列布局

------

#### （2）整体边距

```kotlin
contentPadding = PaddingValues(8.dp)
```

👉 控制网格四周留白

------

#### （3）卡片间距

```kotlin
verticalArrangement = Arrangement.spacedBy(8.dp)
horizontalArrangement = Arrangement.spacedBy(8.dp)
```

👉 控制卡片之间间距

------

### 3. 数据绑定

```kotlin
items(DataSource.topics)
```

将数据源绑定到 UI，实现列表渲染。

------

### 4. 滚动实现

`LazyVerticalGrid` 自带懒加载与滚动能力：

- 仅渲染可见区域
- 自动支持垂直滑动

------

## 五、遇到的问题与解决过程

### 问题一：模拟器无法运行（Unavailable device）

**原因：**

- 使用了 API 36.1（未安装或不可用）

**解决：**

- 新建模拟器（Pixel 5 + API 33）
- 或使用真机运行

------

### 问题二：找不到资源（R.string / R.drawable 报错）

**原因：**

- 未添加 `strings.xml`
- 图片文件名不匹配

**解决：**

- 补全 strings.xml
- 确保 drawable 文件名全小写且一致

------

### 问题三：ic_grain.xml 缺失

**原因：**

- 未创建图标资源

**解决：**

- 使用 Vector Asset 创建 `ic_grain.xml`

------

### 问题四：布局错位

**原因：**

- 未设置间距或对齐方式

**解决：**

- 使用 `Spacer`
- 设置 `Alignment.CenterVertically`

------

## 六、实验总结

通过本实验，我掌握了：

- 使用 **数据类（Data Class）** 建模数据
- 使用 **object 单例** 管理数据源
- 使用 **LazyVerticalGrid** 构建网格布局
- 熟练使用 **Row / Column / Card / Image / Text**
- 掌握 **Compose 布局与间距控制方法**

同时，本实验提升了：

- 阅读文档能力
- 独立排查问题能力
- UI 规范实现能力

------

## 七、实验结果

- 应用成功运行，无崩溃
- 正确显示 24 个课程主题
- 使用两列网格布局
- 支持垂直滚动
- UI 与设计规范一致

------

（附：运行截图 screenshot.png）

------