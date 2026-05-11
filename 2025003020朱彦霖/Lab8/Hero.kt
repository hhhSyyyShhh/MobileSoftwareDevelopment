package com.example.superheroes.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Hero(
    @get:StringRes val nameRes: Int,
    @get:StringRes val descriptionRes: Int,
    @get:DrawableRes val imageRes: Int
)