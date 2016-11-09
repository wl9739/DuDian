package com.wl.dudian.app.db;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by wanglin on 2016/11/8.
 */

public class TopStoriesBeanSerializer implements JsonSerializer<TopStoriesBeanDB> {
    @Override
    public JsonElement serialize(TopStoriesBeanDB src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("image", src.getImage());
        jsonObject.addProperty("type", src.getType());
        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("ga_prefix", src.getGa_prefix());
        jsonObject.addProperty("title", src.getType());

        return jsonObject;
    }
}
