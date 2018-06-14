package com.sunfusheng.glideimageview.sample.widget.NineImageView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sunfusheng.glideimageview.GlideImageLoader;
import com.sunfusheng.glideimageview.sample.R;
import com.sunfusheng.glideimageview.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

public class NineImageView extends ViewGroup {

    public static final int MODE_GRID = 0;          //网格模式，4张图2X2布局
    public static final int MODE_FILL = 1;          //填充模式，类似于微信

    private int singleImageWidth;
    private int singleImageHeight;
    private int singleImageMaxWidth;
    private int singleImageMinWidth;

    private int maxImageSize = 9;                   // 最大显示的图片数
    private int gridSpacing = 5;                    // 宫格间距，单位dp
    private int mode = MODE_GRID;                   // 默认使用GRID模式

    private int columnCount;    // 列数
    private int rowCount;       // 行数
    private int gridWidth;      // 宫格宽度
    private int gridHeight;     // 宫格高度

    private List<ImageView> imageViews;
    private List<ImageAttr> imageAttrs;
    private NineImageViewAdapter mAdapter;

    public NineImageView(Context context) {
        this(context, null);
    }

    public NineImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        int WIDTH = DisplayUtil.getScreenWidth(context) - DisplayUtil.dip2px(context, 15 * 2);
        singleImageMaxWidth = WIDTH * 2 / 3;
        singleImageMinWidth = WIDTH / 3;
        singleImageWidth = singleImageMinWidth;
        singleImageHeight = singleImageMinWidth;

        imageViews = new ArrayList<>();

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        gridSpacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, gridSpacing, displayMetrics);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineGridView);
        gridSpacing = (int) typedArray.getDimension(R.styleable.NineGridView_ngv_gridSpacing, gridSpacing);
        maxImageSize = typedArray.getInt(R.styleable.NineGridView_ngv_maxSize, maxImageSize);
        mode = typedArray.getInt(R.styleable.NineGridView_ngv_mode, mode);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (imageAttrs == null) return;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;
        int totalWidth = width - getPaddingLeft() - getPaddingRight();
        if (imageAttrs.size() == 1) {
            gridWidth = singleImageWidth;
            gridHeight = singleImageHeight;
        } else {
            gridWidth = gridHeight = (totalWidth - gridSpacing * 2) / 3;
        }
        width = gridWidth * columnCount + gridSpacing * (columnCount - 1) + getPaddingLeft() + getPaddingRight();
        height = gridHeight * rowCount + gridSpacing * (rowCount - 1) + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChildrenView();
    }

    private void layoutChildrenView() {
        if (imageAttrs == null) return;
        int childrenCount = imageAttrs.size();
        for (int i = 0; i < childrenCount; i++) {
            ImageView imageView = (ImageView) getChildAt(i);
            ImageAttr imageAttr = imageAttrs.get(i);
            if (imageView == null) continue;

            int rowNum = i / columnCount;
            int columnNum = i % columnCount;
            int left = (gridWidth + gridSpacing) * columnNum + getPaddingLeft();
            int top = (gridHeight + gridSpacing) * rowNum + getPaddingTop();
            int right = left + gridWidth;
            int bottom = top + gridHeight;
            imageView.layout(left, top, right, bottom);

            loadImage(imageView, imageAttr, childrenCount);
        }
    }

    private void loadImage(ImageView imageView, ImageAttr attr, int count) {
        String url = TextUtils.isEmpty(attr.thumbnailUrl) ? attr.url : attr.thumbnailUrl;
        GlideImageLoader imageLoader = GlideImageLoader.create(imageView);

//        RequestOptions requestOptions = imageLoader.requestOptions(R.color.placeholder)
//                .centerCrop()
//                .skipMemoryCache(false)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
//
//        imageLoader.requestBuilder(url, requestOptions)
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        attr.realWidth = resource.getIntrinsicWidth();
//                        attr.realHeight = resource.getIntrinsicHeight();
//                        if (count == 1) {
//                            setSingleImageWidthHeight(resource);
//                        }
//                        return false;
//                    }
//                }).into(imageView);
    }

    private void setSingleImageWidthHeight(Drawable drawable) {
        if (drawable == null) return;
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        float ratio = width * 1.0f / height;

        if (width > singleImageMaxWidth || ratio > 2.1f) {
            singleImageWidth = singleImageMaxWidth;
            singleImageHeight = (int) (singleImageMaxWidth * height * 1.0f / width);
        } else if (width < singleImageMinWidth || ratio < 0.7f) {
            if (ratio < 0.3f) {
                singleImageWidth = singleImageMinWidth / 2;
                singleImageHeight = (int) (singleImageMinWidth / 2 * height * 1.0f / width);
                if (singleImageHeight > singleImageMaxWidth) {
                    singleImageHeight = singleImageMaxWidth;
                }
            } else {
                singleImageWidth = singleImageMinWidth;
                singleImageHeight = (int) (singleImageMinWidth * height * 1.0f / width);
            }
        } else {
            singleImageWidth = width;
            singleImageHeight = height;
        }
    }

    public void setAdapter(@NonNull NineImageViewAdapter adapter) {
        this.mAdapter = adapter;
        List<ImageAttr> attrList = adapter.getImageAttrs();
        if (attrList == null || attrList.isEmpty()) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);

        int imageCount = attrList.size();
        if (maxImageSize > 0 && imageCount > maxImageSize) {
            attrList = attrList.subList(0, maxImageSize);
            imageCount = attrList.size(); // 再次获取图片数量
        }

        // 默认是3列显示，行数根据图片的数量决定
        rowCount = imageCount / 3 + (imageCount % 3 == 0 ? 0 : 1);
        columnCount = 3;
        // Grid模式下，显示4张使用2X2模式
        if (mode == MODE_GRID) {
            if (imageCount == 4) {
                rowCount = 2;
                columnCount = 2;
            }
        }

        // 保证View的复用，避免重复创建
        if (imageAttrs == null) {
            for (int i = 0; i < imageCount; i++) {
                ImageView imageView = getImageView(i);
                addView(imageView, generateDefaultLayoutParams());
            }
        } else {
            int oldViewCount = imageAttrs.size();
            if (oldViewCount > imageCount) {
                removeViews(imageCount, oldViewCount - imageCount);
            } else if (oldViewCount < imageCount) {
                for (int i = oldViewCount; i < imageCount; i++) {
                    ImageView imageView = getImageView(i);
                    addView(imageView, generateDefaultLayoutParams());
                }
            }
        }

        // 修改最后一个条目，决定是否显示更多
        if (adapter.getImageAttrs().size() > maxImageSize) {
            View child = getChildAt(maxImageSize - 1);
            if (child instanceof ImageViewWrapper) {
                ImageViewWrapper imageView = (ImageViewWrapper) child;
                imageView.setMoreNum(adapter.getImageAttrs().size() - maxImageSize);
            }
        }
        imageAttrs = attrList;
        layoutChildrenView();
    }

    // 获得ImageView，并保证ImageView的重用
    private ImageView getImageView(int position) {
        ImageView imageView = null;
        if (position < imageViews.size()) {
            imageView = imageViews.get(position);
        }
        if (imageView == null) {
            imageView = mAdapter.generateImageView(getContext());
            imageView.setOnClickListener(v -> mAdapter.onImageItemClick(getContext(), NineImageView.this, position, mAdapter.getImageAttrs()));
            imageViews.add(imageView);
        }
        return imageView;
    }

    public NineImageViewAdapter getAdapter() {
        return mAdapter;
    }

    public void setGridSpacing(int spacing) {
        gridSpacing = spacing;
    }

    public void setMaxSize(int maxSize) {
        maxImageSize = maxSize;
    }

    public int getMaxSize() {
        return maxImageSize;
    }

}
