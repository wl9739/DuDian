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
import com.wl.dudian.app.BannerView;
import com.wl.dudian.app.model.StoriesBean;
import com.wl.dudian.app.model.TopStoriesBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yisheng on 16/6/21.
 */

public class LatestNewsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {

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

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<StoriesBean> mStoriesBeen;
    private List<TopStoriesBean> mTopStoriesBeenList;
    private Context mContext;
    private OnLatestNewsItemClickListener mOnLatestNewsItemClickListener;

    public LatestNewsItemAdapter(List<StoriesBean> storiesBean, List<TopStoriesBean> topStoriesBeen, Context context) {
        mStoriesBeen = new ArrayList<>();
        mTopStoriesBeenList = new ArrayList<>();
        mTopStoriesBeenList.addAll(topStoriesBeen);
        mStoriesBeen.addAll(storiesBean);
        mContext = context;
    }

    /**
     * 刷新数据
     *
     * @param storiesBeanList
     */
    public void setRefresh(List<StoriesBean> storiesBeanList) {
        mStoriesBeen.clear();
        mStoriesBeen.addAll(storiesBeanList);
        notifyDataSetChanged();
    }

    public void setRefresh(List<StoriesBean> storiesBeanList, List<TopStoriesBean> topStoriesBeen) {
        mStoriesBeen.clear();
        mStoriesBeen.addAll(storiesBeanList);
        mTopStoriesBeenList.clear();
        mTopStoriesBeenList.addAll(topStoriesBeen);
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.latest_news_fragment_header, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.latest_news_fragment_item, parent, false);
            view.setOnClickListener(this);
            return new ItemViewHolder(view);
        }
        throw new RuntimeException(
                "there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.mBannerView.setImages(mTopStoriesBeenList);
        } else if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.titleTv.setText(mStoriesBeen.get(position).getTitle());
            itemViewHolder.itemView.setTag("" + mStoriesBeen.get(position).getId());
            downloadBitmap(mStoriesBeen.get(position).getImages(), ((ItemViewHolder) holder).picImageView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    private void downloadBitmap(List<String> images, ImageView picImageView) {
        if (null == images || images.size() < 1) {
            return;
        }
        Glide.with(mContext).load(images.get(0)).into(picImageView);
    }

    @Override
    public int getItemCount() {
        return mStoriesBeen.size() + 1;
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

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        BannerView mBannerView;
        public HeaderViewHolder(View view) {
            super(view);
            mBannerView = (BannerView) view.findViewById(R.id.latest_news_fragment_header_banner);
        }
    }
}
