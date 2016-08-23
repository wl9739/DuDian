package com.wl.dudian.app.db.mapper;

import com.wl.dudian.app.db.LatestNewsDB;
import com.wl.dudian.app.db.StoriesBeanDB;
import com.wl.dudian.app.db.TopStoriesBeanDB;
import com.wl.dudian.app.model.LatestNews;
import com.wl.dudian.app.model.StoriesBean;
import com.wl.dudian.app.model.TopStoriesBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import rx.schedulers.Timestamped;

/**
 * Created by qiushui on 16/8/16.
 */

public class LatestNewsMapper {

    public static LatestNews getLatestNews(RealmResults<LatestNewsDB> query) {
        LatestNews latestNews = new LatestNews();
        latestNews.setDate(query.get(0).getDate());
        List<StoriesBean> storiesBeanList = new ArrayList<>();
        List<TopStoriesBean> topStoriesBeenList = new ArrayList<>();
        for (int i = 0; i < query.get(0).getStories().size(); i++) {
            StoriesBean storiesBean = new StoriesBean();
            storiesBean.setTitle(query.get(0).getStories().get(i).getTitle());
            storiesBean.setId(query.get(0).getStories().get(i).getId());
            storiesBean.setImages(Arrays.asList(query.get(0).getStories().get(i).getImages()));
            storiesBean.setGa_prefix(query.get(0).getStories().get(i).getGa_prefix());
            storiesBean.setType(query.get(0).getStories().get(i).getType());
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

    public static void getLatestNewsDB(Realm realm, Timestamped<LatestNews> latestNewsTimestamped) {
        LatestNewsDB latestNewsDB = realm.createObject(LatestNewsDB.class);
        LatestNews latestNews = latestNewsTimestamped.getValue();
        latestNewsDB.setTime(latestNewsTimestamped.getTimestampMillis());
        latestNewsDB.setDate(latestNews.getDate());
        RealmList<StoriesBeanDB> storiesBeanDBRealmList = new RealmList<>();
        RealmList<TopStoriesBeanDB> topStoriesBeanDBRealmList = new RealmList<>();
        for (int i = 0; i < latestNews.getStories().size(); i++) {
            StoriesBeanDB storiesBeanDB = realm.createObject(StoriesBeanDB.class);
            storiesBeanDB.setGa_prefix(latestNews.getStories().get(i).getGa_prefix());
            storiesBeanDB.setId(latestNews.getStories().get(i).getId());
            storiesBeanDB.setTitle(latestNews.getStories().get(i).getTitle());
            storiesBeanDB.setType(latestNews.getStories().get(i).getType());
            storiesBeanDB.setImages(latestNews.getStories().get(i).getImages().get(0));
            storiesBeanDBRealmList.add(storiesBeanDB);
        }
        for (int i = 0; i < latestNews.getTop_stories().size(); i++) {
            TopStoriesBeanDB topStoriesBeanDB = realm.createObject(TopStoriesBeanDB.class);
            topStoriesBeanDB.setType(latestNews.getTop_stories().get(i).getType());
            topStoriesBeanDB.setTitle(latestNews.getTop_stories().get(i).getTitle());
            topStoriesBeanDB.setId(latestNews.getTop_stories().get(i).getId());
            topStoriesBeanDB.setGa_prefix(latestNews.getTop_stories().get(i).getGa_prefix());
            topStoriesBeanDB.setImage(latestNews.getTop_stories().get(i).getImage());
            topStoriesBeanDBRealmList.add(topStoriesBeanDB);
        }
        latestNewsDB.setStories(storiesBeanDBRealmList);
        latestNewsDB.setTop_stories(topStoriesBeanDBRealmList);

    }
}
