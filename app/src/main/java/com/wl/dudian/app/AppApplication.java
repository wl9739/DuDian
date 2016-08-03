package com.wl.dudian.app;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Custom Application
 * @author Qiushui
 */
public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initStetho();

        initRealm();
    }

    private void initRealm() {
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("dudian.realm")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(1)
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
