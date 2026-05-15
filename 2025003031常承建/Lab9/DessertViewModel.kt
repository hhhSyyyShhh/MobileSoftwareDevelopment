package com.example.dessertclicker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert
import com.example.dessertclicker.ui.DessertUiState

class DessertViewModel : ViewModel() {

    // 对外只读，对内可写的 UI 状态
    var uiState by mutableStateOf(DessertUiState())
        private set

    // 甜品数据源
    private val desserts = Datasource.dessertList

    // 处理甜品点击事件
    fun onDessertClicked() {
        val currentState = uiState

        // 计算新收入与销量
        val newRevenue = currentState.revenue + currentState.currentDessertPrice
        val newDessertsSold = currentState.dessertsSold + 1

        // 确定当前应展示的甜品
        val dessertToShow = determineDessertToShow(newDessertsSold)

        // 更新状态
        uiState = currentState.copy(
            revenue = newRevenue,
            dessertsSold = newDessertsSold,
            currentDessertImageId = dessertToShow.imageId,
            currentDessertPrice = dessertToShow.price
        )
    }

    // 业务逻辑：根据销量切换甜品
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