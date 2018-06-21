package com.sunfusheng.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class Utils {

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

    public static String getPathFormat(String path) {
        if (!TextUtils.isEmpty(path)) {
            int lastPeriodIndex = path.lastIndexOf('.');
            if (lastPeriodIndex > 0 && lastPeriodIndex + 1 < path.length()) {
                String format = path.substring(lastPeriodIndex + 1);
                if (!TextUtils.isEmpty(format)) {
                    return format.toLowerCase();
                }
            }
        }
        return "";
    }

    public static boolean isGif(String url) {
        return "gif".equals(getPathFormat(url));
    }
}
