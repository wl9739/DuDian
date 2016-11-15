package com.wl.dudian.framework.db.model;

import io.realm.RealmObject;

/**
 * Created by Qiushui on 16/8/3.
 */

public class RecommendersBean extends RealmObject {
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
