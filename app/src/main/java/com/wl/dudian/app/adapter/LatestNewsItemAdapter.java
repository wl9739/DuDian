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
import com.wl.dudian.framework.DateUtil;

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
    private Context mContext;
    private OnLatestNewsItemClickListener mOnLatestNewsItemClickListener;
    private String mDateStr;
    /**
     * headerview
     */
    private View mHeaderView;

    public LatestNewsItemAdapter(List<StoriesBean> storiesBean, Context context) {
        mStoriesBeen = new ArrayList<>();
        mStoriesBeen.addAll(storiesBean);
        mContext = context;
    }

    /**
     * 设置HeaderView
     *
     * @param headerView
     */
    public void setHeaderView(View headerView) {
        this.mHeaderView = headerView;
        notifyItemChanged(0);
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


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(mHeaderView);
        } else if (viewType == TYPE_ITEM) {
            View view =
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.latest_news_fragment_item, parent, false);
            view.setOnClickListener(this);
            return new ItemViewHolder(view);
        }
        throw new RuntimeException(
                "there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            if (position < mStoriesBeen.size()) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                if (position == 0) {
                    itemViewHolder.dateTv.setText(DateUtil.getFullDateFormart("20160701"));
                    itemViewHolder.dateTv.setVisibility(View.VISIBLE);
                }
                itemViewHolder.titleTv.setText(mStoriesBeen.get(position).getTitle());
                itemViewHolder.itemView.setTag("" + mStoriesBeen.get(position).getId());
                downloadBitmap(mStoriesBeen.get(position).getImages(), ((ItemViewHolder) holder).picImageView);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (null != mHeaderView && position == 0) {
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

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View view) {
            super(view);
        }
    }
}
