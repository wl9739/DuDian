package com.wl.dudian.app.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wl.dudian.R;
import com.wl.dudian.app.adapter.ColumnListAdapter;
import com.wl.dudian.framework.db.model.ThemesModel;
import com.wl.dudian.databinding.ColumnfragmentBinding;

/**
 * 专栏列表
 * Created by Qiushui on 16/6/26.
 */

public class ColumnFragment extends BaseFragment {

    private static final String THEMES_MODEL = "THEMES_MODEL";
    private ColumnfragmentBinding mBinding;

    public static ColumnFragment newInstance(ThemesModel themesModel) {
        ColumnFragment fragment = new ColumnFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(THEMES_MODEL, themesModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.columnfragment, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ThemesModel model = (ThemesModel) getArguments().getSerializable(THEMES_MODEL);
        if (null == model) {
            return;
        }
        ColumnListAdapter columnListAdapter = new ColumnListAdapter(getChildFragmentManager(), model.getOthers());
        if (null != mBinding.columnfragmentVp) {
            mBinding.columnfragmentVp.setAdapter(columnListAdapter);
        }
        mBinding.columnfragmentTablayout.setupWithViewPager(mBinding.columnfragmentVp);
    }
}
