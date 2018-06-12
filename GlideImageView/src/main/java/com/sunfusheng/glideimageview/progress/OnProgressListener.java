package com.sunfusheng.glideimageview.progress;

/**
 * @author by sunfusheng on 2017/6/14.
 */
public interface OnProgressListener {
    void onProgress(int percentage, long bytesRead, long totalBytes);
}
