package com.wl.dudian.app.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.wl.dudian.R;
import com.wl.dudian.app.repository.DomainService;
import com.wl.dudian.databinding.SplashActivityBinding;

import java.util.concurrent.TimeUnit;

import cn.sharesdk.framework.ShareSDK;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * @author Qiushui
 * @since 0.0.2
 */
public class SplashActivity extends BaseActivity {

    private DomainService domainService;
    private Subscription startImageSubscription;
    private Subscription launchSubscription;
    private SplashActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // init ShareSDK
        ShareSDK.initSDK(this);
        // init databinding
        binding = DataBindingUtil.setContentView(this, R.layout.splash_activity);
        // init service
        domainService = new DomainService(this);
        // require startImage.
        fetchStartImage();
    }

    private void fetchStartImage() {
        unsubscribe();
        Observable<Bitmap> getRecentStartImage = domainService
                .getStartImage()
                .observeOn(AndroidSchedulers.mainThread());
        startImageSubscription = getRecentStartImage.subscribe(startImageOnNext);
    }

    private final Action1<Bitmap> startImageOnNext = new Action1<Bitmap>() {
        @Override
        public void call(Bitmap bitmap) {
            binding.image.setImageBitmap(bitmap);
            launchSubscription = Observable.timer(2, TimeUnit.SECONDS)
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }
                    });
        }
    };

    private void unsubscribe() {
        if (startImageSubscription != null) {
            startImageSubscription.unsubscribe();
            startImageSubscription = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (launchSubscription != null) {
            launchSubscription.unsubscribe();
            launchSubscription = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // stop ShareSDK
        ShareSDK.stopSDK(this);
    }
}
