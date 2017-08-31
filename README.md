# GlideImageView  [ ![Download](https://api.bintray.com/packages/sfsheng0322/maven/glideimageview/images/download.svg) ](https://bintray.com/sfsheng0322/maven/glideimageview/_latestVersion)

### 该库是基于[Glide](https://github.com/bumptech/glide) V4.0设计的，实现如下特性:<br/>
1、通过提供的属性可以设置图片的圆角、边框。<br/>
2、可以设置点击触摸图片时的颜色、透明度。<br/>
3、一行代码加载来自网络、res、SDCard中的图片，可加载成圆形。<br/>
4、可以监听加载图片时的进度。<br/>

<br/>

### 设置圆角、边框的图片，触摸图片时的效果，显示Gif图的效果

<table>
    <tr>
        <td><img src="/screenshot/image4.png"></td>
        <td><img src="/screenshot/gif6.gif"></td>
        <td><img src="/screenshot/gif1.gif"></td>
        <td><img src="/screenshot/gif3.gif"></td>
    </tr>
</table>

<br/>

### 监听先加载缩略图再加载高清图片进度的效果图

<img src="/screenshot/gif5.gif">

<br/>

### [APK下载地址](https://fir.im/GlideImageView)，去手机上体验吧 (◐‿◑)

<br/>

### 具体使用说明如下

#### Gradle:

    compile 'com.sunfusheng:glideimageview:1.1.0'
   
#### Maven:

    <dependency>
      <groupId>com.sunfusheng</groupId>
      <artifactId>glideimageview</artifactId>
      <version>1.1.0</version>
      <type>pom</type>
    </dependency>

<br/>

#### ShapeImageView 和 GlideImageView 共同的属性

该库提供了一个[ShapeImageView](https://github.com/sfsheng0322/GlideImageView/blob/master/GlideImageView/src/main/java/com/sunfusheng/glideimageview/ShapeImageView.java)类，可以在xml当中，也可以在代码中设置图片的一些属性，
当然这些属性也可以在[GlideImageView](https://github.com/sfsheng0322/GlideImageView/blob/master/GlideImageView/src/main/java/com/sunfusheng/glideimageview/GlideImageView.java)上面设置，具体属性如下

| Attribute 属性          | Description 描述 | 
|:---				     |:---| 
| siv_border_color       | 边框颜色 | 
| siv_border_width       | 边框宽度 | 
| siv_pressed_color         | 触摸图片时的颜色 | 
| siv_pressed_alpha         | 触摸图片时的颜色透明度: 0.0f - 1.0f | 
| siv_radius                | 圆角弧度 | 
| siv_shape_type         | 三种类型：默认是0:none、1:rectangle、1:circle | 

#### 下面是在xml中和代码中设置的效果

| xml中设置 | 代码中设置 | 
| :--- | :--- | 
| ![](https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/screenshot/code2.png) | ![](https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/screenshot/code3.png) |

<br/>

#### 一行代码加载来自网络、res、SDCard中图片

    public GlideImageView loadImage(String url, int placeholderResId);
    public GlideImageView loadLocalImage(@DrawableRes int resId, int placeholderResId); 
    public GlideImageView loadLocalImage(String localPath, int placeholderResId);
    
#### 一行代码加载来自网络、res、SDCard中图片成圆形

    public GlideImageView loadCircleImage(String url, int placeholderResId); 
    public GlideImageView loadLocalCircleImage(int resId, int placeholderResId);
    public GlideImageView loadLocalCircleImage(String localPath, int placeholderResId);
    
#### 如果你觉得上面的方法还不能满足你，那么你可以通过下面的方法追加自己想要的属性来加载图片

    RequestOptions requestOptions(int placeholderResId);
    RequestOptions circleRequestOptions(int placeholderResId);
    
    GlideImageView load(int resId, RequestOptions options);
    GlideImageView load(Uri uri, RequestOptions options);
    GlideImageView load(String url, RequestOptions options);
    
#### 如果你还是觉得得不到满足，好吧，我提供了[GlideImageLoader](https://github.com/sfsheng0322/GlideImageView/blob/master/GlideImageView/src/main/java/com/sunfusheng/glideimageview/GlideImageLoader.java)类加载图片，比如这样加载图片：先加载缩略图再加载高清图片，并监听加载的进度

    private void loadImage(String image_url_thumbnail， String image_url) {
        RequestOptions requestOptions = glideImageView.requestOptions(R.color.black)
                .centerCrop()
                .skipMemoryCache(true) // 跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE); // 不缓存到SDCard中

        glideImageView.getImageLoader().setOnGlideImageViewListener(image_url, new OnGlideImageViewListener() {
            @Override
            public void onProgress(int percent, boolean isDone, GlideException exception) {
                progressView.setProgress(percent);
                progressView.setVisibility(isDone ? View.GONE : View.VISIBLE);
            }
        });

        glideImageView.getImageLoader().requestBuilder(image_url, requestOptions)
                .thumbnail(Glide.with(ImageActivity.this) // 加载缩略图
                        .load(image_url_thumbnail)
                        .apply(requestOptions))
                .transition(DrawableTransitionOptions.withCrossFade()) // 动画渐变加载
                .into(glideImageView);
    }
  
<br/>
    
<img src="/screenshot/gif4.gif">

<br/>

#### 该库提供两种监听加载图片进度的Listener，总有一款是你想要的

    public interface OnGlideImageViewListener {
        void onProgress(int percent, boolean isDone, GlideException exception);
    }
    
    public interface OnProgressListener {
        void onProgress(String imageUrl, long bytesRead, long totalBytes, boolean isDone, GlideException exception);
    }
        
<br/>

### 我的微信公众号

<img src="https://github.com/sfsheng0322/StickyHeaderListView/blob/master/screenshots/%E5%BE%AE%E4%BF%A1%E5%85%AC%E4%BC%97%E5%8F%B7.jpg" style="width: 30%;">

### 关于我

个人邮箱：sfsheng0322@126.com

[GitHub主页](https://github.com/sfsheng0322)

[简书主页](http://www.jianshu.com/users/88509e7e2ed1/latest_articles)

[个人博客](http://sunfusheng.com/)

[新浪微博](http://weibo.com/u/3852192525)

License
--
    Copyright (C) 2017 sfsheng0322@126.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
