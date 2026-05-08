package com.example.superheroes.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * 超级英雄数据类
 * 用于描述单个超级英雄列表项
 *
 * @param nameRes 英雄名称字符串资源ID
 * @param descriptionRes 英雄说明字符串资源ID
 * @param imageRes 英雄图片资源ID
 */
data class Hero(
    @StringRes val nameRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val imageRes: Int
)
