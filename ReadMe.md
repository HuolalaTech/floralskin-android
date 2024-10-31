# 一、项目介绍
本项目是一个换肤的开源项目、基于该项目提供的基础能力.可动态加载皮肤包实现App换肤、并且支持日夜间模式。以上换肤能力、日夜间模式切换无需app重启

## 换肤效果
[image](https://p0-xtjj-private.juejin.cn/tos-cn-i-73owjymdk6/81950d9c0dc74b948809541edee81609~tplv-73owjymdk6-jj-mark-v1:0:0:0:0:5o6Y6YeR5oqA5pyv56S-5Yy6IEAgZmlyc3RibG9vZA==:q75.awebp?policy=eyJ2bSI6MywidWlkIjoiMTY2MjExNzMxMzc4OTU0NCJ9&rk3s=e9ecf3d6&x-orig-authkey=f32326d3454f2ac7e96d3d06cdbb035152127018&x-orig-expires=1727342591&x-orig-sign=a2cFVPdo3GGEIZIA2zwVgXk2bD0%3D)

# 二、工程结构
- app :换肤demo工程
- lib-skin: 换肤的核心能力sdk、并包含基础的能够换肤的基础UI组件
- skinred: 皮肤包工程，基于该工程构建出的apk ,加载该apk ，即可实现换肤


# 三、使用方法
## 3.1 换肤能力初始化

```
SkinManager.init(true, listener = {
            onLoadStart {
                Log.d("SkinApplication","onstart")
            }
            onLoadSuccess {  Log.d("SkinApplication","onLoadSuccess")}
            onLoadError { Log.d("SkinApplication","onLoadError") }
        }, this)
```
## 3.2 页面换肤能力支持
- 在Activity 的onCreate中调用 SkinManager.attach

- 在Activity onDestroy 中调用 SkinManager.detach
- 如果某页面不需要换肤，则不需要调用以上2个函数

## 3.3 基础组件
- DayNightBgConstraintLayout 支持换肤的约束布局
- DayNightImageView 支持换肤的ImageView
- DayNightLinearLayout  支持换肤的线性布局
- DayNightRecyclerView 支持换肤的RecyclerView
- DayNightRelativeLayout 支持换肤的关系布局
- DayNightTextView 支持换肤的TextView

如果以上UI组件不满足实际需求，可自行扩充


## 3.3 组件使用方法
见app 工程中
frag_index.xml  frag_mine.xml

## 加载皮肤包
SkinManager.enableSkin()

SkinManager.loadSkin

具体参考 SettinActivity中的用法

## 3.4 皮肤包制作
在skinred工程中放入需要换肤的资源，然后把该工程打成apk。 修改安装包后缀apk 为zip 。