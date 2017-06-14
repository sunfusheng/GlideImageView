package com.sunfusheng.glideimageview;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sunfusheng.glideimageview.progress.GlideApp;
import com.sunfusheng.glideimageview.progress.ProgressListener;
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

    private WeakReference<ImageView> mImageView;

    public GlideImageLoader(ImageView imageView) {
        mImageView = new WeakReference<>(imageView);
    }

    public ImageView getImageView() {
        if (mImageView != null) {
            return mImageView.get();
        }
        return null;
    }

    public Context getContext() {
        if (mImageView != null) {
            return mImageView.get().getContext();
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
        if (obj == null || getContext() == null) {
            return;
        }

        addProgressListener(obj);
        GlideApp.with(getContext()).load(obj).apply(options).into(getImageView());
    }

    private long lastBytesRead = 0;
    private boolean lastStatus = false;

    private void addProgressListener(Object obj) {
        if (obj instanceof String) {
            final String url = (String) obj;
            if (url.startsWith("http") || url.startsWith("https")) {
                ProgressManager.addProgressListener(new ProgressListener() {
                    @Override
                    public void onProgress(String imageUrl, long bytesRead, long totalBytes, boolean isDone) {
                        if (totalBytes == 0) return;
                        if (!url.equals(imageUrl)) return;
                        if (onGlideImageViewListener == null) return;

                        if (lastBytesRead != bytesRead || lastStatus != isDone) {
                            lastBytesRead = bytesRead;
                            lastStatus = isDone;
                            int percent = (int) ((bytesRead * 1.0f / totalBytes) * 100.0f);
                            onGlideImageViewListener.onProgress(percent, isDone);
                        }
                    }
                });
            }
        }
    }

    private OnGlideImageViewListener onGlideImageViewListener;

    public void setOnGlideImageViewListener(OnGlideImageViewListener onGlideImageViewListener) {
        this.onGlideImageViewListener = onGlideImageViewListener;
    }

    public interface OnGlideImageViewListener {
        void onProgress(int percent, boolean isDone);
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
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
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
}
