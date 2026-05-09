package com.example.dessertclicker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert
import com.example.dessertclicker.ui.DessertUiState

class DessertViewModel : ViewModel() {
    private val desserts = Datasource.dessertList
    var uiState by mutableStateOf(DessertUiState(currentDessert = desserts.first()))
        private set

    fun onDessertClicked() {
        val newRevenue = uiState.revenue + uiState.currentDessert.price
        val newSold = uiState.dessertsSold + 1
        val newDessert = determineDessertToShow(newSold)
        uiState = uiState.copy(revenue = newRevenue, dessertsSold = newSold, currentDessert = newDessert)
    }

    private fun determineDessertToShow(dessertsSold: Int): Dessert {
        var dessertToShow = desserts.first()
        for (dessert in desserts) {
            if (dessertsSold >= dessert.startProductionAmount) dessertToShow = dessert
            else break
        }
        return dessertToShow
    }
}