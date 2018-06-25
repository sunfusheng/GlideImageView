package com.sunfusheng.glideimageview.sample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunfusheng.glideimageview.sample.model.ImageModel;
import com.sunfusheng.glideimageview.sample.model.ModelUtil;
import com.sunfusheng.glideimageview.sample.widget.MultiImageView.GridLayoutHelper;
import com.sunfusheng.glideimageview.sample.widget.MultiImageView.ImageCell;
import com.sunfusheng.glideimageview.sample.widget.MultiImageView.ImageData;
import com.sunfusheng.glideimageview.sample.widget.MultiImageView.MultiImageView;
import com.sunfusheng.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MultiImageViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recyclerview);

        setTitle("MultiImageView");

        RecyclerView recyclerView = findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, ModelUtil.getImages());
        recyclerView.setAdapter(adapter);
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private List<ImageModel> list;

        private int margin;
        private int maxImgWidth;
        private int maxImgHeight;
        private int cellWidth;
        private int cellHeight;
        private int minImgWidth;
        private int minImgHeight;

        private Drawable gifDrawable;
        private Drawable longDrawable;

        private MultiImageView multiImageView;
        private int count = 2;
        private String text = "+" + String.valueOf(count);

        RecyclerViewAdapter(Context context, List<ImageModel> list) {
            this.list = list;
            margin = Utils.dp2px(context, 3);
            maxImgHeight = maxImgWidth = (Utils.getWindowWidth(context) - Utils.dp2px(context, 16) * 2) * 3 / 4;
            cellHeight = cellWidth = (maxImgWidth - margin * 3) / 3;
            minImgHeight = minImgWidth = cellWidth;

            gifDrawable = Utils.getTextDrawable(context, 24, 14, 2, "GIF", 11, R.color.transparent30);
            longDrawable = Utils.getTextDrawable(context, 25, 14, 2, "长图", 10, R.color.transparent30);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    int index = 8;
                    if (multiImageView != null && multiImageView.getImageCell(index) != null) {
                        count++;
                        text = "+" + String.valueOf(count);
                        ImageCell imageCell = multiImageView.getImageCell(index);
                        imageCell.setText(text);
                        multiImageView.getData().get(index).text = text;
                    }
                }
            }, 1000, 1000);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_multiimageview, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            ImageModel model = list.get(position);

            viewHolder.vDesc.setText(model.desc);

            List<ImageData> list = new ArrayList<>();
            int size = 0;
            for (String url : model.images) {
                list.add(new ImageData(url));
                size++;
                if (size >= 9) break;
            }

            if (model.images.size() > 9) {
                list.get(8).text = text;
                if (multiImageView == null) {
                    multiImageView = viewHolder.multiImageView;
                }
            }

            viewHolder.multiImageView.enableRoundCorner(true)
                    .setRoundCornerRadius(5)
                    .loadGif(false)
                    .setGifDrawable(gifDrawable)
                    .setData(list, getLayoutHelper(list), model.desc);
        }

        private GridLayoutHelper getLayoutHelper(List<ImageData> list) {
            int imageCount = list != null ? list.size() : 0;
            if (imageCount > 3) {
                imageCount = (int) Math.ceil(Math.sqrt(imageCount));
            }
            return new GridLayoutHelper(imageCount, cellWidth, cellHeight, margin);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView vDesc;
            MultiImageView multiImageView;

            ViewHolder(View view) {
                super(view);
                vDesc = view.findViewById(R.id.desc);
                multiImageView = view.findViewById(R.id.multiImageView);
            }
        }
    }
}
