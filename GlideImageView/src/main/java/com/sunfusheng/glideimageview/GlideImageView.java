package com.sunfusheng.glideimageview;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

import com.sunfusheng.glideimageview.helper.ILoadMethod;

/**
 * Created by sunfusheng on 2017/6/6.
 */
public class GlideImageView extends ShapeImageView implements ILoadMethod {

    private GlideImageLoader mLoader;

    public GlideImageView(Context context) {
        this(context, null);
    }

    public GlideImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GlideImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLoader = new GlideImageLoader(this);
    }

    @Override
    public void loadImage(String url, int placeholderResId) {
        mLoader.loadImage(url, placeholderResId);
    }

    @Override
    public void loadLocalImage(@DrawableRes int resId, int placeholderResId) {
        mLoader.loadLocalImage(resId, placeholderResId);
    }

    @Override
    public void loadLocalImage(String localPath, int placeholderResId) {
        mLoader.loadLocalImage(localPath, placeholderResId);
    }

    @Override
    public void loadCircleImage(String url, int placeholderResId) {
        mLoader.loadCircleImage(url, placeholderResId);
    }

    @Override
    public void loadLocalCircleImage(int resId, int placeholderResId) {
        mLoader.loadLocalCircleImage(resId, placeholderResId);
    }

    @Override
    public void loadLocalCircleImage(String localPath, int placeholderResId) {
        mLoader.loadLocalCircleImage(localPath, placeholderResId);
    }
}
