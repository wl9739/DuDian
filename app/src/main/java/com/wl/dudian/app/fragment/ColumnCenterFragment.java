package com.wl.dudian.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wl.dudian.R;
import com.wl.dudian.app.activity.LatestNewsDetailActivity;
import com.wl.dudian.app.adapter.LatestNewsItemAdapter;
import com.wl.dudian.app.model.StoriesBean;
import com.wl.dudian.app.model.ThemeDetailModel;
import com.wl.dudian.framework.HttpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author zfeiyu
 * @since 0.0.2
 */
public class ColumnCenterFragment extends BaseFragment {

    private static final String COLUMN_ID = "COLUMN_ID";
    @BindView(R.id.columncenterfragment_recyclerview)
    RecyclerView mRecyclerview;

    private LatestNewsItemAdapter mNewsItemAdapter;
    private ThemeDetailModel mDetailModel;
    private List<StoriesBean> mStoriesBeanList;
    private String mColumnId;

    public static ColumnCenterFragment newInstance(String id) {
        ColumnCenterFragment fragment = new ColumnCenterFragment();
        Bundle bundle = new Bundle();
        bundle.putString(COLUMN_ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.columncenterfragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mColumnId = getArguments().getString(COLUMN_ID);
        if (null == mColumnId || TextUtils.isEmpty(mColumnId)) {
            return;
        }
        getThemeDetails(mColumnId);
        mStoriesBeanList = new ArrayList<>();
        mNewsItemAdapter = new LatestNewsItemAdapter(mStoriesBeanList, getActivity());
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerview.setAdapter(mNewsItemAdapter);

        mNewsItemAdapter.setOnLatestNewsItemClickListener(new LatestNewsItemAdapter.OnLatestNewsItemClickListener() {
            @Override
            public void onItemClick(View view, StoriesBean storiesBean) {
                LatestNewsDetailActivity.launch(getActivity(), storiesBean);
            }
        });

    }

    /**
     * 获取主题日报栏目详情
     *
     * @param columnId 主题ID
     */
    private void getThemeDetails(String columnId) {
        HttpUtil.getInstance()
                .getThemeDetail(columnId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ThemeDetailModel>() {
                    @Override
                    public void onCompleted() {
                        mNewsItemAdapter.setRefresh(mStoriesBeanList);


                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ThemeDetailModel themeDetailModel) {
                        mStoriesBeanList.clear();
                        mStoriesBeanList.addAll(themeDetailModel.getStories());
                    }
                });
    }
}
