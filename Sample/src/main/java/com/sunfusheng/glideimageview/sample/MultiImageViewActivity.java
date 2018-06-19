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

import com.sunfusheng.glideimageview.sample.model.ImageModel;
import com.sunfusheng.glideimageview.sample.model.ModelUtil;
import com.sunfusheng.glideimageview.sample.widget.MultiImageView.GridLayoutHelper;
import com.sunfusheng.glideimageview.sample.widget.MultiImageView.ImageData;
import com.sunfusheng.glideimageview.sample.widget.MultiImageView.MultiImageView;
import com.sunfusheng.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

public class MultiImageViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recyclerview);

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

        RecyclerViewAdapter(Context context, List<ImageModel> list) {
            this.list = list;
            margin = DisplayUtil.dp2px(context, 3);
            maxImgHeight = maxImgWidth = DisplayUtil.getWindowWidth(context) - DisplayUtil.dp2px(context, 16) * 2;
            cellHeight = cellWidth = (maxImgWidth - margin * 3) / 3;
            minImgHeight = minImgWidth = cellWidth;
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

            viewHolder.multiImageView.setData(list, getLayoutHelper(list));
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
