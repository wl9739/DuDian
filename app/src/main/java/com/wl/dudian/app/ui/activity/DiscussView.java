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
import com.wl.dudian.app.model.CommentsBean;
import com.wl.dudian.app.model.DiscussDataModel;
import com.wl.dudian.app.model.DiscussExtraModel;
import com.wl.dudian.framework.HttpUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author zfeiyu
 * @since 0.0.2
 */
public class DiscussView {

    private Context mContext;
    private int mId;
    private BottomSheetDialog mBottomSheetDialog;

    private TextView mShortTv;
    private TextView mLongTv;
    private TextView mProvalTv;

    private DiscussAdapter mAdapter;
    private List<CommentsBean> mCommentsBeenList = new ArrayList<>();

    public DiscussView(Context context, int id) {
        mContext = context;
        mId = id;

        init(mContext);
        loadDiscussData(id, true);

    }

    private void loadExtraData(int id) {
        HttpUtil.getInstance().getDiscussExtra("" + id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DiscussExtraModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DiscussExtraModel discussExtraModel) {
                        mShortTv.setText("短评论数 (" + discussExtraModel.getShort_comments() + ")");
                        mLongTv.setText("长评论数 (" + discussExtraModel.getLong_comments() + ")");
                        mProvalTv.setText("" + discussExtraModel.getPopularity());
                    }
                });
    }

    private void loadDiscussData(int id, boolean isShortDiscuss) {
        Observable discussObservable;
        if (isShortDiscuss) {
            discussObservable = HttpUtil.getInstance().getDiscussShort("" + id);
        } else {
            discussObservable = HttpUtil.getInstance().getDiscussLong("" + id);
        }
        discussObservable.subscribeOn(Schedulers.io())
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Subscriber<DiscussDataModel>() {
                             @Override
                             public void onCompleted() {

                             }

                             @Override
                             public void onError(Throwable e) {

                             }

                             @Override
                             public void onNext(DiscussDataModel discussDataModel) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.discuss_view, null, false);
        mBottomSheetDialog.setContentView(view);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.discuss_view_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new DiscussAdapter(mCommentsBeenList, mContext);
        recyclerView.setAdapter(mAdapter);

        mLongTv = (TextView) view.findViewById(R.id.discuss_view_long_tv);
        mShortTv = (TextView) view.findViewById(R.id.discuss_view_short_tv);
        mProvalTv = (TextView) view.findViewById(R.id.discuss_view_prov_tv);

        loadExtraData(mId);

        mLongTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLongClick();
            }
        });

        mShortTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setShortClick();
            }
        });

        if (mBottomSheetDialog.isShowing()) {
            mBottomSheetDialog.dismiss();
        } else {
            mBottomSheetDialog.show();
        }
    }

    private void setShortClick() {
        mShortTv.setTextColor(mContext.getResources().getColor(R.color.textColorDiscussShow));
        mLongTv.setTextColor(mContext.getResources().getColor(R.color.textColorDiscussHide));
        loadDiscussData(mId, true);
    }

    private void setLongClick() {
        mShortTv.setTextColor(mContext.getResources().getColor(R.color.textColorDiscussHide));
        mLongTv.setTextColor(mContext.getResources().getColor(R.color.textColorDiscussShow));
        loadDiscussData(mId, false);
    }
}
