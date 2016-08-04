package com.wl.dudian.app.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.wl.dudian.R;
import com.wl.dudian.app.repository.DomainService;
import com.wl.dudian.app.repository.ITimestampedView;
import com.wl.dudian.app.viewmodel.StartImageVM;
import com.wl.dudian.databinding.SplashActivityBinding;

import cn.sharesdk.framework.ShareSDK;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * @author zfeiyu
 * @since 0.0.2
 */
public class SplashActivity extends BaseActivity implements ITimestampedView {

    private static final String TAG = SplashActivity.class.getCanonicalName();
    private DomainService domainService;
    private Subscription startImageSubscription;
    private SplashActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // init ShareSDK
        ShareSDK.initSDK(this);
        binding = DataBindingUtil.setContentView(this, R.layout.splash_activity);


        domainService = new DomainService();
        fetchStartImage();
    }

    private void fetchStartImage() {
        unsubscribe();
        Observable<StartImageVM> getRecentStartImage = domainService
                .getStartImage(this)
                .observeOn(AndroidSchedulers.mainThread());
        startImageSubscription = getRecentStartImage.subscribe(startImageOnNext, startImageOnError, startImageOnComplete);
    }

    private void unsubscribe() {
        if (startImageSubscription != null) {
            startImageSubscription.unsubscribe();
            startImageSubscription = null;
        }
    }

    private final Action1<StartImageVM> startImageOnNext = new Action1<StartImageVM>() {
        @Override
        public void call(StartImageVM startImageVM) {
            binding.setSplash(startImageVM);
        }
    };

    private final Action1<Throwable> startImageOnError = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            Log.e(TAG, "cstartImageOnError.call() - Error", throwable);
            Toast.makeText(SplashActivity.this, "OnError=" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private final Action0 startImageOnComplete = new Action0() {
        @Override
        public void call() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, 2000);
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // stop ShareSDK
        ShareSDK.stopSDK(this);
    }

    @Override
    public long getViewDataTimestampMillis() {
        return 0;
    }
}
