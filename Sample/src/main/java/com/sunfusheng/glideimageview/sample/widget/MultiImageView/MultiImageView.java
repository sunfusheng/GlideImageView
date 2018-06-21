package com.sunfusheng.glideimageview.sample.widget.MultiImageView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

import com.sunfusheng.util.Utils;

import java.util.List;

/**
 * @author sunfusheng on 2018/6/19.
 */
public class MultiImageView extends ViewGroup {

    private List<ImageData> dataSource;
    private int size;
    private int margin;
    private int cellWidth;
    private int cellHeight;

    private boolean enableRoundCorner;
    private int roundCornerRadius;
    private final Xfermode DST_IN = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private final Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path roundPath = new Path();
    private RectF roundRect = new RectF(0, 0, 0, 0);

    private boolean loadGif;
    private boolean shouldLoad;

    public MultiImageView(Context context) {
        this(context, null);
    }

    public MultiImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        margin = Utils.dp2px(getContext(), 3);
        cellWidth = cellHeight = Utils.dp2px(getContext(), 60);
        setRoundCornerRadius(5);
    }

    public void setData(List<ImageData> list) {
        setData(list, getDefaultLayoutHelper(list));
    }

    private GridLayoutHelper getDefaultLayoutHelper(List<ImageData> list) {
        int imageCount = list != null ? list.size() : 0;
        if (imageCount > 3) {
            imageCount = (int) Math.ceil(Math.sqrt(imageCount));
        }
        return new GridLayoutHelper(imageCount, cellWidth, cellHeight, margin);
    }

    public void setData(List<ImageData> list, LayoutHelper layoutHelper) {
        this.dataSource = list;
        this.shouldLoad = true;

        long start = SystemClock.currentThreadTimeMillis();
        size = dataSource != null ? dataSource.size() : 0;
        if (size > 0 && layoutHelper != null) {
            for (int i = 0; i < getChildCount(); i++) {
                getChildAt(i).setVisibility(GONE);
            }

            for (int position = 0; position < size; position++) {
                ImageData imageData = dataSource.get(position);
                imageData.from(imageData, layoutHelper, position);
                ImageCell imageCell = (ImageCell) getChildAt(position);
                if (imageCell == null) {
                    imageCell = new ImageCell(getContext(), loadGif);
                    addView(imageCell);
                }
                imageCell.setData(imageData);
                imageCell.setVisibility(VISIBLE);
            }
        }
        requestLayout();
        Log.d("--->", "MultiImageView setData() consume time:" + (SystemClock.currentThreadTimeMillis() - start));
    }

    public MultiImageView enableRoundCorner(boolean enableRoundCorner) {
        this.enableRoundCorner = enableRoundCorner;
        return this;
    }

    public MultiImageView setRoundCornerRadius(int roundCornerRadius) {
        this.roundCornerRadius = Utils.dp2px(getContext(), roundCornerRadius);
        return this;
    }

    public MultiImageView loadGif(boolean loadGif) {
        this.loadGif = loadGif;
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;
        for (int i = 0; i < size; i++) {
            ImageData item = dataSource.get(i);
            int currWidth = item.startX + item.width;
            int currHeight = item.startY + item.height;
            width = currWidth > width ? currWidth : width;
            height = currHeight > height ? currHeight : height;
        }

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        float widthScale = 1;
        float heightScale = 1;

        switch (widthSpecMode) {
            case MeasureSpec.EXACTLY:
            case MeasureSpec.AT_MOST:
                if (width > widthSpecSize) {
                    widthScale = widthSpecSize * 1.0f / width;
                }
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                break;
        }

        switch (heightSpecMode) {
            case MeasureSpec.EXACTLY:
            case MeasureSpec.AT_MOST:
                if (height > heightSpecSize) {
                    heightScale = heightSpecSize * 1.0f / height;
                }
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                break;
        }

        float scale = Math.min(widthScale, heightScale);
        if (scale < 1) {
            width = 0;
            height = 0;
            for (int i = 0; i < size; i++) {
                ImageData item = dataSource.get(i);
                item.startX *= scale;
                item.startY *= scale;
                item.width *= scale;
                item.height *= scale;

                int currWidth = item.startX + item.width;
                int currHeight = item.startY + item.height;
                width = currWidth > width ? currWidth : width;
                height = currHeight > height ? currHeight : height;
            }
        }

        if (enableRoundCorner) {
            roundRect.right = width;
            roundRect.bottom = height;
            roundPath.reset();
            roundPath.addRoundRect(roundRect, roundCornerRadius, roundCornerRadius, Path.Direction.CW);
        }

        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        for (int i = 0; i < dataSource.size(); i++) {
            ImageCell imageCell = (ImageCell) getChildAt(i);
            if (imageCell != null && imageCell.getVisibility() != GONE) {
                ImageData imageData = dataSource.get(i);
                imageCell.measure(MeasureSpec.makeMeasureSpec(imageData.width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(imageData.height, MeasureSpec.EXACTLY));
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for (int i = 0; i < size; i++) {
            ImageCell imageCell = (ImageCell) getChildAt(i);
            if (imageCell != null && imageCell.getVisibility() != GONE) {
                ImageData imageData = dataSource.get(i);
                imageCell.layout(imageData.startX, imageData.startY, imageData.startX + imageCell.getMeasuredWidth(), imageData.startY + imageCell.getMeasuredHeight());
                if (shouldLoad) {
                    imageCell.setData(imageData);
                }
            }
        }
        shouldLoad = false;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (enableRoundCorner) {
            canvas.saveLayer(0, 0, getMeasuredWidth(), getMeasuredHeight(), roundPaint, Canvas.ALL_SAVE_FLAG);
            super.dispatchDraw(canvas);
            if (size == 1) {
                Paint borderPaint = new Paint();
                borderPaint.setAntiAlias(true);
                borderPaint.setStyle(Paint.Style.STROKE);
                borderPaint.setColor(getResources().getColor(android.R.color.transparent));
                canvas.drawPath(roundPath, borderPaint);
            }

            roundPaint.setXfermode(DST_IN);
            canvas.drawPath(roundPath, roundPaint);
            roundPaint.setXfermode(null);
            canvas.restore();
        } else {
            super.dispatchDraw(canvas);
        }
    }
}
