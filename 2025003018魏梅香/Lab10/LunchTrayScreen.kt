package com.example.lunchtray

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

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
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "返回"
                    )
                }
            }
        }
    )
}

@Composable
fun LunchTrayApp(
    viewModel: OrderViewModel = viewModel()
) {
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

@Composable
fun StartOrderScreen(onStart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "欢迎点餐", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = onStart) {
            Text("开始点餐")
        }
    }
}

@Composable
fun EntreeMenuScreen(onNext: () -> Unit, onCancel: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("主菜选择页面")
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = onNext) { Text("下一步") }
        Spacer(modifier = Modifier.size(8.dp))
        Button(onClick = onCancel) { Text("取消") }
    }
}

@Composable
fun SideDishMenuScreen(onNext: () -> Unit, onCancel: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("配菜选择页面")
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = onNext) { Text("下一步") }
        Spacer(modifier = Modifier.size(8.dp))
        Button(onClick = onCancel) { Text("取消") }
    }
}

@Composable
fun AccompanimentMenuScreen(onNext: () -> Unit, onCancel: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("饮品选择页面")
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = onNext) { Text("下一步") }
        Spacer(modifier = Modifier.size(8.dp))
        Button(onClick = onCancel) { Text("取消") }
    }
}

@Composable
fun CheckoutScreen(onSubmit: () -> Unit, onCancel: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("订单结账页面")
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = onSubmit) { Text("提交订单") }
        Spacer(modifier = Modifier.size(8.dp))
        Button(onClick = onCancel) { Text("取消") }
    }
}