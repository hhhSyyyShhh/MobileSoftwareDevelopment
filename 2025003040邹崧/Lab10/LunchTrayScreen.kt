package com.example.myapplicationlab10

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

enum class Screen(@StringRes val title: Int) {
    START(R.string.app_name),
    ENTREE(R.string.choose_entree),
    SIDE(R.string.choose_side_dish),
    DRINK(R.string.choose_accompaniment),
    CHECKOUT(R.string.order_checkout)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentScreen: Screen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun LunchTrayApp(
    viewModel: OrderViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.START.name
    )

    Scaffold(
        topBar = {
            AppTopBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.START.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.START.name) {
                StartOrderScreen(
                    onStartOrderButtonClicked = {
                        navController.navigate(Screen.ENTREE.name)
                    }
                )
            }

            composable(Screen.ENTREE.name) {
                EntreeMenuScreen(
                    onNext = { navController.navigate(Screen.SIDE.name) },
                    onCancel = {
                        navController.popBackStack(Screen.START.name, false)
                        viewModel.reset()
                    }
                )
            }

            composable(Screen.SIDE.name) {
                SideDishMenuScreen(
                    onNext = { navController.navigate(Screen.DRINK.name) },
                    onCancel = {
                        navController.popBackStack(Screen.START.name, false)
                        viewModel.reset()
                    }
                )
            }

            composable(Screen.DRINK.name) {
                AccompanimentMenuScreen(
                    onNext = { navController.navigate(Screen.CHECKOUT.name) },
                    onCancel = {
                        navController.popBackStack(Screen.START.name, false)
                        viewModel.reset()
                    }
                )
            }

            composable(Screen.CHECKOUT.name) {
                CheckoutScreen(
                    onDone = {
                        navController.popBackStack(Screen.START.name, false)
                        viewModel.reset()
                    },
                    viewModel = viewModel
                )
            }
        }
    }
}