package com.wl.dudian.app.repository;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.wl.dudian.app.model.StartImage;

import java.util.concurrent.Callable;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.schedulers.Timestamped;

/**
 * Created by Qiushui on 16/8/3.
 */

public class DiskRepository {

    /**
     * Save StartImage, Delete the origin if exist.
     *
     * @param startImage startimage
     */
    public void saveStartImage(final Timestamped<StartImage> startImage) {
        long timestamp = startImage.getTimestampMillis();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final RealmResults<StartImage> results = realm.where(StartImage.class).findAll();
        // delete the origin
        if (results.size() > 0) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    results.deleteFirstFromRealm();
                }
            });
        }
        // save
        StartImage image = realm.copyToRealm(startImage.getValue());
        image.setTime(timestamp);
        realm.commitTransaction();
    }

    @RxLogObservable
    public Observable<Timestamped<StartImage>> getStartImage() {
        return Observable.fromCallable(new Callable<Timestamped<StartImage>>() {
            @Override
            public Timestamped<StartImage> call() throws Exception {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<StartImage> results = realm.where(StartImage.class).findAll();
                if (results.size() > 0) {
                    StartImage startImage = new StartImage();
                    startImage.setImg(results.get(0).getImg());
                    startImage.setText(results.get(0).getText());
                    startImage.setTime(results.get(0).getTime());
                    return new Timestamped<>(results.get(0).getTime(), startImage);
                }
                return null;
            }
        });
    }
}
