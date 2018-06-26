package com.sunfusheng.glideimageview.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sunfusheng.FirUpdater;
import com.sunfusheng.GlideImageView;
import com.sunfusheng.glideimageview.sample.about.AboutActivity;
import com.sunfusheng.glideimageview.sample.image.SingleImageActivity;
import com.sunfusheng.progress.CircleProgressView;
import com.sunfusheng.transformation.BlurTransformation;

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

    GlideImageView image41;

    View draggableView;

    String url1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1497688355699&di=ea69a930b82ce88561c635089995e124&imgtype=0&src=http%3A%2F%2Fcms-bucket.nosdn.127.net%2Ff84e566bcf654b3698363409fbd676ef20161119091503.jpg";
    String url2 = "http://img1.imgtn.bdimg.com/it/u=4027212837,1228313366&fm=23&gp=0.jpg";
    String url3 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1529402445474&di=b5da3b2f6a466e618e1e32d4dd2bda4d&imgtype=0&src=http%3A%2F%2F2b.zol-img.com.cn%2Fproduct%2F133_500x2000%2F801%2Fce21ke76FRh4A.jpg";

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

        image41 = findViewById(R.id.image41);

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
        line4();
    }

    private void line1() {
        image11.enableState(true).load(url1);
        image12.loadCircle(url1);
        image13.load(url2, R.mipmap.image_loading);
        image14.load(url2, R.mipmap.image_loading, 10);
    }

    private void line2() {
        image21.fitCenter().load(gif1);
        image22.fitCenter().load(gif2, R.mipmap.image_loading, 10);
        image23.fitCenter().loadCircle(gif3);
        image24.fitCenter().loadDrawable(R.drawable.gif_robot_walk);
    }

    private void line3() {
        image31.centerCrop().error(R.mipmap.image_load_err).diskCacheStrategy(DiskCacheStrategy.NONE).load(girl, R.color.placeholder, (isComplete, percentage, bytesRead, totalBytes) -> {
//            Log.d("--->", "load percentage: " + percentage + " totalBytes: " + totalBytes + " bytesRead: " + bytesRead);
            if (isComplete) {
                progressView1.setVisibility(View.GONE);
            } else {
                progressView1.setVisibility(View.VISIBLE);
                progressView1.setProgress(percentage);
            }
        });

        image32.centerCrop().error(R.mipmap.image_load_err).load(cat, R.color.placeholder, (isComplete, percentage, bytesRead, totalBytes) -> {
            if (isComplete) {
                progressView2.setVisibility(View.GONE);
            } else {
                progressView2.setVisibility(View.VISIBLE);
                progressView2.setProgress(percentage);
            }
        });
    }

    private void line4() {
        image41.fitCenter().load(url3, R.mipmap.image_loading, new BlurTransformation(this, 25, 1));

        image41.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SingleImageActivity.class);
            intent.putExtra(SingleImageActivity.KEY_IMAGE_URL, url3);
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, image41, getString(R.string.transitional_image));
            ActivityCompat.startActivity(MainActivity.this, intent, compat.toBundle());
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
