package com.wl.dudian.app.repository;

import com.wl.dudian.app.model.StartImage;
import com.wl.dudian.app.viewmodel.StartImageVM;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.schedulers.Timestamped;

/**
 * Created by Qiushui on 16/8/3.
 */

public class DomainService {

    private final DiskRepository diskRepository;
    private final NetWorkRepository netWorkRepository;

    public DomainService() {
        diskRepository = new DiskRepository();
        netWorkRepository = new NetWorkRepository();
    }

    public Observable<Timestamped<StartImageVM>> getStartImage(ITimestampedView timestampedView) {
        return getMergedStartImage()
                .onErrorReturn(new Func1<Throwable, Timestamped<StartImage>>() {
                    @Override
                    public Timestamped<StartImage> call(Throwable throwable) {
                        return null;
                    }
                })
                .filter(getStartImageFilter(timestampedView))
                .map(StartImageMapping.instance());
    }

    private Func1<? super Timestamped<StartImage>, Boolean> getStartImageFilter(final ITimestampedView timestampedView) {
        return new Func1<Timestamped<StartImage>, Boolean>() {
            @Override
            public Boolean call(Timestamped<StartImage> startImageTimestamped) {
                return startImageTimestamped != null
                        && startImageTimestamped.getValue() != null
                        && startImageTimestamped.getTimestampMillis() > timestampedView.getViewDataTimestampMillis();
            }
        };
    }

    private Observable<Timestamped<StartImage>> getMergedStartImage() {
        return Observable.merge(
                diskRepository.getStartImage().subscribeOn(Schedulers.io()),
                netWorkRepository.getStartImage().timestamp().doOnNext(new Action1<Timestamped<StartImage>>() {
                    @Override
                    public void call(Timestamped<StartImage> startImageTimestamped) {
                        diskRepository.saveStartImage(startImageTimestamped);
                    }
                })
                        .subscribeOn(Schedulers.io())
        );
    }
}
