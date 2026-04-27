MainActivity.kt（添加注释版）

package com.yxyn.mhomework71

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yxyn.mhomework71.data.DataSource
import com.yxyn.mhomework71.model.Topic

/**
 * 应用主活动，作为整个应用的入口
 * 负责加载并展示应用的主界面（课程网格布局）
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置Compose内容，加载应用主界面
        setContent {
            CoursesApp()
        }
    }
}

/**
 * 应用主界面可组合函数
 * 构建两列可滚动的课程网格，承载所有课程卡片
 */
@Composable
fun CoursesApp() {
    LazyVerticalGrid(
        // 固定两列网格布局
        columns = GridCells.Fixed(2),
        // 网格整体内边距：8dp，与屏幕边缘保持距离
        contentPadding = PaddingValues(8.dp),
        // 水平方向卡片间距：8dp
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        // 垂直方向卡片间距：8dp
        verticalArrangement = Arrangement.spacedBy(8.dp),
        // 修饰符：占满整个屏幕
        modifier = Modifier.fillMaxSize()
    ) {
        // 遍历数据源中的所有课程主题，为每个主题创建一个课程卡片
        items(DataSource.topics) {
            TopicCard(topic = it)
        }
    }
}

/**
 * 单个课程主题卡片可组合函数
 * 展示单个课程的图片、名称和可用课程数量，左图右文布局
 * @param topic 单个课程主题的数据模型，包含名称、课程数、图片资源ID
 */
@Composable
fun TopicCard(topic: Topic) {
    // 卡片容器，设置阴影高度（2dp），提升界面层次感
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        // 横向布局：左侧图片，右侧文字区域
        Row {
            // 课程主题图片
            Image(
                // 加载当前课程主题对应的图片资源
                painter = painterResource(id = topic.imageResId),
                // 内容描述：对应课程主题名称，提升可访问性
                contentDescription = stringResource(id = topic.name),
                // 图片尺寸：固定宽高68dp，与实验要求一致
                modifier = Modifier
                    .width(68.dp)
                    .height(68.dp),
                // 图片缩放模式：裁剪填充，避免图片拉伸变形
                contentScale = ContentScale.Crop
            )
            // 右侧文字区域：垂直排列课程名称和课程数量
            Column(
                modifier = Modifier
                    .padding(16.dp) // 文字区域内边距16dp
                    .align(Alignment.CenterVertically), // 与左侧图片垂直居中对齐
                horizontalAlignment = Alignment.Start // 文字左对齐
            ) {
                // 课程主题名称
                Text(
                    text = stringResource(id = topic.name),
                    // 字体样式：bodyMedium，符合实验要求
                    style = MaterialTheme.typography.bodyMedium
                )
                // 间距：课程名称与下方课程数量之间的垂直间距8dp
                Spacer(modifier = Modifier.height(8.dp))
                // 横向布局：装饰图标与课程数量
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // 装饰图标（ic_grain）
                    Image(
                        painter = painterResource(id = R.drawable.ic_grain),
                        contentDescription = null, // 纯装饰图标，无实际语义，设为null
                        // 图标尺寸：16dp×16dp，贴合界面比例
                        modifier = Modifier
                            .width(16.dp)
                            .height(16.dp)
                    )
                    // 间距：装饰图标与课程数量之间的水平间距8dp
                    Spacer(modifier = Modifier.width(8.dp))
                    // 可用课程数量
                    Text(
                        text = topic.availableCourses.toString(),
                        // 字体样式：labelMedium，符合实验要求
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

/**
 * 课程卡片预览函数
 * 用于在Android Studio预览界面中快速查看卡片样式
 */
@Preview
@Composable
fun TopicCardPreview() {
    // 预览用测试数据，使用Architecture主题的资源
    TopicCard(
        topic = Topic(
            name = R.string.architecture,
            availableCourses = 58,
            imageResId = R.drawable.architecture
        )
    )
}
