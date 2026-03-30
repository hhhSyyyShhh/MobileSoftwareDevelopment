# Lab2  Jetpack Compose 名片应用实验报告

## 1. 个人展示信息
- 姓名：陈云超
- 职位：学生
- 联系方式：手机号、邮箱、社交账号

## 2. 布局结构说明
1. 整体使用最外层 `Column` 垂直布局，设置全屏深色背景，内部上下两大区域均匀分布；
2. 封装 `CardTop` 顶部组件：嵌套 `Column` 实现头像、姓名、职位垂直居中排列，姓名文字加粗；
3. 封装 `CardBottom` 底部组件：`Column` 包裹多条联系方式；
4. 复用 `ContactItem` 单行组件：用 `Row` 水平排列 Icon 图标与 Text 文字，统一间距和样式；
5. 通过 `Modifier` 设置 padding、size、background、fillMaxSize 等修饰UI样式。

## 3. 遇到的问题与解决方法
1. 问题：导入图片后代码报红
   解决：确认图片命名纯英文无空格，正确放入 res/drawable，重新同步项目；
2. 问题：界面元素不居中
   解决：给 Column 设置 `horizontalAlignment = Alignment.CenterHorizontally`；
3. 问题：文字/图标大小不合适
   解决：调整 fontSize、Icon 的 size 和 padding 边距优化视觉。

## 4. 实验总结
掌握了 Jetpack Compose 可组合函数开发，熟练使用 Column、Row 布局容器，
学会 Image、Icon、Text 基础组件与 Modifier 样式修饰，完成了美观规范的个人电子名片。