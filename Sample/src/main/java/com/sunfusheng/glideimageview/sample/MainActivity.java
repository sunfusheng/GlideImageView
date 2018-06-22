package com.sunfusheng.glideimageview.sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sunfusheng.FirUpdater;
import com.sunfusheng.GlideImageView;
import com.sunfusheng.glideimageview.sample.about.AboutActivity;
import com.sunfusheng.glideimageview.sample.image.SingleImageActivity;
import com.sunfusheng.progress.CircleProgressView;
import com.sunfusheng.util.Utils;

/**
 * @author by sunfusheng on 2017/6/3.
 */
public class MainActivity extends BaseActivity {

    GlideImageView image11;
    GlideImageView image12;
    GlideImageView image13;
    GlideImageView image14;

    GlideImageView image21;
    GlideImageView image22;
    GlideImageView image23;
    GlideImageView image24;

    GlideImageView image31;
    CircleProgressView progressView1;
    GlideImageView image32;
    CircleProgressView progressView2;

    View draggableView;

    String url1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1497688355699&di=ea69a930b82ce88561c635089995e124&imgtype=0&src=http%3A%2F%2Fcms-bucket.nosdn.127.net%2Ff84e566bcf654b3698363409fbd676ef20161119091503.jpg";
    String url2 = "http://img1.imgtn.bdimg.com/it/u=4027212837,1228313366&fm=23&gp=0.jpg";

    String gif1 = "http://img.zcool.cn/community/01e97857c929630000012e7e3c2acf.gif";
    String gif2 = "http://5b0988e595225.cdn.sohucs.com/images/20171202/a1cc52d5522f48a8a2d6e7426b13f82b.gif";
    String gif3 = "http://img.zcool.cn/community/01d6dd554b93f0000001bf72b4f6ec.jpg";

    public static final String cat = "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/resources/cat.jpg";
    public static final String cat_thumbnail = "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/resources/cat_thumbnail.jpg";

    public static final String girl = "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/resources/girl.jpg";
    public static final String girl_thumbnail = "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/resources/girl_thumbnail.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new FirUpdater(this, "3c57fb226edf7facf821501e4eba08d2", "59461f4c548b7a45cb000050").checkVersion();

        image11 = findViewById(R.id.image11);
        image12 = findViewById(R.id.image12);
        image13 = findViewById(R.id.image13);
        image14 = findViewById(R.id.image14);

        image21 = findViewById(R.id.image21);
        image22 = findViewById(R.id.image22);
        image23 = findViewById(R.id.image23);
        image24 = findViewById(R.id.image24);

        image31 = findViewById(R.id.image31);
        progressView1 = findViewById(R.id.progressView1);
        image32 = findViewById(R.id.image32);
        progressView2 = findViewById(R.id.progressView2);

        draggableView = findViewById(R.id.draggableView);
        image11.setOnClickListener(v -> startActivity(new Intent(mContext, RecyclerViewActivity.class)));
        draggableView.setOnClickListener(v -> startActivity(new Intent(mContext, MultiImageViewActivity.class)));

        image31.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SingleImageActivity.class);
            intent.putExtra(SingleImageActivity.KEY_IMAGE_URL, girl);
            intent.putExtra(SingleImageActivity.KEY_IMAGE_URL_THUMBNAIL, girl_thumbnail);
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, image31, getString(R.string.transitional_image));
            ActivityCompat.startActivity(MainActivity.this, intent, compat.toBundle());
        });

        image32.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SingleImageActivity.class);
            intent.putExtra(SingleImageActivity.KEY_IMAGE_URL, cat);
            intent.putExtra(SingleImageActivity.KEY_IMAGE_URL_THUMBNAIL, cat_thumbnail);
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, image32, getString(R.string.transitional_image));
            ActivityCompat.startActivity(MainActivity.this, intent, compat.toBundle());
        });

        line1();
        line2();
        line3();

        ImageView vBitMap = findViewById(R.id.bitmap);
        Bitmap bitmap = getTextBitmap(this, 60, 30, "GIF", 20);
        vBitMap.setImageBitmap(bitmap);

    }

    public static Bitmap getTextBitmap(Context context, int width, int height, String text, int textSize) {
        int padding = Utils.dp2px(context, 1);
        int radius = Utils.dp2px(context, 3);
        Bitmap bitmap = Bitmap.createBitmap(Utils.dp2px(context, width), Utils.dp2px(context, height), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        RectF rect = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // draw background
        paint.setColor(Color.BLUE);
        canvas.drawRoundRect(rect, radius, radius, paint);
        paint.setColor(Color.RED);
        canvas.drawRoundRect(new RectF(padding, padding, rect.width() - padding, rect.height() - padding), radius, radius, paint);
        // draw text
        paint.setColor(Color.WHITE);
        paint.setTextSize(Utils.dp2px(context, textSize));
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        float baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText(text, rect.centerX(), baseline, paint);
        return bitmap;
    }

    private void line1() {
        image11.load(url1);
        image12.loadCircle(url1);
        image13.load(url2, R.mipmap.image_loading);
        image14.load(url2, R.mipmap.image_loading, 10);
    }

    private void line2() {
        image21.load(gif1);
        image22.load(gif2, R.mipmap.image_loading, 10);
        image23.loadCircle(gif3);
        image24.loadDrawable(R.drawable.gif_robot_walk);
    }

    private void line3() {
        image31.fitCenter().error(R.mipmap.image_load_err).diskCacheStrategy(DiskCacheStrategy.NONE).load(girl, R.color.placeholder, (isComplete, percentage, bytesRead, totalBytes) -> {
//            Log.d("--->", "load percentage: " + percentage + " totalBytes: " + totalBytes + " bytesRead: " + bytesRead);
            if (isComplete) {
                progressView1.setVisibility(View.GONE);
            } else {
                progressView1.setVisibility(View.VISIBLE);
                progressView1.setProgress(percentage);
            }
        });

        image32.error(R.mipmap.image_load_err).load(cat, R.color.placeholder, (isComplete, percentage, bytesRead, totalBytes) -> {
            if (isComplete) {
                progressView2.setVisibility(View.GONE);
            } else {
                progressView2.setVisibility(View.VISIBLE);
                progressView2.setProgress(percentage);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_menu_app:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
