package com.example.dessertclicker.ui.theme

import androidx.annotation.DrawableRes
import com.example.dessertclicker.R

/**
 * 描述 Dessert Clicker 界面所需的全部 UI 状态。
 * 使用 data class 保证每次状态变更都能创建新对象，触发 Compose 重组。
 */
data class DessertUiState(
    /** 用户当前的总收入（美元） */
    val revenue: Int = 0,

    /** 已售出的甜品总数 */
    val dessertsSold: Int = 0,

    /** 当前甜品在 Datasource.dessertList 中的索引（保留，方便调试） */
    val currentDessertIndex: Int = 0,

    /** 当前甜品图片资源 ID，初始为 cupcake */
    @DrawableRes val currentDessertImageId: Int = R.drawable.cupcake,

    /** 当前甜品单价，初始为 $5（cupcake 的价格） */
    val currentDessertPrice: Int = 5
)