# Lab6：Kotlin 类与集合练习

## 实验背景

本次实验涵盖 Kotlin 编程中类与集合的核心知识，包括数据类、枚举类、可变列表、集合操作（`filter`、`groupBy`）、列表扩展函数以及扩展属性。
建议使用 [Kotlin Playground](https://play.kotlinlang.org/) 在线编写并运行代码，完成后将代码粘贴到对应代码块中提交。

---

## 前提条件

- 熟悉 [Kotlin Playground](https://play.kotlinlang.org/)
- 熟悉 Kotlin 编程基础知识，包括类、对象、集合和高阶函数

---

## 提交要求

```
学号姓名/
└── Lab6/
    └── kotlin_classes_and_collections.md    # 将答案填入本文件各题代码块后提交
```

截止时间：**2026-4-27** (下午 18:00)，届时关于 Lab6 的 PR 请求将不会被合并。

---

## 应用概览

你是新加入活动跟踪应用团队的软件工程师。该应用旨在让用户能够跟踪他们的活动。你的任务是帮助构建应用的功能。

每个任务结束时，你应将你的解决方案与所提供的解决方案进行比较。实现所需功能的方式有很多种，因此如果你的代码与所提供的解决方案代码不完全一致，大可不必担心。

**使用上一个任务中提供的解决方案代码作为下一个任务的起始代码，以便从共同的起点开始。**

---

## 题目 1：Event 数据类

另一位软件工程师已经完成了该应用的一些主体工作，你将负责实现细节部分。

你需要实现 `Event` 类。此类用于存储用户输入的活动详细信息。（提示：此类不需要定义任何方法或执行任何操作。）

对于此任务，你需要创建一个名为 `Event` 的**数据类**。

此类的实例应该能够存储：

- 活动**标题**（以字符串表示）。
- 活动**说明**（以字符串表示，可以为 null）。
- 活动**时段**（以字符串表示）。我们只需跟踪活动在上午、下午还是晚上开始。
- 活动**时长**（分钟，以整数表示）。

> **提示：** 使用 `data class` 关键字定义数据类，对于可以为 null 的字符串属性，可以使用 `String?` 类型并设置默认值为 `null`。

**你的答案：**

```kotlin
// 请将完整代码粘贴至此
// 定义Event数据类，包含要求的所有属性
data class Event(
    val title: String,
    val description: String? = null, // 可空字符串，默认值null
    val daypart: String,
    val durationInMinutes: Int
)

fun main() {
    // 测试创建对象
    val event = Event("学习Kotlin", "编程练习", "上午", 60)
    println(event)
}
```

---

## 题目 2：Daypart 枚举类

> **注意：** 请使用题目 1 中你实现的 `Event` 类作为本任务的起始代码。

整个团队使用 `Event` 类一段时间后，资深的团队成员意识到使用字符串表示时段效果并不理想。

一些开发者存储了 `"Morning"` 值，还有一些使用了 `"morning"`，另一些开发者使用了 `"MORNING"`。这会导致许多问题。

你的任务就是通过进行一些**重构**来解决此问题。重构是改进代码而不改变其功能的过程。

哪种类型的类可用于对一组有限的不同值进行建模，以帮助纠正此问题？

你的团队希望你将时段代码更改为使用**枚举类**。使用枚举类时，你的同事不得不选择所提供的时段值之一，这可以防止出现此类问题。

枚举类应命名为 `Daypart`。它应该有三个值：

- `MORNING`
- `AFTERNOON`
- `EVENING`

你需要创建此枚举类，并重构 `Event` 类以使用它。

> **提示：** 使用 `enum class` 关键字定义枚举类，将 `Event` 中 `daypart` 属性的类型从 `String` 改为 `Daypart`。

**你的答案：**

```kotlin
// 请将完整代码粘贴至此（包含 Daypart 枚举类和重构后的 Event 类）
// 定义时段枚举类
enum class Daypart {
    MORNING, AFTERNOON, EVENING
}

// 重构Event数据类，使用枚举类型替代字符串
data class Event(
    val title: String,
    val description: String? = null,
    val daypart: Daypart, // 类型改为Daypart枚举
    val durationInMinutes: Int
)

fun main() {
    val event = Event("学习Kotlin", "编程练习", Daypart.MORNING, 60)
    println(event)
}
```

---

## 题目 3：存储活动列表

> **注意：** 请使用题目 2 中你实现的代码作为本任务的起始代码。

以下代码展示了你的同事当前如何创建和存储用户的活动：

```kotlin
val event1 = Event(title = "Wake up", description = "Time to get up", daypart = Daypart.MORNING, durationInMinutes = 0)
val event2 = Event(title = "Eat breakfast", daypart = Daypart.MORNING, durationInMinutes = 15)
val event3 = Event(title = "Learn about Kotlin", daypart = Daypart.AFTERNOON, durationInMinutes = 30)
val event4 = Event(title = "Practice Compose", daypart = Daypart.AFTERNOON, durationInMinutes = 60)
val event5 = Event(title = "Watch latest DevBytes video", daypart = Daypart.AFTERNOON, durationInMinutes = 10)
val event6 = Event(title = "Check out latest Android Jetpack library", daypart = Daypart.EVENING, durationInMinutes = 45)
```

他们创建了许多活动，目前每个活动都需要有自己的变量。随着创建的活动越来越多，跟踪这些活动变得更加困难。

请想出一个更好的方法来整理这些活动的存储方式。将所有活动存储在一个变量中。（注意：此方案必须灵活，因为可能会添加更多活动。）还需要能够高效地返回存储在变量中的活动数。

你应使用哪种类或数据类型？可以通过哪种方法添加更多活动？

> **提示：** 使用 `mutableListOf<Event>()` 创建可变列表，可以在初始化时直接传入所有活动对象。

**你的答案：**

```kotlin
// 请将完整代码粘贴至此（包含 Daypart、Event 类定义和 events 变量）
enum class Daypart {
    MORNING, AFTERNOON, EVENING
}

data class Event(
    val title: String,
    val description: String? = null,
    val daypart: Daypart,
    val durationInMinutes: Int
)

fun main() {
    // 创建可变列表存储所有活动
    val events = mutableListOf(
        Event("Wake up", "Time to get up", Daypart.MORNING, 0),
        Event("Eat breakfast", daypart = Daypart.MORNING, durationInMinutes = 15),
        Event("Learn about Kotlin", daypart = Daypart.AFTERNOON, durationInMinutes = 30),
        Event("Practice Compose", daypart = Daypart.AFTERNOON, durationInMinutes = 60),
        Event("Watch latest DevBytes video", daypart = Daypart.AFTERNOON, durationInMinutes = 10),
        Event("Check out latest Android Jetpack library", daypart = Daypart.EVENING, durationInMinutes = 45)
    )

    // 打印活动总数
    println("总活动数量：${events.size}")
}
```

---

## 题目 4：筛选短期活动

> **注意：** 请使用题目 3 中你实现的代码作为本任务的起始代码。

你的经理决定应该基于活动的时长向用户显示其**短期**活动的摘要。例如，"您有 5 个短期活动。"

"短期"活动是指不到 60 分钟的活动。

请使用上一个任务中的 `events` 变量代码实现此结果。

> **提示：** Kotlin 集合提供了 `filter {}` 高阶函数，可以传入一个 lambda 表达式来筛选符合条件的元素，筛选结果为一个新的列表，可通过 `.size` 获取元素数量。

**预期输出：**

```
You have 5 short events.
```

**你的答案：**

```kotlin
// 请将完整代码粘贴至此（包含之前所有定义以及本题的筛选逻辑）
enum class Daypart {
    MORNING, AFTERNOON, EVENING
}

data class Event(
    val title: String,
    val description: String? = null,
    val daypart: Daypart,
    val durationInMinutes: Int
)

fun main() {
    val events = mutableListOf(
        Event("Wake up", "Time to get up", Daypart.MORNING, 0),
        Event("Eat breakfast", daypart = Daypart.MORNING, durationInMinutes = 15),
        Event("Learn about Kotlin", daypart = Daypart.AFTERNOON, durationInMinutes = 30),
        Event("Practice Compose", daypart = Daypart.AFTERNOON, durationInMinutes = 60),
        Event("Watch latest DevBytes video", daypart = Daypart.AFTERNOON, durationInMinutes = 10),
        Event("Check out latest Android Jetpack library", daypart = Daypart.EVENING, durationInMinutes = 45)
    )

    // 筛选时长小于60分钟的短期活动
    val shortEvents = events.filter { it.durationInMinutes < 60 }
    println("You have ${shortEvents.size} short events.")
}
```

---

## 题目 5：按时段分组活动

> **注意：** 请使用题目 4 中你实现的代码作为本任务的起始代码。

你的同事希望用户能够看到所有活动及其时段的摘要。

> **提示：** Kotlin 集合提供了 `groupBy {}` 函数，可以按照指定条件将元素分组，返回一个 `Map<Key, List<Value>>`。可以使用 `forEach` 遍历该 Map 来输出每组信息。

**预期输出：**

```
MORNING: 2 events
AFTERNOON: 3 events
EVENING: 1 events
```

**你的答案：**

```kotlin
// 请将完整代码粘贴至此（包含之前所有定义以及本题的分组逻辑）
enum class Daypart {
    MORNING, AFTERNOON, EVENING
}

data class Event(
    val title: String,
    val description: String? = null,
    val daypart: Daypart,
    val durationInMinutes: Int
)

fun main() {
    val events = mutableListOf(
        Event("Wake up", "Time to get up", Daypart.MORNING, 0),
        Event("Eat breakfast", daypart = Daypart.MORNING, durationInMinutes = 15),
        Event("Learn about Kotlin", daypart = Daypart.AFTERNOON, durationInMinutes = 30),
        Event("Practice Compose", daypart = Daypart.AFTERNOON, durationInMinutes = 60),
        Event("Watch latest DevBytes video", daypart = Daypart.AFTERNOON, durationInMinutes = 10),
        Event("Check out latest Android Jetpack library", daypart = Daypart.EVENING, durationInMinutes = 45)
    )

    // 按时段分组活动
    val groupedEvents = events.groupBy { it.daypart }
    // 遍历输出分组结果
    groupedEvents.forEach { (daypart, eventList) ->
        println("$daypart: ${eventList.size} events")
    }
}
```

---

## 题目 6：获取最后一个活动

> **注意：** 请使用题目 5 中你实现的代码作为本任务的起始代码。

目前，你的同事会通过索引查找并输出最后一项内容。所用的代码为：

```kotlin
println("Last event of the day: ${events[events.size - 1].title}")
```

你的经理建议查看 Kotlin 文档，寻找可以简化此代码的函数。

请尝试使用该函数来确认输出的结果是否相同。

> **提示：** `List` 接口提供了 `last()` 扩展函数，可以直接返回列表中的最后一个元素，无需手动计算索引。

**预期输出：**

```
Last event of the day: Check out latest Android Jetpack library
```

**你的答案：**

```kotlin
// 请将完整代码粘贴至此（包含之前所有定义以及本题简化的代码）
enum class Daypart {
    MORNING, AFTERNOON, EVENING
}

data class Event(
    val title: String,
    val description: String? = null,
    val daypart: Daypart,
    val durationInMinutes: Int
)

fun main() {
    val events = mutableListOf(
        Event("Wake up", "Time to get up", Daypart.MORNING, 0),
        Event("Eat breakfast", daypart = Daypart.MORNING, durationInMinutes = 15),
        Event("Learn about Kotlin", daypart = Daypart.AFTERNOON, durationInMinutes = 30),
        Event("Practice Compose", daypart = Daypart.AFTERNOON, durationInMinutes = 60),
        Event("Watch latest DevBytes video", daypart = Daypart.AFTERNOON, durationInMinutes = 10),
        Event("Check out latest Android Jetpack library", daypart = Daypart.EVENING, durationInMinutes = 45)
    )

    // 使用last()扩展函数简化获取最后一个元素
    println("Last event of the day: ${events.last().title}")
}
```

---

## 题目 7：活动时长扩展属性

> **注意：** 请使用题目 6 中你实现的代码作为本任务的起始代码。

大家很喜欢你设计的数据类，但发现每次需要以字符串形式表示活动的时长时，都要重复编写代码：

```kotlin
val durationOfEvent = if (events[0].durationInMinutes < 60) {
    "short"
} else {
    "long"
}
println("Duration of first event of the day: $durationOfEvent")
```

虽然你可以通过直接向类添加方法来解决这种重复问题，但这并不理想，因为其他团队已开始在他们的应用中使用你的活动类。如果该类发生变化，他们需要重新测试所有代码，以确保你所做的更改不会造成任何中断。

如果不直接更改数据类，如何编写会返回与上述代码相同的值的**扩展属性**？

> **提示：** 扩展属性的定义格式为 `val ClassName.propertyName: Type`，并在其中提供一个自定义的 `get()` 方法。这样无需修改 `Event` 类本身即可为其添加新属性。

正确实现后，你将能够使用以下代码，它将输出与此任务开头所显示的代码相同的消息。

```kotlin
println("Duration of first event of the day: ${events[0].durationOfEvent}")
```

**预期输出：**

```
Duration of first event of the day: short
```

**你的答案：**

```kotlin
// 请将完整代码粘贴至此（包含之前所有定义以及扩展属性）
enum class Daypart {
    MORNING, AFTERNOON, EVENING
}

data class Event(
    val title: String,
    val description: String? = null,
    val daypart: Daypart,
    val durationInMinutes: Int
)

// 为Event类定义扩展属性，无需修改原类
val Event.durationOfEvent: String
    get() = if (durationInMinutes < 60) "short" else "long"

fun main() {
    val events = mutableListOf(
        Event("Wake up", "Time to get up", Daypart.MORNING, 0),
        Event("Eat breakfast", daypart = Daypart.MORNING, durationInMinutes = 15),
        Event("Learn about Kotlin", daypart = Daypart.AFTERNOON, durationInMinutes = 30),
        Event("Practice Compose", daypart = Daypart.AFTERNOON, durationInMinutes = 60),
        Event("Watch latest DevBytes video", daypart = Daypart.AFTERNOON, durationInMinutes = 10),
        Event("Check out latest Android Jetpack library", daypart = Daypart.EVENING, durationInMinutes = 45)
    )

    // 使用扩展属性
    println("Duration of first event of the day: ${events[0].durationOfEvent}")
}
```

---

## 参考资料

- ###### [Kotlin Playground](https://play.kotlinlang.org/)

  
