package com.sunfusheng.glideimageview.helper;

import android.support.annotation.DrawableRes;

/**
 * Created by sunfusheng on 2017/6/6.
 */
public interface IImageLoader {

    // 加载网络图片
    void loadImage(String url, int placeholderResId);

    // 加载Res图片
    void loadLocalImage(@DrawableRes int resId, int placeholderResId);

    // 加载本地图片
    void loadLocalImage(String localPath, int placeholderResId);

    // 加载网络图片成圆型
    void loadCircleImage(String url, int placeholderResId);

    // 加载Res图片成圆型
    void loadLocalCircleImage(int resId, int placeholderResId);

    // 加载本地图片成圆型
    void loadLocalCircleImage(String localPath, int placeholderResId);
}
