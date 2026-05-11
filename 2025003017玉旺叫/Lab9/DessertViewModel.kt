package com.example.dessertclicker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dessertclicker.ui.DessertUiState

class DessertViewModel : ViewModel() {
    // 定义甜点列表
    private val desserts = listOf(
        Dessert(price = 2, startProductionAmount = 0, imageId = R.drawable.cupcake)
    )

    // 初始化UI状态（必须导入getValue/setValue扩展才能用by委托）
    var uiState by mutableStateOf(
        DessertUiState(
            currentDessertImageId = desserts[0].imageId,
            currentDessertPrice = desserts[0].price
        )
    )
        private set

    // 点击事件处理
    fun onDessertClicked() {
        val newRevenue = uiState.revenue + uiState.currentDessertPrice
        val newSold = uiState.dessertsSold + 1
        val newIndex = newSold % desserts.size
        val newDessert = desserts[newIndex]

        // 更新状态
        uiState = uiState.copy(
            revenue = newRevenue,
            dessertsSold = newSold,
            currentDessertIndex = newIndex,
            currentDessertImageId = newDessert.imageId,
            currentDessertPrice = newDessert.price
        )
    }
}

// 甜点数据模型（如果已在别处定义，可删除这段）
data class Dessert(
    val price: Int,
    val startProductionAmount: Int,
    val imageId: Int
)