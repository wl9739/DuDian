package com.wl.dudian.app.model;

import io.realm.RealmObject;

/**
 * 启动图片model
 *
 * Created by yisheng on 16/6/20.
 */

public class StartImage extends RealmObject{

    private long time;
    private String text;
    private String img;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
