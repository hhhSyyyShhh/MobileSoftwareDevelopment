package com.example.dessertclicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dessertclicker.ui.DessertUiState
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

@Composable
fun DessertClickerApp(
    viewModel: DessertViewModel = viewModel()
) {
    val uiState: DessertUiState = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "总收入: ${uiState.revenue}", fontSize = 16.sp)
            Text(text = "已售出: ${uiState.dessertsSold}", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = uiState.currentDessertImageId),
            contentDescription = "甜点",
            modifier = Modifier
                .size(200.dp)
                .clickable { viewModel.onDessertClicked() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "点击甜点来售卖它！",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}