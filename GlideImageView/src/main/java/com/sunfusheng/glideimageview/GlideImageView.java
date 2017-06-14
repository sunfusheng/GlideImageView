package com.sunfusheng.glideimageview;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

import com.sunfusheng.glideimageview.progress.OnGlideImageViewListener;
import com.sunfusheng.glideimageview.progress.OnProgressListener;

/**
 * Created by sunfusheng on 2017/6/6.
 */
public class GlideImageView extends ShapeImageView {

    private GlideImageLoader mImageLoader;

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
        mImageLoader = new GlideImageLoader(this);
    }

    public GlideImageView loadImage(String url, int placeholderResId) {
        mImageLoader.loadImage(url, placeholderResId);
        return this;
    }

    public GlideImageView loadLocalImage(@DrawableRes int resId, int placeholderResId) {
        mImageLoader.loadLocalImage(resId, placeholderResId);
        return this;
    }

    public GlideImageView loadLocalImage(String localPath, int placeholderResId) {
        mImageLoader.loadLocalImage(localPath, placeholderResId);
        return this;
    }

    public GlideImageView loadCircleImage(String url, int placeholderResId) {
        setShapeType(ShapeType.CIRCLE);
        mImageLoader.loadCircleImage(url, placeholderResId);
        return this;
    }

    public GlideImageView loadLocalCircleImage(int resId, int placeholderResId) {
        setShapeType(ShapeType.CIRCLE);
        mImageLoader.loadLocalCircleImage(resId, placeholderResId);
        return this;
    }

    public GlideImageView loadLocalCircleImage(String localPath, int placeholderResId) {
        setShapeType(ShapeType.CIRCLE);
        mImageLoader.loadLocalCircleImage(localPath, placeholderResId);
        return this;
    }

    public GlideImageView listener(OnGlideImageViewListener listener) {
        mImageLoader.setOnGlideImageViewListener(listener);
        return this;
    }

    public GlideImageView listener(OnProgressListener listener) {
        mImageLoader.setOnProgressListener(listener);
        return this;
    }

}
