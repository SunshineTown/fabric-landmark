## LandMark MOD 丨 地标MOD

>这是我第一次制作模组，应大家的需要，实现了绝大部分功能，制作过程中大量参考了老王写的代码，也承蒙老王和开发者群各位大佬亲自帮助。
因为本人java编程经验很少，代码写的一般，刚写出来的时候算是勉强够用。
>* ps:因为个人觉得悄悄话指令用处不是很大，所以没写。看需求吧。。。
>* 另外我还在代码中预留了分类和注释的字段位置和参数，如果未来有需要，可以很快完善代码实现地标分类和地标注释的功能。
## 安装说明  
* 基于fabric做的模组，支持服务器或单人客户端。(一边安装就行)  
* 确保你的服务器/单人客户端装了fabric。  
* 和一般mod安装方法相同，拖入mods文件夹即可。  
## 指令功能  
>##### landmark基础指令  
* `/landmark set <id> <x y z> <color> [dimension]`  
建立一个新的地标，id将作为识别地标的标志。参数全部支持自动填充。坐标的name属性默认为id(自动写入文件)
* `/landmark list`  
列出所有地标。功能比较齐全。（对不齐我也很难受，但是难搞啊。。）
  * 左侧地标显示默认使用的是地标的name属性，即可以重命名(op)修改显示文字，支持中文。
  * 地标『鼠标左键单击』为分享指令自动覆盖聊天栏，即`/landmark share id`，可在后面追加参数player，不加则为全服发送。（详细看下面share指令部分）
  * 地标『shift+鼠标左键单击』为自动追加坐标文本到聊天栏输入框，格式为  
  `[id 维度丨name:x y z]` 如 `[base 主世界丨基地：232  4  175]`
  * 右侧三个按钮，『重命名』『颜色』『删除』均需要op权限。还有下面的添加地标也需要op权限
* `/landmark delete <id>`
删除id为id的坐标。(自动写入文件)
* `/landmark color <id> <color>`
更改id为id的坐标颜色。(自动写入文件)
* `/landmark rename <id> <name>`
更改id为id的name属性，即显示的文字，支持中文。(自动写入文件)
* `/landmark save`
手动的保存全部坐标，写入json文件中。
* `/landmark reload config`
重载config配置文件。
* `/landmark reload point`
重载地标。  
>##### 分享指令相关
* `/landmark share <id> [player]`
将id为id的地标分享到聊天框。忽略player参数时，全服发送。填写时，只发送给id为player的玩家。
  * 『鼠标左键单击』相当于自动执行`/landmark return id player`指令，回复发出坐标的玩家。
  * 『shift+鼠标左键单击』为自动追加坐标文本到聊天栏输入框，格式为`[name:x y z]`
* `/landmark return <id> <player>`
回复指令，不推荐直接输入，这样不方便。
*`/landmark at <player> <text>`
输出一句话，开头会附带紫色的@player  
>##### 聊天信息坐标替换指令
* `/lmsg <text>`
text中输入的文字，所有[地标id]都会被替换为类似坐标列表中的坐标一样，附带悬浮提示和点击事件。
这些格式都是合法的:  
`[name]`  `[HellDoor]`  `[SlimeFarm:x y z]`   `[base 主世界丨基地：232  4  175]`(列表的shift+左击  
`只需要保证[后直接跟一个地标id即可，之后格式随意`
