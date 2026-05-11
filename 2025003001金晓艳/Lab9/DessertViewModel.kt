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

    // 这里用你 Datasource 里的变量名 dessertList
    private val desserts: List<Dessert> = Datasource.dessertList

    fun onDessertClicked() {
        val current = uiState
        val newRevenue = current.revenue + current.currentDessertPrice
        val newSold = current.dessertsSold + 1
        val newDessert = determineDessertToShow(newSold)

        uiState = current.copy(
            revenue = newRevenue,
            dessertsSold = newSold,
            currentDessertImageId = newDessert.imageId,
            currentDessertPrice = newDessert.price
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