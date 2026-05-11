/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.example.lunchtray.ui.theme.LunchTrayTheme

/**
 * Enum class representing the screens in the Lunch Tray app.
 * Each screen has a title associated with it.
 */
enum class LunchTrayScreen(@StringRes val title: Int) {
    Start(R.string.app_name),
    Entree(R.string.choose_entree),
    SideDish(R.string.choose_side_dish),
    Accompaniment(R.string.choose_accompaniment),
    Checkout(R.string.order_checkout)
}

/**
 * Composable function for the top app bar with navigation support.
 *
 * @param currentScreen The current screen being displayed.
 * @param canNavigateBack Whether the back button should be shown.
 * @param navigateUp Function to call when the back button is clicked.
 * @param modifier Modifier to apply to the app bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayAppBar(
    currentScreen: LunchTrayScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(text = stringResource(currentScreen.title)) },
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

/**
 * Main composable function for the Lunch Tray app.
 * Sets up the navigation controller and hosts all the screens.
 */
@Composable
fun LunchTrayApp() {
    LunchTrayTheme {
        val navController = rememberNavController()
        
        // Get the current back stack entry to determine the current screen
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentScreen = LunchTrayScreen.valueOf(
            backStackEntry?.destination?.route ?: LunchTrayScreen.Start.name
        )
        
        // Check if we can navigate back (i.e., there's a previous screen)
        val canNavigateBack = navController.previousBackStackEntry != null
        
        Scaffold(
            topBar = {
                LunchTrayAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = canNavigateBack,
                    navigateUp = { navController.navigateUp() }
                )
            }
        ) { innerPadding ->
            val viewModel: OrderViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
            val uiState by viewModel.uiState.collectAsState()
            
            NavHost(
                navController = navController,
                startDestination = LunchTrayScreen.Start.name,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = LunchTrayScreen.Start.name) {
                    StartOrderScreen(
                        onStartOrderButtonClicked = {
                            navController.navigate(LunchTrayScreen.Entree.name) {
                                // Pop Start from the back stack so pressing back exits the app
                                popUpTo(LunchTrayScreen.Start.name) { inclusive = true }
                            }
                        }
                    )
                }
                
                composable(route = LunchTrayScreen.Entree.name) {
                    EntreeMenuScreen(
                        options = DataSource.entreeMenuItems,
                        onCancelButtonClicked = {
                            navController.navigate(LunchTrayScreen.Start.name) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                            }
                            viewModel.resetOrder()
                        },
                        onNextButtonClicked = {
                            navController.navigate(LunchTrayScreen.SideDish.name)
                        },
                        onSelectionChanged = { viewModel.updateEntree(it) }
                    )
                }
                
                composable(route = LunchTrayScreen.SideDish.name) {
                    SideDishMenuScreen(
                        options = DataSource.sideDishMenuItems,
                        onCancelButtonClicked = {
                            navController.navigate(LunchTrayScreen.Start.name) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                            }
                            viewModel.resetOrder()
                        },
                        onNextButtonClicked = {
                            navController.navigate(LunchTrayScreen.Accompaniment.name)
                        },
                        onSelectionChanged = { viewModel.updateSideDish(it) }
                    )
                }
                
                composable(route = LunchTrayScreen.Accompaniment.name) {
                    AccompanimentMenuScreen(
                        options = DataSource.accompanimentMenuItems,
                        onCancelButtonClicked = {
                            navController.navigate(LunchTrayScreen.Start.name) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                            }
                            viewModel.resetOrder()
                        },
                        onNextButtonClicked = {
                            navController.navigate(LunchTrayScreen.Checkout.name)
                        },
                        onSelectionChanged = { viewModel.updateAccompaniment(it) }
                    )
                }
                
                composable(route = LunchTrayScreen.Checkout.name) {
                    CheckoutScreen(
                        orderUiState = uiState,
                        onCancelButtonClicked = {
                            navController.navigate(LunchTrayScreen.Start.name) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                            }
                            viewModel.resetOrder()
                        },
                        onNextButtonClicked = {
                            navController.navigate(LunchTrayScreen.Start.name) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                            }
                            viewModel.resetOrder()
                        }
                    )
                }
            }
        }
    }
}