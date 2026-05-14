package com.example.dessertclicker

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DessertClickerApp(
    viewModel: DessertViewModel = viewModel()
) {
    val uiState = viewModel.uiState
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Dessert Clicker") },
                actions = {
                    FloatingActionButton(
                        onClick = {
                            shareSoldDessertsInformation(
                                intentContext = context,
                                dessertsSold = uiState.dessertsSold,
                                revenue = uiState.revenue
                            )
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(Icons.Filled.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) { contentPadding ->
        DessertClickerScreen(
            revenue = uiState.revenue,
            dessertsSold = uiState.dessertsSold,
            dessertImageId = uiState.currentDessertImageId,
            onDessertClicked = { viewModel.onDessertClicked() },
            modifier = Modifier.padding(contentPadding)
        )
    }
}

/**
 * 甜品点击界面：纯展示层
 */
@Composable
private fun DessertClickerScreen(
    revenue: Int,
    dessertsSold: Int,
    dessertImageId: Int,
    onDessertClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 交易信息展示
            TransactionInfo(revenue = revenue, dessertsSold = dessertsSold)

            // 甜品图片（点击触发事件）
            Image(
                painter = painterResource(id = dessertImageId),
                contentDescription = "Dessert Image",
                modifier = Modifier
                    .padding(vertical = 32.dp)
                    .clickable { onDessertClicked() }
            )
        }
    }
}

@Composable
private fun TransactionInfo(
    revenue: Int,
    dessertsSold: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Total Revenue: $$revenue",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Desserts Sold: $dessertsSold",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

private fun shareSoldDessertsInformation(
    intentContext: android.content.Context,
    dessertsSold: Int,
    revenue: Int
) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT,
            "I've sold $dessertsSold desserts and made $$revenue! Play Dessert Clicker and see how much you can make!"
        )
    }
    intentContext.startActivity(Intent.createChooser(shareIntent, null))
}


@Preview(showBackground = true)
@Composable
fun DessertClickerPreview() {
    DessertClickerTheme {
        DessertClickerScreen(
            revenue = 100,
            dessertsSold = 20,
            dessertImageId = R.drawable.cupcake,
            onDessertClicked = {}
        )
    }
}