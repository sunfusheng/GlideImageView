package com.sunfusheng.glideimageview.sample.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author by sunfusheng on 2017/6/27.
 */
public class ImageModel implements Serializable {
    public String desc;
    public List<String> images;

    public ImageModel(String desc, List<String> images) {
        this.desc = desc;
        this.images = images;
    }
}
