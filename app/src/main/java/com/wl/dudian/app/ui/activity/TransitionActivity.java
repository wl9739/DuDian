package com.wl.dudian.app.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.wl.dudian.R;
import com.wl.dudian.databinding.TextActivityBinding;


/**
 * Created by Qiushui on 16/6/24.
 */

public class TransitionActivity extends BaseActivity {

    public static final String IMAGE_NAME = "IMAGE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        TextActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.text_activity);

        Bitmap bitmap = getIntent().getParcelableExtra(IMAGE_NAME);
        binding.testImgView.setImageBitmap(bitmap);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }
}
