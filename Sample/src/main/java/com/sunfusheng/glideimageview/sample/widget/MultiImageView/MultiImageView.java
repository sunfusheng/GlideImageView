package com.sunfusheng.glideimageview.sample.widget.MultiImageView;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.sunfusheng.util.DisplayUtil;

import java.util.List;

/**
 * @author sunfusheng on 2018/6/19.
 */
public class MultiImageView extends ViewGroup {

    private List<ImageData> dataSource;
    private LayoutHelper layoutHelper;
    private int size;
    private int radius;
    private final Paint coverRoundRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
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
        radius = DisplayUtil.dp2px(getContext(), 5);
    }

    public void setData(List<ImageData> dataSource, LayoutHelper layoutHelper) {
        this.dataSource = dataSource;
        this.layoutHelper = layoutHelper;
        this.shouldLoad = true;

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
                    imageCell = new ImageCell(getContext());
                    addView(imageCell);
                }
                imageCell.setData(imageData);
                imageCell.setVisibility(VISIBLE);
            }
        }
        requestLayout();
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
        float scale = Math.min(widthScale, heightScale);

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

        roundRect.right = width;
        roundRect.bottom = height;
        roundPath.reset();
        roundPath.addRoundRect(roundRect, radius, radius, Path.Direction.CW);
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
}
