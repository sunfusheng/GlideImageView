# GlideImageView  [ ![Download](https://api.bintray.com/packages/sfsheng0322/maven/GlideImageView/images/download.svg) ](https://bintray.com/sfsheng0322/maven/GlideImageView/_latestVersion)

### 该库是基于[Glide](https://github.com/bumptech/glide) V4.8.0设计的，实现如下特性:<br/>
1、监听加载网络图片的进度<br/>
2、动态加载成有弧度的图片、圆形图片、高斯模糊图片更方便<br/>
3、链式设置触摸图片的透明度和非使能的透明度<br/>
4、增加九宫格控件，具体使用参考[NineImageViewActivity](https://github.com/sfsheng0322/GlideImageView/blob/master/Sample/src/main/java/com/sunfusheng/glideimageview/sample/NineImageViewActivity.java)<br/>

<br/>

### 监听加载高清图片进度的效果图

<img src="/resources/gif5.gif">

<br/>

### [APK下载地址](https://fir.im/GlideImageView)，去手机上体验吧 (◐‿◑)

<br/>

### 具体使用说明如下

#### Gradle:

    compile 'com.sunfusheng:GlideImageView:<latest-version>'
    
<br/>

#### GlideImageView 主要方法

    load(String url, @DrawableRes int placeholder, int radius, OnProgressListener onProgressListener)

    load(Object obj, @DrawableRes int placeholder, Transformation<Bitmap> transformation, OnProgressListener onProgressListener)

    loadCircle(String url, @DrawableRes int placeholder, OnProgressListener onProgressListener)
    
    loadDrawable(@DrawableRes int resId, @DrawableRes int placeholder)
      
<br/>

### 个人微信公众号

<img src="http://ourvm0t8d.bkt.clouddn.com/wx_gongzhonghao.png">

<br/>

### 打点赏给作者加点油^_^

<img src="http://ourvm0t8d.bkt.clouddn.com/wx_shoukuanma.png" >

<br/>

### 关于我

[GitHub: sfsheng0322](https://github.com/sfsheng0322)  

[个人邮箱: sfsheng0322@126.com](https://mail.126.com/)
  
[个人博客: sunfusheng.com](http://sunfusheng.com/)
  
[简书主页](http://www.jianshu.com/users/88509e7e2ed1/latest_articles)
  
[新浪微博](http://weibo.com/u/3852192525) 