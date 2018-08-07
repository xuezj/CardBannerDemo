package com.xuezj.cardbanner.adapter;

import android.view.ViewGroup;

/**
 * Created by xuezj on 2017/7/29.
 */

public interface BannerAdapter {
    int getCount();
    BannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType);
    void onBindViewHolder(BannerViewHolder holder, int position);
}
