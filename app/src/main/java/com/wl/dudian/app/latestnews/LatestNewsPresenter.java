package com.wl.dudian.app.latestnews;

import android.content.Context;

import com.wl.dudian.app.db.BeforeNewsDB;
import com.wl.dudian.app.db.LatestNewsDB;
import com.wl.dudian.app.db.StoriesBeanDB;
import com.wl.dudian.app.repository.DomainService;
import com.wl.dudian.framework.BusinessUtil;
import com.wl.dudian.framework.DateUtil;

import io.realm.Realm;
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

    private LatestNewsContract.View view;
    private DomainService domainService;
    private Subscription beforeNewsSubscription;
    private String currentData;

    LatestNewsPresenter(Context context, LatestNewsContract.View view) {
        this.view = view;
        domainService = DomainService.getInstance(context);
    }

    @Override
    public void loadLatestNews() {
        domainService.getLatestNewsFromDB()
                .onErrorReturn(new Func1<Throwable, LatestNewsDB>() {
                    @Override
                    public LatestNewsDB call(Throwable throwable) {
                        return null;
                    }
                })
                .subscribe(new Action1<LatestNewsDB>() {
                    @Override
                    public void call(final LatestNewsDB latestNewsDB) {
//                        if (latestNewsDB != null) {
//                            Realm realm = null;
//                            realm.executeTransaction(new Realm.Transaction() {
//                                @Override
//                                public void execute(Realm realm) {
//                                    realm.copyToRealmOrUpdate(latestNewsDB);
//                                }
//                            });
//                            currentData = latestNewsDB.getDate();
//                            view.stopRefresh();
//                            view.showLatestNews();
//                            view.showHeaderView();
//                        }
                    }
                });

    }

    @Override
    public void loadMoreNews() {
        Observable<BeforeNewsDB> beforeNewsObservable = Observable.concat(
                domainService.getBeforeNewsFromDB(DateUtil.getLastDay(currentData)),
                domainService.getBeforeNewsFromNet(currentData))
                .first()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        beforeNewsSubscription = beforeNewsObservable.subscribe(new Action1<BeforeNewsDB>() {
            @Override
            public void call(BeforeNewsDB beforeNewsDB) {
                if (beforeNewsDB == null) {
                    return;
                }
                currentData = beforeNewsDB.getDate();
                view.loadBeforNews(beforeNewsDB.getStories(), currentData);
            }
        });
    }

    @Override
    public void updateRead(StoriesBeanDB storiesBean) {
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
