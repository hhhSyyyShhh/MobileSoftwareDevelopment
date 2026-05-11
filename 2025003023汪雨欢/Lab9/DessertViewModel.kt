package com.example.dessertclicker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert
import com.example.dessertclicker.ui.DessertUiState

class DessertViewModel : ViewModel() {
    var uiState by mutableStateOf(DessertUiState())
        private set

    private val desserts = Datasource.dessertList

    fun onDessertClicked() {
        val current = uiState
        val newRevenue = current.revenue + current.currentDessertPrice
        val newSold = current.dessertsSold + 1
        val dessert = determineDessertToShow(newSold)

        uiState = current.copy(
            revenue = newRevenue,
            dessertsSold = newSold,
            currentDessertImageId = dessert.imageId,
            currentDessertPrice = dessert.price,
            currentDessertIndex = desserts.indexOf(dessert)
        )
    }

    private fun determineDessertToShow(dessertsSold: Int): Dessert {
        var show = desserts.first()
        for (d in desserts) {
            if (dessertsSold >= d.startProductionAmount) {
                show = d
            } else break
        }
        return show
    }
}