package com.wl.dudian.app.db;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by wanglin on 16/8/16.
 */

public class BeforeNewsDB extends RealmObject{

    private String date;

    private RealmList<StoriesBeanDB> stories;

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
}
