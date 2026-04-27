# Jetpack Compose 课程网格应用实验报告

## 一、应用整体结构说明

### 1. 数据层组织方式
- 定义了 `Topic.kt` 数据类，封装课程主题的名称资源 ID、课程数量、图片资源 ID。
- 定义了 `DataSource.kt` 单例对象，以列表形式提供所有课程主题的静态数据，实现数据与 UI 的解耦。

### 2. UI 层组织方式
`MainActivity.kt` 为入口文件，包含两个核心可组合项：
- `CoursesGrid`：实现 2 列垂直懒加载网格，负责整体列表的渲染与滚动优化。
- `TopicCard`：实现单个课程主题卡片，包含图片、主题名称、课程数 + 装饰图标三部分。

---

## 二、Topic 数据类的字段设计与选择理由

### 1. 字段说明
- `@StringRes val nameResId: Int`：存储主题名称的字符串资源 ID。
- `val courseCount: Int`：存储该主题下的课程数量。
- `@DrawableRes val imageResId: Int`：存储主题图片的资源 ID。

### 2. 选择理由
- 使用 `@StringRes`/`@DrawableRes` 注解，可在编译期校验资源 ID 的有效性，避免传入无效值。
- 以资源 ID 而非硬编码字符串 / 图片，符合 Android 资源管理规范，便于多语言适配与资源修改。
- `courseCount` 使用整型存储，直接表示数值，无需额外转换即可在 UI 中展示。

---

## 三、卡片布局实现思路

### 1. 组合项嵌套结构
- 外层使用 `Card` 组件，提供圆角与基础容器样式。
- 内部使用 `Row` 水平布局，分为图片区与文字区两部分：
  - 左侧图片区：使用 `Image` 组件，尺寸固定为 68dp×68dp，通过 `clip(RoundedCornerShape)` 实现左侧圆角，`contentScale = ContentScale.Crop` 保证图片适配容器。
  - 右侧文字区：使用 `Column` 垂直布局，内部包含：
    - 主题名称：`Text` 组件，应用 `bodyMedium` 样式，底部添加 8dp 内边距。
    - 课程数行：`Row` 水平布局，包含装饰图标、8dp 间距、课程数文本（应用 `labelMedium` 样式）。

### 2. 对齐与间距控制
- 文字区整体通过 `align(Alignment.CenterVertically)` 与图片区垂直居中对齐。
- 文字区添加 16dp 内边距，保证与卡片边缘的留白，符合设计图要求。

---

## 四、网格布局实现思路（LazyVerticalGrid 参数配置说明）

### 1. 核心参数配置
- `columns = GridCells.Fixed(2)`：设置为固定 2 列布局，满足实验要求的网格形式。
- `verticalArrangement = Arrangement.spacedBy(8.dp)`：控制卡片之间的垂直间距为 8dp。
- `horizontalArrangement = Arrangement.spacedBy(8.dp)`：控制卡片之间的水平间距为 8dp。
- `contentPadding = PaddingValues(8.dp)`：为网格整体添加 8dp 内边距，避免卡片紧贴屏幕边缘。

### 2. 懒加载与数据绑定
- 通过 `items(DataSource.topics)` 遍历数据源，为每个 `Topic` 对象生成对应的 `TopicCard`。
- `LazyVerticalGrid` 仅渲染可视区域内的卡片，实现滚动性能优化。

---

## 五、断点设置与观察内容

### 1. 断点 1：数据源遍历入口
- 位置：`items(DataSource.topics)` 行
- 观察目标：验证数据源是否正常加载，列表长度与数据内容是否正确。

### 2. 断点 2：TopicCard 图片资源行
- 位置：`painter = painterResource(id = topic.imageResId)`
- 观察目标：图片资源 ID 是否正确获取，图片是否能正常加载。

### 3. 断点 3：文字资源渲染行
- 位置：`text = stringResource(id = topic.nameResId)`
- 观察目标：字符串资源是否正确解析，课程数量是否正常显示。

---

## 六、Step Into、Step Over、Step Out 的使用体会

### 1. Step Over（单步跳过）
- 使用场景：逐行执行当前函数内的代码，不进入子函数内部；
- 体会：本次实验最常用的调试方式，适合观察数据遍历、卡片渲染、布局构建等线性业务逻辑，能清晰看到每一步变量的变化，快速定位基础问题。

### 2. Step Into（单步跳入）
- 使用场景：进入当前行调用的函数或系统源码内部；
- 体会：可深入理解 Compose 网格布局、图片加载的底层逻辑，适合排查深层次 bug，但需避免随意跳入系统源码，防止调试流程混乱。

### 3. Step Out（单步跳出）
- 使用场景：快速退出当前子函数，返回到调用处；
- 体会：当不小心跳入系统源码时，可快速回到自身业务代码，大幅提升调试效率，避免在无关代码中浪费时间。

### 4. 整体总结
三者配合使用，日常调试优先用 Step Over，需深究底层逻辑用 Step Into，需快速返回上层用 Step Out，可高效完成所有调试工作。

---

## 七、遇到的问题与解决过程

### 1. 问题：drawable 目录报错 The file name must end with .xml or .png
- 现象：构建报错，提示 `The file name must end with .xml or .png`；
- 原因：`drawable` 目录中放入了非资源文件（如 `README`）；
- 解决：删除 `drawable` 目录下的无效文件，仅保留 `.jpg`/`.png`/`.xml` 等合法资源文件。

### 2. 问题：Unresolved reference 'R.string.xxx' / 'R.drawable.xxx'
- 现象：资源引用报红，无法识别 `R.string` 或 `R.drawable`；
- 原因：`strings.xml` 格式错误、图片未放入正确目录、文件名不匹配；
- 解决：修正 `strings.xml` 格式，将图片放入 `drawable` 目录，统一资源名称与代码引用。

### 3. 问题：Package directive does not match the file location
- 现象：提示包名声明与文件路径不匹配；
- 原因：`Topic.kt` 与 `DataSource.kt` 中的包名声明与文件实际所在目录不匹配；
- 解决：将 `Topic.kt` 放入 `model` 包，`DataSource.kt` 放入 `data` 包，统一包名声明。

### 4. 问题：卡片文字区域与图片区域无法垂直居中
- 现象：文字区域始终靠上，未与图片居中对齐；
- 原因：文字区 `Column` 未设置垂直对齐方式，默认按顶部对齐；
- 解决：为 `Column` 添加 `align(Alignment.CenterVertically)` 修饰符，强制在 `Row` 中垂直居中，适配不同屏幕尺寸。

### 5. 问题：网格布局只显示一列，不是两列
- 现象：列表仅显示一列，未形成网格；
- 原因：未正确配置 `LazyVerticalGrid` 的 `columns` 参数；
- 解决：设置 `columns = GridCells.Fixed(2)`，开启两列网格布局。

---

## 八、实验结论

### 1. 卡片与网格自动渲染的原因
`DataSource` 提供静态数据，`Topic` 数据类存储信息，`LazyVerticalGrid` 自动遍历数据并创建列表项。数据一旦传递给列表，Compose 自动完成界面重组与渲染，实现高效滚动布局，是声明式 UI「数据驱动界面」的体现。

### 2. 调试器中变量值与界面结果的一致性
调试器中观察到的图片资源 ID、字符串资源、课程数量，与界面显示完全一致，证明数据传递、布局渲染、资源加载均正常工作。

### 3. 实验总结
通过本次开发，我掌握了 Jetpack Compose 数据类设计、静态数据源管理、卡片布局、网格懒加载、资源引用及调试技巧，深刻理解了数据与 UI 分离的设计思想，能够独立完成可滚动网格类应用开发。