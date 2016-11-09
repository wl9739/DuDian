package com.wl.dudian.app.db;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by wanglin on 2016/11/8.
 */

public class LatestNewsSerializer implements JsonSerializer<LatestNewsDB> {
    @Override
    public JsonElement serialize(LatestNewsDB src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("time", src.getTime());
        jsonObject.addProperty("date", src.getDate());
        jsonObject.add("stories", context.serialize(src.getStories()));
        jsonObject.add("top_stories", context.serialize(src.getTop_stories()));
        return jsonObject;
    }
}
