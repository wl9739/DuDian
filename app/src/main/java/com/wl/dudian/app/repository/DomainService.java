package com.wl.dudian.app.repository;

import android.content.Context;
import android.graphics.Bitmap;

import com.wl.dudian.app.model.StartImage;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Qiushui on 16/8/3.
 */

public class DomainService {

    private final DiskRepository diskRepository;
    private final NetWorkRepository netWorkRepository;

    public DomainService(Context context) {
        diskRepository = new DiskRepository(context);
        netWorkRepository = new NetWorkRepository();
    }

    /**
     * 从本地文件中获取起始页图片, 并从网络下载图片并更新本地文件
     *
     * @return  图片对象
     */
    public Observable<Bitmap> getStartImage() {
        //
        getAndSaveImageFromNet();
        return diskRepository.getStartImage();
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
}
