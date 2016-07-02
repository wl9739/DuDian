package com.wl.dudian.app;

import android.app.Application;
import android.content.Context;

import com.wl.dudian.framework.db.DBHelper;
import com.wl.dudian.framework.db.DaoMaster;
import com.wl.dudian.framework.db.DaoSession;

/**
 * @author zfeiyu
 * @since 0.0.2
 */
public class AppApplication extends Application {

    private static AppApplication instance;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;

    public static AppApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static DaoMaster getDaoMaster(Context ctx) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(ctx, DBHelper.DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession(Context ctx) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(ctx);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }


}
