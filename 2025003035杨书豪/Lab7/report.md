# Lab7 实验报告

## 1. 应用整体结构

本应用采用分层结构设计：

- model：定义数据类 Topic
- data：使用 DataSource 提供静态数据
- UI：通过 Compose 构建界面，包括网格和卡片组件

数据从 DataSource 提供 → 传入 Compose UI → 渲染网格。

---

## 2. Topic 数据类设计

Topic 包含三个字段：

- name：字符串资源ID（便于国际化）
- availableCourses：课程数量
- imageRes：图片资源ID

这种设计将 UI 与资源解耦，符合 Android 开发规范。

---

## 3. 卡片布局实现思路

使用以下组合项：

- Card：作为整体容器
- Row：横向布局（图片 + 文字）
- Column：纵向排列文本
- Image：显示图片和图标
- Text：显示文字

结构：

```text
Row
├── Image（左）
└── Column（右）
  ├── Text（标题）
  └── Row（图标 + 数字）
```

通过 padding 和 Spacer 控制间距。

---

## 4. 网格布局实现思路

使用 LazyVerticalGrid 实现：

- columns = GridCells.Fixed(2)：两列
- contentPadding = PaddingValues(8.dp)：整体边距
- horizontalArrangement = spacedBy(8.dp)：水平间距
- verticalArrangement = spacedBy(8.dp)：垂直间距

items() 用于绑定数据列表。

---

## 5. 遇到的问题与解决

### 问题1：图片比例不一致  
解决：使用 size(68.dp) 固定尺寸

### 问题2：间距不符合要求  
解决：通过 Spacer + padding 精细控制
