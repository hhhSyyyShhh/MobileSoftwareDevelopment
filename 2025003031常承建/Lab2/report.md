# 一、名片展示的个人信息
- **姓名**：常承建
- **职位**：Android 开发工程师
- **手机号码**：+86 15752407238
- **电子邮箱**：3157267907@qq.com
- **社交账号**：@wechat（微信）
- **头像**：自定义 `logo` 图片资源
---

# 二、布局结构简要说明
## 1. 核心 Composable 组件
- `Surface`：作为根容器，设置全屏深青色背景
- `Column`：垂直布局，用于整体页面、顶部信息区和底部联系方式区
- `Row`：水平布局，用于实现“图标+文字”的联系方式条目
- `Image`：加载并展示个人头像
- `Text`：显示姓名、职位、联系方式等文本内容
- `Icon`：展示电话、邮箱、分享等 Material Design 图标
- `Spacer`：控制组件之间的间距，优化视觉效果

## 2. 布局嵌套关系
1.  **根布局**：`MainActivity` 中通过 `setContent` 启动界面，使用 `MaterialTheme` + `Surface` 包裹 `BusinessCard` 组件，设置背景色。
2.  **主界面（BusinessCard）**：
    - 采用 `Column` 全屏布局，`verticalArrangement = Arrangement.SpaceBetween` 将界面分为上下两部分。
    - 上半部分为 `CardTop`（头像+姓名+职位），下半部分为 `CardBottom`（联系方式列表）。
3.  **顶部区域（CardTop）**：
    - 嵌套 `Column` 垂直排列，依次为：`Image`（头像）→ `Spacer`（间距）→ `Text`（姓名）→ `Spacer`（间距）→ `Text`（职位）。
4.  **底部区域（CardBottom）**：
    - 嵌套 `Column` 垂直排列，复用 `ContactRow` 组件展示三组联系方式。
5.  **联系方式条目（ContactRow）**：
    - 采用 `Row` 水平布局，左侧为 `Icon`（图标），中间为 `Spacer`（间距），右侧为 `Text`（信息文本），并通过 `verticalAlignment = Alignment.CenterVertically` 实现垂直居中对齐。

---

# 三、遇到的问题和解决过程
## 问题1：运行时崩溃，提示资源不存在
- **问题描述**：代码中引用了 `R.drawable.logo` 头像资源，但项目 `res/drawable` 目录下未添加该图片，导致程序运行时找不到资源而崩溃。
- **解决方法**：在 `res/drawable` 文件夹中添加名为 `logo.png` 的图片文件，清理项目后重新运行，问题解决。

## 问题2：联系方式行中图标与文字未垂直居中
- **问题描述**：`Row` 布局默认顶部对齐，图标和文字高度不一致，视觉上错位，影响美观。
- **解决方法**：在 `Row` 组件中添加 `verticalAlignment = Alignment.CenterVertically` 属性，使子组件在垂直方向上居中对齐。

## 问题3：预览界面与真机运行效果不一致
- **问题描述**：Compose 预览界面未显示背景色，仅展示文字和图标，与真机运行效果不符。
- **解决方法**：在 `Preview` 函数中添加与主界面一致的 `Surface` 布局，并设置相同的背景色 `Color(0xFF073042)`，统一预览与真机效果。

## 问题4：代码结构不规范，缺少主题包裹
- **问题描述**：未使用 `MaterialTheme` 包裹布局，不符合 Jetpack Compose 官方开发规范，可能导致样式兼容性问题。
- **解决方法**：在 `setContent` 中添加 `MaterialTheme` 主题容器，将 `Surface` 包裹其中，提升代码规范性和兼容性。

---

# 四、实验总结
本次实验基于 Jetpack Compose 完成了商务名片界面开发，熟练掌握了 `Column`、`Row`、`Image`、`Text`、`Icon` 等基础组件的使用，理解了 Compose 声明式 UI 的布局嵌套思想，成功解决了资源缺失、布局对齐、预览异常等常见问题，实现了美观、规范的移动端商务名片界面。