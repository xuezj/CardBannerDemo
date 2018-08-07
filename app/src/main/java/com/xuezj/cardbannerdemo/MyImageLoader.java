package com.xuezj.cardbannerdemo;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xuezj.cardbanner.imageloader.CardImageLoader;

/**
 * Created by xuezj on 2018/8/6.
 */
public class MyImageLoader implements CardImageLoader {
    @Override
    public void load(Context context, ImageView imageView, Object path) {
            Glide.with(context)
                    .load(path)
//                    .bitmapTransform(new CropCircleTransformation(getContext()))
                    .into(imageView);
    }
}
