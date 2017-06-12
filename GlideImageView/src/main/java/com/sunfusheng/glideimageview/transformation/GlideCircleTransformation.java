package com.sunfusheng.glideimageview.transformation;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Created by sunfusheng on 2017/6/6.
 */
public class GlideCircleTransformation extends BitmapTransformation {

    public GlideCircleTransformation() {
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap square = Bitmap.createBitmap(source, x, y, size, size);
        Bitmap circle = pool.get(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(circle);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(square, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        return circle;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}
