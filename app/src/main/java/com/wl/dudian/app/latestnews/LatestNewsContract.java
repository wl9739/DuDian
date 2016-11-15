package com.wl.dudian.app.latestnews;

import com.wl.dudian.app.BasePresenter;
import com.wl.dudian.app.BaseView;
import com.wl.dudian.framework.db.model.StoriesBean;
import com.wl.dudian.framework.db.model.TopStoriesBean;

import java.util.List;

/**
 * @author Qiushui on 16/8/16.
 */

interface LatestNewsContract {

    interface View extends BaseView<Presenter> {

        void showHeaderView(List<TopStoriesBean> topStoriesBeen);

        void showLatestNews(List<StoriesBean> storiesBeanList);

        void loadBeforNews(List<StoriesBean> storiesBeanList, String currentData);

        void stopRefresh();
    }

    interface Presenter extends BasePresenter {

        void loadLatestNews();

        void loadMoreNews();

        void updateRead(StoriesBean storiesBean);
    }
}
