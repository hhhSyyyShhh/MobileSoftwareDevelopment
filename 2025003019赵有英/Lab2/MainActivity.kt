package com.example.lab2

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.lab2.ui.theme.Lab2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab2Theme {
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
            .background(Color(0xFFE6F7F0)), // 浅绿色背景
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CardTop(
            name = "赵有英",
            title = "Android 开发学习者"
        )

        Spacer(modifier = Modifier.height(50.dp))

        CardBottom(
            phone = "17874647512",
            email = "zhaoyouying@school.com",
            handle = "@zhaoyouying"
        )
    }
}

@Composable
fun CardTop(name: String, title: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(130.dp)
                .background(Color(0xFF4CAF50), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable._68975f399642b7d50729fe2f5aa13be), 
                contentDescription = "个人头像",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = name,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E3B37)
        )

        Text(
            text = title,
            fontSize = 18.sp,
            color = Color(0xFF4CAF50),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CardBottom(phone: String, email: String, handle: String) {
    Column {
        ContactRow(icon = Icons.Default.Phone, info = phone)
        ContactRow(icon = Icons.Default.Email, info = email)
        ContactRow(icon = Icons.Default.Share, info = handle)
    }
}

@Composable
fun ContactRow(icon: androidx.compose.ui.graphics.vector.ImageVector, info: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF388E3C),
            modifier = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = info,
            fontSize = 17.sp,
            color = Color(0xFF333333)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BusinessCardPreview() {
    Lab2Theme {
        BusinessCard()
    }
}