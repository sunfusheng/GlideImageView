package com.sunfusheng.glideimageview.sample.widget.MultiImageView;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sunfusheng.glideimageview.sample.R;
import com.sunfusheng.progress.GlideApp;
import com.sunfusheng.progress.GlideRequest;
import com.sunfusheng.progress.GlideRequests;

/**
 * @author sunfusheng on 2018/6/19.
 */
public class ImageCell extends android.support.v7.widget.AppCompatImageView {

    private static final float THUMBNAIL_RATIO = 0.1f;

    private GlideRequests glideRequests;
    private ImageData imageData;
    private boolean isGif;
    private Rect gifIconRect;

    public ImageCell(Context context) {
        this(context, null);
    }

    public ImageCell(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        glideRequests = GlideApp.with(getContext());
    }

    public void setData(ImageData imageData) {
        this.imageData = imageData;
        if (imageData != null) {
            if (!TextUtils.isEmpty(imageData.localUrl)) {
                loadLocal();
            } else {
                loadRemote();
            }
        }
    }

    private void loadLocal() {
        glideRequests.load(imageData.localUrl)
                .placeholder(R.mipmap.image_loading)
                .error(getRemoteGlideRequest())
                .fitCenter()
                .transition(new DrawableTransitionOptions().dontTransition())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(new ImageCellTarget(this));
    }

    private void loadRemote() {
        getRemoteGlideRequest().into(new ImageCellTarget(this));
    }

    private GlideRequest<Drawable> getRemoteGlideRequest() {
        return glideRequests.load(imageData.remoteUrl)
                .placeholder(R.mipmap.image_loading)
                .error(R.mipmap.image_load_err)
                .fitCenter()
                .transition(new DrawableTransitionOptions().dontTransition())
                .thumbnail(THUMBNAIL_RATIO)
                .diskCacheStrategy(DiskCacheStrategy.DATA);
    }

    private class ImageCellTarget extends DrawableImageViewTarget {
        ImageCellTarget(ImageView view) {
            super(view);
        }

        @Override
        public void onLoadStarted(Drawable placeholder) {
            getView().setBackgroundResource(R.drawable.drawable_image_cell_bg);
            getView().setScaleType(ImageView.ScaleType.FIT_CENTER);
            super.onLoadStarted(placeholder);
        }

        @Override
        public void onLoadFailed(@Nullable Drawable errorDrawable) {
            getView().setBackgroundResource(R.drawable.drawable_image_cell_bg);
            getView().setScaleType(ImageView.ScaleType.FIT_CENTER);
            super.onLoadFailed(errorDrawable);
        }

        @Override
        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
            getView().setBackgroundResource(0);
            getView().setScaleType(ImageView.ScaleType.CENTER_CROP);
            super.onResourceReady(resource, transition);
        }
    }
}
