package com.wl.dudian.app.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wl.dudian.R;
import com.wl.dudian.app.BannerView;
import com.wl.dudian.app.newsdetail.NewsDetailActivity;
import com.wl.dudian.app.adapter.LatestNewsItemAdapter;
import com.wl.dudian.app.model.BeforeNews;
import com.wl.dudian.app.model.LatestNews;
import com.wl.dudian.app.model.StoriesBean;
import com.wl.dudian.framework.ACache;
import com.wl.dudian.framework.Constants;
import com.wl.dudian.framework.DateUtil;
import com.wl.dudian.framework.HttpUtil;
import com.wl.dudian.framework.Variable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * 新闻展示页面
 * <p/>
 * Created by yisheng on 16/6/21.
 */

public class LatestNewsFragment extends BaseFragment {

    public static final String TAG = "LatestNewsFragment11111";
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";

    @BindView(R.id.latest_news_fragment_recyclerview)
    RecyclerView mNewsItemRecyclerView;
    @BindView(R.id.latest_news_sr)
    SwipeRefreshLayout mContentMainSwiperefresh;

    /**
     * item adapter
     */
    private LatestNewsItemAdapter mItemAdapter;

    /**
     * Recycler view 线性布局管理器
     */
    private LinearLayoutManager mLinearLayoutManager;

    /**
     * 当前日期
     */
    private String mNowDate;

    /**
     * 新闻内容实体累集合
     */
    private List<StoriesBean> mStoriesBeanList = new ArrayList<>();

    /**
     * 轮播广告组件
     */
    private BannerView mBannerView;

    /**
     * Recycler view 的 header view
     */
    private View mHeaderView;

    public static LatestNewsFragment newInstance() {
        return new LatestNewsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        // init
        mContentMainSwiperefresh =
                (SwipeRefreshLayout) inflater.inflate(R.layout.latest_news_fragment, container, false);

        ButterKnife.bind(this, mContentMainSwiperefresh);
        // init headerview
        mHeaderView = inflater.inflate(R.layout.latest_news_fragment_header, mContentMainSwiperefresh, false);
        mBannerView = (BannerView) mHeaderView.findViewById(R.id.latest_news_fragment_header_banner);
        return mContentMainSwiperefresh;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mNewsItemRecyclerView.setLayoutManager(mLinearLayoutManager);
        mNewsItemRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // init adapter
        mItemAdapter = new LatestNewsItemAdapter(mStoriesBeanList, getContext());
        // set adapter
        mNewsItemRecyclerView.setAdapter(mItemAdapter);


        // set header
        mItemAdapter.setHeaderView(mHeaderView);

        // init swiprefresh
        mContentMainSwiperefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mContentMainSwiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLatestNewsInfo();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        refreshLatestNewsInfo();

        mItemAdapter.setOnLatestNewsItemClickListener(
                new LatestNewsItemAdapter.OnLatestNewsItemClickListener() {
                    @Override
                    public void onItemClick(View view, StoriesBean storiesBean) {
                        NewsDetailActivity.launch(getActivity(), storiesBean);
                    }
                });


        mNewsItemRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mItemAdapter.getItemCount()) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            loadMoreNews(mNowDate);
                        }
                    });
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    /**
     * 获取数据
     */
    public void refreshLatestNewsInfo() {
        Observable<LatestNews> memory = Observable.create(new Observable.OnSubscribe<LatestNews>() {
            @Override
            public void call(Subscriber<? super LatestNews> subscriber) {

            }
        });
        Observable<LatestNews> disk = Observable.create(new Observable.OnSubscribe<LatestNews>() {
            @Override
            public void call(Subscriber<? super LatestNews> subscriber) {

            }
        });
        Observable<LatestNews> network = Observable.create(new Observable.OnSubscribe<LatestNews>() {
            @Override
            public void call(Subscriber<? super LatestNews> subscriber) {

            }
        });

        Observable<LatestNews> source = Observable.concat(memory, disk, network).first();

        Observable<LatestNews> networkWithSave = network.doOnNext(new Action1<LatestNews>() {
            @Override
            public void call(LatestNews latestNews) {

            }
        });

        Observable<LatestNews> diskWithCache = disk.doOnNext(new Action1<LatestNews>() {
            @Override
            public void call(LatestNews latestNews) {

            }
        });
        HttpUtil.getInstance().getLatestNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<LatestNews>() {
                    @Override
                    public void call(LatestNews latestNews) {
                        // TODO 保存到数据库中
                    }
                })
                .subscribe(new Subscriber<LatestNews>() {
                    @Override
                    public void onCompleted() {
                        stopRefresh();
                        Variable.isRefresh = !Variable.isRefresh;
                        ACache.get(getActivity()).put(Constants.REFRESH_NETWORK, Constants.REFRESH_FLAG, 60);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        stopRefresh();
                    }

                    @Override
                    public void onNext(LatestNews latestNews) {
                        // 保存到数据库中
//                        latestNews.save();
                        mStoriesBeanList.clear();
                        mStoriesBeanList.addAll(latestNews.getStories());
                        mNowDate = latestNews.getDate();
                        mBannerView.setImages(latestNews.getTop_stories(), true);
                        mItemAdapter.setRefresh(mStoriesBeanList);

                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: ");
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mLinearLayoutManager.onSaveInstanceState());

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "onViewStateRestored: ");
        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mNewsItemRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    /**
     * 停止下拉刷新
     */
    private void stopRefresh() {
        if (mContentMainSwiperefresh.isRefreshing()) {
            mContentMainSwiperefresh.setRefreshing(false);
        }
    }

    /**
     * 加载历史信息
     *
     * @param nowDate
     */
    private void loadMoreNews(String nowDate) {
        HttpUtil.getInstance().getBeforeNews(DateUtil.getLastDay(nowDate))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<BeforeNews>() {
                    @Override
                    public void call(BeforeNews beforeNews) {
                        // 保存到数据库
                    }
                })
                .subscribe(new Subscriber<BeforeNews>() {
                    @Override
                    public void onCompleted() {
                        mItemAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(BeforeNews beforeNews) {
                        mStoriesBeanList.addAll(beforeNews.getStories());
                        mNowDate = beforeNews.getDate();
                        mItemAdapter.setRefresh(mStoriesBeanList);
                    }
                });
    }

}
