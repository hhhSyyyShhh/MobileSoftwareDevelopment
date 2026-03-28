package com.example.businesscard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.businesscard.ui.theme.BusinessCardTheme

// 自定义主题色（Android绿 #3DDC84）
val androidGreen = Color(0xFF3DDC84)
val cardBgColor = Color(0xFF073042) // 深青背景色

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BusinessCardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BusinessCard()
                }
            }
        }
    }
}

@Composable
fun BusinessCard() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cardBgColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 上半部分：头像 + 姓名 + 职位
        CardTop(
            name = "罗贵媛",
            title = "Android Developer"
        )
        // 下半部分：联系方式列表
        CardBottom(
            phone = "18388621668",
            email = "3568298347@qq.com",
            handle = "@luoguiyuan"
        )
    }
}

@Composable
fun CardTop(name: String, title: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 方案1：用代码画Android图标（零资源依赖，彻底解决图片找不到问题）
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.foundation.Image(
                painter = painterResource(id = android.R.drawable.ic_dialog_info),
                contentDescription = "Android Logo",
                modifier = Modifier.size(80.dp),
                colorFilter = ColorFilter.tint(androidGreen)
            )
        }

        Spacer(modifier = Modifier.size(24.dp))
        // 姓名
        Text(
            text = name,
            fontSize = 36.sp,
            fontWeight = FontWeight.W300,
            color = Color.White
        )
        // 职位
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = androidGreen
        )
    }
}

@Composable
fun CardBottom(phone: String, email: String, handle: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp)
    ) {
        // 电话行
        ContactRow(icon = Icons.Default.Phone, info = phone)
        HorizontalDivider(
            color = Color.Gray.copy(alpha = 0.5f),
            modifier = Modifier.padding(horizontal = 40.dp),
            thickness = 1.dp
        )
        // 邮箱行
        ContactRow(icon = Icons.Default.Email, info = email)
        HorizontalDivider(
            color = Color.Gray.copy(alpha = 0.5f),
            modifier = Modifier.padding(horizontal = 40.dp),
            thickness = 1.dp
        )
        // 社交账号行
        ContactRow(icon = Icons.Default.Share, info = handle)
    }
}

@Composable
fun ContactRow(icon: androidx.compose.ui.graphics.vector.ImageVector, info: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = androidGreen,
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

@Preview(showBackground = true)
@Composable
fun BusinessCardPreview() {
    BusinessCardTheme {
        BusinessCard()
    }
}