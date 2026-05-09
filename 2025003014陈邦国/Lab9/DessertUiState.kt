package com.example.myapplicationlab9

import androidx.annotation.DrawableRes
import com.example.myapplicationlab9.R

data class DessertUiState(
    val revenue: Int = 0,
    val dessertsSold: Int = 0,
    val currentDessertIndex: Int = 0,
    @DrawableRes val currentDessertImageId: Int = R.drawable.tianping,
    val currentDessertPrice: Int = 5
)