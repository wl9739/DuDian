package com.wl.dudian.app.latestnews;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wl.dudian.R;
import com.wl.dudian.app.BannerView;
import com.wl.dudian.app.adapter.LatestNewsItemAdapter;
import com.wl.dudian.framework.db.model.StoriesBean;
import com.wl.dudian.framework.db.model.TopStoriesBean;
import com.wl.dudian.app.newsdetail.NewsDetailActivity;
import com.wl.dudian.app.ui.fragment.BaseFragment;
import com.wl.dudian.databinding.LatestNewsFragmentBinding;
import com.wl.dudian.framework.util.BusinessUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻展示页面
 * <p/>
 * Created by Qiushui on 16/6/21.
 */

public class LatestNewsFragment extends BaseFragment implements LatestNewsContract.View {

    LatestNewsFragmentBinding mBinding;

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

    private LatestNewsContract.Presenter mPresenter;
    private int datePosition = 1;

    public static LatestNewsFragment newInstance() {
        return new LatestNewsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new LatestNewsPresenter(getContext(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.latest_news_fragment, container, false);

        // init headerview
        mHeaderView = inflater.inflate(R.layout.latest_news_fragment_header, mBinding.latestNewsSr, false);
        mBannerView = (BannerView) mHeaderView.findViewById(R.id.latest_news_fragment_header_banner);
        return mBinding.latestNewsSr;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.latestNewsFragmentRecyclerview.setLayoutManager(mLinearLayoutManager);
        mBinding.latestNewsFragmentRecyclerview.setItemAnimator(new DefaultItemAnimator());

        // init adapter
        mItemAdapter = new LatestNewsItemAdapter(mStoriesBeanList, getContext());
        // set adapter
        mBinding.latestNewsFragmentRecyclerview.setAdapter(mItemAdapter);

        // set header
        mItemAdapter.setHeaderView(mHeaderView);

        // init swiprefresh
        mBinding.latestNewsSr.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mBinding.latestNewsSr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadMoreNews();
            }
        });

        mBinding.latestNewsFragmentRecyclerview.setAdapter(mItemAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        mPresenter.loadLatestNews();

        mItemAdapter.setOnLatestNewsItemClickListener(
                new LatestNewsItemAdapter.OnLatestNewsItemClickListener() {
                    @Override
                    public void onItemClick(View view, StoriesBean storiesBean) {
                        int index = mStoriesBeanList.indexOf(storiesBean);
                        ((TextView)mLinearLayoutManager.findViewByPosition(index + 1)
                                .findViewById(R.id.latest_news_fragment_title))
                                .setTextColor(getResources().getColor(R.color.textColorSecond));
                        mItemAdapter.setRead(index);
                        mPresenter.updateRead(storiesBean);
                        NewsDetailActivity.launch(getActivity(), storiesBean);
                    }
                });

        mBinding.latestNewsSr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadLatestNews();
            }
        });

        mBinding.latestNewsFragmentRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastPosition;
            int itemCount;
            int lastItemCount;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                itemCount = mLinearLayoutManager.getItemCount();
                lastPosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastItemCount != itemCount && lastPosition == itemCount - 1) {
                    datePosition = lastPosition + 1;
                    mPresenter.loadMoreNews();
                }
            }
        });
    }

    /**
     * 停止下拉刷新
     */
    @Override
    public void stopRefresh() {
        if (mBinding.latestNewsSr.isRefreshing()) {
            mBinding.latestNewsSr.setRefreshing(false);
        }
    }

    @Override
    public void setPresenter(LatestNewsContract.Presenter presenter) {
        this.mPresenter = BusinessUtil.checkNotNull(presenter);
    }

    @Override
    public void showHeaderView(List<TopStoriesBean> topStoriesBeen) {
        stopRefresh();
        mBannerView.setImages(topStoriesBeen);
    }

    @Override
    public void showLatestNews(List<StoriesBean> storiesBeanList) {
        mItemAdapter.setRefresh(storiesBeanList);
        mItemAdapter.changeDateTitle(1, "今日热文");
    }

    @Override
    public void loadBeforNews(List<StoriesBean> storiesBeanList, String currentData) {
        mItemAdapter.setRefresh(storiesBeanList);
        mItemAdapter.changeDateTitle(datePosition, currentData);
    }

}
