# 一、应用整体结构说明
## 1. 数据层组织方式
### 定义了 Topic.kt 数据类，封装课程主题的名称资源 ID、课程数量、图片资源 ID。
### 定义了 DataSource.kt 单例对象，以列表形式提供所有课程主题的静态数据，实现数据与 UI 的解耦。
## 2. UI 层组织方式
### MainActivity.kt 为入口文件，包含两个核心可组合项：
### CoursesGrid：实现 2 列垂直懒加载网格，负责整体列表的渲染与滚动优化。
### TopicCard：实现单个课程主题卡片，包含图片、主题名称、课程数 + 装饰图标三部分。
# 二、Topic 数据类的字段设计与选择理由
## 1. 字段说明
### @StringRes val nameResId: Int：存储主题名称的字符串资源 ID。
### val courseCount: Int：存储该主题下的课程数量。
### @DrawableRes val imageResId: Int：存储主题图片的资源 ID。
## 2. 选择理由
### 使用 @StringRes/@DrawableRes 注解，可在编译期校验资源 ID 的有效性，避免传入无效值。
### 以资源 ID 而非硬编码字符串 / 图片，符合 Android 资源管理规范，便于多语言适配与资源修改。
### courseCount 使用整型存储，直接表示数值，无需额外转换即可在 UI 中展示。
# 三、卡片布局实现思路
## 1. 组合项嵌套结构
### 外层使用 Card 组件，提供圆角与基础容器样式。
### 内部使用 Row 水平布局，分为图片区与文字区两部分：
### 左侧图片区：使用 Image 组件，尺寸固定为 68dp×68dp，通过 clip(RoundedCornerShape) 实现左侧圆角，contentScale = ContentScale.Crop 保证图片适配容器。
### 右侧文字区：使用 Column 垂直布局，内部包含：
### 主题名称：Text 组件，应用 bodyMedium 样式，底部添加 8dp 内边距。
### 课程数行：Row 水平布局，包含装饰图标、8dp 间距、课程数文本（应用 labelMedium 样式）。
## 2. 对齐与间距控制
### 文字区整体通过 align(Alignment.CenterVertically) 与图片区垂直居中对齐。
### 文字区添加 16dp 内边距，保证与卡片边缘的留白，符合设计图要求。
# 四、网格布局实现思路（LazyVerticalGrid 参数配置说明）
## 1. 核心参数配置
### columns = GridCells.Fixed(2)：设置为固定 2 列布局，满足实验要求的网格形式。
### verticalArrangement = Arrangement.spacedBy(8.dp)：控制卡片之间的垂直间距为 8dp。
### horizontalArrangement = Arrangement.spacedBy(8.dp)：控制卡片之间的水平间距为 8dp。
### contentPadding = androidx.compose.foundation.layout.PaddingValues(8.dp)：为网格整体添加 8dp 内边距，避免卡片紧贴屏幕边缘。
## 2. 懒加载与数据绑定
### 通过 items(DataSource.topics) 遍历数据源，为每个 Topic 对象生成对应的 TopicCard。
### LazyVerticalGrid 仅渲染可视区域内的卡片，实现滚动性能优化。
# 五、调试方法与使用体会
## 1. 断点设置与观察内容
### 在 CoursesGrid 的 items 循环入口处设置断点，观察数据源是否正确遍历，Topic 对象数据是否完整。
### 在 TopicCard 内部 Image 组件处设置断点，观察 imageResId 是否正确引用到对应图片资源。
### 在 Text 组件处设置断点，观察 stringResource(topic.nameResId) 是否能正确获取字符串资源，课程数文本是否正常显示。
## 2. Step Into、Step Over、Step Out 使用体会
### Step Into（跳入）：进入可组合项内部执行流程，可观察 TopicCard 内部组件的构建顺序，适合排查卡片布局渲染异常的问题。
### Step Over（跳过）：逐行执行代码，不进入子函数内部，是调试网格数据遍历逻辑时最常用的操作，可快速确认数据传递流程是否正常。
### Step Out（跳出）：快速执行完当前函数并回到调用处，适合跳过已知无问题的组件渲染代码，直接观察上层布局的整体效果。
### 整体使用下来，三个调试功能配合使用，可清晰追踪数据从数据源到 UI 组件的传递流程，快速定位资源引用、布局对齐等问题。
# 六、遇到的问题与解决过程
## 1. 问题：Unresolved reference 'enableEdgeToEdge'
### 原因：当前项目使用的 Android Studio/Compose 版本不支持 enableEdgeToEdge() API。
### 解决：删除 enableEdgeToEdge() 调用，仅移除沉浸式状态栏效果，不影响核心功能实现，保证代码可正常编译运行。
## 2. 问题：Unresolved reference 'R.string.xxx' / 'R.drawable.xxx'
### 原因：资源文件未正确配置，或 XML 格式错误导致资源无法被识别。
### 解决：
### 修正 strings.xml 文件格式，确保所有 <string> 标签在 <resources> 根节点内部，无多个根标签。
### 确认所有图片资源与 ic_grain.xml 图标文件已放入 res/drawable/ 目录，文件名与代码引用一致。
## 3. 问题：Package directive does not match the file location
### 原因：Topic.kt 与 DataSource.kt 中的包名声明与文件实际所在目录不匹配。
### 解决：统一文件的包名声明，确保 Topic.kt 包名为 com.example.demo.model，DataSource.kt 包名为 com.example.demo.data，与文件目录结构一致。
## 4. 问题：卡片文字区域与图片区域无法垂直居中
### 原因：文字区 Column 未设置垂直对齐方式，默认按顶部对齐。
### 解决：为 Column 添加 align(Alignment.CenterVertically) 修饰符，强制在 Row 中垂直居中，适配不同屏幕尺寸。
