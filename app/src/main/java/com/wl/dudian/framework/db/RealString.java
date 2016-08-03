package com.wl.dudian.framework.db;

import io.realm.RealmObject;

/**
 * @author zfeiyu
 * @since 0.0.2
 */
public class RealString extends RealmObject {

    // Make a custom Gson instance, with a custom TypeAdapter for each wrapper object.
// In this instance we only have RealmList<RealmInt> as a a wrapper for RealmList<Integer>
//    Type token = new TypeToken<RealmList<RealmInt>>(){}.getType();
//    Gson gson = new GsonBuilder()
//            .setExclusionStrategies(new ExclusionStrategy() {
//                @Override
//                public boolean shouldSkipField(FieldAttributes f) {
//                    return f.getDeclaringClass().equals(RealmObject.class);
//                }
//
//                @Override
//                public boolean shouldSkipClass(Class<?> clazz) {
//                    return false;
//                }
//            })
//            .registerTypeAdapter(token, new TypeAdapter<RealmList<RealmInt>>() {
//
//                @Override
//                public void write(JsonWriter out, RealmList<RealmInt> value) throws IOException {
//                    // Ignore
//                }
//
//                @Override
//                public RealmList<RealmInt> read(JsonReader in) throws IOException {
//                    RealmList<RealmInt> list = new RealmList<RealmInt>();
//                    in.beginArray();
//                    while (in.hasNext()) {
//                        list.add(new RealmInt(in.nextInt()));
//                    }
//                    in.endArray();
//                    return list;
//                }
//            })
//            .create();
//
//    // Convert JSON to objects as normal
//    List<MainObject> objects = gson.fromJson(json, new TypeToken<List<MainObject>>(){}.getType());
//
//// Copy objects to Realm
//    realm.beginTransaction();
//    realm.copyToRealm(objects);
//    realm.commitTransaction();
}
