package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
            BusinessCardApp()
        }
    }
}

// 主界面：满足实验要求 深色背景 + 绿色强调色
@Composable
fun BusinessCardApp() {
    val backgroundColor = Color(0xFF073042)
    val highlightColor = Color(0xFF3DDC84)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 顶部：头像 + 姓名 + 职位
        CardTop(
            name = "彭静怡",
            title = "Android 开发者",
            highlightColor = highlightColor
        )

        // 底部：联系方式
        CardBottom(
            phone = "138 0000 0000",
            email = "PENGJINGYI@example.com",
            social = "@PENGJINGYI",
            highlightColor = highlightColor
        )
    }
}

// 顶部卡片组件（头像、姓名、职位）
@Composable
fun CardTop(name: String, title: String, highlightColor: Color) {
    Column(
        modifier = Modifier.padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 圆形头像（实验要求：圆形裁剪）
        Image(
            painter = painterResource(id = R.drawable.my_avatar),
            contentDescription = "头像",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 姓名（大号白色粗体）
        Text(
            text = name,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 职位（绿色）
        Text(
            text = title,
            color = highlightColor,
            fontSize = 18.sp
        )
    }
}

// 底部联系方式区域
@Composable
fun CardBottom(phone: String, email: String, social: String, highlightColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 60.dp)
    ) {
        // 电话
        ContactRow(
            icon = Icons.Default.Phone,
            info = phone,
            highlightColor = highlightColor
        )

        HorizontalDivider(
            color = Color.Gray.copy(alpha = 0.5f),
            modifier = Modifier.padding(horizontal = 40.dp)
        )

        // 邮箱
        ContactRow(
            icon = Icons.Default.Email,
            info = email,
            highlightColor = highlightColor
        )

        HorizontalDivider(
            color = Color.Gray.copy(alpha = 0.5f),
            modifier = Modifier.padding(horizontal = 40.dp)
        )

        // 社交账号
        ContactRow(
            icon = Icons.Default.Share,
            info = social,
            highlightColor = highlightColor
        )
    }
}

// 单行联系人样式（图标 + 文字）
@Composable
fun ContactRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    info: String,
    highlightColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = highlightColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = info,
            color = Color.White,
            fontSize = 16.sp
        )
    }
}

// 预览（实验要求必须有）
@Preview(showBackground = true, name = "名片预览")
@Composable
fun BusinessCardPreview() {
    BusinessCardApp()
}