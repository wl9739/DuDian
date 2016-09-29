package com.wl.dudian.app.newsdetail;

import com.wl.dudian.app.BasePresenter;
import com.wl.dudian.app.BaseView;
import com.wl.dudian.app.model.NewsDetails;

/**
 * Created by Qiushui on 16/8/8.
 */

public interface NewsDetailContract {

    interface View extends BaseView<Presenter> {

        void showNormalData(String newsDetails);

        void showNobodyData(String shareUrl);

        void share(NewsDetails newsDetails);

        void showHeaderImage(String imageUrl);

        void showWebView(boolean isShowNight);
    }

    interface Presenter extends BasePresenter {

        void loadData(String newsId);

        void favorite();

        void share();

        void updateRead(int id);
    }
}
