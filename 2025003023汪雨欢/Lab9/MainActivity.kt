package com.example.dessertclicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dessertclicker.ui.theme.DessertClickerTheme
import com.example.dessertclicker.ui.DessertUiState

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
    val uiState: DessertUiState = viewModel.uiState

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Dessert Clicker") }
            )
        }
    ) { padding ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DessertClickerScreen(
                revenue = uiState.revenue,
                dessertsSold = uiState.dessertsSold,
                dessertImageId = uiState.currentDessertImageId,
                onDessertClicked = { viewModel.onDessertClicked() },
                modifier = Modifier.padding(padding)
            )
        }
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
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TransactionInfo(revenue, dessertsSold, Modifier.padding(16.dp))
        Box(modifier = Modifier.weight(1f)) {
            FilledTonalButton(
                onClick = onDessertClicked,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Image(
                    painter = painterResource(dessertImageId),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun TransactionInfo(revenue: Int, dessertsSold: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Total Revenue: $$revenue", style = MaterialTheme.typography.headlineMedium)
        Text("Desserts Sold: $dessertsSold", style = MaterialTheme.typography.headlineMedium)
    }
}