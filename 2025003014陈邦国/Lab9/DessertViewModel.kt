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
        val currentState = uiState

        val newRevenue = currentState.revenue + currentState.currentDessertPrice
        val newDessertsSold = currentState.dessertsSold + 1

        val dessertToShow = determineDessertToShow(newDessertsSold)
        val newIndex = desserts.indexOf(dessertToShow)

        uiState = currentState.copy(
            revenue = newRevenue,
            dessertsSold = newDessertsSold,
            currentDessertIndex = newIndex,
            currentDessertImageId = dessertToShow.imageId,
            currentDessertPrice = dessertToShow.price
        )
    }

    private fun determineDessertToShow(dessertsSold: Int): Dessert {
        var dessertToShow = desserts.first()
        for (dessert in desserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                break
            }
        }
        return dessertToShow
    }
}