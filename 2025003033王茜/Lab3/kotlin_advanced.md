# Lab3：Kotlin 进阶语法练习

## 实验背景

本次实验涵盖 Kotlin 编程的进阶语法，包括条件语句（`if/else`、`when`）、可空类型处理、类与对象、继承以及 Lambda 表达式。
建议使用 [Kotlin Playground](https://play.kotlinlang.org/) 在线编写并运行代码，完成后将代码粘贴到对应代码块中提交。

---

## 前提条件

- 熟悉 [Kotlin Playground](https://play.kotlinlang.org/)
- 能够定义和调用函数
- 了解 Kotlin 编程基础知识，包括变量以及 `println()` 和 `main()` 函数
- 熟悉 Kotlin 条件语句，包括 `if/else` 和 `when` 语句和表达式
- 熟悉 Kotlin Lambda 表达式
- 了解如何处理可为 null 的变量
- 了解如何创建 Kotlin 类和对象

---

## 提交要求

```
学号姓名/
└── Lab3/
    └── kotlin_advanced.md    # 将答案填入本文件各题代码块后提交
```

截止时间：**2026-4-10** (下午 18:00)，届时关于 Lab3 的 PR 请求将不会被合并。

---

## 题目 1：移动设备通知

通常情况下，手机会为用户提供通知摘要。

请在以下初始代码中，编写相应程序来根据所收到通知的数量输出摘要消息。该消息应显示以下内容：

- 如果通知少于 100 条，显示确切的通知数量。
- 如果通知等于或超过 100 条，通知数量显示为 `99+`。

**初始代码：**

```kotlin
fun main() {
    val morningNotification = 51
    val eveningNotification = 135
    
    printNotificationSummary(morningNotification)
    printNotificationSummary(eveningNotification)
}

fun printNotificationSummary(numberOfMessages: Int) {
    // Fill in the code.
}
```

**预期输出：**

```
You have 51 notifications.
Your phone is blowing up! You have 99+ notifications.
```

**你的答案：**
```kotlin
// 请将完整代码粘贴至此
fun main() {
    val morningNotification = 51
    val eveningNotification = 135

    printNotificationSummary(morningNotification)
    printNotificationSummary(eveningNotification)
}

fun printNotificationSummary(numberOfMessages: Int) {
    if (numberOfMessages < 100) {
        println("You have $numberOfMessages notifications.")
    } else {
        println("Your phone is blowing up! You have 99+ notifications.")
    }
}

```

---

## 题目 2：电影票价格

电影票的价格通常因电影观众的年龄而异。

请在以下初始代码中，编写相应程序来根据年龄计算电影票价格：

- 对于 12 岁及以下的观众，按儿童票计算，价格为 15 美元。
- 对于年龄介于 13-60 岁的观众，按标准票计算，价格为 30 美元。每逢周一，针对这个年龄段，将标准票的价格打折至 25 美元。
- 对于年龄在 61 岁及以上的观众，按老年票计算，价格为 20 美元。假设电影观众的最高年龄为 100 岁。
- 如果用户输入的年龄超出规定的年龄范围，使用 `-1` 值来指明价格无效。

**初始代码：**

```kotlin
fun main() {
    val child = 5
    val adult = 28
    val senior = 87
    
    val isMonday = true
    
    println("The movie ticket price for a person aged $child is \$${ticketPrice(child, isMonday)}.")
    println("The movie ticket price for a person aged $adult is \$${ticketPrice(adult, isMonday)}.")
    println("The movie ticket price for a person aged $senior is \$${ticketPrice(senior, isMonday)}.")
}

fun ticketPrice(age: Int, isMonday: Boolean): Int {
    // Fill in the code.
}
```

**预期输出：**

```
The movie ticket price for a person aged 5 is $15.
The movie ticket price for a person aged 28 is $25.
The movie ticket price for a person aged 87 is $20.
```

**你的答案：**

```kotlin
// 请将完整代码粘贴至此
fun main() {
    val child = 5
    val adult = 28
    val senior = 87

    val isMonday = true

    println("The movie ticket price for a person aged $child is \$${ticketPrice(child, isMonday)}.")
    println("The movie ticket price for a person aged $adult is \$${ticketPrice(adult, isMonday)}.")
    println("The movie ticket price for a person aged $senior is \$${ticketPrice(senior, isMonday)}.")
}

fun ticketPrice(age: Int, isMonday: Boolean): Int {
    return when {
        age < 0 || age > 100 -> -1
        age <= 12 -> 15
        age in 13..60 -> if (isMonday) 25 else 30
        age in 61..100 -> 20
        else -> -1
    }
}
```

---

## 题目 3：温度转换器

目前全球主要使用以下三种温标：摄氏度、华氏度和开尔文。

请在以下初始代码中，编写相应程序来按照下列公式在不同的温标之间转换温度：

- 摄氏度转换为华氏度：°F = 9/5 (°C) + 32
- 开尔文转换为摄氏度：°C = K - 273.15
- 华氏度转换为开尔文：K = 5/9 (°F - 32) + 273.15

请注意，`String.format("%.2f", /* measurement */)` 方法用于将数字转换为具有 2 位小数的 `String` 类型。

**初始代码：**

```kotlin
fun main() {
    // Fill in the code.
}

fun printFinalTemperature(
    initialMeasurement: Double,
    initialUnit: String,
    finalUnit: String,
    conversionFormula: (Double) -> Double
) {
    val finalMeasurement = String.format("%.2f", conversionFormula(initialMeasurement)) // two decimal places
    println("$initialMeasurement degrees $initialUnit is $finalMeasurement degrees $finalUnit.")
}
```

完成 `main()` 函数，使其调用 `printFinalTemperature()` 函数并输出以下代码行。您需要传递温度和转换公式的实参。

> **提示**：您可能需要使用 `Double` 值来避免除法运算期间 `Integer` 被截断。

**预期输出：**

```
27.0 degrees Celsius is 80.60 degrees Fahrenheit.
350.0 degrees Kelvin is 76.85 degrees Celsius.
10.0 degrees Fahrenheit is 260.93 degrees Kelvin.
```

**你的答案：**

```kotlin
// 请将完整代码粘贴至此
fun main() {
    printFinalTemperature(
        initialMeasurement = 27.0,
        initialUnit = "Celsius",
        finalUnit = "Fahrenheit",
        conversionFormula = { celsius -> 9.0 / 5.0 * celsius + 32 }
    )
    printFinalTemperature(
        initialMeasurement = 350.0,
        initialUnit = "Kelvin",
        finalUnit = "Celsius",
        conversionFormula = { kelvin -> kelvin - 273.15 }
    )
    printFinalTemperature(
        initialMeasurement = 10.0,
        initialUnit = "Fahrenheit",
        finalUnit = "Kelvin",
        conversionFormula = { fahrenheit -> 5.0 / 9.0 * (fahrenheit - 32) + 273.15 }
    )
}

fun printFinalTemperature(
    initialMeasurement: Double,
    initialUnit: String,
    finalUnit: String,
    conversionFormula: (Double) -> Double
) {
    val finalMeasurement = String.format("%.2f", conversionFormula(initialMeasurement)) // two decimal places
    println("$initialMeasurement degrees $initialUnit is $finalMeasurement degrees $finalUnit.")
}
```

---

## 题目 4：歌曲目录

假设您需要创建一个音乐播放器应用。

请创建一个可代表歌曲结构的类。`Song` 类必须包含以下代码元素：

- 歌名、音乐人、发行年份和播放次数的属性
- 用于指明歌曲是否流行的属性。如果播放次数少于 1,000，则将相应歌曲视为不流行。
- 一个按照以下格式输出歌曲介绍的方法：

```
"[歌名], performed by [音乐人], was released in [发行年份]."
```

**你的答案：**

```kotlin
// 请将完整代码粘贴至此
class Song(
    val title: String,
    val artist: String,
    val yearReleased: Int,
    val playCount: Int
) {
    val isPopular: Boolean
        get() = playCount >= 1000
    fun printSongDescription() {
        println("$title, performed by $artist, was released in $yearReleased.")
    }
}

fun main() {
    val song1 = Song("晴天", "周杰伦", 2003, 800)
    song1.printSongDescription()
    val song2 = Song("七里香", "周杰伦", 2004, 1500000)
    song2.printSongDescription()
}
```

---

## 题目 5：互联网个人资料

人们常常需要从在线网站中填写个人资料，其中会包含必填字段和选填字段。例如，您可以添加个人信息，并关联到推荐您注册相应个人资料的其他用户。

请在以下初始代码中，编写相应程序来输出用户的个人资料详细信息。

**初始代码：**

```kotlin
fun main() {
    val amanda = Person("Amanda", 33, "play tennis", null)
    val atiqah = Person("Atiqah", 28, "climb", amanda)
    
    amanda.showProfile()
    atiqah.showProfile()
}

class Person(val name: String, val age: Int, val hobby: String?, val referrer: Person?) {
    fun showProfile() {
        // Fill in code
    }
}
```

完成 `showProfile()` 函数，让程序输出以下代码行：

**预期输出：**

```
Name: Amanda
Age: 33
Likes to play tennis. Doesn't have a referrer.

Name: Atiqah
Age: 28
Likes to climb. Has a referrer named Amanda, who likes to play tennis.
```

**你的答案：**

```kotlin
// 请将完整代码粘贴至此
fun main() {
    val amanda = Person("Amanda", 33, "play tennis", null)
    val atiqah = Person("Atiqah", 28, "climb", amanda)

    amanda.showProfile()
    atiqah.showProfile()
}

class Person(val name: String, val age: Int, val hobby: String?, val referrer: Person?) {
    fun showProfile() {
        println("Name: $name")
        println("Age: $age")
        
        val hobbyText = hobby ?: ""
        val referrerText = if (referrer != null) {
            "Has a referrer named ${referrer.name}, who likes to ${referrer.hobby}."
        } else {
            "Doesn't have a referrer."
        }
        
        println("Likes to $hobbyText. $referrerText")
        println()
    }
}
```

---

## 题目 6：可折叠手机

通常情况下，手机屏幕会在用户按电源按钮时开启和关闭。相反，当可折叠手机处于折叠状态时，如果按电源按钮，可折叠手机的主内屏不会开启。

请在以下初始代码中，编写一个继承自 `Phone` 类的 `FoldablePhone` 类。该类应包含以下元素：

- 用于指明手机是否处于折叠状态的属性。
- 在行为上与 `Phone` 类不同的 `switchOn()` 函数，以便让手机只有在处于展开状态时才开启相应屏幕。
- 几个可更改折叠状态的方法。

**初始代码：**

```kotlin
open class Phone(var isScreenLightOn: Boolean = false) {
    open fun switchOn() {
        isScreenLightOn = true
    }
    
    fun switchOff() {
        isScreenLightOn = false
    }
    
    fun checkPhoneScreenLight() {
        val phoneScreenLight = if (isScreenLightOn) "on" else "off"
        println("The phone screen's light is $phoneScreenLight.")
    }
}
```

**你的答案：**

```kotlin
// 请将完整代码粘贴至此（包含 Phone 类和你实现的 FoldablePhone 类）
open class Phone(var isScreenLightOn: Boolean = false) {
    open fun switchOn() {
        isScreenLightOn = true
    }

    fun switchOff() {
        isScreenLightOn = false
    }

    fun checkPhoneScreenLight() {
        val phoneScreenLight = if (isScreenLightOn) "on" else "off"
        println("The phone screen's light is $phoneScreenLight.")
    }
}

class FoldablePhone(isScreenLightOn: Boolean = false) : Phone(isScreenLightOn) {
    var isFolded: Boolean = false

    override fun switchOn() {
        if (!isFolded) {
            super.switchOn()
        }
    }

    fun fold() {
        isFolded = true
    }

    fun unfold() {
        isFolded = false
    }
}

fun main() {
    //  测试未折叠状态
    val foldablePhone1 = FoldablePhone()
    foldablePhone1.unfold()      // 展开手机
    foldablePhone1.switchOn()    // 尝试亮屏
    foldablePhone1.checkPhoneScreenLight() // 输出：The phone screen's light is on.

    // 测试折叠状态
    val foldablePhone2 = FoldablePhone()
    foldablePhone2.fold()        // 折叠手机
    foldablePhone2.switchOn()    // 尝试亮屏（无效）
    foldablePhone2.checkPhoneScreenLight() // 输出：The phone screen's light is off.
}
```

---

## 题目 7：特别拍卖

通常，拍卖会上是由出价最高方来决定相应商品的价格。在这种特别拍卖中，如果某件商品没有人出价，则会以最低价格自动售卖给拍卖行。

在以下初始代码中，您会获得一个 `auctionPrice()` 函数，它接受可为 null 的 `Bid?` 类型作为实参：

**初始代码：**

```kotlin
fun main() {
    val winningBid = Bid(5000, "Private Collector")
    
    println("Item A is sold at ${auctionPrice(winningBid, 2000)}.")
    println("Item B is sold at ${auctionPrice(null, 3000)}.")
}

class Bid(val amount: Int, val bidder: String)

fun auctionPrice(bid: Bid?, minimumPrice: Int): Int {
    // Fill in the code.
}
```

完成 `auctionPrice()` 函数，让程序输出以下代码行：

**预期输出：**

```
Item A is sold at 5000.
Item B is sold at 3000.
```

**你的答案：**

```kotlin
// 请将完整代码粘贴至此
fun main() {
    val winningBid = Bid(5000, "Private Collector")

    println("Item A is sold at ${auctionPrice(winningBid, 2000)}.")
    println("Item B is sold at ${auctionPrice(null, 3000)}.")
}

class Bid(val amount: Int, val bidder: String)

fun auctionPrice(bid: Bid?, minimumPrice: Int): Int {
    if (bid != null && bid.amount >= minimumPrice) {
        return bid.amount
    }
    return minimumPrice
}
```

---

## 参考资料

- [Kotlin Playground](https://play.kotlinlang.org/)
