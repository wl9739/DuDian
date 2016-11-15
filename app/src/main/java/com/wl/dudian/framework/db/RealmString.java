package com.wl.dudian.framework.db;

import io.realm.RealmObject;

/**
 * Created by Qiushui on 16/8/3.
 */

public class RealmString extends RealmObject {

    private String val;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
