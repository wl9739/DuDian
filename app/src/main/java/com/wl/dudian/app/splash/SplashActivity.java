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
import com.wl.dudian.framework.BusinessUtil;

/**
 * @author Qiushui
 * @since 0.0.2
 */
public class SplashActivity extends BaseActivity implements SplashContract.View {

    private SplashActivityBinding binding;

    private SplashContract.Presenter presenter;

    @Override
    public void showStartImage(Bitmap bitmap) {
        binding.image.setImageBitmap(bitmap);
    }

    @Override
    public void startActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        this.presenter = BusinessUtil.checkNotNull(presenter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        presenter.unLanchSubscription();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // init presenter
        new SplashPresenter(this, this);
        // init databinding
        binding = DataBindingUtil.setContentView(this, R.layout.splash_activity);
        // require startImage.
        presenter.getStartImage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

}
