# Lab7：构建可滚动课程网格应用实验报告

## 应用整体结构说明

本应用采用了清晰的三层结构：

1. **数据层**：包含 `Topic.kt` 数据类和 `DataSource.kt` 数据源
2. **UI层**：包含 `MainActivity.kt` 中的组合项
3. **资源层**：包含字符串资源和图片资源

### 数据层
- `Topic.kt`：定义了课程主题的数据模型
- `DataSource.kt`：使用 `object` 单例模式提供静态数据

### UI层
- `MainActivity.kt`：包含 `CoursesApp` 主组合项和 `TopicCard` 卡片组合项

## Topic 数据类的字段设计与选择理由

### 字段设计

| 字段 | 类型 | 说明 | 选择理由 |
|------|------|------|----------|
| `name` | `@StringRes Int` | 主题名称的字符串资源ID | 使用资源ID而非硬编码字符串，便于国际化和维护 |
| `availableCourses` | `Int` | 可用课程数量 | 直接使用整型存储课程数量，符合数据类型语义 |
| `imageResId` | `@DrawableRes Int` | 主题图片的资源ID | 使用资源ID引用图片，便于管理和访问 |

### 选择理由

1. **使用 `@StringRes` 和 `@DrawableRes` 注解**：确保类型安全，避免运行时错误
2. **使用资源ID**：便于应用主题切换和资源管理
3. **数据类**：自动生成 `equals()`、`hashCode()`、`toString()` 等方法，简化代码

## 卡片布局实现思路

### 使用的组合项

1. **Card**：提供卡片式容器，带有阴影效果
2. **Row**：水平排列图片和文字内容
3. **Column**：垂直排列主题名称和课程数量信息
4. **Image**：显示主题图片和装饰图标
5. **Text**：显示主题名称和课程数量
6. **Spacer**：提供间距

### 布局嵌套结构

```
Card
└── Row
    ├── Image (主题图片)
    └── Column
        ├── Text (主题名称)
        ├── Spacer
        └── Row
            ├── Image (装饰图标)
            ├── Spacer
            └── Text (课程数量)
```

### 关键参数配置

- 图片尺寸：宽高均为 68dp，使用 `contentScale = ContentScale.Crop` 确保图片填充
- 文字区域内边距：16dp
- 主题名称与图标行间距：8dp
- 图标与课程数量间距：8dp
- 字体样式：主题名称使用 `bodyMedium`，课程数量使用 `labelMedium`

## 网格布局实现思路

### LazyVerticalGrid 参数配置

1. **columns**：设置为 `GridCells.Fixed(2)`，实现两列布局
2. **contentPadding**：设置为 `PaddingValues(8.dp)`，控制网格四周的留白
3. **horizontalArrangement**：设置为 `Arrangement.spacedBy(8.dp)`，控制水平方向卡片间距
4. **verticalArrangement**：设置为 `Arrangement.spacedBy(8.dp)`，控制垂直方向卡片间距
5. **items**：使用 `DataSource.topics` 作为数据源，为每个主题创建卡片

### 实现效果

- 两列网格布局，自适应不同屏幕尺寸
- 支持垂直滚动，可查看所有 24 个课程主题
- 卡片间距均匀，视觉效果整洁

## 遇到的问题与解决过程

### 问题 1：图片资源引用

**问题**：在 `DataSource.kt` 中直接使用 `R.drawable.architecture` 等资源ID时，可能会出现编译错误。

**解决方法**：确保项目中已正确添加所有图片资源到 `res/drawable/` 目录，并且资源名称与代码中引用的一致。

### 问题 2：网格布局间距

**问题**：初始实现时，卡片之间的间距不符合设计要求。

**解决方法**：同时使用 `contentPadding` 和 `Arrangement.spacedBy` 参数，分别控制网格四周的留白和卡片之间的间距。

### 问题 3：图片显示效果

**问题**：图片显示时可能会出现拉伸或变形。

**解决方法**：使用 `contentScale = ContentScale.Crop` 确保图片按比例填充，同时保持宽高比为 1:1。

## 总结

本实验成功实现了一个可滚动的课程主题网格应用，主要功能包括：

1. 展示 24 个课程主题，每个主题包含名称、课程数量和图片
2. 采用两列网格布局，支持垂直滚动
3. 卡片布局美观，符合设计规格
4. 代码结构清晰，数据管理合理

通过本次实验，我掌握了如何使用 `LazyVerticalGrid` 构建网格布局，以及如何组织数据类和数据源，为后续的 Android 开发打下了坚实的基础。
