package com.yxyn.myhomework2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val androidGreen = Color(0xFF3DDC84)
val lightGreenBackground = Color(0xFFE0F2E9)

val blackText = Color.Black

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightGreenBackground)
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            painter = painterResource(id = R.drawable.drawable),
            contentDescription = "头像",
            modifier = Modifier.size(100.dp),
            tint = androidGreen
        )

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "李太国",
            fontSize = 40.sp,
            color = blackText
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Android Developer Extraordinaire",
            fontSize = 16.sp,
            color = androidGreen, 
            letterSpacing = 1.sp
        )


        Spacer(modifier = Modifier.height(64.dp))

        ContactList(
            phone = "+86 18683332586",
            share = "@AndroidDev",
            email = "taiguoli@yunu.edu.cn"
        )
    }
}

@Composable
fun ContactItem(
    icon: ImageVector,
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = androidGreen,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            color = blackText
        )
    }
}

@Composable
fun ContactList(phone: String, share: String, email: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        ContactItem(icon = Icons.Default.Phone, text = phone)
        ContactItem(icon = Icons.Default.Share, text = share)
        ContactItem(icon = Icons.Default.Email, text = email)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}