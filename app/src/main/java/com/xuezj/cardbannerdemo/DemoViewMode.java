package com.xuezj.cardbannerdemo;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xuezj.cardbanner.mode.ItemViewMode;

/**
 * Created by xuezj on 2017/7/29.
 */

public class DemoViewMode implements ItemViewMode {


    public DemoViewMode(){}


    @Override
    public void applyToView(View v, RecyclerView parent) {
        int halfWidth = v.getWidth();
        int parentHalfWidth = parent.getWidth() ;
        float x = ((int)(v.getX())-((parentHalfWidth-halfWidth)/2));
        /**
         * 左右滑动时从0变化到1为  x/halfWidth
         * 可以根据此变化对 传入的参数v进行缩放旋转等效果，就看你的本事了，
         * 具体可以下看一下对view的相关知识
         */



    }
}
