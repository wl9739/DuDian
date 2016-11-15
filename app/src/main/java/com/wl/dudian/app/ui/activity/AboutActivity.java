package com.wl.dudian.app.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wl.dudian.R;

/**
 * 关于界面
 * Created by Qiushui on 2016/11/7.
 */

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.wl.dudian.databinding.AboutLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.about_layout);
        new Thread(binding.headerImage).start();
    }
}
