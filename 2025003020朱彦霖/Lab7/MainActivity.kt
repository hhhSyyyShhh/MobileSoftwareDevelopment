package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.DataSource
import com.example.myapplication.model.Topic

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 核心修正：使用项目默认的MaterialTheme，替代不存在的CoursesTheme
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CoursesGrid() // 渲染课程网格
                }
            }
        }
    }
}


@Composable
fun TopicGridItem(topic: Topic, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(topic.imageRes),
                contentDescription = stringResource(topic.name),
                modifier = Modifier
                    .size(68.dp)
                    .aspectRatio(1f) // 强制1:1比例，适配不同屏幕
                    .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)),
                alignment = Alignment.Center
            )

            Column(
                modifier = Modifier
                    .padding(16.dp) // 文字区内边距（16dp）
                    .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.Center
            ) {
                // 主题名称
                Text(
                    text = stringResource(topic.name),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp) // 与下方行间距8dp
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_grain), // 需添加该图标资源
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // 图标与数字间距8dp
                    Text(
                        text = topic.courseCount.toString(),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}


@Composable
fun CoursesGrid(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 固定2列
        modifier = modifier.padding(8.dp), // 网格外层内边距
        horizontalArrangement = Arrangement.spacedBy(8.dp), // 水平间距8dp
        verticalArrangement = Arrangement.spacedBy(8.dp), // 垂直间距8dp
        contentPadding = androidx.compose.foundation.layout.PaddingValues(8.dp)
    ) {
        items(DataSource.topics) { topic ->
            TopicGridItem(topic = topic)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopicGridItemPreview() {
    MaterialTheme { // 修正：使用MaterialTheme替代CoursesTheme
        TopicGridItem(
            topic = Topic(
                name = R.string.architecture,
                courseCount = 58,
                imageRes = R.drawable.architecture
            )
        )
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=731dp")
@Composable
fun CoursesGridPreview() {
    MaterialTheme { // 修正：使用MaterialTheme替代CoursesTheme
        CoursesGrid()
    }
}