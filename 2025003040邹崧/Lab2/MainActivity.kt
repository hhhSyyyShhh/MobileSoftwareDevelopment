package com.example.myapplication

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
import androidx.compose.material3.Divider
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
            .fillMaxSize() // 这里改成了 fillMaxSize()
            .background(Color(0xFF073042)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CardTop(
            name = "邹崧",
            title = "Android 开发者"
        )
        CardBottom(
            phone = "2025003040",
            email = "zousong@example.com",
            handle = "@zousong"
        )
    }
}

@Composable
fun CardTop(name: String, title: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(100.dp))

        Image(
            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
            contentDescription = "头像",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = name,
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = title,
            color = Color(0xFF3DDC84),
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
fun ContactRow(icon: androidx.compose.ui.graphics.vector.ImageVector, info: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF3DDC84),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = info, color = Color.White, fontSize = 16.sp)
    }
}

@Composable
fun CardBottom(phone: String, email: String, handle: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Divider(color = Color.Gray)
        ContactRow(icon = Icons.Default.Phone, info = phone)
        Divider(color = Color.Gray)
        ContactRow(icon = Icons.Default.Email, info = email)
        Divider(color = Color.Gray)
        ContactRow(icon = Icons.Default.Share, info = handle)
        Divider(color = Color.Gray)
        Spacer(modifier = Modifier.height(60.dp))
    }
}