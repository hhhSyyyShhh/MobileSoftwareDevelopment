package com.example.myapplication.model // 原superheroes→myapplication

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

// 英雄数据类：封装名称、描述、图片资源ID
data class Hero(
    @StringRes val nameRes: Int,      // 英雄名称（字符串资源ID）
    @StringRes val descriptionRes: Int,// 英雄描述（字符串资源ID）
    @DrawableRes val imageRes: Int    // 英雄图片（图片资源ID）
)