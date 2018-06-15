package com.sunfusheng.glideimageview.sample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sunfusheng.glideimageview.GlideImageView;
import com.sunfusheng.glideimageview.progress.CircleProgressView;
import com.sunfusheng.glideimageview.sample.about.AboutActivity;

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

    TextView draggableView1;
    TextView draggableView2;

    String url1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1497688355699&di=ea69a930b82ce88561c635089995e124&imgtype=0&src=http%3A%2F%2Fcms-bucket.nosdn.127.net%2Ff84e566bcf654b3698363409fbd676ef20161119091503.jpg";
    String url2 = "http://img1.imgtn.bdimg.com/it/u=4027212837,1228313366&fm=23&gp=0.jpg";

    String gif1 = "http://img.zcool.cn/community/01e97857c929630000012e7e3c2acf.gif";
    String gif2 = "http://5b0988e595225.cdn.sohucs.com/images/20171202/a1cc52d5522f48a8a2d6e7426b13f82b.gif";
    String gif3 = "http://img.zcool.cn/community/01d6dd554b93f0000001bf72b4f6ec.jpg";

    public static final String cat = "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/screenshot/cat.jpg";
    public static final String cat_thumbnail = "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/screenshot/cat_thumbnail.jpg";

    public static final String girl = "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/screenshot/girl.jpg";
    public static final String girl_thumbnail = "https://raw.githubusercontent.com/sfsheng0322/GlideImageView/master/screenshot/girl_thumbnail.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        draggableView1 = findViewById(R.id.draggableView1);
        draggableView2 = findViewById(R.id.draggableView2);
        draggableView1.setOnClickListener(v -> startActivity(new Intent(mContext, RecyclerViewActivity.class)));
        draggableView2.setOnClickListener(v -> startActivity(new Intent(mContext, NineImageViewActivity.class)));

        line1();
        line2();
        line3();
    }

    private void line1() {
        image11.load(url1);
        image12.loadCircle(url1);
        image13.load(url2, R.color.placeholder);
        image14.load(url2, R.color.placeholder, 10);
    }

    private void line2() {
        image21.load(gif1);
        image22.load(gif2, R.color.placeholder, 10);
        image23.loadCircle(gif3);
        image24.loadDrawable(R.drawable.gif_robot_walk);
    }

    private void line3() {
        image31.load(girl, R.color.placeholder, (percentage, bytesRead, totalBytes) -> {
            Log.d("--->", "【load girl】 percentage: " + percentage + " totalBytes: " + totalBytes + " bytesRead: " + bytesRead);
            if (percentage >= 100) {
                progressView1.setVisibility(View.GONE);
            } else {
                progressView1.setVisibility(View.VISIBLE);
                progressView1.setProgress(percentage);
            }
        });

        image32.load(cat, R.color.placeholder, (percentage, bytesRead, totalBytes) -> {
            Log.d("--->", "【load cat】percentage: " + percentage + " totalBytes: " + totalBytes + " bytesRead: " + bytesRead);
            if (percentage >= 100) {
                progressView2.setVisibility(View.GONE);
            } else {
                progressView2.setVisibility(View.VISIBLE);
                progressView2.setProgress(percentage);
            }
        });
    }

//    Intent intent = new Intent(MainActivity.this, SingleImageActivity.class);
//            intent.putExtra(KEY_IMAGE_URL, cat);
//            intent.putExtra(KEY_IMAGE_URL_THUMBNAIL, cat_thumbnail);
//    ActivityOptionsCompat compat = ActivityOptionsCompat
//            .makeSceneTransitionAnimation(MainActivity.this, image41, getString(R.string.transitional_image));
//            ActivityCompat.startActivity(MainActivity.this, intent, compat.toBundle());

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
