package com.wl.dudian.app.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.ImageView;

import com.wl.dudian.R;


/**
 * Created by yisheng on 16/6/24.
 */

public class TestActivity extends BaseActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.text_activity);

        Bitmap bitmap = getIntent().getParcelableExtra("IMAGE");
        mImageView = (ImageView) findViewById(R.id.test_img_view);
        mImageView.setImageBitmap(bitmap);

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
