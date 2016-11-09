package com.wl.dudian.app.repository;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.wl.dudian.app.db.BeforeNewsDB;
import com.wl.dudian.app.db.BeforeNewsSerializer;
import com.wl.dudian.app.db.LatestNewsDB;
import com.wl.dudian.app.db.LatestNewsSerializer;
import com.wl.dudian.app.db.NewsDetailDB;
import com.wl.dudian.app.db.NewsDetailSerializer;
import com.wl.dudian.app.db.RealmString;
import com.wl.dudian.app.db.StoriesBeanSerializer;
import com.wl.dudian.app.db.TopStoriesBeanSerializer;
import com.wl.dudian.app.db.model.DiscussDataModel;
import com.wl.dudian.app.db.model.DiscussExtraModel;
import com.wl.dudian.app.db.model.StartImage;
import com.wl.dudian.app.db.model.ThemeDetailModel;
import com.wl.dudian.app.db.model.ThemesModel;
import com.wl.dudian.framework.DailyApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.realm.RealmList;
import io.realm.RealmObject;
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

    /**
     * BaseURL
     */
    private static final String BASE_URL = "http://news-at.zhihu.com/";
    /**
     * TIMEOUT
     */
    private static final long DEFAULT_TIMEOUT = 10 * 1000;
    private DailyApi dailyApi;

    public NetWorkRepository() {
        try {

            Gson gson = new GsonBuilder()
                    .setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return f.getDeclaringClass().equals(RealmObject.class);
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    })
                    .registerTypeAdapter(Class.forName("com.wl.dudian.app.db.BeforeNewsDB"), new BeforeNewsSerializer())
                    .registerTypeAdapter(Class.forName("com.wl.dudian.app.db.LatestNewsDB"), new LatestNewsSerializer())
                    .registerTypeAdapter(Class.forName("com.wl.dudian.app.db.NewsDetailDB"), new NewsDetailSerializer())
                    .registerTypeAdapter(Class.forName("com.wl.dudian.app.db.StoriesBeanDB"), new StoriesBeanSerializer())
                    .registerTypeAdapter(Class.forName("com.wl.dudian.app.db.TopStoriesBeanDB"), new TopStoriesBeanSerializer())
                    .registerTypeAdapter(new TypeToken<RealmList<RealmString>>() {
                    }.getType(), new TypeAdapter<RealmList<RealmString>>() {

                        @Override
                        public void write(JsonWriter out, RealmList<RealmString> value) throws IOException {
                            // Ignore
                        }

                        @Override
                        public RealmList<RealmString> read(JsonReader in) throws IOException {
                            RealmList<RealmString> list = new RealmList<>();
                            in.beginArray();
                            while (in.hasNext()) {
                                list.add(new RealmString(in.nextString()));
                            }
                            in.endArray();
                            return list;
                        }
                    })
                    .create();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(interceptor);
            if (dailyApi == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .client(builder.build())
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();

                dailyApi = retrofit.create(DailyApi.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RxLogObservable
    public Observable<StartImage> getStartImage() {
        return dailyApi.getStartImage();
    }

    @RxLogObservable
    public Observable<LatestNewsDB> getLatestNews() {
        return dailyApi.getLatestNews();
    }

    @RxLogObservable
    public Observable<NewsDetailDB> getNewsDetails(String id) {
        return dailyApi.getNewsDetails(id);
    }

    @RxLogObservable
    public Observable<BeforeNewsDB> getBeforeNews(String date) {
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
