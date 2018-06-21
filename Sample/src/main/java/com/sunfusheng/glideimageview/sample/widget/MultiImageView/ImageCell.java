package com.sunfusheng.glideimageview.sample.widget.MultiImageView;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sunfusheng.glideimageview.sample.R;
import com.sunfusheng.progress.GlideApp;
import com.sunfusheng.progress.GlideRequests;

/**
 * @author sunfusheng on 2018/6/19.
 */
public class ImageCell extends RelativeLayout {

    private static final float THUMBNAIL_RATIO = 0.1f;

    private ImageView vImage;
    private View vCover;
    private TextView vCenterText;
    private TextView vCornerText;

    private GlideRequests glideRequests;
    private ImageData imageData;

    private boolean loadGif;

    public ImageCell(Context context, boolean loadGif) {
        this(context, null);
        this.loadGif = loadGif;
    }

    public ImageCell(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_image_cell, this);
        vImage = findViewById(R.id.imageView);
        vCover = findViewById(R.id.cover);
        vCenterText = findViewById(R.id.center_text);
        vCornerText = findViewById(R.id.corner_text);
        glideRequests = GlideApp.with(getContext());
    }

    public void setData(ImageData imageData) {
        this.imageData = imageData;
        if (imageData != null) {
            setCenterText(imageData.centerText);
            setCornerText(imageData.cornerText);
            load(imageData.url);
        }
    }

    public ImageCell setCenterText(String text) {
        if (!TextUtils.isEmpty(text)) {
            vCenterText.setText(text);
            if (vCover.getVisibility() != VISIBLE && vCenterText.getVisibility() != VISIBLE) {
                vCover.setVisibility(VISIBLE);
                vCenterText.setVisibility(VISIBLE);
            }
        } else {
            if (vCover.getVisibility() != GONE && vCenterText.getVisibility() != GONE) {
                vCover.setVisibility(GONE);
                vCenterText.setVisibility(GONE);
            }
        }
        return this;
    }

    public ImageCell setCornerText(String text) {
        if (!TextUtils.isEmpty(text)) {
            vCornerText.setText(text);
            if (vCornerText.getVisibility() != VISIBLE) {
                vCornerText.setVisibility(VISIBLE);
            }
        } else {
            if (vCornerText.getVisibility() != GONE) {
                vCornerText.setVisibility(GONE);
            }
        }
        return this;
    }

    public void load(String url) {
        glideRequests.load(url)
                .placeholder(R.mipmap.image_loading)
                .error(R.mipmap.image_load_err)
                .fitCenter()
                .transition(new DrawableTransitionOptions().dontTransition())
                .thumbnail(THUMBNAIL_RATIO)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(new ImageCellTarget(vImage) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Drawable drawable = resource;
                        Log.d("--->", "width: " + drawable.getIntrinsicWidth() + " height: " + drawable.getIntrinsicHeight());
                        if (!loadGif && resource instanceof GifDrawable) {
                            GifDrawable gifDrawable = (GifDrawable) resource;
                            drawable = new BitmapDrawable(gifDrawable.getFirstFrame());
                            setCornerText("GIF");
                        } else {
                            setCornerText(imageData.cornerText);
                        }
                        super.onResourceReady(drawable, transition);
                    }
                });
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
