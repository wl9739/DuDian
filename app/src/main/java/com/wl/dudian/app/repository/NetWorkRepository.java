package com.wl.dudian.app.repository;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.wl.dudian.app.model.BeforeNews;
import com.wl.dudian.app.model.DiscussDataModel;
import com.wl.dudian.app.model.DiscussExtraModel;
import com.wl.dudian.app.model.LatestNews;
import com.wl.dudian.app.model.NewsDetails;
import com.wl.dudian.app.model.StartImage;
import com.wl.dudian.app.model.ThemeDetailModel;
import com.wl.dudian.app.model.ThemesModel;
import com.wl.dudian.framework.DailyApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by Qiushui on 16/8/3.
 */

public class NetWorkRepository {

    private DailyApi dailyApi;
    /**
     * BaseURL
     */
    private static final String BASE_URL = "http://news-at.zhihu.com/";
    /**
     * TIMEOUT
     */
    private static final long DEFAULT_TIMEOUT = 10 * 1000;

    public NetWorkRepository() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(interceptor);
        if (dailyApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            dailyApi = retrofit.create(DailyApi.class);
        }
    }

    @RxLogObservable
    public Observable<StartImage> getStartImage() {
        return dailyApi.getStartImage();
    }

    @RxLogObservable
    public Observable<LatestNews> getLatestNews() {
        return dailyApi.getLatestNews();
    }

    @RxLogObservable
    public Observable<NewsDetails> getNewsDetails(String id) {
        return dailyApi.getNewsDetails(id);
    }

    @RxLogObservable
    public Observable<BeforeNews> getBeforeNews(String date) {
        return dailyApi.getBeforeNews(date);
    }

    public Observable<ThemesModel> getThemesModel() {
        return dailyApi.getThemesModel();
    }

    public Observable<ThemeDetailModel> getThemeDetail(String id) {
        return dailyApi.getThemeDetail(id);
    }

    public Observable<DiscussDataModel> getDiscussLong(String id) {
        return dailyApi.getDiscussLong(id);
    }

    public Observable<DiscussDataModel> getDiscussShort(String id) {
        return dailyApi.getDiscussShort(id);
    }

    public Observable<DiscussExtraModel> getdiscussExtra(String id) {
        return dailyApi.getDiscussExtra(id);
    }
}
