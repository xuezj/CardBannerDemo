package com.xuezj.cardbanner.imageloader;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by xuezj on 2018/8/6.
 */
public interface CardImageLoader {
    void load(Context context, ImageView imageView,Object path);
}
