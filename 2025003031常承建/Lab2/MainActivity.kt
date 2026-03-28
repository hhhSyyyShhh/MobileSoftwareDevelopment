package com.example.a2025003031
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFF073042)
            ) {
                BusinessCard()
            }
        }
    }
}

@Composable
fun BusinessCard() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        CardTop(name = "常承建", title = "Android 开发工程师")
        CardBottom(phone = "+86 15752407238", email = "3157267907@qq.com", social = "@wechat")
    }
}

@Composable
fun CardTop(name: String, title: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "头像",
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = name, color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title, color = Color(0xFF3DDC84), fontSize = 18.sp)
    }
}

@Composable
fun ContactRow(icon: androidx.compose.ui.graphics.vector.ImageVector, info: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF3DDC84),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = info, color = Color.White, fontSize = 16.sp)
    }
}

@Composable
fun CardBottom(phone: String, email: String, social: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(bottom = 60.dp)
    ) {
        ContactRow(icon = Icons.Default.Phone, info = phone)
        ContactRow(icon = Icons.Default.Email, info = email)
        ContactRow(icon = Icons.Default.Share, info = social)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BusinessCard()
}