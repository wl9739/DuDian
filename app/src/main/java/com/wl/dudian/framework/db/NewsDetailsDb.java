package com.wl.dudian.framework.db;

import io.realm.RealmObject;

/**
 * @author zfeiyu
 * @since 0.0.2
 */
public class NewsDetailsDb extends RealmObject {
    private String body;
    private String image_source;
    private String title;
    private String image;
    private String share_url;
    private String ga_prefix;
}
