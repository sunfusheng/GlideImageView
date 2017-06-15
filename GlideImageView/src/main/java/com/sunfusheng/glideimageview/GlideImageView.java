package com.sunfusheng.glideimageview;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

import com.bumptech.glide.request.RequestOptions;
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

    public GlideImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = new GlideImageLoader(this);
        }
        return mImageLoader;
    }

    public String getImageUrl() {
        return getImageLoader().getImageUrl();
    }

    public RequestOptions requestOptions(int placeholderResId) {
        return getImageLoader().requestOptions(placeholderResId);
    }

    public RequestOptions circleRequestOptions(int placeholderResId) {
        return getImageLoader().circleRequestOptions(placeholderResId);
    }

    public GlideImageView load(int resId, RequestOptions options) {
        getImageLoader().load(resId, options);
        return this;
    }

    public GlideImageView load(Uri uri, RequestOptions options) {
        getImageLoader().load(uri, options);
        return this;
    }

    public GlideImageView load(String url, RequestOptions options) {
        getImageLoader().load(url, options);
        return this;
    }

    public GlideImageView loadImage(String url, int placeholderResId) {
        getImageLoader().loadImage(url, placeholderResId);
        return this;
    }

    public GlideImageView loadLocalImage(@DrawableRes int resId, int placeholderResId) {
        getImageLoader().loadLocalImage(resId, placeholderResId);
        return this;
    }

    public GlideImageView loadLocalImage(String localPath, int placeholderResId) {
        getImageLoader().loadLocalImage(localPath, placeholderResId);
        return this;
    }

    public GlideImageView loadCircleImage(String url, int placeholderResId) {
        setShapeType(ShapeType.CIRCLE);
        getImageLoader().loadCircleImage(url, placeholderResId);
        return this;
    }

    public GlideImageView loadLocalCircleImage(int resId, int placeholderResId) {
        setShapeType(ShapeType.CIRCLE);
        getImageLoader().loadLocalCircleImage(resId, placeholderResId);
        return this;
    }

    public GlideImageView loadLocalCircleImage(String localPath, int placeholderResId) {
        setShapeType(ShapeType.CIRCLE);
        getImageLoader().loadLocalCircleImage(localPath, placeholderResId);
        return this;
    }

    public GlideImageView listener(OnGlideImageViewListener listener) {
        getImageLoader().setOnGlideImageViewListener(getImageUrl(), listener);
        return this;
    }

    public GlideImageView listener(OnProgressListener listener) {
        getImageLoader().setOnProgressListener(getImageUrl(), listener);
        return this;
    }
}
