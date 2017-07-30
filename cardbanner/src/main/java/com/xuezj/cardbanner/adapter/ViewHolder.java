package com.xuezj.cardbanner.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xuezj.cardbanner.R;
import com.xuezj.cardbanner.view.RoundedImageView;

/**
 * Created by xuezj on 2017/7/29.
 */

public class ViewHolder extends BannerViewHolder {
    public RoundedImageView roundedImageView;
    public TextView mainTitle,subtitleTitle;
    public ViewHolder(View itemView) {
        super(itemView);

        roundedImageView=(RoundedImageView)itemView.findViewById(R.id.item_img);
        mainTitle=(TextView)itemView.findViewById(R.id.main_text);
        subtitleTitle=(TextView)itemView.findViewById(R.id.subtitle_text);
    }
}
