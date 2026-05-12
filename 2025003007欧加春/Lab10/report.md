# Lab10 实验报告：为 Lunch Tray 添加导航

## 一、Compose Navigation 核心组件关系

### 1.1 NavController、NavHost 和 composable() 三者关系

在 Jetpack Compose Navigation 中，这三个组件构成了导航系统的核心架构：

**NavController**
- 导航系统的中央控制器，负责管理返回堆栈（Back Stack）
- 提供 `navigate()` 方法进行页面跳转
- 提供 `navigateUp()` 方法返回上一页
- 通过 `currentBackStackEntryAsState()` 监听当前页面状态
- 是导航操作的唯一可信来源（Single Source of Truth）

**NavHost**
- 导航的容器组件，负责显示当前路由对应的页面内容
- 将 `NavController` 与具体的页面路由关联起来
- 通过 `startDestination` 指定起始页面
- 内部包含多个 `composable()` 路由定义

**composable()**
- 定义单个页面的路由规则
- 将路由名称（route）与对应的 Composable 函数绑定
- 每个页面都在 `composable()` 块中声明
- 支持传递参数、深层链接等高级功能

**三者协作流程：**
```
用户操作 → NavController.navigate(route) → NavHost 查找对应 composable() → 显示目标页面
```

---

## 二、LunchTrayScreen 枚举类设计说明

### 2.1 为什么使用枚举类而非字符串

```kotlin
enum class LunchTrayScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Entree(title = R.string.choose_entree),
    SideDish(title = R.string.choose_side_dish),
    Accompaniment(title = R.string.choose_accompaniment),
    Checkout(title = R.string.order_checkout)
}
```

**使用枚举类的优势：**

1. **类型安全**：编译时检查，避免拼写错误导致的运行时崩溃
2. **集中管理**：所有页面定义在一个地方，便于维护和扩展
3. **关联数据**：每个枚举值可以携带额外的属性（如标题资源 ID）
4. **IDE 支持**：自动补全和重构支持，提高开发效率
5. **可扩展性**：后续可轻松添加新的页面或属性

**枚举类的特性利用：**
- `.name` 属性：获取枚举值的名称字符串，直接用作路由名称
- `.valueOf(name)` 方法：根据字符串反向查找对应的枚举值
- `@StringRes` 注解：确保 title 属性只能是字符串资源 ID

---

## 三、LunchTrayAppBar 设计思路

### 3.1 组件设计

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayAppBar(
    currentScreen: LunchTrayScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
)
```

### 3.2 返回按钮显示条件

**判断逻辑：**
```kotlin
val canNavigateBack = navController.previousBackStackEntry != null
```

- 当 `previousBackStackEntry` 不为 null 时，说明当前页面不是起始页面，显示返回按钮
- 当 `previousBackStackEntry` 为 null 时（即 Start 页面），隐藏返回按钮

**这种设计的优点：**
1. **自动适应**：无需手动判断当前是哪个页面
2. **响应式**：当返回堆栈变化时自动更新 UI
3. **符合规范**：Start 页面作为应用入口，不应该有返回按钮

### 3.3 动态标题

通过 `currentScreen.title` 获取当前页面对应的字符串资源 ID，使用 `stringResource()` 函数动态显示标题，实现 AppBar 标题随页面切换而变化。

---

## 四、导航流程与返回堆栈管理

### 4.1 导航流程设计

```
Start → Entree → SideDish → Accompaniment → Checkout
```

### 4.2 返回堆栈管理策略

**Start 页面弹出策略：**

当用户点击 "Start Order" 进入点餐流程时：
```kotlin
navController.navigate(LunchTrayScreen.Entree.name) {
    popUpTo(LunchTrayScreen.Start.name) { inclusive = true }
}
```

**为什么要弹出 Start 页面？**

1. **用户体验**：点餐流程是一个独立的任务流，完成后不应返回到 Start 页面
2. **系统返回键行为**：进入点餐流程后，按系统返回键应直接退出应用，而不是回到 Start 页面
3. **逻辑清晰**：Start 页面只是入口，点餐流程形成独立的返回堆栈

**Cancel 操作的处理：**

```kotlin
navController.navigate(LunchTrayScreen.Start.name) {
    popUpTo(navController.graph.findStartDestination().id) {
        inclusive = true
    }
}
viewModel.resetOrder()
```

- 使用 `popUpTo()` 清空整个返回堆栈
- 同时调用 `viewModel.resetOrder()` 重置订单状态
- 确保用户回到 Start 页面时，订单数据已清空

### 4.3 返回堆栈状态示例

| 操作 | 返回堆栈状态 |
|------|-------------|
| 应用启动 | [Start] |
| 点击 Start Order | [Entree] （Start 被弹出） |
| 点击 Next | [Entree, SideDish] |
| 点击 Next | [Entree, SideDish, Accompaniment] |
| 点击 Next | [Entree, SideDish, Accompaniment, Checkout] |
| 点击 Submit/Cancel | [Start] （堆栈清空，重新开始） |

---

## 五、实验中遇到的问题与解决过程

### 5.1 问题一：返回按钮在所有页面都显示

**现象**：Start 页面也出现了返回按钮

**原因**：初始使用固定值 `canNavigateBack = true`，没有根据返回堆栈动态判断

**解决**：改为使用 `navController.previousBackStackEntry != null` 动态判断

### 5.2 问题二：Cancel 后订单数据未清空

**现象**：点击 Cancel 返回 Start 页面后，再次进入点餐流程，之前选择的菜品还在

**原因**：只做了导航，没有重置 ViewModel 中的订单状态

**解决**：在 Cancel 回调中添加 `viewModel.resetOrder()`

### 5.3 问题三：系统返回键行为不符合预期

**现象**：从 Entree 页面按系统返回键，回到了 Start 页面而不是退出应用

**原因**：Start 页面没有被弹出返回堆栈

**解决**：在 Start → Entree 导航时使用 `popUpTo(LunchTrayScreen.Start.name) { inclusive = true }`

---

## 六、总结

通过本次实验，我深入理解了 Jetpack Compose Navigation 的工作原理：

1. **NavController** 是导航的核心，管理返回堆栈和导航操作
2. **枚举类**是定义路由的最佳实践，提供类型安全和可维护性
3. **返回堆栈管理**是多屏应用设计的关键，需要根据业务逻辑合理配置 `popUpTo`
4. **状态管理**与导航需要配合，页面跳转时考虑是否需要重置状态

Lunch Tray 应用的导航设计体现了"任务流"的概念——点餐流程是一个完整的任务，完成后应该回到起点，而不是在流程中循环。
