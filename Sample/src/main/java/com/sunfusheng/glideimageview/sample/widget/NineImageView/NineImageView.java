package com.sunfusheng.glideimageview.sample.widget.NineImageView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.SystemClock;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.sunfusheng.glideimageview.sample.R;
import com.sunfusheng.util.Utils;

import java.util.List;

/**
 * @author sunfusheng on 2018/6/19.
 */
public class NineImageView extends ViewGroup {

    private static final int MAX_IMAGE_SIZE = 9;
    private static final int MAX_SPAN_COUNT = 3;

    private List<ImageData> dataSource;
    private int size;
    private int margin;
    private int cellWidth;
    private int cellHeight;
    private boolean shouldLoad;

    private boolean enableRoundCorner;
    private int roundCornerRadius;
    private final Xfermode DST_IN = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private final Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path roundPath = new Path();
    private RectF roundRect = new RectF(0, 0, 0, 0);

    private boolean loadGif;
    private int textColor;
    private int textSize;

    private Rect cellRect = new Rect();
    private int clickPosition;
    private boolean hasPerformedLongClick;
    private boolean hasPerformedItemClick;
    private OnItemClickListener onItemClickListener;

    public NineImageView(Context context) {
        this(context, null);
    }

    public NineImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        cellWidth = cellHeight = Utils.dp2px(context, 60);
        setMargin(4);
        setRoundCornerRadius(5);
        textColor = R.color.white;
        textSize = 20;
    }

    public void setData(List<ImageData> list) {
        setData(list, getDefaultLayoutHelper(list));
    }

    private GridLayoutHelper getDefaultLayoutHelper(List<ImageData> list) {
        int spanCount = list != null ? list.size() : 0;
        if (spanCount > MAX_SPAN_COUNT) {
            spanCount = (int) Math.ceil(Math.sqrt(spanCount));
        }
        if (spanCount > MAX_SPAN_COUNT) {
            spanCount = MAX_SPAN_COUNT;
        }
        return new GridLayoutHelper(spanCount, cellWidth, cellHeight, margin);
    }

    public void setData(List<ImageData> data, LayoutHelper layoutHelper) {
        this.dataSource = data;
        this.shouldLoad = true;
        if (layoutHelper == null) {
            layoutHelper = getDefaultLayoutHelper(data);
        }

        long start = SystemClock.currentThreadTimeMillis();
        size = Utils.getSize(data);
        if (size > MAX_IMAGE_SIZE) {
            size = MAX_IMAGE_SIZE;
            data.get(MAX_IMAGE_SIZE - 1).text = "+" + String.valueOf(Utils.getSize(data) - MAX_IMAGE_SIZE);
        }

        if (size > 0) {
            int index = 0;
            for (; index < size; index++) {
                ImageData imageData = data.get(index);
                imageData.from(imageData, layoutHelper, index);
                ImageCell imageCell = (ImageCell) getChildAt(index);
                if (imageCell == null) {
                    imageCell = new ImageCell(getContext())
                            .setLoadGif(loadGif)
                            .setTextColor(textColor)
                            .setTextSize(textSize)
                            .setRadius(enableRoundCorner ? roundCornerRadius : 0);
                    addView(imageCell);
                }
                imageCell.setData(imageData);
                imageCell.setVisibility(VISIBLE);
            }
            for (; index < getChildCount(); index++) {
                getChildAt(index).setVisibility(GONE);
            }
        }
        requestLayout();
        Log.d("--->", "MultiImageView setData() consume time:" + (SystemClock.currentThreadTimeMillis() - start));
    }

    public List<ImageData> getData() {
        return dataSource;
    }

    public NineImageView setMargin(int margin) {
        this.margin = Utils.dp2px(getContext(), margin);
        return this;
    }

    public NineImageView enableRoundCorner(boolean enableRoundCorner) {
        this.enableRoundCorner = enableRoundCorner;
        return this;
    }

    public NineImageView setRoundCornerRadius(int roundCornerRadius) {
        this.roundCornerRadius = Utils.dp2px(getContext(), roundCornerRadius);
        return this;
    }

    public NineImageView loadGif(boolean loadGif) {
        this.loadGif = loadGif;
        return this;
    }

    public NineImageView setTextColor(@ColorRes int textColor) {
        this.textColor = textColor;
        return this;
    }

    public NineImageView setTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    public ImageCell getImageCell(int index) {
        View view = getChildAt(index);
        if (view != null && view.getVisibility() == VISIBLE) {
            return (ImageCell) view;
        }
        return null;
    }

    public void setText(int index, String text) {
        ImageCell imageCell = getImageCell(index);
        if (imageCell != null) {
            imageCell.setText(text);
        }
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                hasPerformedLongClick = false;
                hasPerformedItemClick = false;
                clickPosition = getPositionByXY(x, y);
                break;
            case MotionEvent.ACTION_UP:
                if (onItemClickListener != null && !hasPerformedLongClick && clickPosition == getPositionByXY(x, y)) {
                    if (clickPosition >= 0) {
                        hasPerformedItemClick = true;
                        onItemClickListener.onItemClick(clickPosition);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private int getPositionByXY(int x, int y) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.getHitRect(cellRect);
            if (cellRect.contains(x, y)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean performLongClick() {
        hasPerformedLongClick = true;
        return super.performLongClick();
    }

    @Override
    public boolean performClick() {
        return !hasPerformedItemClick && super.performClick();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
