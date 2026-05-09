package com.example.dessertclicker.ui

/**
 * 数据类 DessertUiState
 * 用于统一管理 DessertClicker 应用的 UI 状态
 *
 * 包含所有界面需要展示的数据，如收入、销量、当前甜点信息等
 * 实现单一数据源原则，确保 UI 状态的一致性和可维护性
 */
data class DessertUiState(
    // 当前总收入，初始值为0
    val revenue: Int = 0,

    // 已售出的甜点总数，初始值为0
    val dessertsSold: Int = 0,

    // 当前展示的甜点在列表中的索引，初始值为0（对应第一个甜点）
    val currentDessertIndex: Int = 0,

    // 当前甜点的图片资源ID，初始值为纸杯蛋糕
    val currentDessertImageId: Int = com.example.dessertclicker.R.drawable.cupcake,

    // 当前甜点的单价，初始值为纸杯蛋糕的价格
    val currentDessertPrice: Int = 2
)