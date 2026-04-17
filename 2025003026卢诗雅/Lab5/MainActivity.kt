package com.example.artspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ArtSpaceApp()
                }
            }
        }
    }
}

@Composable
fun ArtSpaceApp() {
    // 状态管理：当前作品索引
    var currentArtwork by remember { mutableStateOf(1) }

    // 根据状态切换图片
    val imageResource = when (currentArtwork) {
        1 -> R.drawable.artwork_1
        2 -> R.drawable.artwork_2
        else -> R.drawable.artwork_3
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // 1. 艺术作品墙
        ArtworkWall(
            imageResource = imageResource,
            modifier = Modifier.padding(top = 110.dp)
        )

        Spacer(modifier = Modifier.height(90.dp))

        // 2. 作品信息
        ArtworkDescriptor(artwork = currentArtwork)

        Spacer(modifier = Modifier.weight(1f))

        // 3. 控制按钮（Previous / Next）
        DisplayController(
            onPrevious = {
                currentArtwork = when (currentArtwork) {
                    1 -> 3
                    else -> currentArtwork - 1
                }
            },
            onNext = {
                currentArtwork = when (currentArtwork) {
                    3 -> 1
                    else -> currentArtwork + 1
                }
            }
        )
    }
}

// 作品展示墙
@Composable
fun ArtworkWall(imageResource: Int, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(430.dp)
            .shadow(8.dp)
            .border(2.dp, Color.LightGray),
        color = Color.White
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = "Artwork",
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentScale = ContentScale.Crop
        )
    }
}

// 作品信息描述（名称、艺术家、年份）
@Composable
fun ArtworkDescriptor(artwork: Int) {
    val (title, artist, year) = when (artwork) {
        1 -> Triple("灵魂", "乔治·鲁", "1885")
        2 -> Triple("绽放", "阿博特·富勒·格拉夫", "1877")
        else -> Triple("撑阳伞的女人", "克劳德·莫奈", "1875")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            // 1. 整体外间距
            .padding(horizontal = 2.dp, vertical = 24.dp)
            // 3. 文字和背景框的内边距
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            fontSize = 28.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp)) // 标题和作者之间的间距
        Text(
            text = "$artist ($year)",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

// 按钮控制器
@Composable
fun DisplayController(onPrevious: () -> Unit, onNext: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = onPrevious,
            modifier = Modifier.width(150.dp)
        ) {
            Text("Previous")
        }
        Button(
            onClick = onNext,
            modifier = Modifier.width(150.dp)
        ) {
            Text("Next")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtSpacePreview() {
    ArtSpaceApp()
}