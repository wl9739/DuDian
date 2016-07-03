package com.wl.dudian.framework.db;

import android.content.Context;

import com.wl.dudian.app.AppApplication;

/**
 * @author zfeiyu
 * @since 0.0.2
 */
public class DBHelper {

    public static final String DB_NAME = "WL";

    private static Context context;
    private static DBHelper instance;

    /**
     * 新闻详情
     */
    private static NewsDetailDao newsDetailDao;

    private DBHelper() {}

    public static DBHelper getInstance(Context ctx) {
        if (null == instance) {
            instance = new DBHelper();
            if (null == context) {
                context = ctx;
            }
            DaoSession daoSession = AppApplication.getDaoSession(context);
            newsDetailDao = daoSession.getNewsDetailDao();
        }
        return instance;
    }

//    public static String addDetailNewsItem(StoriesBean storiesBean) {
//        newsDetailDao.insert(storiesBean);
//    }
}
