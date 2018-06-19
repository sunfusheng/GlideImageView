package com.sunfusheng.glideimageview.sample.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class SlideVerticallyLayout extends FrameLayout {

    private int mDownY;
    private int mEndY;
    private ValueAnimator mSlideAnimator;
    private boolean isCanScroll;
    private boolean isTouchWithAnimRunning;
    private final int MIN_SCROLL_OFFSET = 50;
    private final static int ANIM_DURATION = 280;
    private final static int THRESHOLD = 4;
    private List<View> mSlideView = new ArrayList<>();
    private List<ISlideCallback> slideCblist = new ArrayList<>();
    private List<IStateCallback> stateCblist = new ArrayList<>();
    private boolean isSlideShown = true;

    public void addSlideCallback(ISlideCallback callback) {
        slideCblist.add(callback);
    }

    public void addStateCallback(IStateCallback callback) {
        stateCblist.add(callback);
    }

    public SlideVerticallyLayout(Context context) {
        this(context, null);
    }

    public SlideVerticallyLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideVerticallyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        mSlideAnimator = ValueAnimator.ofFloat(0, 1.0f).setDuration(ANIM_DURATION);
        mSlideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float factor = (float) animation.getAnimatedValue();
                int diffY = mEndY - mDownY;
                for (ISlideCallback cb : slideCblist) {
                    cb.onPositionChange((int) (mDownY + diffY * factor));
                }
            }
        });
        mSlideAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isSlideShown && mEndY == getHeight()) {
                    isSlideShown = false;
                    for (IStateCallback cb : stateCblist) {
                        cb.onHide();
                    }
                } else if (!isSlideShown && mEndY == 0) {
                    isSlideShown = true;
                    for (IStateCallback cb : stateCblist) {
                        cb.onShow();
                    }
                }
                mDownY = mEndY;
                isCanScroll = false;
            }
        });
        addSlideCallback(offsetY -> {
            float factor = (float) (getHeight() - offsetY) / (float) getHeight();
            setBackgroundColor(((int) (factor * 0xc4)) << 24);
        });
        addSlideCallback(offsetY -> {
            for (View view : mSlideView) {
                view.setTranslationY(offsetY);
            }
        });
    }

    public void addSlideView(View view) {
        mSlideView.add(view);
    }

    public void slideDown(boolean anim) {
        if (!isSlideShown) return;
        if (anim) {
            mEndY = getHeight();
            mDownY = 0;
            mSlideAnimator.start();
        } else {
            for (ISlideCallback cb : slideCblist) {
                cb.onPositionChange(getHeight());
            }
            isSlideShown = false;
        }
    }

    public void slideUp(boolean anim) {
        if (isSlideShown) return;
        if (anim) {
            mEndY = 0;
            mDownY = getHeight();
            mSlideAnimator.start();
        } else {
            for (ISlideCallback cb : slideCblist) {
                cb.onPositionChange(0);
            }
            isSlideShown = true;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!canChildScrollUp()) {
            final int y = (int) event.getY();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    isTouchWithAnimRunning = mSlideAnimator.isRunning();
                    mDownY = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isGreaterThanMinSize(mDownY, y) && !isTouchWithAnimRunning) {
                        isCanScroll = true;
                        return true;
                    }
                    break;
            }
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int y = (int) event.getY();
        int offsetY = y - mDownY;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:
                if (isGreaterThanMinSize(mDownY, y) && isCanScroll) {
                    for (ISlideCallback cb : slideCblist) {
                        cb.onPositionChange(getPositionChangeY(offsetY));
                    }
                } else if (isCanScroll) {
                    for (ISlideCallback cb : slideCblist) {
                        cb.onPositionChange(0);
                    }
                }
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_UP:
                if (isGreaterThanMinSize(mDownY, y) && isCanScroll) {
                    mDownY = getPositionChangeY(offsetY);
                    fixPosition(offsetY);
                    mSlideAnimator.start();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean canChildScrollUp() {
        for (View view : mSlideView) {
            if (canChildScrollUp(view)) return true;
        }
        return false;
    }

    private boolean canChildScrollUp(View view) {
        if (Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(view, -1) || view.getScrollY() > 0;
        } else {
            return ViewCompat.canScrollVertically(view, -1);
        }
    }

    private int getPositionChangeY(int offsetY) {
        if (isSlideShown) {
            return Math.abs(offsetY) - MIN_SCROLL_OFFSET;
        } else {
            return getHeight() - (Math.abs(offsetY)) - MIN_SCROLL_OFFSET;
        }
    }

    private boolean isGreaterThanMinSize(int y1, int y2) {
        if (isSlideShown) {
            return y2 - y1 > MIN_SCROLL_OFFSET;
        } else {
            return y1 - y2 > MIN_SCROLL_OFFSET;
        }
    }

    private void fixPosition(int offsetY) {
        int absOffsetY = Math.abs(offsetY);
        if (isSlideShown && absOffsetY > getHeight() / THRESHOLD) {
            mEndY = getHeight();
        } else if (!isSlideShown && absOffsetY > getHeight() / THRESHOLD) {
            mEndY = 0;
        }
    }

    public interface ISlideCallback {
        void onPositionChange(int offsetY);
    }

    public interface IStateCallback {
        void onShow();

        void onHide();
    }

}
