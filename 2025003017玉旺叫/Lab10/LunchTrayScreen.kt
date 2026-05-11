package com.example.lunchtray

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lunchtray.ui.*

enum class LunchTrayScreen {
    Start, EntreeMenu, SideMenu, AccompanimentMenu, Checkout
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text("Lunch Tray") },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                }
            }
        }
    )
}

@Composable
fun LunchTrayApp() {
    val navController: NavHostController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = enumValueOf<LunchTrayScreen>(
        backStackEntry?.destination?.route ?: LunchTrayScreen.Start.name
    )

    Scaffold(
        topBar = {
            LunchTrayAppBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LunchTrayScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(LunchTrayScreen.Start.name) {
                StartOrderScreen(
                    onStart = { navController.navigate(LunchTrayScreen.EntreeMenu.name) }
                )
            }
            composable(LunchTrayScreen.EntreeMenu.name) {
                EntreeMenuScreen(
                    onNext = { navController.navigate(LunchTrayScreen.SideMenu.name) },
                    onCancel = { navController.popBackStack() }
                )
            }
            composable(LunchTrayScreen.SideMenu.name) {
                SideDishMenuScreen(
                    onNext = { navController.navigate(LunchTrayScreen.AccompanimentMenu.name) },
                    onCancel = { navController.popBackStack() }
                )
            }
            composable(LunchTrayScreen.AccompanimentMenu.name) {
                AccompanimentMenuScreen(
                    onNext = { navController.navigate(LunchTrayScreen.Checkout.name) },
                    onCancel = { navController.popBackStack() }
                )
            }
            composable(LunchTrayScreen.Checkout.name) {
                CheckoutScreen(
                    onSubmit = { navController.popBackStack(LunchTrayScreen.Start.name, false) },
                    onCancel = { navController.popBackStack() }
                )
            }
        }
    }
}