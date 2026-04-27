package com.example.lab5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 主入口
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 设置 Compose UI
        setContent {
            ArtSpaceApp()  // 启动界面
        }
    }
}

// 主界面
@Composable
fun ArtSpaceApp() {

    // 当前显示第几张图
    var currentIndex by remember { mutableStateOf(0) }
    /**
    - currentIndex = 当前图片索引
    - remember = 记住状态（不会刷新丢失）
    - mutableStateOf = 状态变化时自动刷新 UI
    */

    // 数据源
    val artworks = listOf(
        Artwork(R.drawable.artwork_1, "Het meisje met de parel",
            "Johannes Vermeer", "1665"),

        Artwork(R.drawable.artwork_2, "Mona Lisa",
            "Leonardo da Vinci", "1503"),

        Artwork(R.drawable.artwork_3, "Lady with an Ermine",
            "Leonardo da Vinci", "1489-1490"),

        Artwork(R.drawable.artwork_4, "The Storm on the Sea of Galilee",
        "Rembrandt van Rijn", "1633"),

        Artwork(R.drawable.artwork_5, "The Starry Night",
        "Vincent Willem van Gogh", "1889")
    )

    // 当前展示的作品
    val current = artworks[currentIndex]

    // 页面布局
    Column(
        modifier = Modifier
            .fillMaxSize()   // 占满屏幕
            .padding(16.dp), // 外边距
        verticalArrangement = Arrangement.Top, // 上中下分布
        horizontalAlignment = Alignment.CenterHorizontally // 居中
    ) {

        Spacer(modifier = Modifier.height(100.dp)) // 图片上间距

        // 图片区域
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .shadow(8.dp) // 阴影
        ) {
            Image(
                painter = painterResource(id = current.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .size(400.dp) // 图片大小
            )

        }
        Spacer(modifier = Modifier.height(50.dp))  // 图片和介绍之间的距离

        /*
        - size(300.dp) → 改图片大小
        - shadow(8.dp) → 改阴影强度
        */

        // 文字区域
        Column(
            modifier = Modifier
                .fillMaxWidth()                  // 宽度铺满
                .padding(horizontal = 16.dp)     // 左右留白
                .background(
                    color = Color(0xFFBDBDBD)    // 灰色背景
                )
                .padding(16.dp),            // 内边距
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 标题（作品名）
            Text(
                text = current.title,
                fontSize = 20.sp,         // 字号
//                fontWeight = FontWeight.Bold // 加粗
            )
            // 副标题（作者 + 年份）
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(current.artist) // 只让作者加粗
                    }
                    append(" (${current.year})") // 年份不加粗
                },
                fontSize = 16.sp
            )
        }

        /*
        - 字体大小 fontSize
        - 是否加粗
        - 格式（or to 中文）
        */
        Spacer(modifier = Modifier.height(60.dp))

        // 按钮区域
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {

            // 上一张
            Button(onClick = {
                currentIndex = if (currentIndex > 0) {
                    currentIndex - 1
                } else {
                    artworks.size - 1 // 循环到最后一张
                }
            }) {
                Text("Previous")
            }

            // 下一张
            Button(onClick = {
                currentIndex = (currentIndex + 1) % artworks.size
            }) {
                Text("Next")
            }
        }

    /**
    - 点击按钮 → 改 currentIndex
    - currentIndex 变 → UI 自动刷新
    */

    }
}

// 数据类
data class Artwork(
    val imageRes: Int, // 图片资源ID
    val title: String, // 作品名
    val artist: String, // 作者
    val year: String    // 年份
)