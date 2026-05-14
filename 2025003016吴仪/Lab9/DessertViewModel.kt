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

    // 甜品数据源
    private val desserts = Datasource.dessertList


     //处理甜品点击事件：更新销量、收入，切换甜品

    fun onDessertClicked() {
        val currentState = uiState

        // 计算新状态
        val newRevenue = currentState.revenue + currentState.currentDessertPrice
        val newDessertsSold = currentState.dessertsSold + 1
        val dessertToShow = determineDessertToShow(newDessertsSold)

        // 更新UI状态
        uiState = currentState.copy(
            revenue = newRevenue,
            dessertsSold = newDessertsSold,
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