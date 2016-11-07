package com.wl.dudian.app.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wl.dudian.R;
import com.wl.dudian.databinding.AboutLayoutBinding;

/**
 * Created by wanglin on 2016/11/7.
 */

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AboutLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.about_layout);
        new Thread(binding.headerImage).start();
    }
}
