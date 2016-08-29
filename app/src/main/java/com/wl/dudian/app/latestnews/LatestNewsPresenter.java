package com.wl.dudian.app.latestnews;

import android.content.Context;
import android.util.Log;

import com.wl.dudian.app.model.BeforeNews;
import com.wl.dudian.app.model.LatestNews;
import com.wl.dudian.app.model.StoriesBean;
import com.wl.dudian.app.repository.DomainService;
import com.wl.dudian.app.repository.ITimestampedView;
import com.wl.dudian.framework.BusinessUtil;
import com.wl.dudian.framework.DateUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.schedulers.Timestamped;

/**
 * @author Qiushui on 16/8/16.
 */

public class LatestNewsPresenter implements LatestNewsContract.Presenter {

    private static final String TAG = "LatestNews";
    private LatestNewsContract.View view;
    private ITimestampedView timestampedView;
    private DomainService domainService;
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
        domainService.getLatestNewsFromDB()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Func1<Throwable, LatestNews>() {
                    @Override
                    public LatestNews call(Throwable throwable) {
                        Log.d(TAG, "call: " + throwable.getMessage());
                        return null;
                    }
                })
                .subscribe(new Action1<LatestNews>() {
                    @Override
                    public void call(LatestNews latestNews) {
                        if (latestNews != null) {
                            currentData = latestNews.getDate();
                            view.stopRefresh();
                            storiesBeanList.clear();
                            storiesBeanList.addAll(latestNews.getStories());
                            view.showLatestNews(storiesBeanList);
                            view.showHeaderView(latestNews.getTop_stories());
                        }
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
                view.loadBeforNews(storiesBeanList, currentData);
            }
        });
    }

    @Override
    public void updateRead(StoriesBean storiesBean) {
        domainService.updateRead(storiesBean.getId());
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        BusinessUtil.unsubscribe(beforeNewsSubscription);
    }
}
