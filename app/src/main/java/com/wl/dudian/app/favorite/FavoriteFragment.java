package com.wl.dudian.app.favorite;

import android.animation.ObjectAnimator;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.wl.dudian.R;
import com.wl.dudian.app.adapter.FavoriteListAdapter;
import com.wl.dudian.app.model.StoriesBean;
import com.wl.dudian.app.newsdetail.NewsDetailActivity;
import com.wl.dudian.app.repository.DomainService;
import com.wl.dudian.app.ui.fragment.BaseFragment;
import com.wl.dudian.databinding.FavoriteFragmentBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏栏
 * Created by Qiushui on 16/6/26.
 */

public class FavoriteFragment extends BaseFragment {

    private FavoriteListAdapter mAdapter;
    private List<StoriesBean> mStoriesBeanList;
    private DomainService mDomainService;
    private FavoriteFragmentBinding mBinding;
    private boolean delete;
    private boolean beginRecord;
    private int mPosition;

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
        mStoriesBeanList = new ArrayList<>();
        mDomainService = DomainService.getInstance(getContext());
        if (mDomainService.getFavoriteNews() == null || mDomainService.getFavoriteNews().size() < 1) {
            Snackbar.make(mBinding.favoriteFragmentRecyclerview, "没有收藏哦", Snackbar.LENGTH_SHORT).show();
        } else {
            mStoriesBeanList.addAll(mDomainService.getFavoriteNews());
        }

        showFavoriteNews();
    }

    private void showFavoriteNews() {
        mAdapter = new FavoriteListAdapter(getContext(), mStoriesBeanList);

        mBinding.favoriteFragmentRecyclerview.setAdapter(mAdapter);
        mBinding.favoriteFragmentRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.favoriteFragmentRecyclerview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                startDragAndDrop(motionEvent);
                return false;
            }
        });
        mAdapter.setOnFavoriteItemClickListener(new FavoriteListAdapter.OnFavoriteItemClickListener() {
            @Override
            public void onItemClick(View view, StoriesBean storiesBean) {
                NewsDetailActivity.launch(getActivity(), storiesBean);
            }
        });

        SimpleItemTouchHelperCallback callback = new SimpleItemTouchHelperCallback(mAdapter, mStoriesBeanList);
        callback.setOnItemTouchCountListener(new SimpleItemTouchHelperCallback.OnItemTouchCountListener() {
            @Override
            public void onItemTouchStart() {
                startAnime();
                beginRecord = true;
            }

            @Override
            public void onItemDetouch() {
                endAnime();
                deleteItem();
            }

            @Override
            public void onItemIndexChanged(int position) {
                mPosition = position;
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mBinding.favoriteFragmentRecyclerview);
    }

    private void deleteItem() {
        if (delete) {
            mDomainService.deleteFavorite(mStoriesBeanList.get(mPosition).getId());
            mStoriesBeanList.remove(mPosition);
            mAdapter.notifyItemRemoved(mPosition);
            delete = false;
        }
    }

    private void startDragAndDrop(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (beginRecord && mBinding.imageview.isContains(motionEvent.getRawX(), motionEvent.getRawY())) {
                    mBinding.imageview.setImageResource(R.drawable.ic_garbage_red);
                } else {
                    mBinding.imageview.setImageResource(R.drawable.ic_garbage);
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (beginRecord && mBinding.imageview.isContains(motionEvent.getRawX(), motionEvent.getRawY())) {
                    delete = true;
                    beginRecord = false;
                }
                break;
        }
    }

    private void endAnime() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBinding.imageview, "translationX", -0, -200);
        animator.setDuration(300);
        animator.start();
    }

    private void startAnime() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mBinding.imageview, "translationX", -200, 0);
        animator.setDuration(300);
        mBinding.imageview.setVisibility(View.VISIBLE);
        animator.start();
    }
}
