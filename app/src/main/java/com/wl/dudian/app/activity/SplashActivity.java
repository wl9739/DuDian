package com.wl.dudian.app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wl.dudian.R;
import com.wl.dudian.app.model.StartImage;
import com.wl.dudian.framework.HttpUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author zfeiyu
 * @since 0.0.2
 */
public class SplashActivity extends BaseActivity {

    /**
     * 启动图片文件路径
     */
    private static final String SPLASH_IMAGE_FILEPATH = "start.jpg";
    @BindView(R.id.splash_activity_img)
    ImageView mSplashView;


    /**
     * 保存图片的文件
     */
    private File mImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // init ShareSDK
        ShareSDK.initSDK(this);

        setContentView(R.layout.splash_activity);
        ButterKnife.bind(this);

        initStartImage();

        initAnimation();
    }

    /**
     * 从文件中检查是否有启动图片
     */
    private void initStartImage() {
        File dir = getFilesDir();
        mImageFile = new File(dir, SPLASH_IMAGE_FILEPATH);
        if (mImageFile.exists()) {
            Bitmap storedSplashImage = BitmapFactory.decodeFile(mImageFile.getAbsolutePath());
            mSplashView.setImageBitmap(storedSplashImage);
        } else {
            mSplashView.setImageResource(R.mipmap.start);
        }
    }

    /**
     * 设置动画效果
     */
    private void initAnimation() {
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        animation.setDuration(3000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getSplashImageInfo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mSplashView.startAnimation(animation);
    }

    /**
     * 获取图片URL
     */
    private void getSplashImageInfo() {

        HttpUtil.getInstance().getStartImage("1080*1776")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StartImage>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(StartImage startImage) {
                        downloadImage(startImage.getImg());
                    }
                });
    }


    /**
     * 跳转到首页
     */
    private void forward() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    /**
     * 使用Glide下载图片
     *
     * @param imgUrl 图片URL
     */
    private void downloadImage(String imgUrl) {
        Glide.with(this).load(imgUrl).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                saveAsFile(resource);
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
            forward();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // stop ShareSDK
        ShareSDK.stopSDK(this);
    }
}
