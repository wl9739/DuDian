package com.wl.dudian.app.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.wl.dudian.R;
import com.wl.dudian.app.model.StartImage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Qiushui on 16/8/3.
 */

public class DiskRepository {

    private Context context;
    /**
     * 启动图片文件路径
     */
    private static final String SPLASH_IMAGE_FILEPATH = "start.jpg";

    /**
     * 保存图片的文件
     */
    private File mImageFile;

    public DiskRepository(Context context) {
        this.context = context;
    }

    /**
     * Save StartImage, Delete the origin if exist.
     *
     * @param startImage startimage
     */
    public void saveStartImage(final StartImage startImage) {
        AndroidSchedulers.mainThread().createWorker().schedule(new Action0() {
            @Override
            public void call() {
                loadImage(startImage);
            }
        });
    }

    private void loadImage(StartImage startImage) {
        Glide.with(context)
                .load(startImage.getCreatives().get(0).getUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Schedulers.io().createWorker().schedule(new Action0() {
                            @Override
                            public void call() {
                                saveAsFile(resource);
                            }
                        });
                    }
                });
    }

    @RxLogObservable
    public Observable<Bitmap> getStartImage() {
        return Observable.fromCallable(new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {
                File dir = context.getFilesDir();
                mImageFile = new File(dir, SPLASH_IMAGE_FILEPATH);
                if (mImageFile.exists()) {
                    return BitmapFactory.decodeFile(mImageFile.getAbsolutePath());
                } else {
                    return BitmapFactory.decodeResource(context.getResources(), R.drawable.dayu);
                }
            }
        });
    }

    /**
     * 将下载的图片保存到文件里
     *
     * @param bitmap 下载的图片Bitmap
     */
    private void saveAsFile(Bitmap bitmap) {
        try {
            if (mImageFile.exists()) {
                mImageFile.delete();
            }
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(mImageFile));
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
