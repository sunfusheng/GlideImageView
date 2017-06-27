package com.sunfusheng.glideimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 实现与图像叠加的ImageView，效果类似微信聊天列表的图片，可以设置不同形状的底部图片来实现任意图形
 */

public class VariantImageView extends ImageView {

    // 定义Bitmap的默认配置
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLOR_DRAWABLE_DIMENSION = 1;
    //矩阵变换，实现按下的暗影效果
    final ColorMatrix colorMatrix = new ColorMatrix(new float[]{
            0.5f, 0, 0, 0, 0,
            0, 0.5f, 0, 0, 0,
            0, 0, 0.5f, 0, 0,
            0, 0, 0, 0.5f, 0,});
    private ColorFilter cf;
    private boolean pressed = false;    //是否按下
    private Paint ultimatePaint;
    private Paint drawPaint; // 画在画布上的笔

    // 图片的宽高
    private int width;
    private int height;
    private int bgId = R.drawable.chatscene_item_bg_to_nor; //背景叠加图片ID
    private Bitmap bgBitmap;    //底部图片
    private NinePatch bgPatch;    //底部图片生成的9.patch


    public VariantImageView(Context context) {
        this(context, null);
    }

    public VariantImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VariantImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VariantImageViewStyle);
            bgId = array.getResourceId(R.styleable.VariantImageViewStyle_variant_bg_id, R.drawable.chatscene_item_bg_to_nor);
            array.recycle();
        }

        bgBitmap = getBgBitmap(bgId);
        bgPatch = getBgNinePatch(bgBitmap);
        initPaint();
        setClickable(true);
        setDrawingCacheEnabled(true);
        setWillNotDraw(false);
    }

    // 初始化按下的画笔
    private void initPaint() {
        ultimatePaint = new Paint();
        ultimatePaint.setAntiAlias(true);
        ultimatePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// SRC_IN 只显示两层图像交集部分的上层图像

        drawPaint = new Paint();
        drawPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap srcBitmap = getBitmapFromDrawable(drawable);
        Bitmap ultimateBitmap = generateUltimateBitmap(srcBitmap);
        if (ultimateBitmap != null) {
            drawUltimateBitmap(canvas, ultimateBitmap);
        }
    }

    // 获取Bitmap内容
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        try {
            Bitmap bitmap;
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            } else if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap getBgBitmap(int resId) {
        Bitmap bgBitmap = BitmapFactory.decodeResource(getResources(), resId);
        return bgBitmap;
    }

    private NinePatch getBgNinePatch(Bitmap bitmap_bg) {
        NinePatch patch = new NinePatch(bitmap_bg, bitmap_bg.getNinePatchChunk(), null);
        return patch;
    }

    private Bitmap generateUltimateBitmap(Bitmap bitmap_in) {
        if (bgPatch == null) {
            return bitmap_in;
        }
        Bitmap ultimateBitmap = Bitmap.createBitmap(width, width,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(ultimateBitmap);
        Rect rect = new Rect(0, 0, width, height);
        Rect rects = new Rect(0, 0, bitmap_in.getWidth(), bitmap_in.getHeight());
        bgPatch.draw(canvas, rect);
        canvas.drawBitmap(bitmap_in, rects, rect, ultimatePaint);

        return ultimateBitmap;
    }

    // 绘图
    private void drawUltimateBitmap(Canvas canvas, Bitmap bitmap) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        if (pressed) {
            cf = new ColorMatrixColorFilter(colorMatrix);
        } else {
            cf = null;
        }
        drawPaint.setColorFilter(cf);
        canvas.drawBitmap(bitmap, 0, 0, drawPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }


    //分发消息
    @Override
    protected void dispatchSetPressed(boolean pressed) {
        super.dispatchSetPressed(pressed);
        this.pressed = pressed;
        invalidate();
    }

    public int getBgId() {
        return bgId;
    }

    public void setBgId(int bgId) {
        this.bgId = bgId;
        bgBitmap = getBgBitmap(bgId);
        bgPatch = getBgNinePatch(bgBitmap);
        invalidate();
    }

    public void setBgBitmap(Bitmap bgBitmap) {
        if (bgBitmap == null) {
            return;
        }
        this.bgBitmap = bgBitmap;
        bgPatch = getBgNinePatch(bgBitmap);
        invalidate();
    }
}
