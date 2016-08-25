package com.wl.dudian.app.latestnews;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wl.dudian.R;
import com.wl.dudian.app.BannerView;
import com.wl.dudian.app.adapter.LatestNewsItemAdapter;
import com.wl.dudian.app.model.StoriesBean;
import com.wl.dudian.app.model.TopStoriesBean;
import com.wl.dudian.app.newsdetail.NewsDetailActivity;
import com.wl.dudian.app.repository.ITimestampedView;
import com.wl.dudian.app.ui.fragment.BaseFragment;
import com.wl.dudian.framework.BusinessUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 新闻展示页面
 * <p/>
 * Created by Qiushui on 16/6/21.
 */

public class LatestNewsFragment extends BaseFragment implements LatestNewsContract.View, ITimestampedView{

    public static final String TAG = "LatestNewsFragment11111";

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

    private LatestNewsContract.Presenter presenter;
    private int datePosition = 1;

    public static LatestNewsFragment newInstance() {
        return new LatestNewsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new LatestNewsPresenter(getContext(), this, this);
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
                presenter.loadMoreNews();
            }
        });

        mNewsItemRecyclerView.setAdapter(mItemAdapter);

        new LatestNewsPresenter(getContext(), this, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        presenter.loadLatestNews();

        mItemAdapter.setOnLatestNewsItemClickListener(
                new LatestNewsItemAdapter.OnLatestNewsItemClickListener() {
                    @Override
                    public void onItemClick(View view, StoriesBean storiesBean) {
                        presenter.updateRead(storiesBean);
                        NewsDetailActivity.launch(getActivity(), storiesBean);
                    }
                });

        mContentMainSwiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadLatestNews();
            }
        });

        mNewsItemRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mItemAdapter.getItemCount()) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            datePosition = lastVisibleItem + 1;
                            presenter.loadMoreNews();
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
     * 停止下拉刷新
     */
    @Override
    public void stopRefresh() {
        if (mContentMainSwiperefresh.isRefreshing()) {
            mContentMainSwiperefresh.setRefreshing(false);
        }
    }

    @Override
    public void setPresenter(LatestNewsContract.Presenter presenter) {
        this.presenter = BusinessUtil.checkNotNull(presenter);
    }

    @Override
    public long getViewDataTimestampMillis() {
        return mItemAdapter == null ? 0 : mItemAdapter.getTimestampMillis();
    }

    @Override
    public void showHeaderView(List<TopStoriesBean> topStoriesBeen) {
        stopRefresh();
        mBannerView.setImages(topStoriesBeen);
    }

    @Override
    public void showLatestNews(List<StoriesBean> storiesBeanList, long timestampMillis) {
        mItemAdapter.setRefresh(storiesBeanList, timestampMillis);
        mItemAdapter.changeDateTitle(1, "今日热文");
    }

    @Override
    public void loadBeforNews(List<StoriesBean> storiesBeanList, String currentData) {
        mItemAdapter.setRefresh(storiesBeanList, 0);
        mItemAdapter.changeDateTitle(datePosition, currentData);
    }

}
