package com.xuezj.cardbanner.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xuezj.cardbanner.R;
import com.xuezj.cardbanner.mode.BaseTransformer;


/**
 * Created by xuezj on 2017/7/29.
 */

public class CardView extends RecyclerView implements View.OnClickListener {

    private int itemCount;
    private boolean mIsForceCentering;
    private BaseTransformer mViewMode;
    //    private boolean mNeedCenterForce;
    private boolean mNeedLoop = true;
    private OnCenterItemClickListener mCenterItemClickListener;
    private View mCurrentCenterChildView;
    private OnScrollListener mOnScrollListener;
    private boolean mFirstOnLayout;
    private boolean mFirstSetAdapter = true;


    private int dataCount;
    private Handler mPostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            scrollToPosition(Integer.MAX_VALUE >> 2 - Integer.MAX_VALUE >> 2 % dataCount);
        }
    };

    public CardView(Context context) {
        this(context, null);
    }

    public CardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!mFirstSetAdapter) {
            if (mNeedLoop) {
                if (!mFirstOnLayout) {
                    mFirstOnLayout = true;
                    mPostHandler.sendEmptyMessage(0);
                }
                mCurrentCenterChildView = findViewAtCenter();
                smoothScrollToView(mCurrentCenterChildView);
            }


            if (mCurrentCenterChildView != null)
                mCurrentCenterChildView.setOnClickListener(this);
        }

    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mViewMode != null) {
            final int count = getChildCount();
            for (int i = 0; i < count; ++i) {
                View v = getChildAt(i);
                if (v != mCurrentCenterChildView && mCenterItemClickListener != null)
                    v.setOnClickListener(null);

                mViewMode.applyToView(v, this);
            }
        }

        if (mOnScrollListener != null)
            mOnScrollListener.onScrollChanged(l, t, oldl, oldt);
    }

    public void smoothScrollToView(View v) {
        int distance = 0;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            if (getLayoutManager().canScrollVertically()) {
                final float y = v.getY() + v.getHeight() * 0.5f;
                final float halfHeight = getHeight() * 0.5f;
                distance = (int) (y - halfHeight);
            } else if (getLayoutManager().canScrollHorizontally()) {
                final float x = v.getX() + v.getWidth() * 0.5f;
                final float halfWidth = getWidth() * 0.5f;
                distance = (int) (x - halfWidth);
            }

        } else
            throw new IllegalArgumentException("CircleRecyclerView just support T extend LinearLayoutManager!");
        smoothScrollBy(distance, distance);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        if (mOnScrollListener != null)
            mOnScrollListener.onScrolled(dx, dy);
    }

    @Override
    public void onScrollStateChanged(int state) {
        if (state == SCROLL_STATE_IDLE) {

            mCurrentCenterChildView = findViewAtCenter();
            if (mCurrentCenterChildView != null && mCenterItemClickListener != null) {
                mCurrentCenterChildView.setOnClickListener(this);
            }

        }

        if (mOnScrollListener != null)
            mOnScrollListener.onScrollStateChanged(state);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        mIsForceCentering = false;
        return super.onTouchEvent(e);
    }

    public View findViewAt(int x, int y) {
        final int count = getChildCount();
        for (int i = 0; i < count; ++i) {
            final View v = getChildAt(i);
            final int x0 = v.getLeft();
            final int y0 = v.getTop();
            final int x1 = v.getWidth() + x0;
            final int y1 = v.getHeight() + y0;
            if (x >= x0 && x <= x1 && y >= y0 && y <= y1) {
                return v;
            }
        }
        return null;
    }

    public int getCurrentItem() {
        mCurrentCenterChildView = findViewAtCenter();
        return (int) findViewAtCenter().getTag(R.id.key_item);
    }

    public View findViewAtCenter() {
        if (getLayoutManager().canScrollVertically()) {
            return findViewAt(0, getHeight() / 2);
        } else if (getLayoutManager().canScrollHorizontally()) {
            return findViewAt(getWidth() / 2, 0);
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        if (mCenterItemClickListener != null)
            mCenterItemClickListener.onCenterItemClick(v);
    }


    public void setNeedLoop(boolean needLoop) {
        mNeedLoop = needLoop;
    }


    public void setOnCenterItemClickListener(OnCenterItemClickListener listener) {
        mCenterItemClickListener = listener;
    }

    public void setDataCount(int dataCount) {
        this.dataCount = dataCount;
    }

    public void setViewMode(BaseTransformer mode) {
        mViewMode = mode;
    }

    public void setOnScrollListener(OnScrollListener listener) {
        mOnScrollListener = listener;
    }

    public interface OnCenterItemClickListener {
        void onCenterItemClick(View v);
    }

    public interface OnScrollListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);

        void onScrollStateChanged(int state);

        void onScrolled(int dx, int dy);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        itemCount = adapter.getItemCount();
        super.setAdapter(adapter);
        if (mFirstSetAdapter) {
            mFirstSetAdapter = false;
        } else {
            if (adapter != null)
                mPostHandler.sendEmptyMessage(0);
        }
    }
}
