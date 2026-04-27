package com.example.courses
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Topic(
    @StringRes val nameId: Int,
    val courseCount: Int,
    @DrawableRes val imageId: Int
)