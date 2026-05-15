package com.example.dessertclicker

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dessertclicker.ui.theme.DessertclickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DessertclickerTheme {
                DessertClickerApp()
            }
        }
    }
}

private fun shareSoldDessertsInformation(
    intentContext: android.content.Context,
    dessertsSold: Int,
    revenue: Int
) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "I've sold $dessertsSold desserts and made $$revenue in Dessert Clicker!"
        )
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    intentContext.startActivity(shareIntent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DessertClickerApp(
    viewModel: DessertViewModel = viewModel()
) {
    val uiState = viewModel.uiState
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dessert Clicker") },
                actions = {
                    IconButton(onClick = {
                        shareSoldDessertsInformation(
                            context,
                            uiState.dessertsSold,
                            uiState.revenue
                        )
                    }) {
                        Icon(Icons.Filled.Share, contentDescription = "Share")
                    }
                },
                // 修复：使用新版的 topAppBarColors
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        DessertClickerScreen(
            revenue = uiState.revenue,
            dessertsSold = uiState.dessertsSold,
            dessertImageId = uiState.currentDessertImageId,
            onDessertClicked = { viewModel.onDessertClicked() },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun DessertClickerScreen(
    revenue: Int,
    dessertsSold: Int,
    dessertImageId: Int,
    onDessertClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable { onDessertClicked() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(dessertImageId),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Total Revenue: $$revenue",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Desserts Sold: $dessertsSold",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DessertClickerPreview() {
    DessertclickerTheme {
        DessertClickerScreen(
            revenue = 100,
            dessertsSold = 20,
            dessertImageId = R.drawable.cupcake,
            onDessertClicked = {}
        )
    }
}