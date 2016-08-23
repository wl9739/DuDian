package com.wl.dudian.app.latestnews;

import android.content.Context;

import com.wl.dudian.app.model.BeforeNews;
import com.wl.dudian.app.model.LatestNews;
import com.wl.dudian.app.model.StoriesBean;
import com.wl.dudian.app.repository.DomainService;
import com.wl.dudian.app.repository.ITimestampedView;
import com.wl.dudian.framework.DateUtil;

import java.util.ArrayList;
import java.util.List;

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
    private String currentData;

    private List<StoriesBean> storiesBeanList = new ArrayList<>();

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
                currentData = latestNewsTimestamped.getValue().getDate();

                storiesBeanList.clear();
                storiesBeanList.addAll(latestNewsTimestamped.getValue().getStories());

                view.showHeaderView(latestNewsTimestamped.getValue().getTop_stories());
                view.showLatestNews(storiesBeanList, latestNewsTimestamped.getTimestampMillis());
            }
        });
    }

    @Override
    public void loadMoreNews() {
        Observable<BeforeNews> beforeNewsObservable = Observable.concat(
                domainService.getBeforeNewsFromDB(DateUtil.getLastDay(currentData)),
                domainService.getBeforeNewsFromNet(currentData))
                .first()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        beforeNewsSubscription = beforeNewsObservable.subscribe(new Action1<BeforeNews>() {
            @Override
            public void call(BeforeNews beforeNews) {
                if (beforeNews == null) {
                    return;
                }
                currentData = beforeNews.getDate();
                storiesBeanList.addAll(beforeNews.getStories());
                view.loadBeforNews(storiesBeanList);
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
