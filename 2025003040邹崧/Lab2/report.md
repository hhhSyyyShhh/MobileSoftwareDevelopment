# Android 实验二：电子名片

## 一、实验目的
1. 掌握 Android Studio 基本开发环境的使用。
2. 学习 Jetpack Compose 基础 UI 组件的使用，如文本、图片、布局、图标等。
3. 完成一个简单的个人电子名片界面，实现基本信息展示。
4. 学会运行、调试 App 并截图提交作业。

## 二、实验环境
- 操作系统：Windows
- 开发工具：Android Studio
- 开发语言：Kotlin
- 界面框架：Jetpack Compose

## 三、实验内容
1. 创建 Android 新项目，选择 Empty Activity 模板。
2. 使用 Column、Row、Text、Image、Icon 等 Compose 组件搭建 UI。
3. 设置界面背景色、文字大小、颜色、间距等样式。
4. 显示个人信息：姓名、职位、电话、邮箱、社交账号。
5. 运行 App 到模拟器，确认界面正常显示。
6. 截图并整理代码文件，提交到 GitHub。

## 四、布局结构简要说明
界面整体使用 **Column** 垂直布局，分为上下两部分：
- 上半部分：`CardTop` 组件，包含 `Image`（头像）、`Text`（姓名和职位），内部用 `Column` 垂直嵌套。
- 下半部分：`CardBottom` 组件，包含多个 `Divider`（分割线）和 `ContactRow`（联系方式行），`ContactRow` 内部用 `Row` 水平嵌套 `Icon` 和 `Text`。
- 整体通过 `Column` + `Row` 嵌套实现垂直+水平的排版结构。

## 五、遇到的问题和解决过程
1. **问题**：`import` 语句报错 `Unresolved reference 'compose'`
   - **解决**：修正为标准 Compose 导入格式，使用 `androidx.compose.ui` 等正确包名。
2. **问题**：`fillSize()` 方法报错
   - **解决**：替换为 `fillMaxSize()`，适配当前 Compose 版本。
3. **问题**：`report.md` 自动变成 `report.md.txt`
   - **解决**：开启 Windows「文件扩展名」显示，手动删除 `.txt` 后缀。
4. **问题**：截图文件名 `Screenshot.png` 首字母大写，不符合作业要求
   - **解决**：重命名为 `screenshot.png`（全小写）。

## 六、实验结果
成功实现电子名片界面，包含以下内容：
- 姓名：邹崧
- 职位：Android 开发者
- 电话：2025003040
- 邮箱：zousong@example.com
- 社交账号：@zousong
- 深色背景 + 绿色图标，界面美观整洁。

App 可以正常编译、安装、启动与显示。

## 七、实验总结
通过本次实验，我掌握了 Android Studio 的基本操作，学会了使用 Jetpack Compose 编写简单界面，理解了常用布局组件和样式设置方法。能够独立完成项目创建、代码编写、运行调试与作业提交的完整流程。

学号：2025003040
姓名：邹崧