package com.wl.dudian.app.splash;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.wl.dudian.R;
import com.wl.dudian.app.ui.activity.BaseActivity;
import com.wl.dudian.app.ui.activity.MainActivity;
import com.wl.dudian.databinding.SplashActivityBinding;
import com.wl.dudian.framework.util.BusinessUtil;

/**
 * @author Qiushui
 * @since 0.0.2
 */
public class SplashActivity extends BaseActivity implements SplashContract.View {

    private SplashActivityBinding mBinding;

    private SplashContract.Presenter mPresenter;

    @Override
    public void showStartImage(Bitmap bitmap) {
        mBinding.image.setImageBitmap(bitmap);
    }

    @Override
    public void startActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        this.mPresenter = BusinessUtil.checkNotNull(presenter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPresenter.unLanchSubscription();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // init mPresenter
        new SplashPresenter(this, this);
        // init databinding
        mBinding = DataBindingUtil.setContentView(this, R.layout.splash_activity);
        // require startImage.
        mPresenter.getStartImage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

}
