package com.wl.dudian.app.viewmodel;

import android.databinding.BaseObservable;

/**
 * 起始页面
 * <p>
 * Created by Qiushui on 16/8/3.
 */

public class StartImageVM extends BaseObservable {

    private String img;

    public StartImageVM(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
