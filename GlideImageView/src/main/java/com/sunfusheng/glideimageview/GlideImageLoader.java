package com.sunfusheng.glideimageview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.sunfusheng.glideimageview.progress.GlideApp;
import com.sunfusheng.glideimageview.progress.OnGlideImageViewListener;
import com.sunfusheng.glideimageview.progress.OnProgressListener;
import com.sunfusheng.glideimageview.progress.ProgressManager;
import com.sunfusheng.glideimageview.transformation.GlideCircleTransformation;

import java.lang.ref.WeakReference;

/**
 * Created by sunfusheng on 2017/6/6.
 */
public class GlideImageLoader {

    private static final String ANDROID_RESOURCE = "android.resource://";
    private static final String FILE = "file://";
    private static final String SEPARATOR = "/";
    private static final String HTTP = "http";
    private static final String HTTPS = "https";

    private Object imageUrlObj;
    private WeakReference<ImageView> imageView;

    private long lastBytesRead = 0;
    private boolean lastStatus = false;

    private OnProgressListener internalProgressListener;
    private OnGlideImageViewListener onGlideImageViewListener;
    private OnProgressListener onProgressListener;

    public GlideImageLoader(ImageView iv) {
        imageView = new WeakReference<>(iv);
    }

    public ImageView getImageView() {
        if (imageView != null) {
            return imageView.get();
        }
        return null;
    }

    public Context getContext() {
        if (getImageView() != null) {
            return getImageView().getContext();
        }
        return null;
    }

    public Uri resId2Uri(int resourceId) {
        if (getContext() == null) {
            return null;
        }
        return Uri.parse(ANDROID_RESOURCE + getContext().getPackageName() + SEPARATOR + resourceId);
    }

    public void load(int resId, RequestOptions options) {
        load(resId2Uri(resId), options);
    }

    public void load(Uri uri, RequestOptions options) {
        loadByGlide(uri, options);
    }

    public void load(String url, RequestOptions options) {
        loadByGlide(url, options);
    }

    private void loadByGlide(Object obj, RequestOptions options) {
        this.imageUrlObj = obj;
        if (obj == null || getContext() == null) {
            return;
        }
        GlideApp.with(getContext())
                .load(obj)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        ProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        ProgressManager.removeProgressListener(internalProgressListener);
                        return false;
                    }
                })
                .into(getImageView());
    }

    public RequestOptions requestOptions(int placeholderResId) {
        return requestOptions(placeholderResId, placeholderResId);
    }

    public RequestOptions requestOptions(int placeholderResId, int errorResId) {
        return new RequestOptions()
                .placeholder(placeholderResId)
                .error(errorResId);
    }

    public RequestOptions circleRequestOptions(int placeholderResId) {
        return circleRequestOptions(placeholderResId, placeholderResId);
    }

    public RequestOptions circleRequestOptions(int placeholderResId, int errorResId) {
        return requestOptions(placeholderResId, errorResId)
                .transform(new GlideCircleTransformation());
    }

    public void loadImage(String url, int placeholderResId) {
        load(url, requestOptions(placeholderResId));
    }

    public void loadLocalImage(@DrawableRes int resId, int placeholderResId) {
        load(resId, requestOptions(placeholderResId));
    }

    public void loadLocalImage(String localPath, int placeholderResId) {
        load(FILE + localPath, requestOptions(placeholderResId));
    }

    public void loadCircleImage(String url, int placeholderResId) {
        load(url, circleRequestOptions(placeholderResId));
    }

    public void loadLocalCircleImage(int resId, int placeholderResId) {
        load(resId, circleRequestOptions(placeholderResId));
    }

    public void loadLocalCircleImage(String localPath, int placeholderResId) {
        load(FILE + localPath, circleRequestOptions(placeholderResId));
    }

    private void addProgressListener() {
        if (imageUrlObj == null) return;
        if (!(imageUrlObj instanceof String)) return;

        final String url = (String) imageUrlObj;

        if (url.startsWith(HTTP) || url.startsWith(HTTPS)) {
            internalProgressListener = new OnProgressListener() {
                @Override
                public void onProgress(String imageUrl, long bytesRead, long totalBytes, boolean isDone) {
                    if (totalBytes == 0) return;
                    if (!url.equals(imageUrl)) return;
                    if (lastBytesRead == bytesRead && lastStatus == isDone) return;

                    lastBytesRead = bytesRead;
                    lastStatus = isDone;

                    if (onProgressListener != null) {
                        onProgressListener.onProgress(imageUrl, bytesRead, totalBytes, isDone);
                    }

                    if (onGlideImageViewListener != null) {
                        int percent = (int) ((bytesRead * 1.0f / totalBytes) * 100.0f);
                        onGlideImageViewListener.onProgress(percent, isDone);
                    }

                    if (isDone) {
                        ProgressManager.removeProgressListener(this);
                    }
                }
            };
            ProgressManager.addProgressListener(internalProgressListener);
        }
    }

    public void setOnGlideImageViewListener(OnGlideImageViewListener onGlideImageViewListener) {
        this.onGlideImageViewListener = onGlideImageViewListener;
        addProgressListener();
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
        addProgressListener();
    }
}
