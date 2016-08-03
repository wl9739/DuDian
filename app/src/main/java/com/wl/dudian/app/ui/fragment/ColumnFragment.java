package com.wl.dudian.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wl.dudian.R;
import com.wl.dudian.app.adapter.ColumnListAdapter;
import com.wl.dudian.app.model.ThemesModel;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by yisheng on 16/6/26.
 */

public class ColumnFragment extends BaseFragment {

    private static final String THEMES_MODEL = "THEMES_MODEL";
    @BindView(R.id.columnfragment_tablayout)
    TabLayout mTablayout;
    @BindView(R.id.columnfragment_vp)
    ViewPager mViewPager;

    private ColumnListAdapter mColumnListAdapter;

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
        View view = inflater.inflate(R.layout.columnfragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ThemesModel model = (ThemesModel) getArguments().getSerializable(THEMES_MODEL);
        if (null == model) {
            return;
        }
        mColumnListAdapter = new ColumnListAdapter(getChildFragmentManager(), model.getOthers());
        if (null != mViewPager) {
            mViewPager.setAdapter(mColumnListAdapter);
        }
        mTablayout.setupWithViewPager(mViewPager);
    }
}
