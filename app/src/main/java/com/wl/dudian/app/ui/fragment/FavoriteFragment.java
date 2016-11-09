package com.wl.dudian.app.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wl.dudian.R;
import com.wl.dudian.app.adapter.LatestNewsItemAdapter;
import com.wl.dudian.app.db.StoriesBeanDB;
import com.wl.dudian.app.newsdetail.NewsDetailActivity;
import com.wl.dudian.app.repository.DomainService;
import com.wl.dudian.databinding.FavoriteFragmentBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏栏
 * Created by Qiushui on 16/6/26.
 */

public class FavoriteFragment extends BaseFragment {

    private LatestNewsItemAdapter mAdapter;
    private List<StoriesBeanDB> mStoriesBeanList;
    private DomainService mDomainService;
    private FavoriteFragmentBinding mBinding;


    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.favorite_fragment, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateFavoriteItem();

    }

    public void updateFavoriteItem() {
        mDomainService = DomainService.getInstance(getContext());
        mStoriesBeanList = new ArrayList<>();
        if (mDomainService.getFavoriteNews() == null || mDomainService.getFavoriteNews().size() < 1) {
            Snackbar.make(mBinding.favoriteFragmentRecyclerview, "没有收藏哦", Snackbar.LENGTH_SHORT).show();
        } else {
            mStoriesBeanList.addAll(mDomainService.getFavoriteNews());
        }

        showFavoriteNews();
    }

    private void showFavoriteNews() {
        mBinding.favoriteFragmentRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new LatestNewsItemAdapter(getContext());
        mBinding.favoriteFragmentRecyclerview.setAdapter(mAdapter);

        mAdapter.setOnLatestNewsItemClickListener(new LatestNewsItemAdapter.OnLatestNewsItemClickListener() {
            @Override
            public void onItemClick(View view, StoriesBeanDB storiesBean) {
                NewsDetailActivity.launch(getActivity(), storiesBean);
            }
        });
    }
}
