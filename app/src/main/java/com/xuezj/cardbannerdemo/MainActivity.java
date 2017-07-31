package com.xuezj.cardbannerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xuezj.cardbanner.CardBanner;
import com.xuezj.cardbanner.ImageData;
import com.xuezj.cardbanner.adapter.BannerAdapter;
import com.xuezj.cardbanner.adapter.BannerViewHolder;
import com.xuezj.cardbanner.adapter.ViewHolder;
import com.xuezj.cardbanner.view.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    CardBanner cardBanner, cardBanner2;
    List<ImageData> D;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardBanner = (CardBanner) findViewById(R.id.banner);
        cardBanner2 = (CardBanner) findViewById(R.id.banner2);
        D = new ArrayList<>();
        ImageData b1 = new ImageData();
        b1.setImage("http://ww1.sinaimg.cn/large/610dc034ly1fhyeyv5qwkj20u00u0q56.jpg");
        b1.setMainTitle("第一张图片");
        D.add(b1);

        ImageData b2 = new ImageData();
        b2.setImage("https://ws1.sinaimg.cn/large/610dc034gy1fhvf13o2eoj20u011hjx6.jpg");
        b2.setSubtitleTitle("23-7期");
        D.add(b2);
        ImageData b3 = new ImageData();
        b3.setImage("http://ww1.sinaimg.cn/large/610dc034ly1fhxe0hfzr0j20u011in1q.jpg");
        D.add(b3);
        cardBanner.setDatas(D).setPlay(false).start();
        cardBanner.setOnItemClickListener(new CardBanner.OnItemClickListener() {
            @Override
            public void onItem(int position) {
                Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
        cardBanner2.setDataCount(D.size()).setBannerAdapter(new BannerAdapter() {
            @Override
            public BannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewHolder h = new ViewHolder(LayoutInflater.from(MainActivity.this)
                        .inflate(R.layout.banner_item_demo, parent, false));
                return h;
            }

            @Override
            public void onBindViewHolder(BannerViewHolder holder, int position) {
                ViewHolder VH = (ViewHolder) holder;
                Glide.with(MainActivity.this)
                        .load(D.get(position).getImage()).centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .bitmapTransform(new CropCircleTransformation(getContext()))
                        .into(VH.roundedImageView);
            }
        });
        //如果想使用自定义样式
        //则设置.setTransformer(new DemoTransformer())

        cardBanner2 .start();
        cardBanner2.setOnItemClickListener(new CardBanner.OnItemClickListener() {
            @Override
            public void onItem(int position) {
                Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    class ViewHolder extends BannerViewHolder {
        public RoundedImageView roundedImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            roundedImageView = (RoundedImageView) itemView.findViewById(R.id.item_img);
        }
    }
}
