# Lab1：Kotlin 基础语法练习

## 实验背景

本次实验涵盖 Kotlin 编程的基础语法，包括输出语句、变量、字符串操作、基本数学运算以及函数定义与调用。
建议使用 [Kotlin Playground](https://play.kotlinlang.org/) 在线编写并运行代码，完成后将代码粘贴到对应代码块中提交。

---

## 📌 个人信息说明

> 本次作业中，部分题目的**变量值、预期输出、函数名**与你的个人信息绑定，请按如下规则确认自己的参数：

| 符号 | 含义 | 示例（学号 20231042，姓名 张伟） |
|------|------|------|
| `[姓名]` | 你的姓名拼音（全小写） | `zhangwei` |
| `[学号]` | 你的完整学号 | `20231042` |
| `A` | 学号**倒数第 1 位**数字 | `2` |
| `B` | 学号**倒数第 2 位**数字 | `4` |
| `C` | 学号**倒数第 3 位**数字 | `0` |
| `AB` | 学号**后两位**组成的整数 | `42` |
| `BC` | 学号**倒数第 2、3 位**组成的整数 | `40` |

> ⚠️ 若某个值为 `0`，令其等于 `10`；若 `AB < 10`，令 `AB = AB + 10`，保证运算结果有意义。

---

## 提交要求

```
学号姓名/
└── Lab1/
    └── kotlin_basics.md    # 将答案填入本文件各题代码块后提交
                            # 你需要把这份文件复制一份到你的文件夹
```

截止时间：**2026-3-20**，届时关于 Lab1 的 PR 请求将不会被合并。

---

## 题目 2：输出个人信息

使用 Kotlin Playground 编写一个程序，输出以下三行消息（**请替换为你自己的信息**）：

```
My name is [姓名]
My student ID is [学号]
I'm learning Kotlin!
```

**示例**（学号 `20231042`，姓名 `zhangwei`）：

```
My name is zhangwei
My student ID is 20231042
I'm learning Kotlin!
```

**你的答案：**

```kotlin
fun main() {
    val name = "wuyi"
    val studentId = "2025003016"
    
    println("My name is $name")
    println("My stdent ID is studentid")
    println("I'm leaning kotlin!")
}
```

---

## 题目 3：修正编译错误

以下每段代码都存在错误，请将它们复制到 Kotlin Playground，找出并修正错误，使程序能正常运行输出预期结果。

**注意：请将代码中所有 `[学号]` 和 `[姓名]` 替换为你自己的学号和姓名拼音。**

---

**练习 3.1**

```kotlin
fun main() {
    println("Student [学号] says hello")
```

预期输出：`Student [学号] says hello`

**你的答案：**

```kotlin
fun main() {
    println("student 2025003016 says hello")
}
```

---

**练习 3.2**

```kotlin
fun main() {
    printLine("Hello, my name is [姓名]")
}
```

预期输出：`Hello, my name is [姓名]`

**你的答案：**

```kotlin
fun main() {
    println("Hello,my name is wuyi")
}
```

---

**练习 3.3**

```kotlin
fun main() {
    println("ID: [学号]") println("Name: [姓名]") println("Course: Kotlin")
}
```

预期输出：

```
ID: [学号]
Name: [姓名]
Course: Kotlin
```

**你的答案：**

```kotlin
fun main() {
    println("ID:2025003016")
    println("Name:wuyi")
    println("course:kotlin")
}
```

---

**练习 3.4**

```kotlin
fun main() (
    println("My ID ends with [A]")
)
```

预期输出：`My ID ends with [A]`（将 `[A]` 替换为你**学号最后一位**数字）

**你的答案：**

```kotlin
fun main() {
    println("My ID ends with 6")    
}
```

---

## 题目 4：字符串模板

请补全 `main()` 函数，使用 `$` 字符串模板将变量嵌入字符串中，输出一条通知消息。

初始代码（**将 `AB` 替换为你学号后两位组成的整数**）：

```kotlin
fun main() {
    val numberOfMessages = AB   // 替换 AB 为你的实际数值
    // 请使用字符串模板输出以下消息
}
```

预期输出（`AB` = 42 时的示例）：

```
You have 42 unread messages.
```

**你的答案：**

```kotlin
fun main() {
    val numberOfMessages = 16
    println("You have $numberOfMessages unread messages")    
}
```

---

## 题目 5：字符串串联

请编写程序，声明变量 `val [姓名]Delivery = "[学号]"`（**将 `[姓名]` 替换为你的姓名拼音，`[学号]` 替换为你的学号**），通过字符串串联（`+` 运算符）输出以下消息。

预期输出（姓名 `zhangwei`，学号 `20231042` 时的示例）：

```
Package for zhangwei will arrive, tracking ID: 20231042.
```

**你的答案：**

```kotlin
fun main() {
    val wuyiDelivery = "2025003016"
    println("Package for wuyi will arrive, tracking ID: " + wuyiDelivery + ".")
}
```

---

## 题目 6：消息格式

请编写程序，根据以下变量（**按规则替换 A、B、C、AB**），使用字符串模板输出一条格式化的个人信息消息。

变量（示例，A=2, B=4, C=0, AB=42）：

```kotlin
val studentName = "[姓名]"   // 替换为你的姓名拼音
val score     = AB          // 替换为你学号后两位的整数值
val bonus     = A           // 替换为你学号最后一位
val total     = score + bonus
```

预期输出（示例）：

```
Name: zhangwei
Score: 42, Bonus: 2
Total: 44
```

**你的答案：**

```kotlin
fun main() {
    val studentName = "wuyi" 
    val score = 16              
    val bonus = 6              
    val total = score + bonus   

    println("Name: $studentName")
    println("Score: $score, Bonus: $bonus")
    println("Total: $total")
}
```

---

## 题目 7：实现基本数学运算

请声明 `val operandOne = BC` 和 `val operandTwo = A`（**将 BC 替换为你学号倒数第2、3位组成的整数，A 替换为你学号最后一位**），输出以下所有运算结果。

示例（BC=40, A=2）：

```
40 + 2 = 42
40 - 2 = 38
40 / 2 = 20
40 * 2 = 80
40 % 2 = 0
```

**你的答案：**

```kotlin
fun main() {
    val operandOne = 10
    val operandTwo = 6
    println("$operandOne + $operandTwo = ${operandOne + operandTwo}")
    println("$operandOne - $operandTwo = ${operandOne - operandTwo}")
    println("$operandOne / $operandTwo = ${operandOne / operandTwo}")
    println("$operandOne * $operandTwo = ${operandOne * operandTwo}")
    println("$operandOne % $operandTwo = ${operandOne % operandTwo}")
}
```

---

## 题目 8：默认参数

请补全 `[姓名]Greeting()` 函数（**将函数名中的 `[姓名]` 替换为你的姓名拼音**），为 `name` 参数设置默认值 `"[学号]"`（**替换为你的学号字符串**），使得调用时不传 `name` 也能正常运行。

初始代码（示例，姓名 `zhangwei`，学号 `20231042`）：

```kotlin
fun main() {
    println(zhangweiGreeting(age = A))       // A 替换为你学号最后一位
    println(zhangweiGreeting("[姓名]", B))   // [姓名] 替换为你的姓名拼音，B 替换为学号倒数第二位
}

fun zhangweiGreeting(name: String, age: Int): String {
    // 请在此填写代码，为 name 添加默认值（默认值为你的学号字符串）并返回格式化祝语
}
```

预期输出（示例，姓名 `zhangwei`，学号 `20231042`，A=2，B=4）：

```
Happy Birthday, 20231042! You are now 2 years old!
Happy Birthday, zhangwei! You are now 4 years old!
```

**你的答案：**

```kotlin
fun main() {
    println(zhangweiGreeting(age = 6))
    println(zhangweiGreeting("wuyi", 1))
}

fun zhangweiGreeting(name: String = "2025003016", age: Int): String {
    return "Happy Birthday, $name! You are now $age years old!"
}
```

---

## 题目 9：计步器

计步器函数会根据步数计算消耗的卡路里。请根据 Kotlin 命名最佳实践（camelCase），重命名以下程序中所有命名不规范的函数、参数和变量，并将步数初始值改为 `AB * 100`（**AB 替换为你学号后两位整数**），程序逻辑不变。

原始代码（命名不规范）：

```kotlin
fun main() {
    val Steps = 4000                                      // 改为 AB * 100
    val caloriesBurned = PEDOMETERstepsTOcalories(Steps)
    println("Walking $Steps steps burns $caloriesBurned calories")
}

fun PEDOMETERstepsTOcalories(NumberOFStepS: Int): Double {
    val CaloriesBURNEDforEachStep = 0.04
    val TotalCALORIESburned = NumberOFStepS * CaloriesBURNEDforEachStep
    return TotalCALORIESburned
}
```

示例（AB=42）预期输出：

```
Walking 4200 steps burns 168.0 calories
```

**你的答案：**

```kotlin
fun main() {
    val steps = 16 * 100
    val caloriesBurned = pedometerStepsToCalories(steps)
    println("Walking $steps steps burns $caloriesBurned calories")
}
fun pedometerStepsToCalories(numberOfSteps: Int): Double {
    val caloriesBurnedForEachStep = 0.04
    val totalCaloriesBurned = numberOfSteps * caloriesBurnedForEachStep
    return totalCaloriesBurned
}
```

---

## 题目 10：比较两个数字

请补全 `main()` 函数，使用 `if/else` 表达式比较以下两个数字，输出较大的那个。若两数相等，则输出 `First and second are equal.`

初始代码（**将 BC 替换为你学号倒数第2、3位整数，AB 替换为你学号后两位整数**）：

```kotlin
fun main() {
    val first = BC    // 替换为你的实际数值
    val second = AB   // 替换为你的实际数值
    // 请在此填写代码
}
```

示例（BC=40, AB=42）预期输出：

```
42
```

**你的答案：**

```kotlin
fun main() {
    val first = 10  
    val second = 16 
    if (first > second) {
        println(first)
    } else if (second > first) {
        println(second)
    } else {
        println("First and second are equal.")
    }
}
```

---

## 题目 11：将重复代码移至一个函数中

以下程序重复输出多个城市的天气信息，存在大量重复代码。请创建一个名为 `print[姓名]Weather()` 的函数（**将 `[姓名]` 替换为你姓名拼音的首字母大写形式，例如 `zhangwei` → `printZhangweiWeather`**）消除重复，并用该函数替代 `main()` 中所有重复的 `println()`。

原始代码：

```kotlin
fun main() {
    println("City: Ankara")
    println("Low temperature: 27, High temperature: 31")
    println("Chance of rain: 82%")
    println()

    println("City: Tokyo")
    println("Low temperature: 32, High temperature: 36")
    println("Chance of rain: 10%")
    println()

    println("City: Cape Town")
    println("Low temperature: 59, High temperature: 64")
    println("Chance of rain: 2%")
    println()

    println("City: Guatemala City")
    println("Low temperature: 50, High temperature: 55")
    println("Chance of rain: 7%")
    println()
}
```

预期输出与原始代码相同，但重复逻辑需封装进你命名的函数中。

**你的答案：**

```kotlin
fun main() {
    printWuyiWeather("Ankara", 27, 31, 82)
    printWuyiWeather("Tokyo", 32, 36, 10)
    printWuyiWeather("Cape Town", 59, 64, 2)
    printWuyiWeather("Guatemala City", 50, 55, 7)
}

fun printWuyiWeather(city: String, lowTemp: Int, highTemp: Int, rainChance: Int) {
    println("City: $city")
    println("Low temperature: $lowTemp, High temperature: $highTemp")
    println("Chance of rain: $rainChance%")
    println()
}
```

---

## 参考资料

- [Kotlin Playground](https://play.kotlinlang.org/)
