package com.example.myapplicationlab9

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplicationlab9.data.Datasource
import com.example.myapplicationlab9.model.Dessert

class DessertViewModel : ViewModel() {
    var uiState by mutableStateOf(DessertUiState())
        private set

    private val desserts = Datasource.dessertList

    fun onDessertClicked() {
        val current = uiState
        val newSold = current.dessertsSold + 1
        val newRevenue = current.revenue + current.currentDessertPrice
        val newDessert = determineDessertToShow(newSold)

        uiState = current.copy(
            revenue = newRevenue,
            dessertsSold = newSold,
            currentDessertIndex = desserts.indexOf(newDessert),
            currentDessertImageId = newDessert.imageId,
            currentDessertPrice = newDessert.price
        )
    }

    private fun determineDessertToShow(
        dessertsSold: Int
    ): Dessert {
        var current = desserts.first()
        for (d in desserts) {
            if (dessertsSold >= d.startProductionAmount) {
                current = d
            } else break
        }
        return current
    }
}