package com.wl.dudian.app.db;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by wanglin on 2016/11/9.
 */

public class NewsDetailSerializer implements JsonSerializer<NewsDetailDB> {
    @Override
    public JsonElement serialize(NewsDetailDB src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("body", src.getBody());
        jsonObject.addProperty("image_source", src.getImage_source());
        jsonObject.addProperty("title", src.getTitle());
        jsonObject.addProperty("image", src.getImage());
        jsonObject.addProperty("share_url", src.getShare_url());
        jsonObject.addProperty("ga_prefix", src.getGa_prefix());
        jsonObject.addProperty("type", src.getType());
        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("isFavorite", src.isFavorite());
        jsonObject.add("images", context.serialize(src.getImages()));

        return jsonObject;
    }
}
