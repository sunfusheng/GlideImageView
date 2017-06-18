package com.sunfusheng.glideimageview.sample.about;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.sunfusheng.glideimageview.sample.R;

/**
 * Created by sunfusheng on 2017/2/15.
 */
@SuppressLint("SetJavaScriptEnabled")
public class WebViewWrapper extends RelativeLayout {

    private WebView webView;
    private ProgressBar progressBar;

    private String mUrl;

    public WebViewWrapper(Context context) {
        this(context, null);
    }

    public WebViewWrapper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebViewWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initWebViewSettings();
        initListener();
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_webview, this);
        webView = (WebView) view.findViewById(R.id.webView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }

    private void initWebViewSettings() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true); // 默认false，设置true后我们才能在WebView里与我们的JS代码进行交互
        settings.setJavaScriptCanOpenWindowsAutomatically(true); // 设置JS是否可以打开WebView新窗口

        settings.setSupportZoom(true); // 支持缩放
        settings.setBuiltInZoomControls(true); // 支持手势缩放
        settings.setDisplayZoomControls(false); // 不显示缩放按钮

        settings.setDatabaseEnabled(true);
        settings.setSaveFormData(true);
        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setAppCacheEnabled(true);

        settings.setUseWideViewPort(true); // 将图片调整到适合WebView的大小
        settings.setLoadWithOverviewMode(true); // 自适应屏幕

        webView.setHorizontalScrollBarEnabled(false);
        webView.setScrollbarFadingEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER); // 取消WebView中滚动或拖动到顶部、底部时的阴影
    }

    private void initListener() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressBar.setVisibility(View.GONE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);

            }
        });


    }

    public void loadUrl(String url) {
        mUrl = url;
        webView.loadUrl(url);
    }

    public void setProgressDrawable(@DrawableRes int id) {
        progressBar.setProgressDrawable(progressBar.getContext().getResources().getDrawable(id));
    }

    public WebView getWebView() {
        return webView;
    }

    public String getUrl() {
        return mUrl;
    }

    public boolean goBack() {
        if (webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return false;
    }

    public void onResume() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.onResume();
    }

    public void onPause() {
        webView.getSettings().setJavaScriptEnabled(false);
        webView.onPause();
    }

    public void onDestroy() {
        webView.setVisibility(GONE);
        webView.destroy();
    }

}
