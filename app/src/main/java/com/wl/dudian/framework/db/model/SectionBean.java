package com.wl.dudian.framework.db.model;

import io.realm.RealmObject;

/**
 * Created by Qiushui on 16/8/3.
 */

public class SectionBean extends RealmObject {
    private String thumbnail;
    private int id;
    private String name;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
