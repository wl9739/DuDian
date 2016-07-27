package com.wl.dudian.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wl.dudian.R;
import com.wl.dudian.app.activity.LatestNewsDetailActivity;
import com.wl.dudian.app.adapter.LatestNewsItemAdapter;
import com.wl.dudian.app.model.StoriesBean;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by yisheng on 16/6/26.
 */

public class FavoriteFragment extends BaseFragment {

    @BindView(R.id.favorite_fragment_recyclerview)
    RecyclerView mRecyclerview;

    private LatestNewsItemAdapter mAdapter;
    private List<StoriesBean> mStoriesBeanList;


    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateFavoriteItem();
    }

    public void updateFavoriteItem() {
        mStoriesBeanList = DataSupport.findAll(StoriesBean.class);
        if (mStoriesBeanList.size() < 1) {
            // TODO 没有收藏记录
        } else {
            showFavoriteNews();
        }
    }

    private void showFavoriteNews() {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new LatestNewsItemAdapter(mStoriesBeanList, getContext());
        mRecyclerview.setAdapter(mAdapter);

        mAdapter.setOnLatestNewsItemClickListener(new LatestNewsItemAdapter.OnLatestNewsItemClickListener() {
            @Override
            public void onItemClick(View view, StoriesBean storiesBean) {
                LatestNewsDetailActivity.launch(getActivity(), storiesBean);
            }
        });
    }
}
