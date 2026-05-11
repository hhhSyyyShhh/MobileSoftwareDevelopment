package com.example.dessertclicker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert
import com.example.dessertclicker.ui.DessertUiState

class DessertViewModel : ViewModel() {

    /** 通过 mutableStateOf 持有 UI 状态，Compose 会自动观察变化 */
    var uiState by mutableStateOf(DessertUiState())
        private set

    /** 甜品列表数据（从数据源获取） */
    private val desserts = Datasource.dessertList

    /**
     * 处理甜品点击事件：点击后更新收入、销量，切换甜品
     */
    fun onDessertClicked() {
        val currentState = uiState

        // 计算新的收入和销售量
        val newRevenue = currentState.revenue + currentState.currentDessertPrice
        val newDessertsSold = currentState.dessertsSold + 1

        // 根据销售量确定应展示的甜品
        val dessertToShow = determineDessertToShow(newDessertsSold)

        // 更新 UI 状态（用 copy 方法，保证状态不可变，触发 Compose 重组）
        uiState = currentState.copy(
            revenue = newRevenue,
            dessertsSold = newDessertsSold,
            currentDessertImageId = dessertToShow.imageId,
            currentDessertPrice = dessertToShow.price
        )
    }

    /**
     * 根据已售数量决定当前应展示的甜品
     */
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