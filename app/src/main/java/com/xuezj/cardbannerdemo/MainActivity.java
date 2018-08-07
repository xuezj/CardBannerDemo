package com.xuezj.cardbannerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.xuezj.cardbanner.CardBanner;
import com.xuezj.cardbanner.ImageData;
import com.xuezj.cardbanner.adapter.BannerAdapter;
import com.xuezj.cardbanner.adapter.BannerViewHolder;
import com.xuezj.cardbanner.view.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    CardBanner cardBanner, cardBanner2;
    List<ImageData> imageData;
    private ArrayList<String> image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardBanner = (CardBanner) findViewById(R.id.banner);
        cardBanner2 = (CardBanner) findViewById(R.id.banner2);
        /**
         *使用原生样式
         */
        //1.先用Library中提供的实体类 （ImageData） new一个集合
         List<ImageData> imageData = new ArrayList<>();
        ImageData b1 = new ImageData();
        b1.setImage("http://ww1.sinaimg.cn/large/610dc034ly1fhyeyv5qwkj20u00u0q56.jpg");
        b1.setMainTitle("第一张图片");
        imageData.add(b1);

        ImageData b2 = new ImageData();
        b2.setImage("https://ws1.sinaimg.cn/large/610dc034gy1fhvf13o2eoj20u011hjx6.jpg");
        b2.setSubtitleTitle("23-7期");
        imageData.add(b2);
        ImageData b3 = new ImageData();
        b3.setImage("http://ww1.sinaimg.cn/large/610dc034ly1fhxe0hfzr0j20u011in1q.jpg");
        imageData.add(b3);
        //2.然后调用setDatas方法填充数据，再start()就可以了，
        // 是否自动轮播setPlay可以不设置，默认为自动轮播即为ture
        cardBanner.setDatas(imageData).setCardImageLoader(new MyImageLoader()).setPlay(true).start();
        //卡片的点击事件
        cardBanner.setOnItemClickListener(new CardBanner.OnItemClickListener() {
            @Override
            public void onItem(int position) {
                Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
        //如果想使用自定义动画样式
        //则设置.setTransformer(new DemoTransformer())
        /**
         *使用自定义item
         * 这里开发者可以自己定义实体类和卡片的xml布局，使用自定义item必须使用setDataCount方法和
         * setBannerAdapter方法，setDataCount告知Library数据集合的大小，setBannerAdapter是设置xml
         * 及数据的加载，这里和RecyclerView一样，需要继承BannerViewHolder实现一个ViewHolder，具体可参考实例
         */
       image=new ArrayList<>();
        image.add("http://ww1.sinaimg.cn/large/610dc034ly1fhyeyv5qwkj20u00u0q56.jpg");
        image.add("https://ws1.sinaimg.cn/large/610dc034gy1fhvf13o2eoj20u011hjx6.jpg");
        image.add("http://ww1.sinaimg.cn/large/610dc034ly1fhxe0hfzr0j20u011in1q.jpg");
        cardBanner2.setBannerAdapter(new BannerAdapter() {
            @Override
            public int getCount() {
                return image.size();
            }

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
                        .load(image.get(position))
//                    .bitmapTransform(new CropCircleTransformation(getContext()))
                        .into(VH.roundedImageView);
            }
        });


        cardBanner2.start();
        cardBanner2.setOnItemClickListener(new CardBanner.OnItemClickListener() {
            @Override
            public void onItem(int position) {
                Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 可以使用startAutoPlay和stopAutoPlay来控制自动轮播
     */
    @Override
    protected void onStart() {
        super.onStart();
        cardBanner.startAutoPlay();
        cardBanner2.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cardBanner.stopAutoPlay();
        cardBanner2.stopAutoPlay();
    }

    class ViewHolder extends BannerViewHolder {
        public RoundedImageView roundedImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            roundedImageView = (RoundedImageView) itemView.findViewById(R.id.item_img);
        }
    }
}
