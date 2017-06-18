package com.sunfusheng.glideimageview.sample.about;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.sunfusheng.glideimageview.GlideImageView;
import com.sunfusheng.glideimageview.sample.BaseActivity;
import com.sunfusheng.glideimageview.sample.R;
import com.sunfusheng.glideimageview.sample.util.AppUtil;

/**
 * Created by sunfusheng on 2017/6/18.
 */
public class AboutActivity extends BaseActivity {

    WebViewWrapper webViewWrapper;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    GlideImageView ivGirl;
    TextView tvTitle;
    AppBarLayout appBarLayout;

    private String url = "file:///android_asset/about.html";

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTranslucent(false);
        setContentView(R.layout.activity_about);

        initView();
        initToolBar();
        webViewWrapper.loadUrl(url);
    }

    private void initView() {
        webViewWrapper = (WebViewWrapper) findViewById(R.id.webViewWrapper);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        ivGirl = (GlideImageView) findViewById(R.id.iv_girl);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        collapsingToolbarLayout.setTitle("");
        toolbar.setTitle("");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));//设置还没收缩时状态下字体颜色
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.transparent));//设置收缩后Toolbar上字体的颜色
        tvTitle.setText(getString(R.string.app_name) + " (" + AppUtil.getVersionName(this) + ")");
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBar, int offset) {
                tvTitle.setAlpha(Math.abs(offset * 1f / appBar.getTotalScrollRange()));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        webViewWrapper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webViewWrapper.onPause();
    }

    @Override
    protected void onDestroy() {
        webViewWrapper.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webViewWrapper.goBack()) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
