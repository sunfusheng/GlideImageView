package com.sunfusheng.glideimageview.progress;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.io.IOException;

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
    private ProgressResponseBody.InternalProgressListener internalProgressListener;

    private ResponseBody responseBody;
    private BufferedSource bufferedSource;

    ProgressResponseBody(String url, ProgressResponseBody.InternalProgressListener internalProgressListener, ResponseBody responseBody) {
        this.url = url;
        this.internalProgressListener = internalProgressListener;
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

                if (internalProgressListener != null && lastTotalBytesRead != totalBytesRead) {
                    lastTotalBytesRead = totalBytesRead;
                    mainThreadHandler.post(() -> internalProgressListener.onProgress(url, totalBytesRead, contentLength()));
                }
                return bytesRead;
            }
        };
    }

    interface InternalProgressListener {
        void onProgress(String url, long bytesRead, long totalBytes);
    }
}
