package com.example.businesscard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BusinessCard()
        }
    }
}

// 名片整体布局
@Composable
fun BusinessCard() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFC0CB)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CardTop(
            name = "oumei",
            title = "计算机科学与技术专业"
        )
        CardBottom(
            phone = "+86 184 6927 6784",
            email = "3010502335@qq.com",
            handle = "@oumei"
        )
    }
}

// 上半部分：头像 + 姓名 + 职位
@Composable
fun CardTop(name: String, title: String) {
    Column(
        modifier = Modifier.padding(top = 120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable._touxiang),
            contentDescription = "头像",
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = name,
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = title,
            color = Color(0xFF3DDC84),
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

// 联系方式行：图标 + 文字
@Composable
fun ContactRow(icon: ImageVector, info: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF3DDC84),
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

// 下半部分：联系方式列表
@Composable
fun CardBottom(phone: String, email: String, handle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 60.dp)
    ) {
        ContactRow(icon = Icons.Default.Phone, info = phone)
        ContactRow(icon = Icons.Default.Email, info = email)
        ContactRow(icon = Icons.Default.Share, info = handle)
    }
}