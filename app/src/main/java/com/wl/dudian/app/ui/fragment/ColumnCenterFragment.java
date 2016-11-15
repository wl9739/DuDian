package com.wl.dudian.app.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wl.dudian.R;
import com.wl.dudian.app.adapter.LatestNewsItemAdapter;
import com.wl.dudian.framework.db.model.StoriesBean;
import com.wl.dudian.framework.db.model.ThemeDetailModel;
import com.wl.dudian.app.newsdetail.NewsDetailActivity;
import com.wl.dudian.framework.repository.DomainService;
import com.wl.dudian.databinding.ColumncenterfragmentBinding;

import java.util.ArrayList;
import java.util.List;
import rx.functions.Action1;

/**
 * 专栏父类
 *
 * @author Qiushui
 * @since 0.0.2
 */
public class ColumnCenterFragment extends BaseFragment {

    private static final String COLUMN_ID = "COLUMN_ID";

    private LatestNewsItemAdapter mNewsItemAdapter;
    private List<StoriesBean> mStoriesBeanList;
    private String mColumnId;
    private DomainService mDomainService;
    private ColumncenterfragmentBinding mBinding;

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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.columncenterfragment, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDomainService = DomainService.getInstance(getContext());
        mColumnId = getArguments().getString(COLUMN_ID);
        if (null == mColumnId || TextUtils.isEmpty(mColumnId)) {
            return;
        }
        getThemeDetails(mColumnId);
        mStoriesBeanList = new ArrayList<>();
        mNewsItemAdapter = new LatestNewsItemAdapter(mStoriesBeanList, getActivity());
        mBinding.columncenterfragmentRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.columncenterfragmentRecyclerview.setAdapter(mNewsItemAdapter);

        mNewsItemAdapter.setOnLatestNewsItemClickListener(new LatestNewsItemAdapter.OnLatestNewsItemClickListener() {
            @Override
            public void onItemClick(View view, StoriesBean storiesBean) {
                NewsDetailActivity.launch(getActivity(), storiesBean, true);
            }
        });
    }

    /**
     * 获取主题日报栏目详情
     *
     * @param columnId 主题ID
     */
    private void getThemeDetails(String columnId) {
        mDomainService.getThemeDetail(columnId)
                     .subscribe(new Action1<ThemeDetailModel>() {
                         @Override
                         public void call(ThemeDetailModel themeDetailModel) {
                             mStoriesBeanList.clear();
                             mStoriesBeanList.addAll(themeDetailModel.getStories());
                             mNewsItemAdapter.setRefresh(mStoriesBeanList);

                         }
                     });
    }
}
