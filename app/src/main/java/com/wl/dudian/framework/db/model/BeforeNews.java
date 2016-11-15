package com.wl.dudian.framework.db.model;

import java.util.List;

/**
 * Created by Qiushui on 16/6/23.
 */
public class BeforeNews {

    private String date;

    private List<StoriesBean> stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }
}
