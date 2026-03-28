# 个人名片项目
## 整体思路
先使用层叠布局，最下的就像桌布，然后上层使用一个纵向布局就像是放了一个长方形的框子里面的东西只能总会竖着放，然后再使用两个纵向布局在之前的长方形框子上 就实现了两个独立的长方形框子，可以对这两个框子的位置和样式进行单独操作。

## 具体实现
首先调用`Image` 这个方法对背景图操作，再使用Box布局让图片在最下面，然后铺满整个屏幕，然后使用`contentScale = ContentScale.Crop`让图片直接截断，背景就有了，之后就是在Box布局上使用一个，列布局因为头像在上介绍在下嘛，首先使用 `Modifier`这个类为整体的
```kotlin  
modifier = Modifier
    .fillMaxSize() // 铺满屏幕
    .padding(24.dp), // 整体内边距（不贴边）
    horizontalAlignment = Alignment.CenterHorizontally
```
让整体都居中，然后再嵌套一个列布局
```kotlin
Column(
    modifier = Modifier.weight(1f), // 占满所有空间，让内容居中
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center // 内部垂直居中
)
```
之后就可以写头像和介绍的文本,样式那些
``` 
Image(
    painter = androidLogo,
    contentDescription = "Logo",
    modifier = Modifier.size(140.dp)
)
Text(
    text = "Yang ShuHao",
    fontSize = 40.sp,
    fontWeight = FontWeight.Bold,
    color = Color(0xFF8800FF),
    modifier = Modifier.padding(top = 24.dp)
)

Text(
    text = "Android Developer Extraordinaire",
    fontSize = 18.sp,
    color = Color(0xFF8800FF),
    modifier = Modifier.padding(top = 8.dp)
)
```
然后头像和介绍写完了，就可以再来一个列布局写电话邮件那些
```kotlin
Column(
    modifier = Modifier.padding(bottom = 40.dp),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    ContactItem(icon = Icons.Default.Phone, te= "+86 12345678910")
    ContactItem(icon = Icons.Default.Share, text = "@AndroidDev")
    ContactItem(icon = Icons.Default.Email, te= "YangShuHao@android.com")
}
```
最后就是调用了`ContactItem`这个自己写的函数
```kotlin
@Composable
fun ContactItem(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(start = 48.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF7AF1FF),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(24.dp)) // 图标和文字间距
        Text(
            text = text,
            fontSize = 18.sp,
            color = Color.Blue
        )
    }
}
```
传入相应的参数就可以直接去复用在函数里封装好的，样式，布局，颜色，大小那些。


## 报错
    开发过程中遇到的问题就是一开始没思考好容器的顺序比如列布局与列布局之间的嵌套，还有就是图片的边界的大小不对，导致将底部的颜色穿透上界面，让头像周围有一个黑框，后面重新对布局进行调整然后就可以正常显示名片卡，这次
## 总结
这次实训让我学到用box以及列布局还有对图片的调整，还有图标的引入增强了我对界面布局的理解，在后面的项目中我可以更好的使用相关组件。
