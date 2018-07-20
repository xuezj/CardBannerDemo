package com.xuezj.cardbanner.mode;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xuezj on 2017/7/29.
 */

public interface BaseTransformer {
    void applyToView(View v, RecyclerView parent);
}
