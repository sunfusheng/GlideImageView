package com.sunfusheng.glideimageview.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sunfusheng.glideimageview.sample.model.ImageModel;
import com.sunfusheng.glideimageview.sample.model.ModelUtil;
import com.sunfusheng.glideimageview.sample.widget.NineImageView.GridLayoutHelper;
import com.sunfusheng.glideimageview.sample.widget.NineImageView.ImageData;
import com.sunfusheng.glideimageview.sample.widget.NineImageView.NineImageView;
import com.sunfusheng.util.Utils;

import java.util.List;

public class NineImageViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recyclerview);

        setTitle("NineImageView");

        RecyclerView recyclerView = findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, ModelUtil.getImages());
        recyclerView.setAdapter(adapter);
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private List<ImageModel> list;
        private int margin;
        private int maxImgWidth;
        private int maxImgHeight;
        private int cellWidth;
        private int cellHeight;
        private int minImgWidth;
        private int minImgHeight;

        RecyclerViewAdapter(Context context, List<ImageModel> list) {
            this.list = list;
            margin = Utils.dp2px(context, 3);
            maxImgHeight = maxImgWidth = (Utils.getWindowWidth(context) - Utils.dp2px(context, 16) * 2) * 3 / 4;
            cellHeight = cellWidth = (maxImgWidth - margin * 3) / 3;
            minImgHeight = minImgWidth = cellWidth;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_nine_images, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            ImageModel model = list.get(position);

            viewHolder.vDesc.setText(model.desc);
            viewHolder.multiImageView.loadGif(false)
                    .enableRoundCorner(true)
                    .setRoundCornerRadius(5)
                    .setData(model.images, getLayoutHelper(model.images));

            viewHolder.multiImageView.setOnLongClickListener(v -> {
                Toast.makeText(getApplicationContext(), "长按：" + model.desc, Toast.LENGTH_SHORT).show();
                return false;
            });

            viewHolder.multiImageView.setOnItemClickListener(index -> {
                Toast.makeText(getApplicationContext(), "点击：position = " + index, Toast.LENGTH_SHORT).show();
            });
        }

        private GridLayoutHelper getLayoutHelper(List<ImageData> list) {
            int spanCount = Utils.getSize(list);
            if (spanCount == 1) {
                int width = list.get(0).realWidth;
                int height = list.get(0).realHeight;
                if (width > 0 && height > 0) {
                    float whRatio = width * 1f / height;
                    if (width > height) {
                        width = Math.max(minImgWidth, Math.min(width, maxImgWidth));
                        height = Math.max(minImgHeight, (int) (width / whRatio));
                    } else {
                        height = Math.max(minImgHeight, Math.min(height, maxImgHeight));
                        width = Math.max(minImgWidth, (int) (height * whRatio));
                    }
                } else {
                    width = cellWidth;
                    height = cellHeight;
                }
                return new GridLayoutHelper(spanCount, width, height, margin);
            }

            if (spanCount > 3) {
                spanCount = (int) Math.ceil(Math.sqrt(spanCount));
            }

            if (spanCount > 3) {
                spanCount = 3;
            }
            return new GridLayoutHelper(spanCount, cellWidth, cellHeight, margin);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView vDesc;
            NineImageView multiImageView;

            ViewHolder(View view) {
                super(view);
                vDesc = view.findViewById(R.id.desc);
                multiImageView = view.findViewById(R.id.multiImageView);
            }
        }
    }
}
