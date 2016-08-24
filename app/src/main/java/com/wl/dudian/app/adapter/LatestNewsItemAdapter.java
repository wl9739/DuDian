package com.wl.dudian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wl.dudian.R;
import com.wl.dudian.app.model.StoriesBean;
import com.wl.dudian.framework.BusinessUtil;
import com.wl.dudian.framework.DateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Qiushui on 16/6/21.
 */

public class LatestNewsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener {


    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<StoriesBean> mStoriesBeen;
    private Context mContext;
    private OnLatestNewsItemClickListener mOnLatestNewsItemClickListener;
    /**
     * headerview
     */
    private View mHeaderView;

    private HashMap<Integer, String> datePositions = new HashMap<>();
    private long timestampMillis;

    public LatestNewsItemAdapter(List<StoriesBean> storiesBean, Context context) {
        mStoriesBeen = new ArrayList<>();
        mStoriesBeen.addAll(storiesBean);
        mContext = context;
    }

    @Override
    public void onClick(View v) {
        if (mOnLatestNewsItemClickListener != null) {
            mOnLatestNewsItemClickListener.onItemClick(v, (StoriesBean) v.getTag());
        }
    }

    public void setOnLatestNewsItemClickListener(OnLatestNewsItemClickListener onLatestNewsItemClickListener) {
        mOnLatestNewsItemClickListener = onLatestNewsItemClickListener;
    }

    /**
     * 设置HeaderView
     *
     * @param headerView
     */
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemChanged(0);
    }

    /**
     * 刷新数据
     *
     * @param storiesBeanList 新闻内容集合
     * @param timestampMillis
     */
    public void setRefresh(List<StoriesBean> storiesBeanList, long timestampMillis) {
        mStoriesBeen.clear();
        mStoriesBeen.addAll(storiesBeanList);
        this.timestampMillis = timestampMillis;
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
                // 如果有Header
                if (mHeaderView != null) {
                    if (datePositions.containsKey(position)) {
                        String date = datePositions.get(position);
                        if (TextUtils.isDigitsOnly(date)) {
                            itemViewHolder.dateTv.setText(DateUtil.getFullDateFormart(date));
                        } else {
                            itemViewHolder.dateTv.setText(date);
                        }
                        itemViewHolder.dateTv.setVisibility(View.VISIBLE);
                    } else {
                        itemViewHolder.dateTv.setVisibility(View.GONE);
                    }
                    // position数需要-1, 因为0是HeaderView
                    itemViewHolder.titleTv.setText(mStoriesBeen.get(position - 1).getTitle());
                    if (mStoriesBeen.get(position - 1).isRead()) {
                        itemViewHolder.titleTv.setTextColor(mContext.getResources().getColor(R.color.textColorSecond));
                    } else {
                        itemViewHolder.titleTv.setTextColor(mContext.getResources().getColor(R.color.textColorPrimary));
                    }
                    itemViewHolder.itemView.setTag(mStoriesBeen.get(position - 1));
                    BusinessUtil.loadImage(mContext, mStoriesBeen.get(position - 1).getImages().get(0),
                            ((ItemViewHolder) holder).picImageView);
                }
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

    @Override
    public int getItemCount() {
        if (null != mHeaderView) {
            return mStoriesBeen.size() + 1;
        }
        return mStoriesBeen.size();
    }

    public long getTimestampMillis() {
        return timestampMillis;
    }

    public void changeDateTitle(int i, String dateTitle) {
        datePositions.put(i, dateTitle);
        notifyItemChanged(i);
    }

    public interface OnLatestNewsItemClickListener {
        void onItemClick(View view, StoriesBean storiesBean);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.latest_news_detail_date_tv)
        TextView dateTv;
        @BindView(R.id.latest_news_fragment_title)
        TextView titleTv;
        @BindView(R.id.latest_news_fragment_image)
        ImageView picImageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
