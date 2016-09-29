package com.wl.dudian.framework;


import com.wl.dudian.app.model.BeforeNews;
import com.wl.dudian.app.model.DiscussDataModel;
import com.wl.dudian.app.model.DiscussExtraModel;
import com.wl.dudian.app.model.LatestNews;
import com.wl.dudian.app.model.NewsDetails;
import com.wl.dudian.app.model.StartImage;
import com.wl.dudian.app.model.ThemeDetailModel;
import com.wl.dudian.app.model.ThemesModel;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Api接口类 Created by Qiushui on 16/6/20.
 */
public interface DailyApi {

    /**
     * 启动页面图像
     *
     * @return
     */
    @GET("api/7/prefetch-launch-images/1080*1668")
    Observable<StartImage> getStartImage();

    /**
     * 获取最近的新闻
     *
     * @return
     */
    @GET("/api/4/news/latest")
    Observable<LatestNews> getLatestNews();

    /**
     * 获取新闻详细内容
     *
     * @param newsId 新闻ID号
     * @return
     */
    @GET("/api/4/news/{newsId}")
    Observable<NewsDetails> getNewsDetails(@Path("newsId") String newsId);

    /**
     * 获得历史新闻
     *
     * @param date
     * @return
     */
    @GET("/api/4/news/before/{date}")
    Observable<BeforeNews> getBeforeNews(@Path("date") String date);

    /**
     * 主题日报列表查看
     *
     * @return
     */
    @GET("/api/4/themes")
    Observable<ThemesModel> getThemesModel();

    /**
     * 主题日报内容查看
     */
    @GET("/api/4/theme/{id}")
    Observable<ThemeDetailModel> getThemeDetail(@Path("id") String id);

    /**
     * 新闻评论额外信息
     *
     * @param id
     * @return
     */
    @GET("/api/4/story-extra/{id}")
    Observable<DiscussExtraModel> getDiscussExtra(@Path("id") String id);

    /**
     * 获取新闻长评
     *
     * @param id
     * @return
     */
    @GET("/api/4/story/{id}/long-comments")
    Observable<DiscussDataModel> getDiscussLong(@Path("id") String id);

    /**
     * 获取新闻短评
     *
     * @param id
     * @return
     */
    @GET("/api/4/story/{id}/short-comments")
    Observable<DiscussDataModel> getDiscussShort(@Path("id") String id);
}
