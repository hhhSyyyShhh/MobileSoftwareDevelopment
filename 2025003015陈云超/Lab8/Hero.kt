package com.example.superheroes.model

import androidx.annotation.DrawableRes

data class Hero(
    val nameRes: Int,
    val descriptionRes: Int,
    @DrawableRes val imageRes: Int
)