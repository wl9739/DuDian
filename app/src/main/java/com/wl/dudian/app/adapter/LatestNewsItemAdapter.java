package com.wl.dudian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wl.dudian.R;
import com.wl.dudian.app.model.StoriesBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yisheng on 16/6/21.
 */

public class LatestNewsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        if (mOnLatestNewsItemClickListener != null) {
            mOnLatestNewsItemClickListener.onItemClick(v, (String) v.getTag());
        }
    }

    public interface OnLatestNewsItemClickListener {
        void onItemClick(View view, String newsId);
    }

    public void setOnLatestNewsItemClickListener(OnLatestNewsItemClickListener onLatestNewsItemClickListener) {
        mOnLatestNewsItemClickListener = onLatestNewsItemClickListener;
    }

    private List<StoriesBean> mStoriesBeen = new ArrayList<>();
    private Context mContext;
    private OnLatestNewsItemClickListener mOnLatestNewsItemClickListener;

    public LatestNewsItemAdapter(List<StoriesBean> storiesBean, Context context) {
        mStoriesBeen.addAll(storiesBean);
        mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.latest_news_fragment_item, parent, false);
        view.setOnClickListener(this);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ItemViewHolder) holder).titleTv.setText(mStoriesBeen.get(position).getTitle());
        holder.itemView.setTag("" + mStoriesBeen.get(position).getId());
        downloadBitmap(mStoriesBeen.get(position).getImages(), ((ItemViewHolder) holder).picImageView);
    }

    private void downloadBitmap(List<String> images, ImageView picImageView) {
        if (null == images || images.size() < 1) {
            return;
        }
        Glide.with(mContext).load(images.get(0)).into(picImageView);
    }

    @Override
    public int getItemCount() {
        return mStoriesBeen.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView dateTv;
        TextView titleTv;
        ImageView picImageView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            dateTv = (TextView) itemView.findViewById(R.id.latest_news_detail_date_tv);
            titleTv = (TextView) itemView.findViewById(R.id.latest_news_fragment_title);
            picImageView = (ImageView) itemView.findViewById(R.id.latest_news_fragment_image);

        }
    }
}
