package com.wl.dudian.app.splash;

import android.content.Context;
import android.graphics.Bitmap;

import com.wl.dudian.framework.util.BusinessUtil;
import com.wl.dudian.framework.db.model.LatestNews;
import com.wl.dudian.framework.repository.DomainService;

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

class SplashPresenter implements SplashContract.Presenter {

    private final SplashContract.View splashView;
    private final DomainService domainService;
    private Subscription startImageSubscription;

    SplashPresenter(Context context, SplashContract.View splashView) {
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
                             throwable.printStackTrace();
                         }
                     });
    }

    @Override
    public void unsubscribe() {
        unLanchSubscription();
        BusinessUtil.unsubscribe(startImageSubscription);
    }
}
