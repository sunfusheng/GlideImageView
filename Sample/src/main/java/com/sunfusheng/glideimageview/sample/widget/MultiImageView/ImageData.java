package com.sunfusheng.glideimageview.sample.widget.MultiImageView;

import android.graphics.Point;

/**
 * @author sunfusheng on 2018/6/19.
 */
public class ImageData {
    public String url;
    public String centerText;
    public String cornerText;

    public int startX;
    public int startY;
    public int width;
    public int height;

    public ImageData(String url) {
        this.url = url;
    }

    public ImageData from(ImageData imageData, LayoutHelper layoutHelper, int position) {
        if (imageData != null && layoutHelper != null) {
            Point coordinate = layoutHelper.getCoordinate(position);
            if (coordinate != null) {
                imageData.startX = coordinate.x;
                imageData.startY = coordinate.y;
            }

            Point size = layoutHelper.getSize(position);
            if (size != null) {
                imageData.width = size.x;
                imageData.height = size.y;
            }
        }
        return imageData;
    }
}
