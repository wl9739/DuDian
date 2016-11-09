package com.wl.dudian.app.latestnews;

import com.wl.dudian.app.BasePresenter;
import com.wl.dudian.app.BaseView;
import com.wl.dudian.app.db.StoriesBeanDB;

import java.util.List;

/**
 * @author Qiushui on 16/8/16.
 */

public interface LatestNewsContract {

    interface View extends BaseView<Presenter> {

        void showHeaderView();

        void showLatestNews();

        void loadBeforNews(List<StoriesBeanDB> storiesBeanList, String currentData);

        void stopRefresh();
    }

    interface Presenter extends BasePresenter {

        void loadLatestNews();

        void loadMoreNews();

        void updateRead(StoriesBeanDB storiesBean);
    }
}
