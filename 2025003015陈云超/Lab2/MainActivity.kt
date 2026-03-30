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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.businesscard.ui.theme.BusinessCardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BusinessCardTheme {

                BusinessCard()
            }
        }
    }
}

@Composable
fun BusinessCard() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF073042)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        CardTop(
            name = "陈云超",
            title = "学生"
        )
        CardBottom(
            phone = "+86 151-9834-6675",
            email = "1548064420@qq.com",
            social = "@chenyunchao_dev"
        )
    }
}

@Composable
fun CardTop(name: String, title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "个人头像",
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = name,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 20.dp)
        )

        Text(
            text = title,
            color = Color(0xFF3DDC84),
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun CardBottom(phone: String, email: String, social: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 60.dp)
    ) {
        ContactItem(icon = Icons.Default.Phone, info = phone)
        ContactItem(icon = Icons.Default.Email, info = email)
        ContactItem(icon = Icons.Default.Share, info = social)
    }
}


@Composable
fun ContactItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    info: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 16.dp),
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
            fontSize = 16.sp
        )
    }
}