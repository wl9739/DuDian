package com.wl.dudian.app.latestnews;

import com.wl.dudian.app.BasePresenter;
import com.wl.dudian.app.BaseView;
import com.wl.dudian.app.model.StoriesBean;
import com.wl.dudian.app.model.TopStoriesBean;

import java.util.List;

/**
 * Created by wanglin on 16/8/16.
 */

public interface LatestNewsContract {

    interface View extends BaseView<Presenter> {

        void showHeaderView(List<TopStoriesBean> topStoriesBeen);

        void showLatestNews(List<StoriesBean> storiesBeanList, long timestampMillis);

        void loadBeforNews(List<StoriesBean> storiesBeanList, String currentData);
    }

    interface Presenter extends BasePresenter{

        void loadLatestNews();

        void loadMoreNews();

        void updateRead(StoriesBean storiesBean);
    }
}
