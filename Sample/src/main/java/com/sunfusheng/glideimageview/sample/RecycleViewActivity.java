package com.sunfusheng.glideimageview.sample;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class RecycleViewActivity extends BaseActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
    }
}
