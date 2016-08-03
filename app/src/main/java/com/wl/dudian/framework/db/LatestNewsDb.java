package com.wl.dudian.framework.db;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * @author zfeiyu
 * @since 0.0.2
 */
public class LatestNewsDb extends RealmObject {

    private String date;
    private RealmList<StoriesBeanDb> stories;
    private RealmList<TopStoriesBeanDb> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public RealmList<StoriesBeanDb> getStories() {
        return stories;
    }

    public void setStories(RealmList<StoriesBeanDb> stories) {
        this.stories = stories;
    }

    public RealmList<TopStoriesBeanDb> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(RealmList<TopStoriesBeanDb> top_stories) {
        this.top_stories = top_stories;
    }
}
