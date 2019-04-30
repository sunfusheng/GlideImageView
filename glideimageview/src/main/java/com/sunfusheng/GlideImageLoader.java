package com.sunfusheng;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sunfusheng.progress.GlideApp;
import com.sunfusheng.progress.GlideRequest;
import com.sunfusheng.progress.OnProgressListener;
import com.sunfusheng.progress.ProgressManager;

import java.lang.ref.WeakReference;

/**
 * @author by sunfusheng on 2017/6/6.
 */
public class GlideImageLoader {

    protected static final String ANDROID_RESOURCE = "android.resource://";
    protected static final String FILE = "file://";
    protected static final String SEPARATOR = "/";

    private String url;
    private WeakReference<ImageView> imageViewWeakReference;
    private GlideRequest<Drawable> glideRequest;

    public static GlideImageLoader create(ImageView imageView) {
        return new GlideImageLoader(imageView);
    }

    private GlideImageLoader(ImageView imageView) {
        imageViewWeakReference = new WeakReference<>(imageView);
        glideRequest = GlideApp.with(getContext()).asDrawable();
    }

    public ImageView getImageView() {
        if (imageViewWeakReference != null) {
            return imageViewWeakReference.get();
        }
        return null;
    }

    public Context getContext() {
        if (getImageView() != null) {
            return getImageView().getContext();
        }
        return null;
    }

    public String getUrl() {
        return url;
    }

    public GlideRequest getGlideRequest() {
        if (glideRequest == null) {
            glideRequest = GlideApp.with(getContext()).asDrawable();
        }
        return glideRequest;
    }

    protected Uri resId2Uri(@DrawableRes int resId) {
        return Uri.parse(ANDROID_RESOURCE + getContext().getPackageName() + SEPARATOR + resId);
    }

    public GlideImageLoader load(@DrawableRes int resId, @DrawableRes int placeholder, @NonNull Transformation<Bitmap> transformation) {
        return loadImage(resId2Uri(resId), placeholder, transformation);
    }

    protected GlideRequest<Drawable> loadImage(Object obj) {
        if (obj instanceof String) {
            url = (String) obj;
        }
        return glideRequest.load(obj);
    }


    protected GlideImageLoader loadImage(Object obj, @DrawableRes int placeholder, Transformation<Bitmap> transformation) {
        glideRequest = loadImage(obj);
        if (placeholder != 0) {
            glideRequest = glideRequest.placeholder(placeholder);
        }

        if (transformation != null) {
            glideRequest = glideRequest.transform(transformation);
        }

        glideRequest.into(new GlideImageViewTarget(getImageView()));
        return this;
    }

    public GlideImageLoader listener(Object obj, OnProgressListener onProgressListener) {
        if (obj instanceof String) {
            url = (String) obj;
        }
        ProgressManager.addListener(url, onProgressListener);
        return this;
    }

    private class GlideImageViewTarget extends DrawableImageViewTarget {
        GlideImageViewTarget(ImageView view) {
            super(view);
        }

        @Override
        public void onLoadStarted(Drawable placeholder) {
            super.onLoadStarted(placeholder);
        }

        @Override
        public void onLoadFailed(@Nullable Drawable errorDrawable) {
            OnProgressListener onProgressListener = ProgressManager.getProgressListener(getUrl());
            if (onProgressListener != null) {
                onProgressListener.onProgress(true, 100, 0, 0);
                ProgressManager.removeListener(getUrl());
            }
            super.onLoadFailed(errorDrawable);
        }

        @Override
        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
            OnProgressListener onProgressListener = ProgressManager.getProgressListener(getUrl());
            if (onProgressListener != null) {
                onProgressListener.onProgress(true, 100, 0, 0);
                ProgressManager.removeListener(getUrl());
            }
            super.onResourceReady(resource, transition);
        }
    }
}
