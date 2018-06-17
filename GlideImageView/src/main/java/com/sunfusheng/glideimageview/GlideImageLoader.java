package com.sunfusheng.glideimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sunfusheng.glideimageview.progress.GlideApp;
import com.sunfusheng.glideimageview.progress.GlideRequest;
import com.sunfusheng.glideimageview.progress.OnProgressListener;
import com.sunfusheng.glideimageview.progress.ProgressManager;

import java.lang.ref.WeakReference;

/**
 * @author by sunfusheng on 2017/6/6.
 */
public class GlideImageLoader {

    protected static final String ANDROID_RESOURCE = "android.resource://";
    protected static final String FILE = "file://";
    protected static final String SEPARATOR = "/";
    protected static final String HTTP = "http";

    private WeakReference<ImageView> imageViewWeakReference;
    private String url;

    public static GlideImageLoader create(ImageView imageView) {
        return new GlideImageLoader(imageView);
    }

    private GlideImageLoader(ImageView imageView) {
        imageViewWeakReference = new WeakReference<>(imageView);
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

    protected Uri resId2Uri(@DrawableRes int resId) {
        return Uri.parse(ANDROID_RESOURCE + getContext().getPackageName() + SEPARATOR + resId);
    }

    public GlideImageLoader load(@DrawableRes int resId, @DrawableRes int placeholder, @NonNull Transformation<Bitmap> transformation) {
        return loadImage(resId2Uri(resId), placeholder, transformation);
    }

    protected GlideImageLoader loadImage(Object obj, @DrawableRes int placeholder, Transformation<Bitmap> transformation) {
        if (obj instanceof String) {
            url = (String) obj;
        }

        GlideRequest<Drawable> glideRequest = GlideApp.with(getContext()).load(obj);
        if (placeholder != 0) {
            glideRequest = glideRequest.placeholder(placeholder).error(placeholder);
        }

        if (transformation != null) {
            glideRequest = glideRequest.transform(transformation);
        }

//        glideRequest = glideRequest.diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true);

        glideRequest.listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(getImageView());
        return this;
    }

    public GlideImageLoader listener(OnProgressListener onProgressListener) {
        ProgressManager.addListener(url, onProgressListener);
        return this;
    }
}
