package com.sunfusheng.glideimageview.helper;

import android.support.annotation.DrawableRes;
import android.widget.ImageView;

/**
 * Created by sunfusheng on 2017/6/6.
 */
public interface IImageLoader<T extends ImageView> {

    // 加载网络图片
    T loadImage(String url, int placeholderResId);

    // 加载Res图片
    T loadLocalImage(@DrawableRes int resId, int placeholderResId);

    // 加载SDCard图片
    T loadLocalImage(String localPath, int placeholderResId);

    // 加载网络图片成圆型
    T loadCircleImage(String url, int placeholderResId);

    // 加载Res图片成圆型
    T loadLocalCircleImage(int resId, int placeholderResId);

    // 加载SDCard图片成圆型
    T loadLocalCircleImage(String localPath, int placeholderResId);
}
