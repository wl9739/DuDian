package com.wl.dudian.framework.db.mapper;

import com.wl.dudian.framework.db.LatestNewsDB;
import com.wl.dudian.framework.db.StoriesBeanDB;
import com.wl.dudian.framework.db.TopStoriesBeanDB;
import com.wl.dudian.framework.db.model.LatestNews;
import com.wl.dudian.framework.db.model.StoriesBean;
import com.wl.dudian.framework.db.model.TopStoriesBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 *
 * 转换类
 *
 * Created by Qiushui on 16/8/16.
 */

public class Mapper {

    public static LatestNews getLatestNews(RealmResults<LatestNewsDB> query) {
        LatestNews latestNews = new LatestNews();
        latestNews.setDate(query.get(0).getDate());
        List<StoriesBean> storiesBeanList = new ArrayList<>();
        List<TopStoriesBean> topStoriesBeenList = new ArrayList<>();
        RealmResults<StoriesBeanDB> storiesBeanDBs = query.get(0).getStories().sort("ga_prefix", Sort.DESCENDING);
        for (int i = 0; i <storiesBeanDBs.size(); i++) {
            StoriesBeanDB storiesBeanDB = storiesBeanDBs.get(i);
            StoriesBean storiesBean = new StoriesBean();
            storiesBean.setTitle(storiesBeanDB.getTitle());
            storiesBean.setId(storiesBeanDB.getId());
            storiesBean.setImages(Collections.singletonList(storiesBeanDB.getImages()));
            storiesBean.setGa_prefix(storiesBeanDB.getGa_prefix());
            storiesBean.setType(storiesBeanDB.getType());
            storiesBean.setRead(storiesBeanDB.isRead());
            storiesBeanList.add(storiesBean);

        }
        for (int i = 0; i < query.get(0).getTop_stories().size(); i++) {
            TopStoriesBeanDB topStoriesBeanDB = query.get(0).getTop_stories().get(i);
            TopStoriesBean topStoriesBean = new TopStoriesBean();
            topStoriesBean.setType(topStoriesBeanDB.getType());
            topStoriesBean.setGa_prefix(topStoriesBeanDB.getGa_prefix());
            topStoriesBean.setId(topStoriesBeanDB.getId());
            topStoriesBean.setImage(topStoriesBeanDB.getImage());
            topStoriesBean.setTitle(topStoriesBeanDB.getTitle());
            topStoriesBeenList.add(topStoriesBean);
        }
        latestNews.setStories(storiesBeanList);
        latestNews.setTop_stories(topStoriesBeenList);

        return latestNews;
    }

    public static void saveLatestNews(Realm realm, RealmResults<LatestNewsDB> latestNewsDBResults, LatestNews latestNews, RealmResults<TopStoriesBeanDB> topStoriesBeanDBResults) {
        RealmList<StoriesBeanDB> storiesBeanDBRealmList = new RealmList<>();
        RealmList<TopStoriesBeanDB> topStoriesBeanDBRealmList = new RealmList<>();
        if (latestNewsDBResults.size() != 0) {
            int latestStorySize = latestNews.getStories().size();
            int latestStoryDBSize = latestNewsDBResults.get(0).getStories().size();
            for (int i = 0; i < latestStorySize - latestStoryDBSize; i++) {
                StoriesBean storiesBean = latestNews.getStories().get(i);
                StoriesBeanDB storiesBeanDB = realm.createObject(StoriesBeanDB.class);
                storiesBeanDB.setId(storiesBean.getId());
                storiesBeanDB.setFavorite(false);
                storiesBeanDB.setImages(storiesBean.getImages().get(0));
                storiesBeanDB.setRead(false);
                storiesBeanDB.setGa_prefix(storiesBean.getGa_prefix());
                storiesBeanDB.setTitle(storiesBean.getTitle());
                storiesBeanDB.setType(storiesBean.getType());
                storiesBeanDBRealmList.add(storiesBeanDB);
            }

            RealmResults<StoriesBeanDB> storiesBeanDB = realm.where(StoriesBeanDB.class).findAll();
            storiesBeanDBRealmList.addAll(storiesBeanDB.subList(latestStorySize - latestStoryDBSize, latestStorySize));
        } else {
            for (int i = 0; i < latestNews.getStories().size(); i++) {
                StoriesBean storiesBean = latestNews.getStories().get(i);
                StoriesBeanDB storiesBeanDB = realm.createObject(StoriesBeanDB.class);
                storiesBeanDB.setId(storiesBean.getId());
                storiesBeanDB.setFavorite(false);
                storiesBeanDB.setImages(storiesBean.getImages().get(0));
                storiesBeanDB.setRead(false);
                storiesBeanDB.setGa_prefix(storiesBean.getGa_prefix());
                storiesBeanDB.setTitle(storiesBean.getTitle());
                storiesBeanDB.setType(storiesBean.getType());
                storiesBeanDBRealmList.add(storiesBeanDB);
            }
        }

        topStoriesBeanDBResults.deleteAllFromRealm();
        for (int i = 0; i < latestNews.getTop_stories().size(); i++) {
            TopStoriesBean topStoriesBean = latestNews.getTop_stories().get(i);
            TopStoriesBeanDB topStoriesBeanDB = realm.createObject(TopStoriesBeanDB.class);
            topStoriesBeanDB.setType(topStoriesBean.getType());
            topStoriesBeanDB.setTitle(topStoriesBean.getTitle());
            topStoriesBeanDB.setId(topStoriesBean.getId());
            topStoriesBeanDB.setGa_prefix(topStoriesBean.getGa_prefix());
            topStoriesBeanDB.setImage(topStoriesBean.getImage());
            topStoriesBeanDBRealmList.add(topStoriesBeanDB);
        }

        latestNewsDBResults.deleteAllFromRealm();
        LatestNewsDB latestNewsDB = realm.createObject(LatestNewsDB.class);
        latestNewsDB.setDate(latestNews.getDate());
        latestNewsDB.setStories(storiesBeanDBRealmList);
        latestNewsDB.setTop_stories(topStoriesBeanDBRealmList);
    }

    public static List<StoriesBean> getStoriesBean(RealmResults<StoriesBeanDB> query) {
        List<StoriesBean> storiesBeanList = new ArrayList<>();
        for (int i = 0; i < query.size(); i++) {
            StoriesBean storiesBean = new StoriesBean();
            storiesBean.setTitle(query.get(i).getTitle());
            storiesBean.setImages(Collections.singletonList(query.get(i).getImages()));
            storiesBean.setType(query.get(i).getType());
            storiesBean.setGa_prefix(query.get(i).getGa_prefix());
            storiesBean.setId(query.get(i).getId());
            storiesBeanList.add(storiesBean);
        }
        return storiesBeanList;
    }
}
