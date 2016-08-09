package com.wl.dudian.app.repository;

import android.content.Context;
import android.graphics.Bitmap;

import com.wl.dudian.app.model.NewsDetails;
import com.wl.dudian.app.model.StartImage;

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

    private final DiskRepository diskRepository;
    private final NetWorkRepository netWorkRepository;

    private DomainService(Context context) {
        diskRepository = new DiskRepository(context);
        netWorkRepository = new NetWorkRepository();
    }

    public static DomainService getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DomainService(context);
        }
        return INSTANCE;
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
    public Observable<NewsDetails> getNewsDetailFromDb(final String newsId) {
        final NewsDetails newsDetails = diskRepository.getNewsDetail(newsId);
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
        return Observable.create(new Observable.OnSubscribe<NewsDetails>() {
            @Override
            public void call(Subscriber<? super NewsDetails> subscriber) {
                netWorkRepository.getNewsDetails(newsId);
            }
        }).doOnNext(new Action1<NewsDetails>() {
            @Override
            public void call(NewsDetails newsDetails) {
                diskRepository.saveNewsDetail(newsDetails);
            }
        }).onErrorReturn(new Func1<Throwable, NewsDetails>() {
            @Override
            public NewsDetails call(Throwable throwable) {
                return null;
            }
        }).observeOn(Schedulers.io());
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
                        diskRepository.saveStartImage(startImage);
                    }
                });
    }

    /**
     * 将点赞的新闻内容在数据库中更新标记
     * @param newsDetails
     */
    public void saveToFavoriteDb(NewsDetails newsDetails) {
        // TODO 保存到数据库
    }
}
