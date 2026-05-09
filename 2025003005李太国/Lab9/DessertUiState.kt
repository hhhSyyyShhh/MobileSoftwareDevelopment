package com.example.dessertclicker.ui

import androidx.annotation.DrawableRes

data class DessertUiState(
    val revenue: Int = 0,
    val dessertsSold: Int = 0,
    val currentDessertIndex: Int = 0,
    @DrawableRes val currentDessertImageId: Int = com.example.dessertclicker.R.drawable.cupcake,
    val currentDessertPrice: Int = 5
)