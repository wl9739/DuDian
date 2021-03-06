package com.wl.dudian.app.newsdetail;

import com.wl.dudian.app.BasePresenter;
import com.wl.dudian.app.BaseView;
import com.wl.dudian.framework.db.model.StoriesBean;

/**
 * Created by Qiushui on 16/8/8.
 */

interface NewsDetailContract {

    interface View extends BaseView<Presenter> {

        void showNormalData(String newsDetails);

        void showNobodyData(String shareUrl);

        void share(StoriesBean newsDetails);

        void showHeaderImage(String imageUrl);

        void showWebView(boolean isShowNight);

        void showNewsStatus(String image_source, boolean favorite);
    }

    interface Presenter extends BasePresenter {

        void loadData(String newsId);

        boolean favorite();

        void updateRead(int id);
    }
}
