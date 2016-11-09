package com.wl.dudian.app.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.wl.dudian.app.db.BeforeNewsDB;
import com.wl.dudian.app.db.LatestNewsDB;
import com.wl.dudian.app.db.NewsDetailDB;
import com.wl.dudian.app.db.StoriesBeanDB;
import com.wl.dudian.app.db.model.DiscussDataModel;
import com.wl.dudian.app.db.model.DiscussExtraModel;
import com.wl.dudian.app.db.model.StartImage;
import com.wl.dudian.app.db.model.ThemeDetailModel;
import com.wl.dudian.app.db.model.ThemesModel;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Qiushui on 16/8/3.
 */

public class DomainService {

    private static DomainService INSTANCE = null;
    private final String TAG = "Domainservice";
    private final DiskRepository diskRepository;
    private final NetWorkRepository netWorkRepository;

    public static DomainService getInstance(Context context) {
        if (INSTANCE == null) {
            // 使用context.getApplicationContext(), 防止内存溢出, 以及activity的context为null的bug
            INSTANCE = new DomainService(context.getApplicationContext());
        }
        return INSTANCE;
    }

    private DomainService(Context context) {
        diskRepository = new DiskRepository(context);
        netWorkRepository = new NetWorkRepository();
    }

    /**
     * 从本地文件中获取起始页图片, 并从网络下载图片并更新本地文件
     *
     * @return 图片对象
     */
    public Observable<Bitmap> getStartImage() {
        getAndSaveImageFromNet();
        return diskRepository.getStartImage();
    }

    /**
     * 从本地数据库中获取新闻详情
     *
     * @param newsId 新闻ID
     * @return
     */
    public Observable<NewsDetailDB> getNewsDetailFromDb(final String newsId) {
        final NewsDetailDB newsDetails = diskRepository.getNewsDetail(newsId);
        return Observable.create(new Observable.OnSubscribe<NewsDetailDB>() {
            @Override
            public void call(Subscriber<? super NewsDetailDB> subscriber) {
                if (newsDetails != null) {
                    subscriber.onNext(newsDetails);
                } else {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 从网络下载新闻详情, 并保存到数据库中
     *
     * @param newsId 新闻ID
     * @return
     */
    public Observable<NewsDetailDB> getNewsDetailsFromNet(final String newsId) {
        return netWorkRepository.getNewsDetails(newsId)
                .doOnNext(new Action1<NewsDetailDB>() {
                    @Override
                    public void call(NewsDetailDB newsDetailDB) {
                        diskRepository.saveNewsDetail(newsDetailDB);
                    }
                }).onErrorReturn(new Func1<Throwable, NewsDetailDB>() {
                    @Override
                    public NewsDetailDB call(Throwable throwable) {
                        return null;
                    }
                });
    }

    /**
     * 将点赞的新闻内容在数据库中更新标记
     *
     * @param newsDetails
     */
    public void saveToFavoriteDb(NewsDetailDB newsDetails) {
        diskRepository.saveFavorite(newsDetails);
    }

    /**
     * 从网络下载往日新闻
     *
     * @param date
     * @return
     */
    public Observable<BeforeNewsDB> getBeforeNewsFromNet(String date) {

        return netWorkRepository.getBeforeNews(date).doOnNext(new Action1<BeforeNewsDB>() {
            @Override
            public void call(BeforeNewsDB beforeNews) {
                diskRepository.saveBeforeNews(beforeNews);
            }
        }).onErrorReturn(new Func1<Throwable, BeforeNewsDB>() {
            @Override
            public BeforeNewsDB call(Throwable throwable) {
                return null;
            }
        });
    }

    /**
     * @param date
     * @return
     */
    public Observable<BeforeNewsDB> getBeforeNewsFromDB(final String date) {
        final BeforeNewsDB beforeNews = diskRepository.getBeforeNews(date);
        return Observable.create(new Observable.OnSubscribe<BeforeNewsDB>() {
            @Override
            public void call(Subscriber<? super BeforeNewsDB> subscriber) {
                if (beforeNews != null) {
                    subscriber.onNext(beforeNews);
                } else {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<LatestNewsDB> getLatestNews() {
        return netWorkRepository.getLatestNews()
                .onErrorReturn(new Func1<Throwable, LatestNewsDB>() {
                    @Override
                    public LatestNewsDB call(Throwable throwable) {
                        Log.d(TAG, "call: " + throwable.getMessage());
                        return null;
                    }
                });
    }

    public void updateRead(int id) {
        diskRepository.updateRead(id);
    }

    public Observable<LatestNewsDB> getLatestNewsFromDB() {
        return diskRepository.getLatestNewsFromDB();
    }

    public Observable<ThemesModel> getTheme() {
        return netWorkRepository.getThemesModel()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ThemeDetailModel> getThemeDetail(String id) {
        return netWorkRepository.getThemeDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public List<StoriesBeanDB> getFavoriteNews() {
        return diskRepository.getFavoriteNews();
    }

    public void saveLatestNews(LatestNewsDB latestNewsDB) {
        diskRepository.saveLatestNews(latestNewsDB);
    }

    public Observable<DiscussDataModel> getDiscussLong(String id) {
        return netWorkRepository.getDiscussLong(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<DiscussDataModel> getDiscussShort(String id) {
        return netWorkRepository.getDiscussShort(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<DiscussExtraModel> getDiscussExtra(String id) {
        return netWorkRepository.getdiscussExtra(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 下载并保存图片
     */
    private void getAndSaveImageFromNet() {
        netWorkRepository.getStartImage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Func1<Throwable, StartImage>() {
                    @Override
                    public StartImage call(Throwable throwable) {
                        return null;
                    }
                })
                .filter(new Func1<StartImage, Boolean>() {
                    @Override
                    public Boolean call(StartImage startImage) {
                        return null != startImage;
                    }
                })
                .subscribe(new Action1<StartImage>() {
                    @Override
                    public void call(StartImage startImage) {
                        Log.d("000000", "call: getAndSaveImageFromNet() " + Thread.currentThread());
                        diskRepository.saveStartImage(startImage);
                    }
                });
    }
}
