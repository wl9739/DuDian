package com.wl.dudian.app.splash;

import android.content.Context;
import android.graphics.Bitmap;

import com.wl.dudian.app.repository.DomainService;
import com.wl.dudian.framework.BusinessUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Listens to user actions from the UI , retrieves the data and updates
 * the UI as required.
 *
 * Created by Qiushui on 16/8/8.
 */

public class SplashPresenter implements SplashContract.Presenter {

    private final SplashContract.View splashView;

    private final DomainService domainService;

    private Subscription startImageSubscription;
    private Subscription launchSubscription;

    public SplashPresenter(Context context, SplashContract.View splashView) {
        domainService = DomainService.getInstance(context);
        this.splashView = splashView;

        splashView.setPresenter(this);
    }

    @Override
    public void getStartImage() {
        unsubscribe();
        Observable<Bitmap> getRecentStartImage = domainService
                .getStartImage()
                .observeOn(AndroidSchedulers.mainThread());
        startImageSubscription = getRecentStartImage.subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                splashView.showStartImage(bitmap);
            }
        });
    }

    @Override
    public void unLanchSubscription() {
        BusinessUtil.unsubscribe(launchSubscription);
    }

    @Override
    public void subscribe() {
        launchSubscription = Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        splashView.startActivity();
                    }
                });
    }

    @Override
    public void unsubscribe() {
        unLanchSubscription();
        BusinessUtil.unsubscribe(startImageSubscription);
    }
}
