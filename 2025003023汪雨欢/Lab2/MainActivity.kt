package com.example.businesscard

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
            BusinessCard()
        }
    }
}

@Composable
fun BusinessCard() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFA6D0F1)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CardTop(
            name = "汪雨欢",
            title = "Android 开发工程师"
        )
        CardBottom(
            phone = "+86 182-1234-5678",
            email = "171633421@qq.com",
            social = "171633421@qq.com"
        )
    }
}

@Composable
fun CardTop(name: String, title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 120.dp, bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.screenshot),
            contentDescription = "个人头像",
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = name,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = title,
            color = Color(0xFFFFFFFF),
            fontSize = 20.sp
        )
    }
}

@Composable
fun CardBottom(phone: String, email: String, social: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ContactRow(icon = Icons.Default.Phone, info = phone)
        ContactRow(icon = Icons.Default.Email, info = email)
        ContactRow(icon = Icons.Default.Share, info = social)
    }
}

@Composable
fun ContactRow(icon: androidx.compose.ui.graphics.vector.ImageVector, info: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF3DDC84),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = info,
            color = Color.White,
            fontSize = 20.sp
        )
    }
}