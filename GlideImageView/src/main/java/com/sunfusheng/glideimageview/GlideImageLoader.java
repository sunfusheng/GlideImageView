package com.sunfusheng.glideimageview;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.lang.ref.WeakReference;

/**
 * Created by sunfusheng on 2017/6/6.
 */
public class GlideImageLoader implements IGlideImageView {

    private static final String ANDROID_RESOURCE = "android.resource://";
    private static final String FILE = "file://";
    private static final String SEPARATOR = "/";

    private WeakReference<ImageView> mImageView;

    public GlideImageLoader(ImageView imageView) {
        mImageView = new WeakReference<ImageView>(imageView);
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

    // 将资源ID转为Uri
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
        if (uri == null || getImageView() == null || getContext() == null) {
            return;
        }

        Glide.with(getContext())
                .load(uri)
                .apply(options)
                .into(getImageView());
    }

    public void load(String url, RequestOptions options) {
        if (TextUtils.isEmpty(url) || getImageView() == null || getContext() == null) {
            return;
        }

        Glide.with(getContext())
                .load(url)
                .apply(options)
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
        if (getContext() == null) {
            return requestOptions(placeholderResId, errorResId);
        }
        return requestOptions(placeholderResId, errorResId)
                .transform(new GlideCircleTransform(getContext()));
    }

    public boolean isGif(String url) {
        if (TextUtils.isEmpty(url)) return false;
        if (url.endsWith(".gif")) return true;
        return false;
    }

    @Override
    public void loadImage(String url, int placeholderResId) {
        load(url, requestOptions(placeholderResId));
    }

    @Override
    public void loadLocalImage(@DrawableRes int resId, int placeholderResId) {
        load(resId, requestOptions(placeholderResId));
    }

    @Override
    public void loadLocalImage(String localPath, int placeholderResId) {
        load(FILE + localPath, requestOptions(placeholderResId));
    }

    @Override
    public void loadCircleImage(String url, int placeholderResId) {
        load(url, circleRequestOptions(placeholderResId));
    }

    @Override
    public void loadLocalCircleImage(int resId, int placeholderResId) {
        load(resId, circleRequestOptions(placeholderResId));
    }

    @Override
    public void loadLocalCircleImage(String localPath, int placeholderResId) {
        load(FILE + localPath, circleRequestOptions(placeholderResId));
    }
}
