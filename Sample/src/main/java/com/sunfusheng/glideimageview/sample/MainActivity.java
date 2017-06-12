package com.sunfusheng.glideimageview.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sunfusheng.glideimageview.GlideImageView;
import com.sunfusheng.glideimageview.ShapeImageView;

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
    String gif2 = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1079460472,383809553&fm=21&gp=0.jpg";

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

        image11.loadImage(url1, R.mipmap.ic_launcher);

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
        image14.setBorderWidth(2);
        image14.setBorderColor(R.color.red);
        image14.loadImage(url1, R.mipmap.ic_launcher);

        image21.loadImage(url2, R.mipmap.ic_launcher);
        image22.loadImage(url2, R.mipmap.ic_launcher);
        image23.loadImage(url2, R.mipmap.ic_launcher);
        image24.loadImage(url2, R.mipmap.ic_launcher);

        image31.loadCircleImage(gif2, R.mipmap.ic_launcher);
        image32.loadImage(gif2, R.mipmap.ic_launcher);
        image33.loadLocalImage(R.drawable.gif_robot_walk, R.mipmap.ic_launcher);
        image34.loadImage(gif1, R.mipmap.ic_launcher);
    }
}
