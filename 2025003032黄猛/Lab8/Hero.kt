package com.example.myapplication08.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Hero(
    @field:StringRes val nameRes: Int,
    @field:StringRes val descriptionRes: Int,
    @field:DrawableRes val imageRes: Int
)
