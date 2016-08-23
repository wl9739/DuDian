package com.wl.dudian.app.latestnews;

import android.content.Context;

import com.wl.dudian.app.model.BeforeNews;
import com.wl.dudian.app.model.LatestNews;
import com.wl.dudian.app.repository.DomainService;
import com.wl.dudian.app.repository.ITimestampedView;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.schedulers.Timestamped;

/**
 * Created by wanglin on 16/8/16.
 */

public class LatestNewsPresenter implements LatestNewsContract.Presenter {

    private LatestNewsContract.View view;
    private ITimestampedView timestampedView;
    private DomainService domainService;
    private Subscription latestNewsSubscription;
    private Subscription beforeNewsSubscription;
    private String nowDate;

    public LatestNewsPresenter(Context context, LatestNewsContract.View view, ITimestampedView timestampedView) {
        this.view = view;
        this.timestampedView = timestampedView;
        domainService = DomainService.getInstance(context);

    }
    @Override
    public void loadLatestNews() {
        Observable<Timestamped<LatestNews>> latestNewsObservable = domainService.getLatestNews(timestampedView)
                .observeOn(AndroidSchedulers.mainThread());

        latestNewsSubscription = latestNewsObservable.subscribe(new Action1<Timestamped<LatestNews>>() {
            @Override
            public void call(Timestamped<LatestNews> latestNewsTimestamped) {
                nowDate = latestNewsTimestamped.getValue().getDate();
                view.showLatestNews(latestNewsTimestamped.getValue(), latestNewsTimestamped.getTimestampMillis());
            }
        });
    }

    @Override
    public void loadMoreNews() {
        Observable<BeforeNews> beforeNewsObservable = Observable.mergeDelayError(
                domainService.getBeforeNewsFromNet(nowDate),
                domainService.getBeforeNewsFromDB(nowDate))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        beforeNewsSubscription = beforeNewsObservable.subscribe(new Action1<BeforeNews>() {
            @Override
            public void call(BeforeNews beforeNews) {
                view.loadBeforNews(beforeNews);
            }
        });
    }

    @Override
    public void saveLatestNews() {

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
