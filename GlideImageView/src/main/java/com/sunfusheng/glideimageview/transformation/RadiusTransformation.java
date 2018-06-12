package com.sunfusheng.glideimageview.transformation;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * @author by sunfusheng on 2017/6/6.
 */
public class RadiusTransformation extends BitmapTransformation {

    private int radius;

    public RadiusTransformation(int radius) {
        this.radius = radius;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return radiusCrop(pool, toTransform);
    }

    private Bitmap radiusCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int size = Math.min(source.getWidth(), source.getHeight());

            Bitmap bitmap = pool.get(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            canvas.drawRoundRect(0, 0, size, size, radius, radius, paint);
            return bitmap;
        }
        return source;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
