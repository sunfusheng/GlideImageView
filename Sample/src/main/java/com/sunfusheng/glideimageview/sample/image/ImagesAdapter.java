package com.sunfusheng.glideimageview.sample.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.OnOutsidePhotoTapListener;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.sunfusheng.glideimageview.GlideImageLoader;
import com.sunfusheng.glideimageview.progress.CircleProgressView;
import com.sunfusheng.glideimageview.sample.R;
import com.sunfusheng.glideimageview.sample.widget.NineImageView.ImageAttr;
import com.sunfusheng.glideimageview.util.DisplayUtil;

import java.util.List;

public class ImagesAdapter extends PagerAdapter implements OnPhotoTapListener, OnOutsidePhotoTapListener {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<ImageAttr> images;
    private SparseArray<PhotoView> photoViews = new SparseArray<>();

    public ImagesAdapter(Context context, @NonNull List<ImageAttr> images) {
        super();
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    public PhotoView getPhotoView(int index) {
        return photoViews.get(index);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.item_photoview, container, false);
        CircleProgressView progressView = (CircleProgressView) view.findViewById(R.id.progressView);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.photoView);
        photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        photoView.setOnPhotoTapListener(this);
        photoView.setOnOutsidePhotoTapListener(this);
        photoViews.put(position, photoView);

        ImageAttr attr = images.get(position);
        String url = TextUtils.isEmpty(attr.thumbnailUrl) ? attr.url : attr.thumbnailUrl;

        GlideImageLoader imageLoader = GlideImageLoader.create(photoView);
        imageLoader.setOnGlideImageViewListener(url, (percent, isDone, exception) -> {
            progressView.setProgress(percent);
            progressView.setVisibility(isDone ? View.GONE : View.VISIBLE);
        });
        RequestOptions requestOptions = imageLoader.requestOptions(R.color.placeholder)
                .centerCrop()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        RequestBuilder<Drawable> requestBuilder = imageLoader.requestBuilder(url, requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade());
        requestBuilder.into(new SimpleTarget<Drawable>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (resource.getIntrinsicHeight() > DisplayUtil.getScreenHeight(mContext)) {
                    photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
                requestBuilder.into(photoView);
            }
        });

        container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void onPhotoTap(ImageView view, float x, float y) {
        ((ImagesActivity) mContext).finishWithAnim();
    }

    @Override
    public void onOutsidePhotoTap(ImageView imageView) {
        ((ImagesActivity) mContext).finishWithAnim();
    }
}