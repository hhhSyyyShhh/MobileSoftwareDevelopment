DataSource.kt（添加注释版）

package com.yxyn.mhomework71.data

import com.yxyn.mhomework71.model.Topic

/**
 * 数据源单例类
 * 提供应用所需的所有课程主题静态数据，供UI层网格布局调用
 * 采用object单例模式，确保全局只有一个数据源实例，提升性能
 */
object DataSource {
    /**
     * 课程主题列表
     * 存储24个课程主题数据，每个主题包含名称、可用课程数、图片资源ID
     * 与Topic数据类字段一一对应，确保数据一致性
     */
    val topics = listOf(
        // 建筑课程主题：名称、课程数、对应图片资源
        Topic(
            name = R.string.architecture,
            availableCourses = 58,
            imageResId = R.drawable.architecture
        ),
        // 汽车课程主题
        Topic(
            name = R.string.automotive,
            availableCourses = 30,
            imageResId = R.drawable.automotive
        ),
        // 生物课程主题
        Topic(
            name = R.string.biology,
            availableCourses = 90,
            imageResId = R.drawable.biology
        ),
        // 手工艺课程主题
        Topic(
            name = R.string.crafts,
            availableCourses = 121,
            imageResId = R.drawable.crafts
        ),
        // 商业课程主题
        Topic(
            name = R.string.business,
            availableCourses = 78,
            imageResId = R.drawable.business
        ),
        // 烹饪课程主题
        Topic(
            name = R.string.culinary,
            availableCourses = 118,
            imageResId = R.drawable.culinary
        ),
        // 设计课程主题
        Topic(
            name = R.string.design,
            availableCourses = 423,
            imageResId = R.drawable.design
        ),
        // 生态学课程主题
        Topic(
            name = R.string.ecology,
            availableCourses = 28,
            imageResId = R.drawable.ecology
        ),
        // 工程课程主题
        Topic(
            name = R.string.engineering,
            availableCourses = 67,
            imageResId = R.drawable.engineering
        ),
        // 时尚课程主题
        Topic(
            name = R.string.fashion,
            availableCourses = 92,
            imageResId = R.drawable.fashion
        ),
        // 金融课程主题
        Topic(
            name = R.string.finance,
            availableCourses = 100,
            imageResId = R.drawable.finance
        ),
        // 电影课程主题
        Topic(
            name = R.string.film,
            availableCourses = 165,
            imageResId = R.drawable.film
        ),
        // 游戏课程主题
        Topic(
            name = R.string.gaming,
            availableCourses = 37,
            imageResId = R.drawable.gaming
        ),
        // 地质学课程主题
        Topic(
            name = R.string.geology,
            availableCourses = 290,
            imageResId = R.drawable.geology
        ),
        // 绘画课程主题
        Topic(
            name = R.string.drawing,
            availableCourses = 326,
            imageResId = R.drawable.drawing
        ),
        // 历史课程主题
        Topic(
            name = R.string.history,
            availableCourses = 189,
            imageResId = R.drawable.history
        ),
        // 新闻课程主题
        Topic(
            name = R.string.journalism,
            availableCourses = 96,
            imageResId = R.drawable.journalism
        ),
        // 法律课程主题
        Topic(
            name = R.string.law,
            availableCourses = 58,
            imageResId = R.drawable.law
        ),
        // 生活方式课程主题
        Topic(
            name = R.string.lifestyle,
            availableCourses = 305,
            imageResId = R.drawable.lifestyle
        ),
        // 音乐课程主题
        Topic(
            name = R.string.music,
            availableCourses = 212,
            imageResId = R.drawable.music
        ),
        // 绘画（油画）课程主题
        Topic(
            name = R.string.painting,
            availableCourses = 172,
            imageResId = R.drawable.painting
        ),
        // 摄影课程主题
        Topic(
            name = R.string.photography,
            availableCourses = 321,
            imageResId = R.drawable.photography
        ),
        // 物理课程主题
        Topic(
            name = R.string.physics,
            availableCourses = 321,
            imageResId = R.drawable.physics
        ),
        // 科技课程主题
        Topic(
            name = R.string.tech,
            availableCourses = 118,
            imageResId = R.drawable.tech
        )
    )
}
