package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyClass {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.wl.dudian.framework.db");

        addLatestNewsItem(schema);
        addNewsDetail(schema);
        new DaoGenerator().generateAll(schema, "app/src/main/java/");
    }

    private static void addNewsDetail(Schema schema) {
        Entity newsDetail = schema.addEntity("NewsDetail");
        newsDetail.addIntProperty("id");
        newsDetail.addStringProperty("body");
        newsDetail.addStringProperty("title");
        newsDetail.addStringProperty("share_url");
        newsDetail.addStringProperty("image_source");

    }

    private static void addLatestNewsItem(Schema schema) {
        Entity latestNewsItem = schema.addEntity("LatestNewsItem");
        latestNewsItem.addIntProperty("id").primaryKey();
        latestNewsItem.addIntProperty("type");
        latestNewsItem.addStringProperty("ga_prefix");
        latestNewsItem.addStringProperty("title").notNull();
        latestNewsItem.addByteArrayProperty("images");

//        Entity images = schema.addEntity("Images");
//        Property url = images.addStringProperty("imgUrl").getProperty();
//
//        latestNewsItem.addToMany(images, url);
//        images.addToOne(latestNewsItem, url);

    }
}
