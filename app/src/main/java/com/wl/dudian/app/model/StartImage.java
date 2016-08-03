package com.wl.dudian.app.model;

/**
 * 启动图片model
 *
 * Created by yisheng on 16/6/20.
 */


public class StartImage {

    private String text;
    private String img;

    public StartImage(String text, String img) {
        this.text = text;
        this.img = img;
    }

    public StartImage() {
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
