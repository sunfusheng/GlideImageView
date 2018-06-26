package com.sunfusheng;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sunfusheng.progress.OnProgressListener;
import com.sunfusheng.transformation.CircleTransformation;
import com.sunfusheng.transformation.RadiusTransformation;

/**
 * @author sunfusheng on 2017/11/10.
 */
@SuppressLint("CheckResult")
public class GlideImageView extends ImageView {

    private boolean enableState = false;
    private float pressedAlpha = 0.4f;
    private float unableAlpha = 0.3f;
    private GlideImageLoader imageLoader;

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
        imageLoader = GlideImageLoader.create(this);
    }

    public GlideImageLoader getImageLoader() {
        if (imageLoader == null) {
            imageLoader = GlideImageLoader.create(this);
        }
        return imageLoader;
    }

    public GlideImageView apply(RequestOptions options) {
        getImageLoader().getGlideRequest().apply(options);
        return this;
    }

    public GlideImageView centerCrop() {
        getImageLoader().getGlideRequest().centerCrop();
        return this;
    }

    public GlideImageView fitCenter() {
        getImageLoader().getGlideRequest().fitCenter();
        return this;
    }

    public GlideImageView diskCacheStrategy(@NonNull DiskCacheStrategy strategy) {
        getImageLoader().getGlideRequest().diskCacheStrategy(strategy);
        return this;
    }

    public GlideImageView placeholder(@DrawableRes int resId) {
        getImageLoader().getGlideRequest().placeholder(resId);
        return this;
    }

    public GlideImageView error(@DrawableRes int resId) {
        getImageLoader().getGlideRequest().error(resId);
        return this;
    }

    public GlideImageView fallback(@DrawableRes int resId) {
        getImageLoader().getGlideRequest().fallback(resId);
        return this;
    }

    public GlideImageView dontAnimate() {
        getImageLoader().getGlideRequest().dontTransform();
        return this;
    }

    public GlideImageView dontTransform() {
        getImageLoader().getGlideRequest().dontTransform();
        return this;
    }

    public void load(String url) {
        load(url, 0);
    }

    public void load(String url, @DrawableRes int placeholder) {
        load(url, placeholder, 0);
    }

    public void load(String url, @DrawableRes int placeholder, int radius) {
        load(url, placeholder, radius, null);
    }

    public void load(String url, @DrawableRes int placeholder, OnProgressListener onProgressListener) {
        load(url, placeholder, null, onProgressListener);
    }

    public void load(String url, @DrawableRes int placeholder, int radius, OnProgressListener onProgressListener) {
        load(url, placeholder, new RadiusTransformation(getContext(), radius), onProgressListener);
    }

    public void load(Object obj, @DrawableRes int placeholder, Transformation<Bitmap> transformation) {
        getImageLoader().loadImage(obj, placeholder, transformation);
    }

    public void load(Object obj, @DrawableRes int placeholder, Transformation<Bitmap> transformation, OnProgressListener onProgressListener) {
        getImageLoader().listener(obj, onProgressListener).loadImage(obj, placeholder, transformation);
    }

    public void loadCircle(String url) {
        loadCircle(url, 0);
    }

    public void loadCircle(String url, @DrawableRes int placeholder) {
        loadCircle(url, placeholder, null);
    }

    public void loadCircle(String url, @DrawableRes int placeholder, OnProgressListener onProgressListener) {
        load(url, placeholder, new CircleTransformation(), onProgressListener);
    }

    public void loadDrawable(@DrawableRes int resId) {
        loadDrawable(resId, 0);
    }

    public void loadDrawable(@DrawableRes int resId, @DrawableRes int placeholder) {
        loadDrawable(resId, placeholder, null);
    }

    public void loadDrawable(@DrawableRes int resId, @DrawableRes int placeholder, @NonNull Transformation<Bitmap> transformation) {
        getImageLoader().load(resId, placeholder, transformation);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (enableState) {
            if (isPressed()) {
                setAlpha(pressedAlpha);
            } else if (!isEnabled()) {
                setAlpha(unableAlpha);
            } else {
                setAlpha(1.0f);
            }
        }
    }

    public GlideImageView enableState(boolean enableState) {
        this.enableState = enableState;
        return this;
    }

    public GlideImageView pressedAlpha(float pressedAlpha) {
        this.pressedAlpha = pressedAlpha;
        return this;
    }

    public GlideImageView unableAlpha(float unableAlpha) {
        this.unableAlpha = unableAlpha;
        return this;
    }
}
