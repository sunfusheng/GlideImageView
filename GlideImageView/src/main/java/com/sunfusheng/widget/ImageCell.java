package com.sunfusheng.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sunfusheng.glideimageview.R;
import com.sunfusheng.progress.GlideApp;
import com.sunfusheng.util.Utils;

/**
 * @author sunfusheng on 2018/6/19.
 */
public class ImageCell extends ImageView {

    private static final float THUMBNAIL_RATIO = 0.1f;
    private ImageData imageData;
    private int radius;

    private static Drawable gifDrawable;
    private static Drawable longDrawable;
    private boolean isGif;
    private boolean loadGif;
    private int  placeholderResId;
    private int errorResId;

    private boolean hasDrawCornerIcon;
    private Drawable cornerIconDrawable;
    private int cornerIconWidth;
    private int cornerIconHeight;
    private Rect cornerIconBounds;
    private int cornerIconMargin;

    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint.FontMetricsInt fontMetrics;

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
        cornerIconBounds = new Rect();
        cornerIconMargin = Utils.dp2px(getContext(), 3);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setData(ImageData imageData) {
        this.imageData = imageData;
        if (imageData != null) {
            load(imageData.url);
        }
    }

    public ImageCell setLoadGif(boolean loadGif) {
        this.loadGif = loadGif;
        return this;
    }

    public ImageCell setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    public ImageCell placeholder(@DrawableRes int resId) {
        this.placeholderResId = resId;
        return this;
    }

    public ImageCell error(@DrawableRes int resId) {
        this.errorResId = resId;
        return this;
    }

    public void load(String url) {
        GlideApp.with(getContext()).load(url)
                .placeholder(placeholderResId)
                .error(errorResId)
                .fitCenter()
                .transition(new DrawableTransitionOptions().dontTransition())
                .thumbnail(THUMBNAIL_RATIO)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(new DrawableTarget(this) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Drawable drawable = resource;
                        if (resource instanceof GifDrawable) {
                            isGif = true;
                            if (!loadGif) {
                                drawable = new BitmapDrawable(((GifDrawable) resource).getFirstFrame());
                            }
                        } else {
                            isGif = false;
                        }
                        initCornerIcon();
                        super.onResourceReady(drawable, transition);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        isGif = Utils.isGif(url);
                        initCornerIcon();
                        super.onLoadFailed(errorDrawable);
                    }
                });
    }

    private void initCornerIcon() {
        if (isGif && !loadGif) {
            cornerIconDrawable = getGifDrawable();
        } else if (isLongImage()) {
            cornerIconDrawable = getLongDrawable();
        } else {
            cornerIconDrawable = null;
        }

        if (cornerIconDrawable != null) {
            cornerIconWidth = Utils.dp2px(getContext(), cornerIconDrawable.getIntrinsicWidth());
            cornerIconHeight = Utils.dp2px(getContext(), cornerIconDrawable.getIntrinsicHeight());
            if (!hasDrawCornerIcon) {
                postInvalidate();
            }
        }
    }

    public boolean isGifImage() {
        return isGif;
    }

    public boolean isLongImage() {
        int realWidth = imageData != null ? imageData.realWidth : 0;
        int realHeight = imageData != null ? imageData.realHeight : 0;
        if (realWidth == 0 || realHeight == 0 || realWidth >= realHeight) {
            return false;
        }
        return (realHeight / realWidth) >= 4;
    }

    public Drawable getGifDrawable() {
        if (gifDrawable == null) {
            gifDrawable = Utils.getTextDrawable(getContext().getApplicationContext(), 24, 14, 2, "GIF", 11, R.color.nine_image_text_background_color);
        }
        return gifDrawable;
    }

    public Drawable getLongDrawable() {
        if (longDrawable == null) {
            longDrawable = Utils.getTextDrawable(getContext().getApplicationContext(), 25, 14, 2, "长图", 10, R.color.nine_image_text_background_color);
        }
        return longDrawable;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (cornerIconDrawable != null) {
            cornerIconBounds.set(r - l - cornerIconMargin - cornerIconWidth,
                    b - t - cornerIconHeight - cornerIconMargin,
                    r - l - cornerIconMargin,
                    b - t - cornerIconMargin);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (cornerIconDrawable != null) {
            hasDrawCornerIcon = true;
            cornerIconDrawable.setBounds(cornerIconBounds);
            cornerIconDrawable.draw(canvas);
        }

        if (!TextUtils.isEmpty(imageData.text)) {
            canvas.drawColor(getResources().getColor(R.color.nine_image_text_background_color));
            int textX = getWidth() / 2;
            int textY = getHeight() / 2 + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
            canvas.drawText(imageData.text, textX, textY, textPaint);
        }
    }

    public ImageCell setText(String text) {
        if (imageData != null) {
            imageData.text = text;
            postInvalidate();
        }
        return this;
    }

    public ImageCell setTextColor(@ColorRes int color) {
        textPaint.setColor(getResources().getColor(color));
        return this;
    }

    public ImageCell setTextSize(int size) {
        textPaint.setTextSize(Utils.dp2px(getContext(), size));
        fontMetrics = textPaint.getFontMetricsInt();
        return this;
    }

    private class DrawableTarget extends DrawableImageViewTarget {
        DrawableTarget(ImageView view) {
            super(view);
        }

        private void setBackground() {
            if (radius == 0) {
                getView().setBackgroundResource(R.drawable.drawable_image_bg_0dp);
            } else if (radius == 5) {
                getView().setBackgroundResource(R.drawable.drawable_image_bg_5dp);
            } else {
                GradientDrawable drawable = (GradientDrawable) getResources().getDrawable(R.drawable.drawable_image_bg_0dp);
                drawable.setCornerRadius(radius);
                getView().setBackgroundDrawable(drawable);
            }
        }

        @Override
        public void onLoadStarted(Drawable placeholder) {
            setBackground();
            getView().setScaleType(ImageView.ScaleType.FIT_CENTER);
            super.onLoadStarted(placeholder);
        }

        @Override
        public void onLoadFailed(@Nullable Drawable errorDrawable) {
            setBackground();
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
