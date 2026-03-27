# 电子名片开发报告
## 一、名片信息
- 姓名：李太国
- 职业：Android Developer Extraordinaire
- 手机号：+86 18683332586
- 社交账号：@AndroidDev
- 邮箱：taiguoli@yunu.edu.cn
- 头像：系统图标展示

## 二、布局结构
- 整体使用 `Column` 容器包裹所有内容，背景为浅绿色，内容整体居中。
- 联系方式每行采用 `Row` 横向布局，图标 + 文字组合，通过 `Spacer` 间距分隔。

## 三、问题与解决
1. 背景未铺满全屏：使用 `fillMaxSize` 实现背景全屏。
2. 图标文字不对齐：添加 `verticalAlignment = Alignment.CenterVertically` 垂直居中。
3. 颜色修改麻烦：定义固定颜色常量，统一修改、全局生效。
4. 代码重复：封装 `ContactItem` 组件，实现联系方式模板复用。
5. 头像不显示：检查资源路径失败后，改用系统图标替代。