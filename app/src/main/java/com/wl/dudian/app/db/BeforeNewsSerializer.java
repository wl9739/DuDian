package com.wl.dudian.app.db;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by wanglin on 2016/11/9.
 */

public class BeforeNewsSerializer implements JsonSerializer<BeforeNewsDB> {
    @Override
    public JsonElement serialize(BeforeNewsDB src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", src.getDate());
        jsonObject.add("stories", context.serialize(src.getStories()));
        return jsonObject;
    }
}
