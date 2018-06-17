package com.sunfusheng.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DisplayUtil {

    private static WindowManager windowManager;

    private static WindowManager getWindowManager(Context context) {
        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return windowManager;
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static float getFontDensity(Context context) {
        return context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager(context).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static int dp2px(Context context, float dp) {
        return (int) (getDensity(context) * dp + 0.5f);
    }

    public static int px2dp(Context context, float px) {
        return (int) (px / getDensity(context) + 0.5f);
    }

    public static int sp2px(Context context, float sp) {
        return (int) (getFontDensity(context) * sp + 0.5f);
    }

    public static int px2sp(Context context, float px) {
        return (int) (px / getFontDensity(context) + 0.5f);
    }

    public static int getWindowWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    public static int getWindowHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }
}
