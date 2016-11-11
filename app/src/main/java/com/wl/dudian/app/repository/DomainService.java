package com.wl.dudian.app.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.wl.dudian.app.model.BeforeNews;
import com.wl.dudian.app.model.DiscussDataModel;
import com.wl.dudian.app.model.DiscussExtraModel;
import com.wl.dudian.app.model.LatestNews;
import com.wl.dudian.app.model.NewsDetails;
import com.wl.dudian.app.model.StartImage;
import com.wl.dudian.app.model.StoriesBean;
import com.wl.dudian.app.model.ThemeDetailModel;
import com.wl.dudian.app.model.ThemesModel;

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
    private final DiskRepository mDiskRepository;
    private final NetWorkRepository mNetWorkRepository;

    public static DomainService getInstance(Context context) {
        if (INSTANCE == null) {
            // 使用context.getApplicationContext(), 防止内存溢出, 以及activity的context为null的bug
            INSTANCE = new DomainService(context.getApplicationContext());
        }
        return INSTANCE;
    }

    private DomainService(Context context) {
        mDiskRepository = new DiskRepository(context);
        mNetWorkRepository = new NetWorkRepository();
    }

    /**
     * 从本地文件中获取起始页图片, 并从网络下载图片并更新本地文件
     *
     * @return 图片对象
     */
    public Observable<Bitmap> getStartImage() {
        getAndSaveImageFromNet();
        return mDiskRepository.getStartImage();
    }

    /**
     * 从本地数据库中获取新闻详情
     *
     * @param newsId 新闻ID
     * @return
     */
    public Observable<NewsDetails> getNewsDetailFromDb(final String newsId) {
        final NewsDetails newsDetails = mDiskRepository.getNewsDetail(newsId);
        return Observable.create(new Observable.OnSubscribe<NewsDetails>() {
            @Override
            public void call(Subscriber<? super NewsDetails> subscriber) {
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
    public Observable<NewsDetails> getNewsDetailsFromNet(final String newsId) {
        return mNetWorkRepository.getNewsDetails(newsId)
                .doOnNext(new Action1<NewsDetails>() {
                    @Override
                    public void call(NewsDetails newsDetails) {
                        mDiskRepository.saveNewsDetail(newsDetails);
                    }
                }).onErrorReturn(new Func1<Throwable, NewsDetails>() {
                    @Override
                    public NewsDetails call(Throwable throwable) {
                        return null;
                    }
                });
    }

    /**
     * 将点赞的新闻内容在数据库中更新标记
     *
     * @param newsDetails
     */
    public void saveToFavoriteDb(NewsDetails newsDetails) {
        mDiskRepository.saveFavorite(newsDetails);
    }

    /**
     * 从网络下载往日新闻
     *
     * @param date
     * @return
     */
    public Observable<BeforeNews> getBeforeNewsFromNet(String date) {

        return mNetWorkRepository.getBeforeNews(date).doOnNext(new Action1<BeforeNews>() {
            @Override
            public void call(BeforeNews beforeNews) {
                mDiskRepository.saveBeforeNews(beforeNews);
            }
        }).onErrorReturn(new Func1<Throwable, BeforeNews>() {
            @Override
            public BeforeNews call(Throwable throwable) {
                return null;
            }
        });
    }

    /**
     * @param date
     * @return
     */
    public Observable<BeforeNews> getBeforeNewsFromDB(final String date) {
        final BeforeNews beforeNews = mDiskRepository.getBeforeNews(date);
        return Observable.create(new Observable.OnSubscribe<BeforeNews>() {
            @Override
            public void call(Subscriber<? super BeforeNews> subscriber) {
                if (beforeNews != null) {
                    subscriber.onNext(beforeNews);
                } else {
                    subscriber.onCompleted();
                }
            }
        });
    }

    public Observable<LatestNews> getLatestNews() {
        return mNetWorkRepository.getLatestNews()
                .onErrorReturn(new Func1<Throwable, LatestNews>() {
                    @Override
                    public LatestNews call(Throwable throwable) {
                        Log.d(TAG, "call: " + throwable.getMessage());
                        return null;
                    }
                });
    }

    public void updateRead(int id) {
        mDiskRepository.updateRead(id);
    }

    public Observable<LatestNews> getLatestNewsFromDB() {
        return mDiskRepository.getLatestNewsFromDB()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ThemesModel> getTheme() {
        return mNetWorkRepository.getThemesModel()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ThemeDetailModel> getThemeDetail(String id) {
        return mNetWorkRepository.getThemeDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public List<StoriesBean> getFavoriteNews() {
        return mDiskRepository.getFavoriteNews();
    }

    public void saveLatestNews(LatestNews latestNews) {
        mDiskRepository.saveLatestNews(latestNews);
    }

    public Observable<DiscussDataModel> getDiscussLong(String id) {
        return mNetWorkRepository.getDiscussLong(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<DiscussDataModel> getDiscussShort(String id) {
        return mNetWorkRepository.getDiscussShort(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<DiscussExtraModel> getDiscussExtra(String id) {
        return mNetWorkRepository.getdiscussExtra(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 下载并保存图片
     */
    private void getAndSaveImageFromNet() {
        mNetWorkRepository.getStartImage()
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
                        mDiskRepository.saveStartImage(startImage);
                    }
                });
    }

    public boolean isFavorite(int id) {
        return mDiskRepository.checkIsFavorite(id);
    }
}
