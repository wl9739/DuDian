package com.wl.dudian.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wl.dudian.R;
import com.wl.dudian.app.adapter.LatestNewsItemAdapter;
import com.wl.dudian.app.model.StoriesBean;
import com.wl.dudian.app.newsdetail.NewsDetailActivity;
import com.wl.dudian.app.repository.DomainService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Qiushui on 16/6/26.
 */

public class FavoriteFragment extends BaseFragment {

    @BindView(R.id.favorite_fragment_recyclerview)
    RecyclerView mRecyclerview;

    private LatestNewsItemAdapter mAdapter;
    private List<StoriesBean> mStoriesBeanList;
    private DomainService domainService;


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
        domainService = DomainService.getInstance(getContext());
        mStoriesBeanList = new ArrayList<>();
        if (domainService.getFavoriteNews() == null || domainService.getFavoriteNews().size() < 1) {
            Snackbar.make(mRecyclerview, "没有收藏哦", Snackbar.LENGTH_SHORT).show();
        } else {
            mStoriesBeanList.addAll(domainService.getFavoriteNews());
        }

        showFavoriteNews();
    }

    private void showFavoriteNews() {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new LatestNewsItemAdapter(mStoriesBeanList, getContext());
        mRecyclerview.setAdapter(mAdapter);

        mAdapter.setOnLatestNewsItemClickListener(new LatestNewsItemAdapter.OnLatestNewsItemClickListener() {
            @Override
            public void onItemClick(View view, StoriesBean storiesBean) {
                NewsDetailActivity.launch(getActivity(), storiesBean);
            }
        });
    }
}
