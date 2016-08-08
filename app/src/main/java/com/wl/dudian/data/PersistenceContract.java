package com.wl.dudian.data;

import android.provider.BaseColumns;

/**
 * Created by Qiushui on 16/8/8.
 */

public final class PersistenceContract {

    public static abstract class NewsEntry implements BaseColumns {
        public static final String TABLE_NAME = "news";
        public static final String COLUMN_NAME_NEWS_ID = "newsid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_IMAGEURL = "imageUrl";
    }
}
