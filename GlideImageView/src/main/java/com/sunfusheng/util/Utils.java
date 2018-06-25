package com.sunfusheng.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
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

    public static Bitmap getTextBitmap(Context context, int width, int height, int radius, String text, int textSize, @ColorRes int bgColor) {
        radius = dp2px(context, radius);
        Bitmap bitmap = Bitmap.createBitmap(dp2px(context, width), dp2px(context, height), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        RectF rect = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(bgColor));
        canvas.drawRoundRect(new RectF(0, 0, rect.width(), rect.height()), radius, radius, paint);
        paint.setColor(Color.WHITE);
        paint.setTextSize(dp2px(context, textSize));
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        float baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText(text, rect.centerX(), baseline, paint);
        return bitmap;
    }

    public static Drawable getTextDrawable(Context context, int width, int height, int radius, String text, int textSize, @ColorRes int bgColor) {
        return new BitmapDrawable(getTextBitmap(context, width, height, radius, text, textSize, bgColor));
    }
}
