package com.sunfusheng.glideimageview.sample.widget.MultiImageView;

import android.graphics.Point;

/**
 * @author sunfusheng on 2018/6/19.
 */
public class GridLayoutHelper implements LayoutHelper {
    private int spanCount;
    private int cellWidth;
    private int cellHeight;
    private int margin;

    public GridLayoutHelper(int spanCount, int cellWidth, int cellHeight, int margin) {
        this.spanCount = spanCount;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.margin = margin;
    }

    @Override
    public Point getCoordinate(int position) {
        Point point = new Point();
        point.x = position % spanCount * (cellWidth + margin);
        point.y = position / spanCount * (cellHeight + margin);
        return point;
    }

    @Override
    public Point getSize(int position) {
        Point point = new Point();
        point.x = cellWidth;
        point.y = cellHeight;
        return point;
    }
}
