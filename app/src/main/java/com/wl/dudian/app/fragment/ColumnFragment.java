package com.wl.dudian.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wl.dudian.R;


/**
 * Created by yisheng on 16/6/26.
 */

public class ColumnFragment extends BaseFragment {

    public static ColumnFragment newInstance() {
        return new ColumnFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.columnfragment, container, false);
    }
}
