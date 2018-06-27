package com.sunfusheng.glideimageview.sample.model;

import com.sunfusheng.glideimageview.sample.widget.NineImageView.ImageData;

import java.io.Serializable;
import java.util.List;

/**
 * @author by sunfusheng on 2017/6/27.
 */
public class ImageModel implements Serializable {
    public String desc;
    public List<ImageData> images;

    public ImageModel(String desc, List<ImageData> images) {
        this.desc = desc;
        this.images = images;
    }
}
