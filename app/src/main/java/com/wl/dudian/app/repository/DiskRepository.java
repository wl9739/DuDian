package com.wl.dudian.app.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.wl.dudian.R;
import com.wl.dudian.app.db.BeforeNewsDB;
import com.wl.dudian.app.db.LatestNewsDB;
import com.wl.dudian.app.db.NewsDetailDB;
import com.wl.dudian.app.db.StoriesBeanDB;
import com.wl.dudian.app.db.model.StartImage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.Callable;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;

/**
 * Created by Qiushui on 16/8/3.
 */

public class DiskRepository {

    /**
     * 启动图片文件路径
     */
    private static final String SPLASH_IMAGE_FILEPATH = "start.jpg";
    private Context context;
    /**
     * 保存图片的文件
     */
    private File mImageFile;

    DiskRepository(Context context) {
        this.context = context;
    }

    /**
     * Save StartImage, Delete the origin if exist.
     *
     * @param startImage startimage
     */
    void saveStartImage(final StartImage startImage) {
        Glide.with(context)
                .load(startImage.getCreatives().get(0).getUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                          @Override
                          public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                              saveAsFile(resource);
                          }
                      }

                );
    }

    /**
     * 获取启动页图片
     *
     * @return
     */
    @RxLogObservable
    Observable<Bitmap> getStartImage() {
        return Observable.fromCallable(new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {
                File dir = context.getFilesDir();
                mImageFile = new File(dir, SPLASH_IMAGE_FILEPATH);
                if (mImageFile.exists()) {
                    return BitmapFactory.decodeFile(mImageFile.getAbsolutePath());
                } else {
                    return BitmapFactory.decodeResource(context.getResources(), R.drawable.dayu);
                }
            }
        });
    }

    /**
     * 获取新闻详情
     *
     * @param newsId 新闻详情ID
     * @return
     */
    NewsDetailDB getNewsDetail(String newsId) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NewsDetailDB> results;
        results = realm.where(NewsDetailDB.class).equalTo("id", Integer.parseInt(newsId)).findAll();
        if (results.size() < 1) {
            return null;
        }

        return results.first();
    }

    /**
     * 将下载的新闻详情保存到数据库中
     */
    void saveNewsDetail(final NewsDetailDB newsDetailDB) {
        Realm realm;
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(newsDetailDB);
            }
        });
    }

    Observable<LatestNewsDB> getLatestNewsFromDB() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LatestNewsDB> result;
        result = realm.where(LatestNewsDB.class).findAll();
        return result.first().asObservable();
    }

    void saveLatestNews(final LatestNewsDB latestNewsDB) {
        Realm realm;
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(latestNewsDB);
            }
        });
    }

    /**
     * 保存往日新闻
     */
    void saveBeforeNews(final BeforeNewsDB beforeNewsDB) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<BeforeNewsDB> results =
                            realm.where(BeforeNewsDB.class).equalTo("date", beforeNewsDB.getDate()).findAll();
                    // 如果已经保存了, 则不再重复保存
                    if (results.size() > 0) {
                        return;
                    }
                    realm.copyToRealmOrUpdate(beforeNewsDB);
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * 获取本地保存的往日新闻。
     *
     * @param date 新闻日期
     * @return
     */
    BeforeNewsDB getBeforeNews(String date) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<BeforeNewsDB> results = realm.where(BeforeNewsDB.class)
                    .equalTo("date", date).findAll();
            if (results.size() < 1) {
                return null;
            }
            return results.first();
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    void updateRead(final int id) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<StoriesBeanDB> query = realm.where(StoriesBeanDB.class).equalTo("id", id).findAll();
                    if (query.size() < 1) {
                        return;
                    }
                    query.get(0).setRead(true);
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * 保存收藏的新闻
     *
     * @param newsDetails
     */
    void saveFavorite(final NewsDetailDB newsDetails) {
        Realm realm;
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<StoriesBeanDB> query =
                        realm.where(StoriesBeanDB.class).equalTo("id", newsDetails.getId()).findAll();
                if (query.size() < 0) {
                    return;
                }
                query.get(0).setFavorite(true);
            }
        });

    }

    /**
     * 获取收藏的新闻
     *
     * @return
     */
    List<StoriesBeanDB> getFavoriteNews() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<StoriesBeanDB> results = realm.where(StoriesBeanDB.class).equalTo("isFavorite", true).findAll();
            results = results.sort("id", Sort.DESCENDING);
            if (results.size() < 1) {
                return null;
            }
            return results;
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * 将下载的图片保存到文件里
     *
     * @param bitmap 下载的图片Bitmap
     */
    private void saveAsFile(Bitmap bitmap) {
        try {
            if (mImageFile.exists()) {
                mImageFile.delete();
            }
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(mImageFile));
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
