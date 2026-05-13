/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.dessertclicker

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert
import com.example.dessertclicker.ui.DessertUiState

/**
 * DessertViewModel 负责管理 Dessert Clicker 应用的所有业务逻辑和状态
 *
 * ViewModel 会保留其状态并在配置更改（如屏幕旋转）后自动恢复，
 * 使得 UI 层可以专注于展示数据而不必担心状态管理
 */
class DessertViewModel : ViewModel() {

    /**
     * UI 状态，使用 mutableStateOf 包装后 Compose 可以自动观察变化并重组界面
     * private set 保证了外部只能读取状态，修改只能通过 ViewModel 的方法进行
     */
    var uiState by mutableStateOf(DessertUiState())
        private set

    /**
     * 甜品列表数据
     */
    private val desserts = Datasource.dessertList

    /**
     * 处理甜品点击事件
     * - 更新当前销售收入
     * - 增加已售甜品数量
     * - 根据已售数量确定应展示的甜品类型
     */
    fun onDessertClicked() {
        val currentState = uiState

        // 计算新的收入和销售量
        val newRevenue = currentState.revenue + currentState.currentDessertPrice
        val newDessertsSold = currentState.dessertsSold + 1

        // 根据销售量确定应展示的甜品
        val dessertToShow = determineDessertToShow(newDessertsSold)

        // 更新 UI 状态，使用 copy() 创建新对象以触发 Compose 重组
        uiState = currentState.copy(
            revenue = newRevenue,
            dessertsSold = newDessertsSold,
            currentDessertImageId = dessertToShow.imageId,
            currentDessertPrice = dessertToShow.price
        )
    }

    /**
     * 根据已售数量决定当前应展示的甜品
     *
     * 甜品列表按 startProductionAmount 从小到大排序，
     * 遍历找到第一个使 dessertsSold >= startProductionAmount 的甜品
     *
     * @param dessertsSold 已售甜品数量
     * @return 应展示的甜品
     */
    private fun determineDessertToShow(dessertsSold: Int): Dessert {
        var dessertToShow = desserts.first()
        for (dessert in desserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                // 一旦遇到第一个 startProductionAmount 大于已售数量的甜品，
                // 后面的甜品由于列表已排序，必然也都大于，直接 break 即可
                break
            }
        }
        return dessertToShow
    }
}
