package com.xuezj.cardbanner.mode;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
/**
 * Created by xuezj on 2017/7/29.
 */

public class ScaleYTransformer implements BaseTransformer {

    private float mScaleRatio = 0.001f;

    public ScaleYTransformer(){}


    @Override
    public void applyToView(View v, RecyclerView parent) {
        float halfWidth = v.getWidth() * 0.9f;
        float parentHalfWidth = parent.getWidth() * 0.9f;
        float x = ((int)(v.getX())-((parentHalfWidth-halfWidth)/2))/10;
        float scale = 1.0f - Math.abs(x) * mScaleRatio;
        ViewCompat.setScaleY(v, scale);
    }
}
