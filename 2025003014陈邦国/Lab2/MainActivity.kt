package com.example.demo3

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 主Activity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BusinessCardApp()
        }
    }
}

// 主题容器
@Composable
fun BusinessCardApp() {
    MaterialTheme {
        BusinessCard()
    }
}

// 主名片页面
@Composable
fun BusinessCard() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF073042)) // 深蓝色背景
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 上半部分：头像+姓名+职位
        CardTop(
            name = "陈邦国",
            title = "Android 开发工程师",
            logoRes = R.drawable.anzhuo
        )

        // 下半部分：联系方式
        CardBottom(
            phone = "1234567890",
            email = "2116321787@qq.com",
            handle = "@chenbangguo"
        )
    }
}

// 名片上半部分组件
@Composable
fun CardTop(name: String, title: String, logoRes: Int) {
    Column(
        modifier = Modifier.padding(top = 100.dp, bottom = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 头像/Logo
        Image(
            painter = painterResource(id = logoRes),
            contentDescription = "个人头像",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 姓名
        Text(
            text = name,
            fontSize = 40.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 职位
        Text(
            text = title,
            fontSize = 20.sp,
            color = Color(0xFF3DDC84)
        )
    }
}

// 联系方式单行组件
@Composable
fun ContactRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    info: String
) {
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

// 名片下半部分联系方式
@Composable
fun CardBottom(phone: String, email: String, handle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 60.dp)
    ) {
        ContactRow(icon = Icons.Default.Phone, info = phone)
        ContactRow(icon = Icons.Default.Share, info = handle)
        ContactRow(icon = Icons.Default.Email, info = email)
    }
}

// 预览
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BusinessCardApp()
}