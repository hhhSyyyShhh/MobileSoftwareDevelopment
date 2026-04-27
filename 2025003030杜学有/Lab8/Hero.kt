package com.example.superheroes.model


import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.superheroes.R

// 英雄数据类
data class Hero(
    @StringRes val nameRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val imageRes: Int
)