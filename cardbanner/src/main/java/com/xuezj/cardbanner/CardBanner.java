package com.xuezj.cardbanner;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.xuezj.cardbanner.adapter.BannerAdapter;
import com.xuezj.cardbanner.adapter.CardAdapter;
import com.xuezj.cardbanner.imageloader.CardImageLoader;
import com.xuezj.cardbanner.mode.BaseTransformer;
import com.xuezj.cardbanner.mode.ScaleYTransformer;
import com.xuezj.cardbanner.utils.BannerUtils;
import com.xuezj.cardbanner.view.CardView;

import java.util.List;

/**
 * Created by xuezj on 2017/7/29.
 */

public class CardBanner extends FrameLayout {
    private WeakHandler handler = new WeakHandler();
    private Context context;
    private int mainTitleTextColor = 0xFFFFFFFF;
    private int subtitleTitleTextColor = 0xFFFFFFFF;
    private int mainTitleTextSize = 15;
    private int subtitleTitleTextSize = 12;
    private int borderWidth = 30;
    private int radius = 8;
    private int dividerWidth = 0;
    private LinearLayoutManager mLayoutManager;
    private PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
    private CardView cardView;
    private List<ImageData> datas;


    private CardImageLoader cardImageLoader;
    private BaseTransformer baseTransformer;
    private int viewWidth;


    private OnItemClickListener onItemClickListener;



    private BannerAdapter bannerAdapter;
    private boolean isPlay = true;
    private int dataCount = 0;



    private int delayTime = 2000;
    private static final int DEFAULT_SELECTION = Integer.MAX_VALUE >> 2;
    private int currentItem = 0;
    private CardAdapter cardAdapter;
    public CardBanner(Context context) {
        this(context, null);
//        this.context = context;
//        borderWidth = BannerUtils.dp2px(context, 30);
//        radius = BannerUtils.dp2px(context, 8);
//        View view = LayoutInflater.from(context).inflate(R.layout.card_banner, this, true);
//        cardBannerView = (CardBannerView) view.findViewById(R.id.card_view);
    }

    public CardBanner( Context context,  AttributeSet attrs) {
//        super(context, attrs);
//        this.context = context;
//        initView(attrs);
        this(context, attrs, 0);
    }


    public CardBanner(Context context, AttributeSet attrs,int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        typedArray(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.card_banner, this, true);
        viewWidth = context.getResources().getDisplayMetrics().widthPixels;
        cardView = (CardView) view.findViewById(R.id.card_view);
        baseTransformer = new ScaleYTransformer();
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
    }

    private void typedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.card_banner);
        borderWidth = typedArray.getDimensionPixelSize(R.styleable.card_banner_border_width, BannerUtils.dp2px(context, borderWidth));
        radius = typedArray.getDimensionPixelSize(R.styleable.card_banner_radius, BannerUtils.dp2px(context, radius));
        mainTitleTextColor = typedArray.getColor(R.styleable.card_banner_main_title_text_color, mainTitleTextColor);
        subtitleTitleTextColor = typedArray.getColor(R.styleable.card_banner_subtitle_title_text_color, subtitleTitleTextColor);
        dividerWidth = typedArray.getDimensionPixelSize(R.styleable.card_banner_divider_width, BannerUtils.dp2px(context, dividerWidth)) / 2;
        mainTitleTextSize = BannerUtils.px2sp(context, typedArray.getDimensionPixelSize(R.styleable.card_banner_main_title_text_size, BannerUtils.sp2px(context, mainTitleTextSize)));
        subtitleTitleTextSize = BannerUtils.px2sp(context, typedArray.getDimensionPixelSize(R.styleable.card_banner_subtitle_title_text_size, BannerUtils.sp2px(context, subtitleTitleTextSize)));
        typedArray.recycle();
    }


    public CardBanner setDatas(List<ImageData> datas) {
        this.datas = datas;
        this.dataCount = datas.size();
        return this;
    }

    public CardBanner setCardImageLoader(CardImageLoader cardImageloader) {
        this.cardImageLoader = cardImageloader;
        return this;
    }

    public CardBanner setTransformer(BaseTransformer baseTransformer) {
        this.baseTransformer = baseTransformer;
        return this;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void start() {
        setData();
        if (isPlay) {
            autoPlay();
        }
    }

    public CardBanner setPlay(boolean play) {
        isPlay = play;
        return this;
    }
    public CardBanner setBannerAdapter(BannerAdapter bannerAdapter) {
        this.datas = null;
        dataCount=bannerAdapter.getCount();
        this.bannerAdapter = bannerAdapter;
        return this;
    }
    private void setData() {
        cardView.setLayoutManager(mLayoutManager);
        cardView.setViewMode(baseTransformer);
        pagerSnapHelper.attachToRecyclerView(cardView);
        cardView.setOnCenterItemClickListener(new CardView.OnCenterItemClickListener() {
            @Override
            public void onCenterItemClick(View v) {
                if (onItemClickListener!=null)
                onItemClickListener.onItem((int)v.getTag(R.id.key_position));
            }
        });

        cardAdapter= new CardAdapter(context, viewWidth, borderWidth, dividerWidth);
        if(datas!=null){
            if (cardImageLoader !=null){
                cardAdapter.setCardImageloader(cardImageLoader);
                cardView.setDataCount(datas.size());
                cardAdapter.setDatas(datas);
            }else{
                throw new RuntimeException("[CardBanner] --> please set CardImageLoader");
            }

        }else{

            if (bannerAdapter!=null) {

                cardView.setDataCount(dataCount);
                cardAdapter.setDataCount(dataCount);
                cardAdapter.setBannerAdapter(bannerAdapter);
            }else
                throw new RuntimeException("[CardBanner] --> please set BannerAdapter");
        }



        cardAdapter.setTextSize(mainTitleTextSize, subtitleTitleTextSize);
        cardView.setAdapter(cardAdapter);

    }
    public CardBanner setDelayTime(int delayTime) {
        this.delayTime = delayTime;
        return this;
    }
    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (dataCount > 1 && isPlay) {
                currentItem = cardView.getCurrentItem() + 1;
                cardView.smoothScrollToPosition(currentItem);
                handler.postDelayed(task, delayTime);
            }
        }
    };

    public void autoPlay() {
        if (isPlay) {
            handler.removeCallbacks(task);
            handler.postDelayed(task, delayTime);
        }
    }

    public void stopPlay() {
        if (isPlay) {
            handler.removeCallbacks(task);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.i(tag, ev.getAction() + "--" + isAutoPlay);
        if (isPlay) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                autoPlay();
            } else if (action == MotionEvent.ACTION_DOWN) {
                stopPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public interface OnItemClickListener {
        void onItem(int position);
    }
    public void stop(){
        stopPlay();
    }

}
