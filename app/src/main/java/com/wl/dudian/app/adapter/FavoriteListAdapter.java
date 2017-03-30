package com.wl.dudian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wl.dudian.R;
import com.wl.dudian.framework.db.model.StoriesBean;
import com.wl.dudian.framework.util.BusinessUtil;

import java.util.List;

/**
 * 收藏页面 adapter
 * Created by Qiushui on 2016/11/15.
 */

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ItemViewHolder>
        implements View.OnClickListener {

    private final Context mContext;
    private List<StoriesBean> mSource;
    private OnFavoriteItemClickListener mOnFavoriteItemClickListener;
    private static OnFavoriteItemTouchListener mOnFavoriteItemTouchListener;

    public FavoriteListAdapter(Context context, List<StoriesBean> storiesBeanList) {
        mContext = context;
        mSource = storiesBeanList;
    }

    public interface OnFavoriteItemClickListener {
        void onItemClick(View view, StoriesBean storiesBean);
    }

    public interface OnFavoriteItemTouchListener {
        void onItemTouch(int position);
    }

    public void setOnFavoriteItemClickListener(OnFavoriteItemClickListener onFavoriteItemClickListener) {
        mOnFavoriteItemClickListener = onFavoriteItemClickListener;
    }

    public void setOnFavoriteItemTouchListener(OnFavoriteItemTouchListener onFavoriteItemTouchListener) {
        mOnFavoriteItemTouchListener = onFavoriteItemTouchListener;
    }

    @Override
    public FavoriteListAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.latest_news_fragment_item, parent, false);
        view.setOnClickListener(this);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteListAdapter.ItemViewHolder holder, int position) {
        holder.titleTv.setText(mSource.get(position).getTitle());
        BusinessUtil.loadImage(mContext.getApplicationContext(), mSource.get(position).getImages().get(0), holder.picImageView);
        holder.itemView.setTag(mSource.get(position));
    }

    @Override
    public int getItemCount() {
        return mSource.size();
    }

    @Override
    public void onClick(View v) {
        mOnFavoriteItemClickListener.onItemClick(v, (StoriesBean) v.getTag());
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener{

        TextView dateTv;
        TextView titleTv;
        ImageView picImageView;

        ItemViewHolder(View itemView) {
            super(itemView);
            dateTv = (TextView) itemView.findViewById(R.id.latest_news_detail_date_tv);
            titleTv = (TextView) itemView.findViewById(R.id.latest_news_fragment_title);
            picImageView = (ImageView) itemView.findViewById(R.id.latest_news_fragment_image);
            itemView.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mOnFavoriteItemTouchListener.onItemTouch(getAdapterPosition());
            }
            return false;
        }
    }
}
