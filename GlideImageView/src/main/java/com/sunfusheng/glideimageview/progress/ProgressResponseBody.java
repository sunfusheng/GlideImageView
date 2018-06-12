package com.sunfusheng.glideimageview.progress;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @author by sunfusheng on 2017/6/14.
 */
public class ProgressResponseBody extends ResponseBody {

    private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    private String url;
    private Map<String, WeakReference<OnProgressListener>> listenersMap;

    private ResponseBody responseBody;
    private BufferedSource bufferedSource;

    ProgressResponseBody(String url, Map<String, WeakReference<OnProgressListener>> listenersMap, ResponseBody responseBody) {
        this.url = url;
        this.listenersMap = listenersMap;
        this.responseBody = responseBody;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0;
            long lastTotalBytesRead = 0;

            @Override
            public long read(@NonNull Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += (bytesRead == -1) ? 0 : bytesRead;

                OnProgressListener onProgressListener = getProgressListener();
                if (onProgressListener != null && lastTotalBytesRead != totalBytesRead) {
                    lastTotalBytesRead = totalBytesRead;
                    int percentage = (int) ((bytesRead * 1f / contentLength()) * 100f);
                    mainThreadHandler.post(() -> onProgressListener.onProgress(percentage, totalBytesRead, contentLength()));
                }
                return bytesRead;
            }
        };
    }

    private OnProgressListener getProgressListener() {
        if (TextUtils.isEmpty(url) || listenersMap == null || listenersMap.size() == 0) {
            return null;
        }

        WeakReference<OnProgressListener> listenerWeakReference = listenersMap.get(url);
        if (listenerWeakReference != null) {
            return listenerWeakReference.get();
        }
        return null;
    }
}
