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

package com.example.dessertclicker.ui

import androidx.annotation.DrawableRes
import com.example.dessertclicker.R

/**
 * UI 状态数据类，集中管理 Dessert Clicker 应用的所有界面状态
 *
 * @property revenue 当前总收入（美元）
 * @property dessertsSold 已售出的甜品数量
 * @property currentDessertIndex 当前甜品在 Datasource.dessertList 中的索引
 * @property currentDessertImageId 当前甜品图片资源 ID
 * @property currentDessertPrice 当前甜品单价（美元）
 */
data class DessertUiState(
    val revenue: Int = 0,
    val dessertsSold: Int = 0,
    val currentDessertIndex: Int = 0,
    @DrawableRes val currentDessertImageId: Int = R.drawable.cupcake,
    val currentDessertPrice: Int = 5
)
