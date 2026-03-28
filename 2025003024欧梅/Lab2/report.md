# Lab2 实验报告
## 1. 名片展示信息
本次实验完成的电子名片展示了以下个人信息：
- 姓名：oumei
- 专业：计算机科学与技术专业
- 手机号：+86 184 6927 6784
- 邮箱：3010502335@qq.com
- 社交账号：@oumei

## 2. 布局结构说明
整个界面使用 Jetpack Compose 声明式 UI 实现，布局层次清晰：

1. 最外层使用 **Column** 纵向布局，设置 `fillMaxSize()` 占满屏幕，并使用粉色背景 `Color(0xFFFFC0CB)`。
2. 上半部分 **CardTop** 组合函数：
   - 使用 `Column` 垂直排列
   - 包含 `Image` 显示头像
   - 包含两个 `Text` 分别显示姓名和专业
   - 通过 `Modifier.padding` 和 `horizontalAlignment` 实现居中效果
3. 下半部分 **CardBottom** 组合函数：
   - 使用 `Column` 垂直排列联系方式
   - 封装了 **ContactRow** 组件，用于统一展示“图标 + 文字”
4. 联系方式行 **ContactRow**：
   - 使用 `Row` 横向排列
   - 左侧是 `Icon`  Material 图标
   - 中间用 `Spacer` 控制间距
   - 右侧是 `Text` 显示具体信息
5. 全程使用 `Modifier` 控制尺寸、内边距、填充方式、对齐方式。

## 3. 遇到的问题与解决方法
1. 头像图片资源找不到
   - 原因：图片路径或名称错误
   - 解决：将图片正确放入 `res/drawable` 目录，并在代码中正确引用 `R.drawable._touxiang`。

2. 界面内容无法水平居中
   - 原因：未设置对齐方式
   - 解决：给 `Column` 添加 `horizontalAlignment = Alignment.CenterHorizontally`。

3. 图标颜色与整体风格不统一
   - 解决：使用 `tint = Color(0xFF3DDC84)` 将所有图标统一为 Android 绿色。

4. 文字与图标间距不均匀
   - 解决：使用 `Spacer` 组件精确控制间距，让布局更美观。

## 4. 实验总结
通过本次实验，我掌握了 Jetpack Compose 的基本使用：
- 学会创建 `@Composable` 可组合函数
- 掌握 `Column`、`Row` 线性布局
- 学会使用 `Image`、`Icon`、`Text` 基础组件
- 掌握 `Modifier` 修饰符的常用用法
- 能够独立完成一个完整的名片界面