# GlideImageView  [ ![Download](https://api.bintray.com/packages/sfsheng0322/maven/glideimageview/images/download.svg) ](https://bintray.com/sfsheng0322/maven/glideimageview/_latestVersion)

### 如何使用

#### Gradle:

    compile 'com.sunfusheng:glideimageview:1.0.0'
   
#### Maven:

    <dependency>
      <groupId>com.sunfusheng</groupId>
      <artifactId>glideimageview</artifactId>
      <version>1.0.0</version>
      <type>pom</type>
    </dependency>

#### ShapeImageView的属性

该库提供了一个[ShapeImageView](https://github.com/sfsheng0322/GlideImageView/blob/master/GlideImageView/src/main/java/com/sunfusheng/glideimageview/ShapeImageView.java)类，可以在xml当中设置图片的一些属性，
具体属性如下，当然这些属性页可以在[GlideImageView](https://github.com/sfsheng0322/GlideImageView/blob/master/GlideImageView/src/main/java/com/sunfusheng/glideimageview/GlideImageView.java)类里面设置。

| Attribute 属性          | Description 描述 | 
|:---				     |:---| 
| siv_border_color       | 边框颜色 | 
| siv_border_width       | 边框宽度 | 
| siv_pressed_color         | 触摸图片时的颜色 | 
| siv_pressed_alpha         | 触摸图片时的颜色透明度: 0.0f - 1.0f | 
| siv_radius                | 圆角弧度 | 
| siv_shape_type         | 两种形状类型：默认是0:rectangle、1:circle | 

| ![](https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/screenshot/image4.png) | ![](https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/screenshot/image4.png) | 
|:--- |:---| 
| 

    <com.sunfusheng.glideimageview.GlideImageView
        android:id="@+id/image22"
        android:layout_width="80dp"
        android:layout_height="80dp"
        android:layout_margin="5dp"
        android:layout_weight="1"
        android:scaleType="centerCrop"
        app:siv_border_color="@color/orange"
        app:siv_border_width="2dp"
        app:siv_pressed_alpha="0.3"
        app:siv_pressed_color="@color/orange"
        app:siv_radius="15dp"
        app:siv_shape_type="rectangle"/> 
                    
| 

     <com.sunfusheng.glideimageview.GlideImageView
         android:id="@+id/image22"
         android:layout_width="80dp"
         android:layout_height="80dp"
         android:layout_margin="5dp"
         android:layout_weight="1"
         android:scaleType="centerCrop"
         app:siv_border_color="@color/orange"
         app:siv_border_width="2dp"
         app:siv_pressed_alpha="0.3"
         app:siv_pressed_color="@color/orange"
         app:siv_radius="15dp"
         app:siv_shape_type="rectangle"/>
                 
| 

#### XML

### [APK下载地址](http://fir.im/MarqueeView)

### 微信公众号

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
