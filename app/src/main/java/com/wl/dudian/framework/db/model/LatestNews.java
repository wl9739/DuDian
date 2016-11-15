package com.wl.dudian.framework.db.model;

import java.util.List;

/**
 * Created by Qiushui on 16/6/20.
 */
public class LatestNews {

    private long time;

    private String date;

    private List<StoriesBean> stories;

    private List<TopStoriesBean> top_stories;

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

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }
}
