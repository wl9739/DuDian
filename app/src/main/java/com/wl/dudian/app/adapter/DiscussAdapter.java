package com.wl.dudian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wl.dudian.R;
import com.wl.dudian.app.model.CommentsBean;
import com.wl.dudian.framework.BusinessUtil;
import com.wl.dudian.framework.DateUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 评论列表Adapter
 *
 * @author Qiushui
 * @since 0.0.2
 */
public class DiscussAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<CommentsBean> mCommentsBean = new ArrayList<>();
    private Context mContext;
    public DiscussAdapter(List<CommentsBean> comments, Context context) {
        mCommentsBean = comments;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DiscussViewHolder(LayoutInflater.from(mContext).inflate(R.layout.discuss_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DiscussViewHolder viewHolder = (DiscussViewHolder) holder;
        viewHolder.likeTv.setText("" + mCommentsBean.get(position).getLikes());
        viewHolder.nameTv.setText(mCommentsBean.get(position).getAuthor());
        viewHolder.timeTv.setText(DateUtil.getDate(mCommentsBean.get(position).getTime() * 1000L));
        viewHolder.contentTv.setText(mCommentsBean.get(position).getContent());
        BusinessUtil.loadImage(mContext, mCommentsBean.get(position).getAvatar(), viewHolder.profileImg);
    }

    @Override
    public int getItemCount() {
        return mCommentsBean.size();
    }

    static class DiscussViewHolder extends RecyclerView.ViewHolder{

        CircleImageView profileImg;
        TextView likeTv;
        TextView nameTv;
        TextView contentTv;
        TextView timeTv;
        public DiscussViewHolder(View itemView) {
            super(itemView);

            profileImg = (CircleImageView) itemView.findViewById(R.id.id_comment_iv_avatar);
            likeTv = (TextView) itemView.findViewById(R.id.id_comment_tv_like);
            nameTv = (TextView) itemView.findViewById(R.id.id_comment_tv_name);
            contentTv = (TextView) itemView.findViewById(R.id.id_comment_tv_content);
            timeTv = (TextView) itemView.findViewById(R.id.id_comment_tv_time);
        }
    }
}
