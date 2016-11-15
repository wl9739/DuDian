package com.wl.dudian.app.favorite;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.wl.dudian.app.adapter.FavoriteListAdapter;
import com.wl.dudian.app.model.StoriesBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Qiushui on 2016/11/15.
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {

    private final FavoriteListAdapter mAdapter;
    private List<StoriesBean> mData;
    private int sCount = 0;

    public interface OnItemTouchCountListener {
        void onItemTouchStart();

        void onItemDetouch();

        void onItemIndexChanged(int finalPosition);
    }

    private OnItemTouchCountListener mOnItemTouchCountListener;

    public void setOnItemTouchCountListener(OnItemTouchCountListener onItemTouchCountListener) {
        mOnItemTouchCountListener = onItemTouchCountListener;
    }

    public SimpleItemTouchHelperCallback(FavoriteListAdapter adapter, List<StoriesBean> data) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0);
        mData = new ArrayList<>();
        mData.addAll(data);

        mAdapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mData, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mData, i, i - 1);
            }
        }
        mAdapter.notifyItemMoved(fromPosition, toPosition);
        mOnItemTouchCountListener.onItemIndexChanged(toPosition);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        mOnItemTouchCountListener.onItemIndexChanged(position);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (sCount == 0) {
            mOnItemTouchCountListener.onItemTouchStart();
            sCount++;
        }
    }


    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        mOnItemTouchCountListener.onItemDetouch();
        sCount = 0;
    }
}

