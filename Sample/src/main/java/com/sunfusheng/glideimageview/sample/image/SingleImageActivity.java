package com.sunfusheng.glideimageview.sample.image;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sunfusheng.GlideImageView;
import com.sunfusheng.progress.CircleProgressView;
import com.sunfusheng.glideimageview.sample.R;

/**
 * @author  by sunfusheng on 2017/6/15.
 */
public class SingleImageActivity extends AppCompatActivity {

    public static final String KEY_IMAGE_URL = "image_url";
    public static final String KEY_IMAGE_URL_THUMBNAIL = "image_url_thumbnail";

    GlideImageView glideImageView;
    CircleProgressView progressView;

    String image_url;
    String image_url_thumbnail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        image_url = getIntent().getStringExtra(KEY_IMAGE_URL);
        image_url_thumbnail = getIntent().getStringExtra(KEY_IMAGE_URL_THUMBNAIL);

        glideImageView = findViewById(R.id.glideImageView);
        progressView = findViewById(R.id.progressView);
        glideImageView.setOnClickListener(v -> onBackPressed());

        loadImage();
    }

    private void loadImage() {
        glideImageView.load(image_url, R.color.transparent, (percentage, bytesRead, totalBytes) -> {
            if (percentage >= 100) {
                progressView.setVisibility(View.GONE);
            } else {
                progressView.setVisibility(View.VISIBLE);
                progressView.setProgress(percentage);
            }
        });
    }
}
