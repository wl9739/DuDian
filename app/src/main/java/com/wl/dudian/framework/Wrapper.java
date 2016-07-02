package com.wl.dudian.framework;

import com.wl.dudian.app.model.StoriesBean;
import com.wl.dudian.framework.db.LatestNewsItem;

/**
 * @author zfeiyu
 * @since 0.0.2
 */
public class Wrapper {

    public static LatestNewsItem storiesBeanToLatestNewsItem(StoriesBean storiesBean) {
        LatestNewsItem latestNewsItem = new LatestNewsItem();
        latestNewsItem.setId(storiesBean.getId());
        latestNewsItem.setGa_prefix(storiesBean.getGa_prefix());
//        for (int i = 0; i < storiesBean.getImages().size(); i++) {
//            Images images = new Images();
//            images.setImgUrl(storiesBean.getImages().get(i));
//            images.setLatestNewsItem(latestNewsItem);
//        }
//        latestNewsItem.setImages(storiesBean.getImages());
        return latestNewsItem;
    }
}
