package com.wl.dudian.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wl.dudian.app.ui.fragment.ColumnCenterFragment;
import com.wl.dudian.app.model.ThemesModel;

import java.util.List;

/**
 * @author zfeiyu
 * @since 0.0.2
 */
public class ColumnListAdapter extends FragmentPagerAdapter {

    private List<ThemesModel.OthersBean> mOthersBeen;

    public ColumnListAdapter(FragmentManager fm, List<ThemesModel.OthersBean> others) {
        super(fm);
        mOthersBeen = others;
    }

    @Override
    public Fragment getItem(int position) {
        return ColumnCenterFragment.newInstance(String.valueOf(mOthersBeen.get(position).getId()));
    }

    @Override
    public int getCount() {
        return mOthersBeen == null ? 0 : mOthersBeen.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mOthersBeen.get(position).getName();
    }
}
