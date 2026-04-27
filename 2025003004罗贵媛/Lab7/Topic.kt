package com.example.lab7.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

// 数据类：表示一个课程主题
data class Topic(
    @StringRes val name: Int,     // 主题名称（字符串资源ID）
    val availableCourses: Int,    // 课程数量
    @DrawableRes val imageRes: Int // 图片资源ID
)