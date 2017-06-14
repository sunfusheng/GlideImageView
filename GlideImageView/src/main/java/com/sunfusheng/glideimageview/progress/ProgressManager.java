package com.sunfusheng.glideimageview.progress;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by sunfusheng on 2017/6/14.
 */
public class ProgressManager {

    private static OkHttpClient okHttpClient;

    private static final List<WeakReference<OnProgressListener>> progressListenerList = Collections.synchronizedList(new ArrayList<WeakReference<OnProgressListener>>());

    private ProgressManager() {
    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(@NonNull Chain chain) throws IOException {
                            Response originalResponse = chain.proceed(chain.request());
                            return originalResponse.newBuilder()
                                    .body(new ProgressResponseBody(chain.request().url().toString(), originalResponse.body(), progressListener))
                                    .build();
                        }
                    }).build();
        }
        return okHttpClient;
    }

    private static final OnProgressListener progressListener = new OnProgressListener() {
        @Override
        public void onProgress(String imageUrl, long bytesRead, long totalBytes, boolean isDone) {
            if (progressListenerList != null && progressListenerList.size() > 0) {
                for (int i = 0; i < progressListenerList.size(); i++) {
                    WeakReference<OnProgressListener> progressListener = progressListenerList.get(i);
                    OnProgressListener listener = progressListener.get();
                    if (listener == null) {
                        progressListenerList.remove(i);
                    } else {
                        listener.onProgress(imageUrl, bytesRead, totalBytes, isDone);
                    }
                }
            }
        }
    };

    public static void addProgressListener(OnProgressListener listener) {
        if (listener == null) return;

        if (findProgressListener(listener) == null) {
            WeakReference<OnProgressListener> progressListener = new WeakReference<>(listener);
            progressListenerList.add(progressListener);
            Log.d("--->", "addProgressListener: " + progressListener.toString());
        }
    }

    public static void removeProgressListener(OnProgressListener listener) {
        if (listener == null) return;

        WeakReference<OnProgressListener> progressListener = findProgressListener(listener);
        if (progressListener != null) {
            progressListenerList.remove(progressListener);
            Log.d("--->", "removeProgressListener: " + progressListener.toString());
        }
    }

    private static WeakReference<OnProgressListener> findProgressListener(OnProgressListener listener) {
        if (listener == null) return null;

        List<WeakReference<OnProgressListener>> listeners = progressListenerList;
        if (listeners != null && listeners.size() > 0) {
            for (int i = 0; i < listeners.size(); i++) {
                WeakReference<OnProgressListener> progressListener = listeners.get(i);
                if (progressListener.get() == listener) {
                    return progressListener;
                }
            }
        }
        return null;
    }
}
