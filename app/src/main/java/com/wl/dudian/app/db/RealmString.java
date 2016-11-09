package com.wl.dudian.app.db;

import io.realm.RealmObject;

/**
 * Created by Qiushui on 16/8/3.
 */

public class RealmString extends RealmObject {

    private String val;

    public RealmString() {
    }

    public RealmString(String s) {
        val = s;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
