# Lab8 实验报告 —— Superheroes App

## 一、实验名称

Lab8 - Superheroes App (Jetpack Compose)

---

## 二、实验目标

1. 学习 Jetpack Compose 基础界面开发
2. 学习 Material 3 UI 设计
3. 学习 LazyColumn 列表布局
4. 学习 Kotlin data class 数据结构设计
5. 学习 Compose 中的主题与字体配置
6. 学习 Android 深色模式适配

---

## 三、实验环境

| 项目 | 内容 |
|---|---|
| IDE | Android Studio |
| Language | Kotlin |
| UI Framework | Jetpack Compose |
| SDK | Android API 24+ |
| Emulator | MuMu Emulator |

---

## 四、项目结构

```text
com.example.superheroes
│
├── data
│   └── HeroesRepository.kt
│
├── model
│   └── Hero.kt
│
├── ui
│   ├── HeroesScreen.kt
│   └── theme
│       ├── Color.kt
│       ├── Shape.kt
│       ├── Theme.kt
│       └── Type.kt
│
└── MainActivity.kt