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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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

private fun shareSoldDessertsInformation(intentContext: android.content.Context, dessertsSold: Int, revenue: Int) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, intentContext.getString(R.string.share_text, dessertsSold, revenue))
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    intentContext.startActivity(shareIntent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DessertClickerApp(
    viewModel: DessertViewModel = viewModel()
) {
    val uiState = viewModel.uiState
    val intentContext = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(
                        onClick = {
                            shareSoldDessertsInformation(
                                intentContext = intentContext,
                                dessertsSold = uiState.dessertsSold,
                                revenue = uiState.revenue
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = stringResource(R.string.share)
                        )
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

@Composable
fun DessertClickerScreen(
    revenue: Int,
    dessertsSold: Int,
    dessertImageId: Int,
    onDessertClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(dessertImageId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clickable { onDessertClicked() }
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.revenue, revenue),
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(R.string.desserts_sold, dessertsSold),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@Composable
fun DessertClickerScreenPreview() {
    DessertClickerTheme {
        DessertClickerScreen(
            revenue = 0,
            dessertsSold = 0,
            dessertImageId = R.drawable.cupcake,
            onDessertClicked = {}
        )
    }
}