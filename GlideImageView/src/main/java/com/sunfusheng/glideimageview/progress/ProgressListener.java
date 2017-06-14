package com.sunfusheng.glideimageview.progress;

/**
 * Created by sunfusheng on 2017/6/14.
 */
public interface ProgressListener {

    void onProgress(String imageUrl, long bytesRead, long totalBytes, boolean isDone);
}
