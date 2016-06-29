package com.wl.dudian.app.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wl.dudian.R;
import com.wl.dudian.app.activity.LatestNewsDetailActivity;
import com.wl.dudian.app.adapter.LatestNewsItemAdapter;
import com.wl.dudian.app.model.BeforeNews;
import com.wl.dudian.app.model.LatestNews;
import com.wl.dudian.app.model.StoriesBean;
import com.wl.dudian.app.model.TopStoriesBean;
import com.wl.dudian.framework.ACache;
import com.wl.dudian.framework.Constants;
import com.wl.dudian.framework.HttpUtil;
import com.wl.dudian.framework.Variable;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by yisheng on 16/6/21.
 */

public class LatestNewsFragment extends BaseFragment {

    public static final String TAG = "LatestNewsFragment11111";

    public interface OnRefreshedListener {
        void onRefreshed();

        void onRefreshError();
    }

    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    private RecyclerView mNewsItemRecyclerView;
    private LatestNewsItemAdapter mItemAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private String mNowDate;

    private List<StoriesBean> mStoriesBeanList = new ArrayList<>();
    private List<TopStoriesBean> mTopStoriesBeen = new ArrayList<>();
    private OnRefreshedListener mOnRefreshedListener;

    public static LatestNewsFragment newInstance() {
        return new LatestNewsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.latest_news_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");

        mNewsItemRecyclerView = (RecyclerView) view.findViewById(R.id.latest_news_fragment_recyclerview);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
        setRetainInstance(true);

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        refreshLatestNewsInfo();
        mNewsItemRecyclerView.setLayoutManager(mLinearLayoutManager);


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

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    /**
     * 加载历史信息
     *
     * @param nowDate
     */
    private void loadMoreNews(String nowDate) {
        int oldDay = Integer.parseInt(nowDate) - 1;
        Log.d(TAG, "loadMoreNews: oldday" + oldDay);
        HttpUtil.getInstance().getBeforeNews("" + oldDay)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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

    /**
     * 获取数据
     */
    public void refreshLatestNewsInfo() {
        HttpUtil.getInstance().getLatestNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LatestNews>() {
                    @Override
                    public void onCompleted() {
                        if (null != mOnRefreshedListener) {
                            mOnRefreshedListener.onRefreshed();
                        }
                        Variable.isRefresh = !Variable.isRefresh;
                        ACache.get(getActivity()).put(Constants.REFRESH_NETWORK, Constants.REFRESH_FLAG, 60);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (null != mOnRefreshedListener) {
                            mOnRefreshedListener.onRefreshError();
                        }
                    }

                    @Override
                    public void onNext(LatestNews latestNews) {
                        mStoriesBeanList.clear();
                        mStoriesBeanList.addAll(latestNews.getStories());
                        mNowDate = latestNews.getDate();
//                        mItemAdapter.setRefresh(mStoriesBeanList, latestNews.getTop_stories());
                        mItemAdapter = new LatestNewsItemAdapter(latestNews.getStories(), latestNews.getTop_stories(), getContext());
                        mNewsItemRecyclerView.setAdapter(mItemAdapter);
                        mItemAdapter.setOnLatestNewsItemClickListener(new LatestNewsItemAdapter.OnLatestNewsItemClickListener() {
                            @Override
                            public void onItemClick(View view, String newsId) {
                                LatestNewsDetailActivity.launch(getActivity(), newsId);
                            }
                        });
                    }
                });
    }

    public void setOnRefreshedListener(OnRefreshedListener onRefreshedListener) {
        mOnRefreshedListener = onRefreshedListener;
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

}
