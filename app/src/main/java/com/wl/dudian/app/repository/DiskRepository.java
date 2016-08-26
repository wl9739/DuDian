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
import com.wl.dudian.app.db.mapper.Mapper;
import com.wl.dudian.app.model.BeforeNews;
import com.wl.dudian.app.model.LatestNews;
import com.wl.dudian.app.model.NewsDetails;
import com.wl.dudian.app.model.StartImage;
import com.wl.dudian.app.model.StoriesBean;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.schedulers.Timestamped;

/**
 * Created by Qiushui on 16/8/3.
 */

public class DiskRepository {

    private Context context;
    /**
     * 启动图片文件路径
     */
    private static final String SPLASH_IMAGE_FILEPATH = "start.jpg";

    /**
     * 保存图片的文件
     */
    private File mImageFile;

    public DiskRepository(Context context) {
        this.context = context;
    }

    /**
     * Save StartImage, Delete the origin if exist.
     *
     * @param startImage startimage
     */
    public void saveStartImage(final StartImage startImage) {
        Glide.with(context)
                .load(startImage.getCreatives().get(0).getUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                          @Override
                          public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                              Schedulers.io().createWorker().schedule(new Action0() {
                                  @Override
                                  public void call() {
                                      saveAsFile(resource);
                                  }
                              });
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
    public Observable<Bitmap> getStartImage() {
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

    /**
     * 获取新闻详情
     *
     * @param newsId 新闻详情ID
     * @return
     */
    public NewsDetails getNewsDetail(String newsId) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NewsDetailDB> query;
        NewsDetails newsDetails = null;
        try {
            query = realm.where(NewsDetailDB.class).equalTo("id", Integer.parseInt(newsId)).findAll();
            if (query.size() < 1) {
                return null;
            }
            newsDetails = new NewsDetails();
            newsDetails.setTitle(query.get(0).getTitle());
            newsDetails.setType(query.get(0).getType());
            newsDetails.setId(query.get(0).getId());
            newsDetails.setImage(query.get(0).getImage());
            newsDetails.setBody(query.get(0).getBody());
            newsDetails.setShare_url(query.get(0).getShare_url());
            newsDetails.setImage_source(query.get(0).getImage_source());
            newsDetails.setGa_prefix(query.get(0).getGa_prefix());
        } finally {
            if (realm != null) {
                realm.close();
            }
        }

        return newsDetails;
    }


    /**
     * 将下载的新闻详情保存到数据库中
     *
     * @param newsDetails
     */
    public void saveNewsDetail(final NewsDetails newsDetails) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    NewsDetailDB newsDetailDB = realm.createObject(NewsDetailDB.class);
                    newsDetailDB.setImage(newsDetails.getImage());
                    newsDetailDB.setGa_prefix(newsDetails.getGa_prefix());
                    newsDetailDB.setTitle(newsDetails.getTitle());
                    newsDetailDB.setBody(newsDetails.getBody());
                    newsDetailDB.setImage_source(newsDetails.getImage_source());
                    newsDetailDB.setShare_url(newsDetails.getShare_url());
                    newsDetailDB.setId(newsDetails.getId());
                    newsDetailDB.setType(newsDetails.getType());
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * 获取在本地保存的最新新闻
     *
     * @return
     */
    public Observable<Timestamped<LatestNews>> getLatestNews() {
        return Observable.fromCallable(new Callable<Timestamped<LatestNews>>() {
            @Override
            public Timestamped<LatestNews> call() throws Exception {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<LatestNewsDB> result;
                LatestNews latestNews;
                try {
                    result = realm.where(LatestNewsDB.class).findAll();
                    if (result.size() < 1) {
                        return null;
                    }
                    latestNews = Mapper.getLatestNews(result);
                    return new Timestamped<>(result.get(0).getTime(), latestNews);
                } finally {
                    if (realm != null) {
                        realm.close();
                    }
                }
            }
        });
    }

    /**
     * 将下载的新闻标题内容保存到数据库中
     *
     * @param latestNews
     */
    public void saveLatestNews(final Timestamped<LatestNews> latestNews) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    // save
                    Mapper.saveLatestNewsDB(realm, latestNews);
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * 保存往日新闻
     *
     * @param beforeNews
     */
    public void saveBeforeNews(final BeforeNews beforeNews) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<BeforeNewsDB> query = realm.where(BeforeNewsDB.class).equalTo("date", beforeNews.getDate()).findAll();
                    // 如果已经保存了, 则不再重复保存
                    if (query.size() > 0) {
                        return;
                    }
                    // 如果之前没有保存,则保存到数据库
                    BeforeNewsDB beforeNewsDB = realm.createObject(BeforeNewsDB.class);
                    beforeNewsDB.setDate(beforeNews.getDate());
                    RealmList<StoriesBeanDB> storiesBeanRealmList = new RealmList<>();
                    for (int i = 0; i < beforeNews.getStories().size(); i++) {
                        StoriesBeanDB storiesBeanDB = realm.createObject(StoriesBeanDB.class);
                        storiesBeanDB.setId(beforeNews.getStories().get(i).getId());
                        storiesBeanDB.setTitle(beforeNews.getStories().get(i).getTitle());
                        storiesBeanDB.setGa_prefix(beforeNews.getStories().get(i).getGa_prefix());
                        storiesBeanDB.setType(beforeNews.getStories().get(i).getType());
                        storiesBeanDB.setImages(beforeNews.getStories().get(i).getImages().get(0));
                        storiesBeanRealmList.add(storiesBeanDB);
                    }
                    beforeNewsDB.setStories(storiesBeanRealmList);
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
    public BeforeNews getBeforeNews(String date) {
        Realm realm = null;
        BeforeNews beforeNews;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<BeforeNewsDB> query = realm.where(BeforeNewsDB.class)
                    .equalTo("date", date).findAll();
            if (query.size() < 1) {
                return null;
            }
            beforeNews = new BeforeNews();
            beforeNews.setDate(query.get(0).getDate());
            List<StoriesBean> storiesBeenList = new ArrayList<>();
            for (int i = 0; i < query.get(0).getStories().size(); i++) {
                StoriesBean storiesBean = new StoriesBean();
                storiesBean.setType(query.get(0).getStories().get(i).getType());
                storiesBean.setGa_prefix(query.get(0).getStories().get(i).getGa_prefix());
                storiesBean.setImages(Arrays.asList(query.get(0).getStories().get(i).getImages()));
                storiesBean.setId(query.get(0).getStories().get(i).getId());
                storiesBean.setTitle(query.get(0).getStories().get(i).getTitle());
                storiesBean.setRead(query.get(0).getStories().get(i).isRead());
                storiesBeenList.add(storiesBean);
            }
            beforeNews.setStories(storiesBeenList);
            return beforeNews;
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    public void updateRead(final int id) {
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
    public void saveFavorite(final NewsDetails newsDetails) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<StoriesBeanDB> query = realm.where(StoriesBeanDB.class).equalTo("id", newsDetails.getId()).findAll();
                    if (query.size() < 0) {
                        return;
                    }
                    query.get(0).setFavorite(true);
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    /**
     * 获取收藏的新闻
     * @return
     */
    public List<StoriesBean> getFavoriteNews() {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<StoriesBeanDB> query = realm.where(StoriesBeanDB.class).equalTo("isFavorite", true).findAll();
            if (query.size() < 1) {
                return null;
            }
            return Mapper.getStoriesBean(query);
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }
}
