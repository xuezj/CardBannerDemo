package com.xuezj.cardbanner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xuezj.cardbanner.ImageData;
import com.xuezj.cardbanner.R;
import com.xuezj.cardbanner.imageloader.CardImageLoader;

import java.util.List;

/**
 * Created by xuezj on 2017/7/29.
 */

public class CardAdapter extends RecyclerView.Adapter<BannerViewHolder> {


    private List<ImageData> datas;


    private int dataCount = 0;
    private BannerAdapter bannerAdapter;
    private Context context;
    private int width;
    private int borderWidth;
    private int dividerWidth;
    private int mainTitleTextSize;
    private int subtitleTitleTextSize;
    private CardImageLoader cardImageloader;

    public CardAdapter(Context context, int width, int borderWidth, int dividerWidth) {
        this.context = context;
        this.width = width;
        this.borderWidth = borderWidth;
        this.dividerWidth = dividerWidth;
    }

    public void setDatas(List<ImageData> datas) {
        this.datas = datas;
    }

    public void setBannerAdapter(BannerAdapter bannerAdapter) {
        this.bannerAdapter = bannerAdapter;
    }
    public void setCardImageloader(CardImageLoader cardImageloader) {
        this.cardImageloader = cardImageloader;
    }
    public void setTextSize(int mainTitleTextSize,int subtitleTitleTextSize) {
        this.mainTitleTextSize = mainTitleTextSize;
        this.subtitleTitleTextSize = subtitleTitleTextSize;
    }
    public void setDataCount(int dataCount) {
        this.dataCount = dataCount;
    }

    @Override
    public BannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder h = null;
        if (dataCount!=0&&bannerAdapter!=null){
            return bannerAdapter.onCreateViewHolder(parent,viewType);
        }else {
            h = new ViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.banner_item, parent, false));
            return h;
        }

    }

    @Override
    public void onBindViewHolder(BannerViewHolder holder, int position) {
        holder.itemView.setTag(R.id.key_position,position % (datas!=null?datas.size():dataCount));
        holder.itemView.setTag(R.id.key_item,position);
        holder.itemView.setPadding(dividerWidth, 0, dividerWidth, 0);
        holder.itemView.setLayoutParams(new ViewGroup.LayoutParams(width - borderWidth * 2, ViewGroup.LayoutParams.MATCH_PARENT));
        if (dataCount!=0&&bannerAdapter!=null){
            bannerAdapter.onBindViewHolder(holder,position%dataCount);
        }else {
            ViewHolder VH = (ViewHolder) holder;
            VH.mainTitle.setText(datas.get(position % datas.size()).getMainTitle());
            VH.mainTitle.setTextSize((float)mainTitleTextSize);
            VH.subtitleTitle.setText(datas.get(position % datas.size()).getSubtitleTitle());
            VH.subtitleTitle.setTextSize((float)subtitleTitleTextSize);if (cardImageloader!=null){
                cardImageloader.load(context,VH.roundedImageView,datas.get(position % datas.size()).getImage());
            }
//            Glide.with(context)
//                    .load(datas.get(position % datas.size()).getImage())
////                    .bitmapTransform(new CropCircleTransformation(getContext()))
//                    .into(VH.roundedImageView);
        }




    }

    @Override
    public int getItemCount() {
        if (dataCount != 0 || datas != null) {
            if (datas == null)
                return Integer.MAX_VALUE;
            else
                return datas.size() == 0 ? 0 : Integer.MAX_VALUE;
        } else {
            return 0;
        }
    }
}
