package com.wl.dudian.app.db;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Qiushui on 16/8/16.
 */

public class LatestNewsDB extends RealmObject {

    @PrimaryKey
    private long time;

    private String date;

    private RealmList<StoriesBeanDB> stories;

    private RealmList<TopStoriesBeanDB> top_stories;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public RealmList<StoriesBeanDB> getStories() {
        return stories;
    }

    public void setStories(RealmList<StoriesBeanDB> stories) {
        this.stories = stories;
    }

    public RealmList<TopStoriesBeanDB> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(RealmList<TopStoriesBeanDB> top_stories) {
        this.top_stories = top_stories;
    }
}
