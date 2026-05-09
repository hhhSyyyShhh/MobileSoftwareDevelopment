package com.example.dessertclicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dessertclicker.ui.theme.DessertClickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DessertClickerTheme {
                DessertClickerApp()
            }
        }
    }
}

// 甜点数据类
data class Dessert(
    val price: Int,
    val startProductionAmount: Int,
    @DrawableRes val imageId: Int
)

// 甜点列表（只用cupcake，避免资源不存在报错）
val desserts = listOf(
    Dessert(2, 0, R.drawable.cupcake)
)

@Composable
fun DessertClickerApp() {
    var revenue by remember { mutableStateOf(0) }
    var dessertsSold by remember { mutableStateOf(0) }

    val currentDessertIndex = dessertsSold % desserts.size
    val currentDessert = desserts[currentDessertIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // 收入和销量显示
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "总收入: $revenue", fontSize = 16.sp)
            Text(text = "已售出: $dessertsSold", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 甜点图片
        Image(
            painter = painterResource(id = currentDessert.imageId),
            contentDescription = "甜点",
            modifier = Modifier
                .size(200.dp)
                .clickable {
                    revenue += currentDessert.price
                    dessertsSold++
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 提示文字
        Text(
            text = "点击甜点来售卖它！",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}