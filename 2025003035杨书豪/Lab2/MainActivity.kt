package com.example.homework1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homework1.ui.theme.Homework1Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Homework1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BusinessCardScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/**
 * 电子名片主布局
 */
@Composable
fun BusinessCardScreen(modifier: Modifier = Modifier) {
    val backGround = painterResource(R.drawable.img_5658)
    val androidLogo = painterResource(R.drawable.img_5927)

    // 铺满整个空间
    Box(modifier = modifier.fillMaxSize()) {

        Image(
            painter = backGround,
            contentDescription = "背景图",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 根布局
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 第一部分
            Column(
                modifier = Modifier.weight(1f), // 占满所有空间，让内容居中
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // 内部垂直居中
            ) {
                // 头像
                Image(
                    painter = androidLogo,
                    contentDescription = "Logo",
                    modifier = Modifier.size(140.dp)
                )

                Text(
                    text = "Yang ShuHao",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8800FF),
                    modifier = Modifier.padding(top = 24.dp)
                )

                Text(
                    text = "Android Developer Extraordinaire",
                    fontSize = 18.sp,
                    color = Color(0xFF8800FF),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // 👇 第二部分：联系方式
            Column(
                modifier = Modifier.padding(bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ContactItem(icon = Icons.Default.Phone, text = "+86 12345678910")
                ContactItem(icon = Icons.Default.Share, text = "@AndroidDev")
                ContactItem(icon = Icons.Default.Email, text = "YangShuHao@android.com")
            }
        }
    }
}
/**
 * 单个联系方式项
 */
@Composable
fun ContactItem(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(start = 48.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF7AF1FF),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(24.dp)) // 图标和文字间距
        Text(
            text = text,
            fontSize = 18.sp,
            color = Color.Blue
        )
    }
}

/**
 * 预览函数（适配主题）
 */
@Preview(showBackground = true)
@Composable
fun BusinessCardPreview() {
    Homework1Theme {
        BusinessCardScreen()
    }
}