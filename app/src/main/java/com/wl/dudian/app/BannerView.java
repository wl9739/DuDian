package com.wl.dudian.app;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.viewpagerindicator.CirclePageIndicator;
import com.wl.dudian.R;
import com.wl.dudian.app.db.RealmString;
import com.wl.dudian.app.db.StoriesBeanDB;
import com.wl.dudian.app.db.TopStoriesBeanDB;
import com.wl.dudian.app.newsdetail.NewsDetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmList;


/**
 * 轮播控件
 * <p/>
 * Created by Qiushui on 16/6/29.
 */

public class BannerView extends FrameLayout {

    private static final String TAG = BannerView.class.getSimpleName();

    /**
     * viewpager
     */
    private ViewPager mViewPager;

    /**
     * viewpager指示器
     */
    private CirclePageIndicator mIndicator;

    /**
     * context
     */
    private Context mContext;

    /**
     * adapter
     */
    private BannerViewPagerAdapter mAdapter;
    /**
     * 定时任务
     */
    private ScheduledExecutorService mScheduledExecutorService;

    /**
     * 计数器,当前显示的位置
     */
    private volatile int mCurrentItem = 0;

    private SlideShowTask slideShowTask;

    /**
     * 处理消息
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "handleMessage: currentItem " + mCurrentItem);
            mViewPager.setCurrentItem(mCurrentItem);
        }
    };

    /**
     * 轮播图片的数量
     */
    private int mImageSize;
    private boolean isMoving;

    /**
     * 将TopStoriesBean转化为StoriesBean
     *
     * @param topStoriesBean
     * @return
     */
    private static StoriesBeanDB topStoriesBeanToStoriesBean(TopStoriesBeanDB topStoriesBean) {
        StoriesBeanDB storiesBean = new StoriesBeanDB();
        storiesBean.setId(topStoriesBean.getId());
        storiesBean.setImages(new RealmList<>(new RealmString(topStoriesBean.getImage())));
        storiesBean.setTitle(topStoriesBean.getTitle());
        storiesBean.setType(topStoriesBean.getType());
        storiesBean.setGa_prefix(topStoriesBean.getGa_prefix());
        return storiesBean;
    }

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mAdapter = new BannerViewPagerAdapter();
        slideShowTask = new SlideShowTask();
        LayoutInflater.from(mContext).inflate(R.layout.banner_view, this, true);
    }

    /**
     * 添加轮播显示的图片
     */
    public void setImages() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mAdapter.setImages(realm.where(TopStoriesBeanDB.class).findAll());
            }
        });
        startPlay();
    }

    @Override
    protected void onFinishInflate() {
        mViewPager = (ViewPager) findViewById(R.id.banner_view_viewpager);
        mIndicator = (CirclePageIndicator) findViewById(R.id.banner_view_indicator);
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                isMoving = mCurrentItem != position;
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentItem = position;
                isMoving = false;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                isMoving = state != ViewPager.SCROLL_STATE_IDLE;
            }
        });
        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isMoving = false;
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        isMoving = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isMoving = true;
                        break;
                }
                return false;
            }
        });
        mAdapter.setOnBannerItemClickListener(new BannerViewPagerAdapter.OnBannerItemClickListener() {
            @Override
            public void onBannerItemClick(StoriesBeanDB storiesBean) {
                NewsDetailActivity.launch(mContext, storiesBean);
            }
        });
    }

    /**
     * 开始启动自动轮播
     */
    private void startPlay() {
        if (mScheduledExecutorService == null) {
            mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            mScheduledExecutorService.scheduleWithFixedDelay(slideShowTask, 4000, 4000, TimeUnit.MILLISECONDS);
        }
    }

    public static class BannerViewPagerAdapter extends PagerAdapter {

        private OnBannerItemClickListener mOnBannerItemClickListener;
        private List<TopStoriesBeanDB> mTopStoriesBeanList;

        public BannerViewPagerAdapter() {
            mTopStoriesBeanList = new ArrayList<>();
        }

        /**
         * set点击事件
         *
         * @param onBannerItemClickListener
         */
        public void setOnBannerItemClickListener(
                BannerViewPagerAdapter.OnBannerItemClickListener onBannerItemClickListener) {
            mOnBannerItemClickListener = onBannerItemClickListener;
        }

        public void setImages(List<TopStoriesBeanDB> images) {
            mTopStoriesBeanList.clear();
            mTopStoriesBeanList = images;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTopStoriesBeanList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            Context context = container.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.banner_view_item, container, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.banner_view_imageview);
            TextView textView = (TextView) view.findViewById(R.id.banner_view_textview);
            Glide.with(context).load(mTopStoriesBeanList.get(position).getImage()).into(imageView);
            textView.setText(mTopStoriesBeanList.get(position).getTitle());
            view.setTag(mTopStoriesBeanList.get(position));
            container.addView(view);

            // 设置点击事件
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnBannerItemClickListener.onBannerItemClick(
                            topStoriesBeanToStoriesBean(mTopStoriesBeanList.get(position)));
                }
            });
            return view;
        }

        /**
         * 点击事件接口
         */
        public interface OnBannerItemClickListener {
            void onBannerItemClick(StoriesBeanDB storiesBean);
        }
    }

    /**
     * 定时任务
     */
    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            synchronized (BannerView.class) {
                if (!isMoving)
                    mCurrentItem = (mCurrentItem + 1) % mImageSize;
                mHandler.obtainMessage().sendToTarget();
            }
        }
    }
}
