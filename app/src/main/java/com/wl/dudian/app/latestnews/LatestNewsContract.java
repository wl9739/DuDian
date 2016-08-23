package com.wl.dudian.app.latestnews;

import com.wl.dudian.app.BasePresenter;
import com.wl.dudian.app.BaseView;
import com.wl.dudian.app.model.BeforeNews;
import com.wl.dudian.app.model.LatestNews;

/**
 * Created by wanglin on 16/8/16.
 */

public interface LatestNewsContract {

    interface View extends BaseView<Presenter> {

        void showLatestNews(LatestNews latestNews, long timestampMillis);

        void loadBeforNews(BeforeNews beforeNews);
    }

    interface Presenter extends BasePresenter{

        void loadLatestNews();

        void loadMoreNews();

        void saveLatestNews();

    }
}
