package com.wl.dudian.app.ui.activity;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wl.dudian.R;
import com.wl.dudian.app.adapter.DiscussAdapter;
import com.wl.dudian.framework.db.model.CommentsBean;
import com.wl.dudian.framework.db.model.DiscussDataModel;
import com.wl.dudian.framework.db.model.DiscussExtraModel;
import com.wl.dudian.framework.repository.DomainService;
import com.wl.dudian.databinding.DiscussViewBinding;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * @author Qiushui
 * @since 0.0.2
 */
public class DiscussView {

    private Context mContext;
    private int mId;
    private BottomSheetDialog mBottomSheetDialog;

    private DiscussAdapter mAdapter;
    private List<CommentsBean> mCommentsBeenList = new ArrayList<>();
    private DiscussViewBinding mBinding;

    private DomainService mDomainService;

    public DiscussView(Context context, int id) {
        mContext = context;
        mId = id;
        mDomainService = DomainService.getInstance(context);

        init(mContext);
        loadDiscussData(id, true);

    }

    private void loadExtraData(int id) {
        mDomainService.getDiscussExtra("" + id)
                     .subscribe(new Action1<DiscussExtraModel>() {
                         @Override
                         public void call(DiscussExtraModel discussExtraModel) {
                             mBinding.discussViewShortTv.setText("短评论数 (" + discussExtraModel.getShort_comments() + ")");
                             mBinding.discussViewLongTv.setText("长评论数 (" + discussExtraModel.getLong_comments() + ")");
                         }
                     });
    }

    private void loadDiscussData(int id, boolean isShortDiscuss) {
        Observable<DiscussDataModel> discussObservable;
        if (isShortDiscuss) {
            discussObservable = mDomainService.getDiscussShort("" + id);
        } else {
            discussObservable = mDomainService.getDiscussLong("" + id);
        }
        discussObservable.subscribe(new Action1<DiscussDataModel>() {
            @Override
            public void call(DiscussDataModel discussDataModel) {
                mCommentsBeenList.clear();
                mCommentsBeenList.addAll(discussDataModel.getComments());
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void init(Context mContent) {
        createBottomSheetDialog(mContent);
    }

    private void createBottomSheetDialog(Context context) {
        mBottomSheetDialog = new BottomSheetDialog(context);
        mBinding = DiscussViewBinding.inflate(LayoutInflater.from(context), null, false);
        mBinding.setHandler(new Handler());
        View view = mBinding.getRoot();
        mBottomSheetDialog.setContentView(view);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.discuss_view_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new DiscussAdapter(mCommentsBeenList, mContext);
        recyclerView.setAdapter(mAdapter);

        loadExtraData(mId);

        if (mBottomSheetDialog.isShowing()) {
            mBottomSheetDialog.dismiss();
        } else {
            mBottomSheetDialog.show();
        }
    }

    public class Handler {
        public void setShortClick(View view) {
            ((TextView) view).setTextColor(mContext.getResources().getColor(R.color.textColorDiscussShow));
            mBinding.discussViewLongTv.setTextColor(mContext.getResources().getColor(R.color.textColorDiscussHide));
            loadDiscussData(mId, true);
        }

        public void setLongClick(View view) {
            mBinding.discussViewShortTv.setTextColor(mContext.getResources().getColor(R.color.textColorDiscussHide));
            ((TextView) view).setTextColor(mContext.getResources().getColor(R.color.textColorDiscussShow));
            loadDiscussData(mId, false);
        }
    }
}
