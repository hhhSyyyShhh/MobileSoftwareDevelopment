package com.yxyn.mhomework71.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * 课程主题数据模型
 * 用于封装单个课程主题的核心信息，供网格布局展示使用
 */
data class Topic(
    /**
     * 课程主题名称的字符串资源ID
     * 标注@StringRes确保传入的是正确的字符串资源，保证类型安全
     */
    @StringRes val name: Int,
    /**
     * 该课程主题下的可用课程数量
     * 以整型存储，直接对应实际课程数量，语义清晰
     */
    val availableCourses: Int,
    /**
     * 课程主题对应图片的资源ID
     * 标注@DrawableRes确保传入的是正确的图片资源，避免运行时错误
     */
    @DrawableRes val imageResId: Int
)