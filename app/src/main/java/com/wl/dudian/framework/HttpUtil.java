package com.wl.dudian.framework;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yisheng on 16/6/21.
 */

public class HttpUtil {

    private static final long DEFAULT_TIMEOUT = 5;
    private static final String BASE_URL = "http://news-at.zhihu.com/";
    private static Retrofit mRetrofit;
    private static DailyService mDailyService;
    private HttpUtil() {

    }

    private static DailyService createService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.addInterceptor(interceptor);

        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        mDailyService = mRetrofit.create(DailyService.class);
        return mDailyService;
    }

    public static DailyService getInstance() {
        if (null == mDailyService) {
            return createService();
        }
        return mDailyService;
    }
}
