package com.sunfusheng.glideimageview.progress;

import com.bumptech.glide.load.engine.GlideException;

/**
 * Created by sunfusheng on 2017/6/14.
 */
public interface OnGlideImageViewListener {

    void onProgress(int percent, boolean isDone, GlideException exception);
}
