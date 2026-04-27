package com.example.course.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Topic(
    @StringRes val nameRes: Int,
    val courses: Int,
    @DrawableRes val imageRes: Int
)