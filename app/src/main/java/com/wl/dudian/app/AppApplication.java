package com.wl.dudian.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;
import com.wl.dudian.framework.Constants;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Custom Application
 *
 * @author Qiushui
 */
public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initStetho();

        initRealm();

        initTheme();
    }

    private void initTheme() {
        SharedPreferences sp = getSharedPreferences(Constants.SP_NAME, MODE_PRIVATE);
        if (sp.getBoolean(Constants.IS_NIGHT, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void initRealm() {
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("dudian.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(2)
                .build();
        Realm.setDefaultConfiguration(config);
    }

    private void initStetho() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build());
    }
}
