

## 一、实验概述
本实验基于 Android Jetpack Compose 框架，从零构建了一款课程主题网格应用 `Courses App`。应用以两列网格的形式展示24个课程主题，支持垂直滚动，每个卡片包含主题图片、名称及课程数量，并严格遵循实验提供的UI间距与样式规格。

---

## 二、应用整体结构说明
本项目采用分层架构设计，实现了数据与UI的解耦，核心分为三层：

| 层级 | 对应文件 | 作用说明 |
|------|----------|----------|
| 数据模型层 | `Topic.kt` | 定义课程主题的数据结构，封装主题名称、课程数量、图片资源三类核心信息 |
| 静态数据源层 | `DataSource.kt` | 以单例模式集中管理24个课程主题的静态数据，统一维护数据集合 |
| UI表现层 | `MainActivity.kt` | 实现核心组合项：`CoursesGrid`（两列网格布局）、`TopicCard`（单个课程卡片），完成界面渲染与交互逻辑 |
| 资源层 | `res/values/strings.xml`、`res/drawable/` | 存储主题名称字符串、主题图片及装饰图标资源，实现资源与代码的分离 |

项目文件结构如下：
```text
app/
└── src/
    └── main/
        ├── java/com/example/myapplication/
        │   ├── MainActivity.kt          # 主界面入口，调用网格组合项
        │   ├── model/
        │   │   └── Topic.kt             # 数据类
        │   └── data/
        │       └── DataSource.kt        # 静态数据源
        └── res/
            ├── drawable/
            │   ├── 24张主题图片（architecture.jpg等）
            │   └── ic_grain.xml         # 课程数量装饰图标
            └── values/
                └── strings.xml          # 主题名称字符串资源
```

---

## 三、Topic 数据类设计说明
### 1. 字段设计与选择理由
根据实验要求，每个课程主题卡片包含三类核心信息，因此数据类设计如下：
```kotlin
package com.example.myapplication.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Topic(
    @StringRes val nameRes: Int,
    val courseCount: Int,
    @DrawableRes val imageRes: Int
)
```

各字段设计理由如下：
| 字段 | 类型 | 设计理由 |
|------|------|----------|
| `nameRes` | `@StringRes Int` | 使用字符串资源ID存储主题名称，而非直接字符串，符合Android资源管理规范，支持多语言适配，便于后续修改主题名称 |
| `courseCount` | `Int` | 课程数量为纯数字类型，直接存储整数即可，无需额外资源引用，提升数据读取效率 |
| `imageRes` | `@DrawableRes Int` | 使用图片资源ID存储主题图片，与字符串资源同理，便于资源替换与多设备适配，同时保证图片资源的类型安全 |

### 2. 数据实例创建
在 `DataSource.kt` 中，通过单例模式创建所有课程主题实例，实现数据的集中管理：
```kotlin
package com.example.myapplication.data

import com.example.myapplication.R
import com.example.myapplication.model.Topic

object DataSource {
    val topics = listOf(
        Topic(R.string.architecture, 58, R.drawable.architecture),
        Topic(R.string.automotive, 30, R.drawable.automotive),
        // 其余22个主题实例省略...
    )
}
```

---

## 四、核心UI组件实现思路
### 1. 单个课程卡片（TopicCard）实现
#### （1）布局结构分析
卡片采用 `Card` 组件作为根容器，内部通过 `Row` 实现水平布局，分为左右两部分：
- 左侧：图片区域，固定尺寸为68dp×68dp的正方形，使用 `Box` 包裹 `Image` 实现居中显示
- 右侧：文字区域，使用 `Column` 垂直排列主题名称与课程数量行，四周设置16dp内边距

#### （2）关键代码实现
```kotlin
@Composable
fun TopicCard(topic: Topic, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧：68dp×68dp主题图片
            Box(
                modifier = Modifier
                    .size(68.dp)
            ) {
                Image(
                    painter = painterResource(topic.imageRes),
                    contentDescription = stringResource(topic.nameRes),
                    modifier = Modifier.fillMaxSize()
                )
            }

            // 右侧：文字区域
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp,
                    end = 8.dp
                )
            ) {
                // 主题名称：bodyMedium样式
                Text(
                    text = stringResource(topic.nameRes),
                    style = MaterialTheme.typography.bodyMedium
                )

                // 课程数量行：图标+数字，间距8dp
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_grain),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "${topic.courseCount}",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}
```

#### （3）UI规格对齐说明
| 元素 | 实验规格 | 实现方式 |
|------|----------|----------|
| 图片尺寸 | 68dp×68dp正方形 | 使用 `Modifier.size(68.dp)` 固定尺寸 |
| 文字区域内边距 | 四周16dp | 通过 `Modifier.padding(start=16.dp, top=16.dp, bottom=16.dp, end=8.dp)` 实现 |
| 主题名称与数量行间距 | 8dp | 使用 `Modifier.padding(top=8.dp)` 设置 |
| 图标与数字间距 | 8dp | 使用 `Spacer(Modifier.width(8.dp))` 实现 |
| 文字样式 | 主题名称：`bodyMedium`；课程数量：`labelMedium` | 直接使用 `MaterialTheme.typography` 提供的样式 |

### 2. 课程网格（CoursesGrid）实现
#### （1）布局结构分析
使用 `LazyVerticalGrid` 实现两列可滚动网格布局，核心配置如下：
- 列数：固定2列，通过 `GridCells.Fixed(2)` 实现
- 内边距：网格整体四周内边距8dp，通过 `contentPadding` 设置
- 间距：卡片水平、垂直间距均为8dp，通过 `Arrangement.spacedBy` 设置

#### （2）关键代码实现
```kotlin
@Composable
fun CoursesGrid(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(DataSource.topics) { topic ->
            TopicCard(topic = topic)
        }
    }
}
```

#### （3）关键配置说明
| 参数 | 配置值 | 作用说明 |
|------|--------|----------|
| `columns` | `GridCells.Fixed(2)` | 设置网格为固定2列布局，实现两列均匀分布 |
| `contentPadding` | `PaddingValues(8.dp)` | 为整个网格添加四周8dp的内边距，避免卡片贴边显示 |
| `horizontalArrangement` | `Arrangement.spacedBy(8.dp)` | 设置相邻卡片之间的水平间距为8dp |
| `verticalArrangement` | `Arrangement.spacedBy(8.dp)` | 设置相邻卡片之间的垂直间距为8dp |

---

## 五、遇到的问题与解决过程
### 问题1：`Unresolved reference 'ContentPadding'` 错误
- **问题现象**：代码中使用 `ContentPadding(8.dp)` 时提示无法识别该类
- **问题原因**：`LazyVerticalGrid` 的内边距参数应为 `PaddingValues`，而非 `ContentPadding`，属于类名误用
- **解决方法**：将 `ContentPadding(8.dp)` 替换为 `androidx.compose.foundation.layout.PaddingValues(8.dp)`，并确保导入对应包

### 问题2：`Unresolved reference 'fillMaxHeight'` 错误
- **问题现象**：使用 `fillMaxHeight()` 修饰符时提示无法识别
- **问题原因**：未导入 `fillMaxHeight` 所在的包
- **解决方法**：在 `MainActivity.kt` 顶部添加导入语句：`import androidx.compose.foundation.layout.fillMaxHeight`

### 问题3：卡片布局文字区域无法自适应高度
- **问题现象**：文字区域高度无法与左侧图片对齐，导致卡片高度不一致
- **问题原因**：`Row` 未设置垂直居中，且文字区域未占满剩余空间
- **解决方法**：为 `Row` 添加 `verticalAlignment = Alignment.CenterVertically`，并为文字区域的 `Column` 添加 `weight(1f)`，使其占满剩余空间，实现垂直居中

### 问题4：运行效果与实验截图不一致
- **问题现象**：卡片图片比例、间距与实验截图存在差异
- **问题原因**：未严格遵循实验提供的UI规格，图片未固定为68dp×68dp，间距设置错误
- **解决方法**：修改图片区域为 `Modifier.size(68.dp)`，重新调整文字区域内边距与元素间距，确保所有规格与实验要求一致

---

## 六、实验总结
通过本次实验，我系统掌握了以下知识与技能：
1.  掌握了数据类的设计方法，能够根据业务需求封装数据模型，并通过单例模式实现静态数据的集中管理
2.  熟练使用 `LazyVerticalGrid` 构建多列可滚动网格布局，理解了 `GridCells.Fixed`、`contentPadding`、`Arrangement.spacedBy` 等参数的作用
3.  能够灵活运用 `Row`、`Column`、`Card`、`Box` 等布局组件实现复杂UI，严格遵循间距与样式规格
4.  提升了问题排查与解决能力，能够独立处理导入错误、布局适配等常见开发问题



---
