package com.example.kotlin_basics


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BusinessCardApp()
        }
    }
}

val backgroundColor = Color(0xFF4682B4)
val textColorPink = Color(0xFFF08080)
val iconColorGreen = Color(0xFF3DDC84)

@Composable
fun BusinessCardApp() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CardTop(
            name = "杨菲",
            title = "Android 开发工程师"
        )

        // 下半部分：联系方式
        CardBottom(
            phone = "+86 18314304709",
            email = "1769686032@qq.com",
            wechat = "yf-13"
        )
    }
}

// 顶部组件：头像 + 姓名 + 职位
@Composable
fun CardTop(name: String, title: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // 头像
        Image(
            painter = painterResource(id = R.drawable.touxiang),
            contentDescription = "头像",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 姓名
        Text(
            text = name,
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 职位
        Text(
            text = title,
            color = textColorPink,
            fontSize = 30.sp
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

// 底部组件：联系方式列表
@Composable
fun CardBottom(phone: String, email: String, wechat: String) {
    Column {
        ContactRow(Icons.Default.Phone, phone)
        ContactRow(Icons.Default.Email, email)
        ContactRow(Icons.Default.Share, wechat)
    }
}


@Composable
fun ContactRow(icon: androidx.compose.ui.graphics.vector.ImageVector, info: String) {
    Row(
        modifier = Modifier
            .padding(16.dp, 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColorGreen,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))

        Text(text = info,
            color = Color.White,
            fontSize = 25.sp)
    }
}