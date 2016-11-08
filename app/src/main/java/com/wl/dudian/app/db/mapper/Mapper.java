package com.wl.dudian.app.db.mapper;

import com.wl.dudian.app.db.LatestNewsDB;
import com.wl.dudian.app.db.StoriesBeanDB;
import com.wl.dudian.app.db.TopStoriesBeanDB;
import com.wl.dudian.app.model.LatestNews;
import com.wl.dudian.app.model.StoriesBean;
import com.wl.dudian.app.model.TopStoriesBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

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
        for (int i = 0; i < query.get(0).getStories().size(); i++) {
            StoriesBean storiesBean = new StoriesBean();
            storiesBean.setTitle(query.get(0).getStories().get(i).getTitle());
            storiesBean.setId(query.get(0).getStories().get(i).getId());
            storiesBean.setImages(Collections.singletonList(query.get(0).getStories().get(i).getImages()));
            storiesBean.setGa_prefix(query.get(0).getStories().get(i).getGa_prefix());
            storiesBean.setType(query.get(0).getStories().get(i).getType());
            storiesBean.setRead(query.get(0).getStories().get(i).isRead());
            storiesBeanList.add(storiesBean);

        }
        for (int i = 0; i < query.get(0).getTop_stories().size(); i++) {
            TopStoriesBean topStoriesBean = new TopStoriesBean();
            topStoriesBean.setType(query.get(0).getTop_stories().get(i).getType());
            topStoriesBean.setGa_prefix(query.get(0).getTop_stories().get(i).getGa_prefix());
            topStoriesBean.setId(query.get(0).getTop_stories().get(i).getId());
            topStoriesBean.setImage(query.get(0).getTop_stories().get(i).getImage());
            topStoriesBean.setTitle(query.get(0).getTop_stories().get(i).getTitle());
            topStoriesBeenList.add(topStoriesBean);
        }
        latestNews.setStories(storiesBeanList);
        latestNews.setTop_stories(topStoriesBeenList);

        return latestNews;
    }

    public static void saveLatestNewsDB(Realm realm, LatestNews latestNews,
            RealmQuery<StoriesBeanDB> storiesBeanResult) {
        LatestNewsDB latestNewsDB = realm.createObject(LatestNewsDB.class);
        latestNewsDB.setDate(latestNews.getDate());
        RealmList<StoriesBeanDB> storiesBeanDBRealmList = new RealmList<>();
        RealmList<TopStoriesBeanDB> topStoriesBeanDBRealmList = new RealmList<>();

        for (int i = 0; i < latestNews.getTop_stories().size(); i++) {
            TopStoriesBeanDB topStoriesBeanDB = realm.createObject(TopStoriesBeanDB.class);
            topStoriesBeanDB.setType(latestNews.getTop_stories().get(i).getType());
            topStoriesBeanDB.setTitle(latestNews.getTop_stories().get(i).getTitle());
            topStoriesBeanDB.setId(latestNews.getTop_stories().get(i).getId());
            topStoriesBeanDB.setGa_prefix(latestNews.getTop_stories().get(i).getGa_prefix());
            topStoriesBeanDB.setImage(latestNews.getTop_stories().get(i).getImage());
            topStoriesBeanDBRealmList.add(topStoriesBeanDB);
        }
        latestNewsDB.setTop_stories(topStoriesBeanDBRealmList);
        for (int i = 0; i < latestNews.getStories().size(); i++) {
            if (storiesBeanResult.equalTo("id", latestNews.getStories().get(i).getId()).findAll().size() == 0) {
                StoriesBeanDB storiesBeanDB = realm.createObject(StoriesBeanDB.class);
                storiesBeanDB.setGa_prefix(latestNews.getStories().get(i).getGa_prefix());
                storiesBeanDB.setId(latestNews.getStories().get(i).getId());
                storiesBeanDB.setTitle(latestNews.getStories().get(i).getTitle());
                storiesBeanDB.setType(latestNews.getStories().get(i).getType());
                storiesBeanDB.setImages(latestNews.getStories().get(i).getImages().get(0));
                storiesBeanDBRealmList.add(storiesBeanDB);
            }
        }
        latestNewsDB.setStories(storiesBeanDBRealmList);
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
