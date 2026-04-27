package com.example.myapplication.model


import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * 课程主题数据类
 * @param nameRes 主题名称的字符串资源ID
 * @param courseCount 该主题下的课程数量
 * @param imageRes 主题图片的Drawable资源ID
 */
data class Topic(
    @StringRes val nameRes: Int,
    val courseCount: Int,
    @DrawableRes val imageRes: Int
)