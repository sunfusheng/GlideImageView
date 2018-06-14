package com.sunfusheng.glideimageview.sample.image;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sunfusheng.glideimageview.GlideImageView;
import com.sunfusheng.glideimageview.progress.CircleProgressView;
import com.sunfusheng.glideimageview.sample.R;

import java.util.Random;

/**
 * Created by sunfusheng on 2017/6/15.
 */
public class SingleImageActivity extends AppCompatActivity {

    GlideImageView glideImageView;
    CircleProgressView progressView;

    CircleProgressView progressView1;
    CircleProgressView progressView2;
    CircleProgressView progressView3;
    View maskView;

    public static final String KEY_IMAGE_URL = "image_url";
    public static final String KEY_IMAGE_URL_THUMBNAIL = "image_url_thumbnail";

    String image_url;
    String image_url_thumbnail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        glideImageView = findViewById(R.id.glideImageView);
        progressView1 = findViewById(R.id.progressView1);
        progressView2 = findViewById(R.id.progressView2);
        progressView3 = findViewById(R.id.progressView3);
        maskView = findViewById(R.id.maskView);

        image_url = getIntent().getStringExtra(KEY_IMAGE_URL);
        image_url_thumbnail = getIntent().getStringExtra(KEY_IMAGE_URL_THUMBNAIL);

        initProgressView();
        loadImage();
    }

    private void initProgressView() {
        int randomNum = new Random().nextInt(3);
        switch (randomNum) {
            case 1:
                progressView = progressView2;
                break;
            case 2:
                progressView = progressView3;
                break;
            case 0:
            default:
                progressView = progressView1;
                break;
        }
        progressView1.setVisibility(View.GONE);
        progressView2.setVisibility(View.GONE);
        progressView3.setVisibility(View.GONE);
        progressView.setVisibility(View.VISIBLE);
    }

    private void loadImage() {

    }
}
