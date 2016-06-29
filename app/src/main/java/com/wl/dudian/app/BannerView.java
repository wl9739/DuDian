package com.wl.dudian.app;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.viewpagerindicator.CirclePageIndicator;
import com.wl.dudian.R;
import com.wl.dudian.app.model.TopStoriesBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 轮播控件
 * <p/>
 * Created by yisheng on 16/6/29.
 */

public class BannerView extends FrameLayout {

    private ViewPager mViewPager;
    private CirclePageIndicator mIndicator;
    private Context mContext;
    private ViewPagerAdapter mAdapter;
    private List<TopStoriesBean> mImages;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        inflate(context, R.layout.banner_view, this);
//        LayoutInflater.from(mContext).inflate(R.layout.banner_view, this, false);
        mContext = context;
        mAdapter = new ViewPagerAdapter();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(mContext).inflate(R.layout.banner_view, this, true);
        mViewPager = (ViewPager) findViewById(R.id.banner_view_viewpager);
        mIndicator = (CirclePageIndicator) findViewById(R.id.banner_view_indicator);
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);
    }

    public void setImages(List<TopStoriesBean> images) {
        mAdapter.setImages(images);
    }


    public static class ViewPagerAdapter extends PagerAdapter {

        private List<TopStoriesBean> mTopStoriesBeanList;

        public ViewPagerAdapter() {
            mTopStoriesBeanList = new ArrayList<>();
        }

        public void setImages(List<TopStoriesBean> images) {
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
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = container.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.banner_view_item, container, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.banner_view_imageview);
            TextView textView = (TextView) view.findViewById(R.id.banner_view_textview);
            Glide.with(context).load(mTopStoriesBeanList.get(position).getImage()).into(imageView);
            textView.setText(mTopStoriesBeanList.get(position).getTitle());
            container.addView(view);
            return view;
        }
    }
}
