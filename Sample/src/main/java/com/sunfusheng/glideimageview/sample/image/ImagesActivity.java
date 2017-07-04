package com.sunfusheng.glideimageview.sample.image;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.sunfusheng.glideimageview.sample.R;
import com.sunfusheng.glideimageview.sample.util.ColorUtil;
import com.sunfusheng.glideimageview.sample.util.StatusBarUtil;
import com.sunfusheng.glideimageview.sample.widget.NineImageView.ImageAttr;
import com.sunfusheng.glideimageview.util.DisplayUtil;

import java.util.List;

public class ImagesActivity extends Activity implements ViewTreeObserver.OnPreDrawListener {

    public static final String IMAGE_ATTR = "image_attr";
    public static final String CUR_POSITION = "cur_position";
    public static final int ANIM_DURATION = 300; // ms

    private RelativeLayout rootView;
    private ViewPager viewPager;
    private TextView tvTip;
    private ImagesAdapter mAdapter;
    private List<ImageAttr> imageAttrs;

    private int curPosition;
    private int screenWidth;
    private int screenHeight;
    private float scaleX;
    private float scaleY;
    private float translationX;
    private float translationY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        StatusBarUtil.setStatusBarTranslucent(this, false);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tvTip = (TextView) findViewById(R.id.tv_tip);
        rootView = (RelativeLayout) findViewById(R.id.rootView);
        screenWidth = DisplayUtil.getScreenWidth(this);
        screenHeight = DisplayUtil.getScreenHeight(this);

        Intent intent = getIntent();
        imageAttrs = (List<ImageAttr>) intent.getSerializableExtra(IMAGE_ATTR);
        curPosition = intent.getIntExtra(CUR_POSITION, 0);
        tvTip.setText(String.format(getString(R.string.image_index), (curPosition + 1), imageAttrs.size()));

        mAdapter = new ImagesAdapter(this, imageAttrs);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(curPosition);
        viewPager.getViewTreeObserver().addOnPreDrawListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                curPosition = position;
                tvTip.setText(String.format(getString(R.string.image_index), (curPosition + 1), imageAttrs.size()));
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishWithAnim();
    }

    private void initImageAttr(ImageAttr attr) {
        int originalWidth = attr.width;
        int originalHeight = attr.height;
        int originalCenterX = attr.left + originalWidth / 2;
        int originalCenterY = attr.top + originalHeight / 2;

        float widthRatio = screenWidth * 1.0f / originalWidth;
        float heightRatio = screenHeight * 1.0f / originalHeight;
        float ratio = widthRatio > heightRatio ? heightRatio : widthRatio;
        int finalWidth = (int) (originalWidth * ratio);
        int finalHeight = (int) (originalHeight * ratio);

        scaleX = originalWidth * 1.0f / finalWidth;
        scaleY = originalHeight * 1.0f / finalHeight;
        translationX = originalCenterX - screenWidth / 2;
        translationY = originalCenterY - screenHeight / 2;

        Log.d("--->", "(left, top): (" + attr.left + ", " + attr.top + ")");
        Log.d("--->", "originalWidth: " + originalWidth + " originalHeight: " + originalHeight);
        Log.d("--->", "finalWidth: " + finalWidth + " finalHeight: " + finalHeight);
        Log.d("--->", "scaleX: " + scaleX + " scaleY: " + scaleY);
        Log.d("--->", "translationX: " + translationX + " translationY: " + translationY);
        Log.d("--->", "" + attr.toString());
        Log.d("--->", "----------------------------------------------------------------");
    }

    @Override
    public boolean onPreDraw() {
        rootView.getViewTreeObserver().removeOnPreDrawListener(this);
        PhotoView photoView = mAdapter.getPhotoView(curPosition);
        ImageAttr attr = imageAttrs.get(curPosition);
        initImageAttr(attr);

        setBackgroundColor(0f, 1f, null);
        translateXAnim(photoView, translationX, 0);
        translateYAnim(photoView, translationY, 0);
        scaleXAnim(photoView, scaleX, 1);
        scaleYAnim(photoView, scaleY, 1);
        return true;
    }

    public void finishWithAnim() {
        PhotoView photoView = mAdapter.getPhotoView(curPosition);
        ImageAttr attr = imageAttrs.get(curPosition);
        initImageAttr(attr);

        translateXAnim(photoView, 0, translationX);
        translateYAnim(photoView, 0, translationY);
        scaleXAnim(photoView, 1, scaleX);
        scaleYAnim(photoView, 1, scaleY);
        setBackgroundColor(1f, 0f, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private void translateXAnim(PhotoView photoView, float from, float to) {
        ValueAnimator anim = ValueAnimator.ofFloat(from, to);
        anim.addUpdateListener(it -> photoView.setX((Float) it.getAnimatedValue()));
        anim.setDuration(ANIM_DURATION);
        anim.start();
    }

    private void translateYAnim(PhotoView photoView, float from, float to) {
        ValueAnimator anim = ValueAnimator.ofFloat(from, to);
        anim.addUpdateListener(it -> photoView.setY((Float) it.getAnimatedValue()));
        anim.setDuration(ANIM_DURATION);
        anim.start();
    }

    private void scaleXAnim(PhotoView photoView, float from, float to) {
        ValueAnimator anim = ValueAnimator.ofFloat(from, to);
        anim.addUpdateListener(it -> photoView.setScaleX((Float) it.getAnimatedValue()));
        anim.setDuration(ANIM_DURATION);
        anim.start();
    }

    private void scaleYAnim(PhotoView photoView, float from, float to) {
        ValueAnimator anim = ValueAnimator.ofFloat(from, to);
        anim.addUpdateListener(it -> photoView.setScaleY((Float) it.getAnimatedValue()));
        anim.setDuration(ANIM_DURATION);
        anim.start();
    }

    private void setBackgroundColor(float from, float to, Animator.AnimatorListener listener) {
        ValueAnimator anim = ValueAnimator.ofFloat(from, to);
        anim.addUpdateListener(it -> rootView.setBackgroundColor(ColorUtil.evaluate((Float) it.getAnimatedValue(), Color.TRANSPARENT, Color.BLACK)));
        anim.setDuration(ANIM_DURATION);
        if (listener != null) {
            anim.addListener(listener);
        }
        anim.start();
    }
}
