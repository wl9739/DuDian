package com.wl.dudian.app.latestnews;

import android.content.Context;
import android.util.Log;

import com.wl.dudian.app.model.BeforeNews;
import com.wl.dudian.app.model.LatestNews;
import com.wl.dudian.app.model.StoriesBean;
import com.wl.dudian.app.repository.DomainService;
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

/**
 * @author Qiushui on 16/8/16.
 */

class LatestNewsPresenter implements LatestNewsContract.Presenter {

    private static final String TAG = "LatestNews";
    private LatestNewsContract.View view;
    private DomainService mDomainService;
    private Subscription mBeforeNewsSubscription;
    private String mCurrentData;

    private List<StoriesBean> mStoriesBeen = new ArrayList<>();

    LatestNewsPresenter(Context context, LatestNewsContract.View view) {
        this.view = view;
        mDomainService = DomainService.getInstance(context);
    }

    @Override
    public void loadLatestNews() {
        mDomainService.getLatestNewsFromDB()
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
                                 mCurrentData = latestNews.getDate();
                                 view.stopRefresh();
                                 mStoriesBeen.clear();
                                 mStoriesBeen.addAll(latestNews.getStories());
                                 view.showLatestNews(mStoriesBeen);
                                 view.showHeaderView(latestNews.getTop_stories());
                             }
                         }
                     });

    }

    @Override
    public void loadMoreNews() {
        Observable<BeforeNews> beforeNewsObservable = Observable.concat(
                mDomainService.getBeforeNewsFromDB(DateUtil.getLastDay(mCurrentData)),
                mDomainService.getBeforeNewsFromNet(mCurrentData))
                                                                .first()
                                                                .subscribeOn(Schedulers.io())
                                                                .observeOn(AndroidSchedulers.mainThread());
        mBeforeNewsSubscription = beforeNewsObservable.subscribe(new Action1<BeforeNews>() {
            @Override
            public void call(BeforeNews beforeNews) {
                if (beforeNews == null) {
                    return;
                }
                mCurrentData = beforeNews.getDate();
                mStoriesBeen.addAll(beforeNews.getStories());
                view.loadBeforNews(mStoriesBeen, mCurrentData);
            }
        });
    }

    @Override
    public void updateRead(StoriesBean storiesBean) {
        mDomainService.updateRead(storiesBean.getId());
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        BusinessUtil.unsubscribe(mBeforeNewsSubscription);
    }
}
