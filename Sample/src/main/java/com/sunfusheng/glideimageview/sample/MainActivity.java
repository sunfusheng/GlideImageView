package com.sunfusheng.glideimageview.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sunfusheng.glideimageview.GlideImageView;
import com.sunfusheng.glideimageview.ShapeImageView;
import com.sunfusheng.glideimageview.progress.OnGlideImageViewListener;
import com.sunfusheng.glideimageview.progress.OnProgressListener;

public class MainActivity extends AppCompatActivity {

    GlideImageView image11;
    GlideImageView image12;
    GlideImageView image13;
    GlideImageView image14;

    GlideImageView image21;
    GlideImageView image22;
    GlideImageView image23;
    GlideImageView image24;

    GlideImageView image31;
    GlideImageView image32;
    GlideImageView image33;
    GlideImageView image34;

    String url1 = "http://img3.imgtn.bdimg.com/it/u=3336351749,2467482848&fm=23&gp=0.jpg";
    String url2 = "http://img1.imgtn.bdimg.com/it/u=4027212837,1228313366&fm=23&gp=0.jpg";

    String gif1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1496754078616&di=cc68338a66a36de619fa11d0c1b2e6f3&imgtype=0&src=http%3A%2F%2Fapp.576tv.com%2FUploads%2Foltz%2F201609%2F25%2F1474813626468299.gif";
    String gif2 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1497276275707&di=57c8c7917e91afc1bc86b1b57e743425&imgtype=0&src=http%3A%2F%2Fimg.haatoo.com%2Fpics%2F2016%2F05%2F14%2F9%2F4faf3f52b8e8315af7a469731dc7dce5.jpg";
    String gif3 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1497276379533&di=71435f66d66221eb36dab266deb9d6d2&imgtype=0&src=http%3A%2F%2Fatt.bbs.duowan.com%2Fforum%2F201608%2F02%2F190418bmy9zqm94qxlmqf4.gif";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image11 = (GlideImageView) findViewById(R.id.image11);
        image12 = (GlideImageView) findViewById(R.id.image12);
        image13 = (GlideImageView) findViewById(R.id.image13);
        image14 = (GlideImageView) findViewById(R.id.image14);

        image21 = (GlideImageView) findViewById(R.id.image21);
        image22 = (GlideImageView) findViewById(R.id.image22);
        image23 = (GlideImageView) findViewById(R.id.image23);
        image24 = (GlideImageView) findViewById(R.id.image24);

        image31 = (GlideImageView) findViewById(R.id.image31);
        image32 = (GlideImageView) findViewById(R.id.image32);
        image33 = (GlideImageView) findViewById(R.id.image33);
        image34 = (GlideImageView) findViewById(R.id.image34);

        line1();
        line2();
        line3();
    }

    private void line1() {
        image11.loadImage(url1, R.mipmap.ic_launcher).listener(new OnProgressListener() {
            @Override
            public void onProgress(String imageUrl, long bytesRead, long totalBytes, boolean isDone) {
                Log.d("--->", "bytesRead: " + bytesRead + " totalBytes: " + totalBytes + " isDone: " + isDone);
            }
        });

        image12.setBorderWidth(3);
        image12.setBorderColor(R.color.gray);
        image12.loadCircleImage(url1, R.mipmap.ic_launcher);

        image13.setRadius(15);
        image13.setBorderWidth(3);
        image13.setBorderColor(R.color.blue);
        image13.setPressedAlpha(0.5f);
        image13.setPressedColor(R.color.blue);
        image13.loadImage(url1, R.mipmap.ic_launcher);

        image14.setShapeType(ShapeImageView.ShapeType.CIRCLE);
        image14.setBorderWidth(1);
        image14.setBorderColor(R.color.red);
        image14.loadImage(url1, R.mipmap.ic_launcher);
    }

    private void line2() {
        image21.loadImage(url2, R.mipmap.ic_launcher);
        image22.loadImage(url2, R.mipmap.ic_launcher);
        image23.loadImage(url2, R.mipmap.ic_launcher);
        image24.loadImage(url2, R.mipmap.ic_launcher);
    }

    private void line3() {
        image31.loadLocalCircleImage(R.drawable.gif_robot_walk, R.mipmap.ic_launcher);

        image32.loadCircleImage(gif1, R.mipmap.ic_launcher).listener(new OnGlideImageViewListener() {
            @Override
            public void onProgress(int percent, boolean isDone) {
                Log.d("--->", "percent: " + percent + " isDone: " + isDone);
            }
        });

        image33.loadImage(gif2, R.mipmap.ic_launcher);
        image34.loadImage(gif3, R.mipmap.ic_launcher);
    }

}
