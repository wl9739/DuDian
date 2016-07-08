package com.wl.dudian.framework;

import android.text.TextUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yisheng on 16/6/21.
 */

public class HttpUtil {

    private static final long DEFAULT_TIMEOUT = 5;
    private final long CACHE_SIZE = 10 * 1024 *1024;
    private static final String BASE_URL = "http://news-at.zhihu.com/";
    private static Retrofit mRetrofit;
    private static DailyService mDailyService;

    private Interceptor mInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            String cacheControl = request.cacheControl().toString();
            if (TextUtils.isEmpty(cacheControl)) {
                cacheControl = "public, max-age=60";

            }
            return response.newBuilder().header("Cache-Control", cacheControl).removeHeader("Pragma").build();
        }
    };
    private HttpUtil() {

    }

    private static DailyService createService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(interceptor);

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
