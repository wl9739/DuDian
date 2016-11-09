package com.wl.dudian.app.db;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by wanglin on 2016/11/8.
 */

public class StoriesBeanSerializer implements JsonSerializer<StoriesBeanDB> {
    @Override
    public JsonElement serialize(StoriesBeanDB src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", src.getType());
        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("ga_prefix", src.getGa_prefix());
        jsonObject.addProperty("title", src.getTitle());
        jsonObject.addProperty("read", src.isRead());
        jsonObject.add("images", context.serialize(src.getImages()));

        return jsonObject;
    }
}
