package com.example.dessertclicker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dessertclicker.ui.theme.DessertUiState
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert

/**
 * DessertViewModel 负责管理 Dessert Clicker 的全部业务状态与逻辑。
 *
 * 设计原则：
 *  - UI 层（MainActivity / Composable）只读取 [uiState]，不直接修改任何状态。
 *  - 所有状态变更通过 ViewModel 暴露的方法（如 [onDessertClicked]）发起。
 *  - ViewModel 的生命周期与 Activity 解耦，屏幕旋转时状态自动保留。
 */
class DessertViewModel : ViewModel() {

    /**
     * 当前 UI 状态，由 Compose 运行时自动观察。
     * [private set] 保证外部只读，修改必须经过 ViewModel 内部方法。
     */
    var uiState by mutableStateOf(DessertUiState())
        private set

    /** 甜品数据源，列表按 startProductionAmount 升序排列 */
    private val desserts: List<Dessert> = Datasource.dessertList

    /**
     * 用户点击甜品时触发。
     *
     * 处理流程：
     * 1. 根据当前单价计算新收入。
     * 2. 累计已售数量。
     * 3. 调用 [determineDessertToShow] 判断是否需要升级甜品。
     * 4. 通过 [DessertUiState.copy] 生成新状态对象，触发 Compose 重组。
     */
    fun onDessertClicked() {
        val currentState = uiState

        val newRevenue = currentState.revenue + currentState.currentDessertPrice
        val newDessertsSold = currentState.dessertsSold + 1

        val dessertToShow = determineDessertToShow(newDessertsSold)

        uiState = currentState.copy(
            revenue = newRevenue,
            dessertsSold = newDessertsSold,
            currentDessertIndex = desserts.indexOf(dessertToShow),
            currentDessertImageId = dessertToShow.imageId,
            currentDessertPrice = dessertToShow.price
        )
    }

    /**
     * 根据累计已售数量决定当前应展示哪种甜品。
     *
     * 规则：遍历甜品列表，找到最后一个 [Dessert.startProductionAmount] ≤ [dessertsSold] 的甜品。
     * 若还未达到任何升级阈值，则返回列表中的第一款（cupcake）。
     *
     * @param dessertsSold 当前已售甜品总数
     * @return 应当展示的 [Dessert] 对象
     */
    private fun determineDessertToShow(dessertsSold: Int): Dessert {
        var dessertToShow = desserts.first()
        for (dessert in desserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                // 列表按升序排列，一旦不满足条件即可提前退出
                break
            }
        }
        return dessertToShow
    }
}