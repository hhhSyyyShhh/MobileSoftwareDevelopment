package com.example.superheroes.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Hero 数据类
 *
 * 用 @StringRes / @DrawableRes 可以让编译器帮我们检查资源类型，避免把图片 ID 当字符串 ID 传进去。
 */
data class Hero(
    @StringRes val nameRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val imageRes: Int
)