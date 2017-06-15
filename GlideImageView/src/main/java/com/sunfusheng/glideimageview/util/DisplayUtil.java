package com.sunfusheng.glideimageview.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DisplayUtil {

    public static final int STANDARD_SCREEN_WIDTH = 720;
    public static final int STANDARD_SCREEN_HEIGHT = 1280;
    public static final int STANDARD_SCREEN_DENSITY = 320;

    private static WindowManager windowManager;

    private static WindowManager getWindowManager(Context context) {
        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return windowManager;
    }

    // 根据手机的分辨率将dp的单位转成px(像素)
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    // 根据手机的分辨率将px(像素)的单位转成dp
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    // 将px值转换为sp值
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    // 将sp值转换为px值
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    // 屏幕宽度（像素）
    public static int getWindowWidth(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    // 屏幕高度（像素）
    public static int getWindowHeight(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        try {
            Display defaultDisplay = getWindowManager(context).getDefaultDisplay();

            DisplayMetrics displayMetrics = new DisplayMetrics();
            defaultDisplay.getMetrics(displayMetrics);
            if (displayMetrics.widthPixels > 0) {
                return displayMetrics.widthPixels;
            }

            Point screenSize = new Point();
            defaultDisplay.getSize(screenSize);
            if (screenSize.x > 0) {
                return screenSize.x;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return STANDARD_SCREEN_WIDTH;
    }

    public static int getScreenHeight(Context context) {
        try {
            Display defaultDisplay = getWindowManager(context).getDefaultDisplay();

            DisplayMetrics displayMetrics = new DisplayMetrics();
            defaultDisplay.getMetrics(displayMetrics);
            if (displayMetrics.heightPixels > 0) {
                return displayMetrics.heightPixels;
            }

            Point screenSize = new Point();
            defaultDisplay.getSize(screenSize);
            if (screenSize.y > 0) {
                return screenSize.y;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return STANDARD_SCREEN_HEIGHT;
    }

    public static float getScreenDensity(Context context) {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager(context).getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.density;
        } catch (Throwable e) {
            e.printStackTrace();
            return 1.0f;
        }
    }

    public static int getDensityDpi(Context context) {
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager(context).getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.densityDpi;
        } catch (Throwable e) {
            e.printStackTrace();
            return STANDARD_SCREEN_DENSITY;
        }
    }
}
