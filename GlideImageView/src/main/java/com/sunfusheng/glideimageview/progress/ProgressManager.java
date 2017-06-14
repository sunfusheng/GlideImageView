package com.sunfusheng.glideimageview.progress;

import android.support.annotation.NonNull;

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

    private ProgressManager() {
    }

    private static OkHttpClient okHttpClient;

    private static final List<WeakReference<ProgressListener>> progressListenerList = Collections.synchronizedList(new ArrayList<WeakReference<ProgressListener>>());

    private static final ProgressListener progressListener = new ProgressListener() {
        @Override
        public void onProgress(String url, long bytesRead, long contentLength, boolean done) {
            if (progressListenerList != null && progressListenerList.size() > 0) {
                for (int i = 0; i < progressListenerList.size(); i++) {
                    WeakReference<ProgressListener> progressListener = progressListenerList.get(i);
                    ProgressListener listener = progressListener.get();
                    if (listener == null) {
                        progressListenerList.remove(i);
                    } else {
                        listener.onProgress(url, bytesRead, contentLength, done);
                    }
                }
            }
        }
    };

    public static OkHttpClient getGlideOkHttpClient() {
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

    public static void addProgressListener(ProgressListener listener) {
        if (listener == null) return;

        if (findProgressListener(listener) == null) {
            progressListenerList.add(new WeakReference<>(listener));
        }
    }

    public static void removeProgressListener(ProgressListener listener) {
        if (listener == null) return;

        WeakReference<ProgressListener> progressListener = findProgressListener(listener);
        if (progressListener != null) {
            progressListenerList.remove(progressListener);
        }
    }

    private static WeakReference<ProgressListener> findProgressListener(ProgressListener listener) {
        if (listener == null) return null;

        List<WeakReference<ProgressListener>> listeners = progressListenerList;
        if (listeners != null && listeners.size() > 0) {
            for (int i = 0; i < listeners.size(); i++) {
                WeakReference<ProgressListener> progressListener = listeners.get(i);
                if (progressListener.get() == listener) {
                    return progressListener;
                }
            }
        }
        return null;
    }
}
