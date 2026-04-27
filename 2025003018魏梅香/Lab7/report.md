# 实验报告：Jetpack Compose 课程网格应用
## 一、实验信息
- **实验名称**：Jetpack Compose 响应式列表与网格布局
- **开发环境**：Android Studio + Kotlin + Jetpack Compose
- **包名**：`com.example.courses`
- **功能**：实现两列滚动课程卡片列表，展示课程图片、名称、课程数量

## 二、实验目的
1. 掌握 Jetpack Compose `LazyVerticalGrid` 网格列表用法
2. 学会使用 `Card`、`Row`、`Column` 构建复合布局
3. 掌握资源文件 `strings.xml` 和 `drawable` 正确引用方式
4. 理解数据模型 `data class` 与数据源 `object` 的分离设计
5. 学会图片、图标、文本组合的列表项实现

## 三、项目结构
```
com.example.courses
├── model/
│   └── Topic.kt          // 数据模型
├── data/
│   └── DataSource.kt      // 数据源
├── ui/theme/             // 主题文件（自动生成）
└── MainActivity.kt       // 主界面与UI布局
```

## 四、核心代码实现
### 1. 数据模型 Topic.kt
```kotlin
package com.example.courses.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Topic(
    @StringRes val nameRes: Int,
    val courses: Int,
    @DrawableRes val imageRes: Int
)
```

### 2. 数据源 DataSource.kt
```kotlin
package com.example.courses.data

import com.example.courses.R
import com.example.courses.model.Topic

object DataSource {
    val topics = listOf(
        Topic(R.string.architecture, 58, R.drawable.architecture),
        Topic(R.string.automotive, 30, R.drawable.automotive),
        Topic(R.string.biology, 90, R.drawable.biology),
        Topic(R.string.crafts, 121, R.drawable.crafts),
        Topic(R.string.business, 78, R.drawable.business),
        Topic(R.string.culinary, 118, R.drawable.culinary),
        Topic(R.string.design, 423, R.drawable.design),
        Topic(R.string.ecology, 28, R.drawable.ecology),
        Topic(R.string.engineering, 67, R.drawable.engineering),
        Topic(R.string.fashion, 92, R.drawable.fashion),
        Topic(R.string.finance, 100, R.drawable.finance),
        Topic(R.string.film, 165, R.drawable.film),
        Topic(R.string.gaming, 37, R.drawable.gaming),
        Topic(R.string.geology, 290, R.drawable.geology),
        Topic(R.string.drawing, 326, R.drawable.drawing),
        Topic(R.string.history, 189, R.drawable.history),
        Topic(R.string.journalism, 96, R.drawable.journalism),
        Topic(R.string.law, 58, R.drawable.law),
        Topic(R.string.lifestyle, 305, R.drawable.lifestyle),
        Topic(R.string.music, 212, R.drawable.music),
        Topic(R.string.painting, 172, R.drawable.painting),
        Topic(R.string.photography, 321, R.drawable.photography),
        Topic(R.string.physics, 321, R.drawable.physics),
        Topic(R.string.tech, 118, R.drawable.tech)
    )
}
```

### 3. 主界面 MainActivity.kt
```kotlin
package com.example.courses

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.courses.data.DataSource
import com.example.courses.model.Topic
import com.example.courses.ui.theme.CoursesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoursesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CoursesApp()
                }
            }
        }
    }
}

@Composable
fun CoursesApp() {
    CoursesGrid(
        topics = DataSource.topics,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun CoursesGrid(
    topics: List<Topic>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(topics) { topic ->
            TopicItem(topic = topic)
        }
    }
}

@Composable
fun TopicItem(topic: Topic, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = topic.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = topic.nameRes),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_grain),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${topic.courses}",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}
```

### 4. 字符串资源 strings.xml
```xml
<resources>
    <string name="app_name">Courses</string>
    <string name="architecture">Architecture</string>
    <string name="automotive">Automotive</string>
    <string name="biology">Biology</string>
    <string name="crafts">Crafts</string>
    <string name="business">Business</string>
    <string name="culinary">Culinary</string>
    <string name="design">Design</string>
    <string name="ecology">Ecology</string>
    <string name="engineering">Engineering</string>
    <string name="fashion">Fashion</string>
    <string name="finance">Finance</string>
    <string name="film">Film</string>
    <string name="gaming">Gaming</string>
    <string name="geology">Geology</string>
    <string name="drawing">Drawing</string>
    <string name="history">History</string>
    <string name="journalism">Journalism</string>
    <string name="law">Law</string>
    <string name="lifestyle">Lifestyle</string>
    <string name="music">Music</string>
    <string name="painting">Painting</string>
    <string name="photography">Photography</string>
    <string name="physics">Physics</string>
    <string name="tech">Tech</string>
</resources>
```

## 五、实验功能与效果
- ✅ 两列网格布局 `LazyVerticalGrid`
- ✅ 可垂直滚动
- ✅ 每个 item 展示：图片 + 课程名 + 数量
- ✅ 使用 `Card` 圆角卡片
- ✅ 使用 `ic_grain.xml` 图标
- ✅ 全部使用 `R.string` 和 `R.drawable` 资源
- ✅ 数据与 UI 完全分离，结构清晰

## 六、遇到的问题与解决方法
### 1. R.string / R.drawable 爆红
- **原因**：Android Studio 资源索引异常
- **解决**：同步 Gradle、清理项目、重建项目、确保导入 `com.example.courses.R`

### 2. 布局报错（Row/Column/Spacer）
- **原因**：缺少导入或嵌套错误
- **解决**：规范布局层级，自动导入缺失组件

### 3. 图片无法显示
- **原因**：文件名不匹配、放错文件夹
- **解决**：全部放入 `res/drawable`，确保代码与文件名一致

## 七、实验总结
本次实验完成了一个基于 Jetpack Compose 的课程列表应用，实现了**网格布局、图片文字组合、资源管理、数据模型分离**等核心知识点。

通过实验掌握了：
- `LazyVerticalGrid` 高性能列表
- `Row` + `Column` 组合布局
- 图片、图标、文本混合 UI 实现
- `strings.xml` 与 `drawable` 正确使用
- Android 资源索引问题排查方法


