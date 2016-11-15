package com.wl.dudian.app.splash;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.wl.dudian.framework.db.model.LatestNews;
import com.wl.dudian.framework.repository.DomainService;
import com.wl.dudian.framework.BusinessUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Listens to user actions from the UI , retrieves the data and updates the UI as required.
 * <p>
 * Created by Qiushui on 16/8/8.
 */

public class SplashPresenter implements SplashContract.Presenter {

    public static final String TAG = "SplashPresenter";

    private final SplashContract.View splashView;

    private final DomainService domainService;

    private Subscription startImageSubscription;

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

    }

    @Override
    public void subscribe() {

        domainService.getLatestNews()
                     .delay(2000, TimeUnit.MILLISECONDS)
                     .onErrorReturn(new Func1<Throwable, LatestNews>() {
                         @Override
                         public LatestNews call(Throwable throwable) {
                             return null;
                         }
                     })
                     .doOnNext(new Action1<LatestNews>() {
                         @Override
                         public void call(LatestNews latestNews) {
                             if (latestNews != null) {
                                 domainService.saveLatestNews(latestNews);
                             }
                         }
                     })
                     .subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(new Action1<LatestNews>() {
                         @Override
                         public void call(LatestNews latestNews) {
                             splashView.startActivity();
                         }
                     }, new Action1<Throwable>() {
                         @Override
                         public void call(Throwable throwable) {
                             Log.d(TAG, "call: " + throwable.getMessage());
                         }
                     });
    }

    @Override
    public void unsubscribe() {
        unLanchSubscription();
        BusinessUtil.unsubscribe(startImageSubscription);
    }
}
