package com.example.course

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.course.data.DataSource
import com.example.course.model.Topic
import com.example.course.ui.theme.CourseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CourseTheme {
                // 最外层 Surface 强制占满屏幕，无多余嵌套
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(Modifier.fillMaxSize()) {
                        // 顶部紫色标题栏（固定不动，和截图一致）
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF6200EE))
                                .height(56.dp)
                        )

                        // 核心：只靠 LazyVerticalGrid 自身滚动，不嵌套任何其他滚动控件
                        // weight(1f) 会强制它占满剩余空间，系统自动触发滚动
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f), // 这行是滚动的关键，100%兼容
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(DataSource.topics) { topic ->
                                CourseCard(topic)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CourseCard(topic: Topic) {
    // 卡片样式和截图1:1匹配，不搞复杂参数
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3E5F5))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧图片，大小和截图一致
            Image(
                painter = painterResource(topic.imageRes),
                contentDescription = stringResource(topic.nameRes),
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 右侧文字和数量，排版和截图一致
            Column {
                Text(
                    text = stringResource(topic.nameRes),
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_grain),
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${topic.coursesCount}",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}