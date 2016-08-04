package com.wl.dudian.app.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * 起始页面
 *
 * Created by Qiushui on 16/8/3.
 */

public class StartImageVM extends BaseObservable {

    private String text;
    private String img;

    public StartImageVM(String text, String img) {
        this.text = text;
        this.img = img;
    }

    public StartImageVM() {
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

    @BindingAdapter({"android:src"})
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }
}
