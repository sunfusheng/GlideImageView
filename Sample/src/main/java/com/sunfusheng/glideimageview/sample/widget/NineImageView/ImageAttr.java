package com.sunfusheng.glideimageview.sample.widget.NineImageView;

import java.io.Serializable;

public class ImageAttr implements Serializable {

    public String url;
    public String thumbnailUrl;

    // 显示的宽高
    public int width;
    public int height;

    // 左上角坐标
    public int left;
    public int top;

    @Override
    public String toString() {
        return "ImageAttr{" +
                "url='" + url + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", left=" + left +
                ", top=" + top +
                '}';
    }
}
