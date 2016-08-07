package com.wl.dudian.app.repository;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.wl.dudian.app.model.StartImage;
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
     * Default image size
     */
    private static final String START_IMAGE = "720*1184";
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
}
