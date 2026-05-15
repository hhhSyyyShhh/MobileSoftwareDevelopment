package com.example.lunchtray

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lunchtray.datasource.DataSource
import com.example.lunchtray.ui.AccompanimentMenuScreen
import com.example.lunchtray.ui.CheckoutScreen
import com.example.lunchtray.ui.EntreeMenuScreen
import com.example.lunchtray.ui.OrderViewModel
import com.example.lunchtray.ui.SideDishMenuScreen
import com.example.lunchtray.ui.StartOrderScreen

enum class LunchTrayScreen(@StringRes val title: Int) {
    Start(R.string.app_name),
    Entree(R.string.choose_entree),
    SideDish(R.string.choose_side_dish),
    Accompaniment(R.string.choose_accompaniment),
    Checkout(R.string.order_checkout)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayAppBar(
    currentScreen: LunchTrayScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayApp(
    viewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // 获取当前页面
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = LunchTrayScreen.valueOf(
        backStackEntry?.destination?.route ?: LunchTrayScreen.Start.name
    )

    Scaffold(
        topBar = {
            LunchTrayAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = LunchTrayScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 开始页面
            composable(LunchTrayScreen.Start.name) {
                StartOrderScreen(
                    onStartOrderButtonClicked = {
                        navController.navigate(LunchTrayScreen.Entree.name) {
                            popUpTo(LunchTrayScreen.Start.name) { inclusive = true }
                        }
                    }
                )
            }

            // 主菜
            composable(LunchTrayScreen.Entree.name) {
                EntreeMenuScreen(
                    options = DataSource.entreeMenuItems,
                    onCancelButtonClicked = {
                        viewModel.resetOrder()
                        navController.navigate(LunchTrayScreen.Start.name) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    },
                    onNextButtonClicked = {
                        navController.navigate(LunchTrayScreen.SideDish.name)
                    },
                    onSelectionChanged = { viewModel.updateEntree(it) }
                )
            }

            // 配菜
            composable(LunchTrayScreen.SideDish.name) {
                SideDishMenuScreen(
                    options = DataSource.sideDishMenuItems,
                    onCancelButtonClicked = {
                        viewModel.resetOrder()
                        navController.navigate(LunchTrayScreen.Start.name) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    },
                    onNextButtonClicked = {
                        navController.navigate(LunchTrayScreen.Accompaniment.name)
                    },
                    onSelectionChanged = { viewModel.updateSideDish(it) }
                )
            }

            // 佐餐
            composable(LunchTrayScreen.Accompaniment.name) {
                AccompanimentMenuScreen(
                    options = DataSource.accompanimentMenuItems,
                    onCancelButtonClicked = {
                        viewModel.resetOrder()
                        navController.navigate(LunchTrayScreen.Start.name) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    },
                    onNextButtonClicked = {
                        navController.navigate(LunchTrayScreen.Checkout.name)
                    },
                    onSelectionChanged = { viewModel.updateAccompaniment(it) }
                )
            }

            // 结账
            composable(LunchTrayScreen.Checkout.name) {
                CheckoutScreen(
                    orderUiState = uiState,
                    onCancelButtonClicked = {
                        viewModel.resetOrder()
                        navController.navigate(LunchTrayScreen.Start.name) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    },
                    onNextButtonClicked = {
                        viewModel.resetOrder()
                        navController.navigate(LunchTrayScreen.Start.name) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}