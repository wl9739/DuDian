package com.wl.dudian.app.splash;

import android.graphics.Bitmap;

import com.wl.dudian.app.BasePresenter;
import com.wl.dudian.app.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 * <p>
 * Created by Qiushui on 16/8/8.
 */

interface SplashContract {

    interface View extends BaseView<Presenter> {

        void showStartImage(Bitmap bitmap);

        void startActivity();
    }

    interface Presenter extends BasePresenter {

        void getStartImage();

        void unLanchSubscription();
    }
}
