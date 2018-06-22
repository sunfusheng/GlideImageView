package com.sunfusheng.glideimageview.sample.widget.MultiImageView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sunfusheng.glideimageview.sample.R;
import com.sunfusheng.progress.GlideApp;
import com.sunfusheng.progress.GlideRequests;
import com.sunfusheng.util.Utils;

/**
 * @author sunfusheng on 2018/6/19.
 */
public class ImageCell extends AppCompatImageView {

    private static final float THUMBNAIL_RATIO = 0.1f;
    private GlideRequests glideRequests;
    private ImageData imageData;

    private boolean isGif;
    private boolean loadGif;
    private Drawable gifDrawable;
    private int gifWidth;
    private int gifHeight;
    private Rect gifRect;
    private int gifMargin;

    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint.FontMetricsInt fontMetrics;
    private float textX;
    private float textY;

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
        gifRect = new Rect();
        gifMargin = Utils.dp2px(getContext(), 4);

        textPaint.setTextSize(Utils.dp2px(getContext(), 20));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.WHITE);
        fontMetrics = textPaint.getFontMetricsInt();
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

    public ImageCell setGifDrawable(Drawable gifDrawable) {
        this.gifDrawable = gifDrawable;
        if (gifDrawable != null) {
            gifWidth = Utils.dp2px(getContext(), gifDrawable.getIntrinsicWidth());
            gifHeight = Utils.dp2px(getContext(), gifDrawable.getIntrinsicHeight());
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
                .into(new ImageCellTarget(this) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Drawable drawable = resource;
                        isGif = false;
                        if (!loadGif && resource instanceof GifDrawable) {
                            isGif = true;
                            drawable = new BitmapDrawable(((GifDrawable) resource).getFirstFrame());
                        }
                        super.onResourceReady(drawable, transition);
                    }
                });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (gifDrawable != null) {
            gifRect.set(r - l - gifMargin - gifWidth,
                    b - t - gifHeight - gifMargin,
                    r - l - gifMargin,
                    b - t - gifMargin);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (isGif && gifDrawable != null) {
            gifDrawable.setBounds(gifRect);
            gifDrawable.draw(canvas);
        }

        Log.d("--->","imageData.text:"+imageData.text);

        if (!TextUtils.isEmpty(imageData.text)) {
            getDrawable().getBounds();
            canvas.drawColor(getResources().getColor(R.color.transparent30));
            textX = getWidth() / 2;
            textY = getHeight() / 2 + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
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
        postInvalidate();
        return this;
    }

    public ImageCell setTextSize(int size) {
        textPaint.setTextSize(Utils.dp2px(getContext(), size));
        fontMetrics = textPaint.getFontMetricsInt();
        postInvalidate();
        return this;
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
