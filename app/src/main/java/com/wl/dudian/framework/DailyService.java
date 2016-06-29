package com.wl.dudian.framework;


import com.wl.dudian.app.model.BeforeNews;
import com.wl.dudian.app.model.LatestNews;
import com.wl.dudian.app.model.NewsDetails;
import com.wl.dudian.app.model.StartImage;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by yisheng on 16/6/20.
 */

public interface DailyService {

    /**
     * 启动页面图像
     *
     * @param image 图像分辨率大小:320*432    480*728    720*1184    1080*1776
     * @return
     */
    @GET("/api/4/start-image/{image}")
    Observable<StartImage> getStartImage(@Path("image") String image);

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

    @GET("/api/4/news/before/{date}")
    Observable<BeforeNews> getBeforeNews(@Path("date") String date);
}
