package com.example.lab7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.lab7.data.Topic
import com.example.lab7.model.DataSource


// MainActivity 是整个Android应用的入口
// 继承 ComponentActivity：Compose 专用的 Activity 基类，支持 Compose 界面渲染
class MainActivity : ComponentActivity() {
    // Activity 创建时自动调用的生命周期方法（页面初始化核心方法）
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // 调用父类的onCreate，完成基础初始化

        // setContent：Compose 核心方法
        // 作用：将 Compose 编写的UI组件，设置为当前Activity的显示内容
        setContent {
            CoursesApp() // 加载应用顶层UI组件
        }
    }
}

/**
 * 应用顶层容器组件：整个应用的根布局，单独封装
 */
@Composable
fun CoursesApp() {
    // 调用网格布局组件，将数据源的课程列表传入
    CoursesGrid(topicList = DataSource.topics)
}

/**
 * 课程网格布局组件
 * <p>
 * 作用：使用懒加载网格，将课程数据以两列网格形式展示
 * @param topicList 传入的课程数据列表
 */
@Composable
fun CoursesGrid(topicList: List<Topic>) {
    // LazyVerticalGrid：Compose 垂直懒加载网格（只渲染屏幕可见项，性能极高）
    LazyVerticalGrid(
        // 列数配置：Fixed(2) = 固定显示2列网格
        columns = GridCells.Fixed(2),

        // 内容内边距：网格整体 四周 留出8dp的空白间距
        contentPadding = PaddingValues(8.dp),

        // 水平排列：列与列之间的间距为8dp
        horizontalArrangement = Arrangement.spacedBy(8.dp),

        // 垂直排列：行与行之间的间距为8dp
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 遍历课程列表：为列表中每一个Topic对象，创建一个课程卡片
        // items = 列表适配器，自动绑定数据和UI
        items(topicList) { topic ->
            TopicCard(topic = topic) // 渲染单个课程卡片
        }
    }
}

/**
 * 单个课程卡片组件
 * <p>
 * 作用：展示单条课程的完整信息（图片+名称+课程数量）
 * @param topic 单条课程数据
 */
@Composable
fun TopicCard(topic: Topic) {
    // Card：Material3 卡片组件，自带圆角、阴影效果，作为卡片容器
    Card(
        // 修饰符：卡片宽度 填充父布局的最大宽度
        modifier = Modifier.fillMaxWidth()
    ) {
        // Row：水平布局组件 -> 图片和文字信息 左右排列
        Row {
            // 左侧：课程图片
            Image(
                // 加载本地图片资源（通过资源id）
                painter = painterResource(id = topic.imageRes),
                // 内容描述：无障碍功能使用，此处为装饰图传null
                contentDescription = null,
                // 修饰符：设置图片固定尺寸 68dp×68dp
                modifier = Modifier.size(68.dp)
            )

            // 右侧：课程文字信息
            // Column：垂直布局组件 → 标题 和 图标+数量 上下排列
            Column(
                // 修饰符：给文字区域设置内边距（左16dp、上16dp、右16dp）
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                )
            ) {
                // 第一行：课程标题
                Text(
                    // 加载本地字符串资源（通过资源id）
                    text = stringResource(id = topic.name),
                    // 文字样式：使用主题中定义的 正文中等字号
                    style = MaterialTheme.typography.bodyMedium
                )

                // 间距组件：创建8dp的垂直间距（标题和下方内容的间隔）
                Spacer(modifier = Modifier.width(8.dp))

                // 第二行：图标 + 课程数量（水平排列）
                Row(
                    // 垂直对齐：让图标和文字 垂直居中对齐
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 课程数量图标
                    Image(
                        painter = painterResource(id = R.drawable.ic_grain),
                        contentDescription = null
                    )

                    // 图标和数字之间的水平间距：8dp
                    Spacer(modifier = Modifier.width(8.dp))

                    // 课程数量文本
                    Text(
                        // 数字转字符串
                        text = topic.availableCourses.toString(),
                        // 文字样式
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}