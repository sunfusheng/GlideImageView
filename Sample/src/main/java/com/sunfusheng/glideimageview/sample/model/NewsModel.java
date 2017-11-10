package com.sunfusheng.glideimageview.sample.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author sunfusheng on 2017/11/10.
 */
public class NewsModel implements Serializable {

    private String title;
    private String image_url;
    private ArrayList<String> images;
    private int type;

    public NewsModel(String title, String image_url) {
        this.title = title;
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
