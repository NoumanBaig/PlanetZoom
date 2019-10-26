package com.angadi.tripmanagementa.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;


public class Prefs {

    public static String TAG = "Prefs";


    static Prefs singleton = null;

    static SharedPreferences preferences;

    static SharedPreferences.Editor editor;

    public Prefs(Context context) {
        preferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static Prefs with(Context context) {
        if (singleton == null) {
            singleton = new Builder(context).build();
        }
        return singleton;
    }

    public void save(String key, boolean value) {
        editor.putBoolean(key, value).apply();
    }

    public void save(String key, String value) {
        editor.putString(key, value).apply();
    }

    public void save(String key, int value) {
        editor.putInt(key, value).apply();
    }

    public void save(String key, float value) {
        editor.putFloat(key, value).apply();

    }

    public void save(String key, long value) {
        editor.putLong(key, value).apply();
    }

    public void save(String key, Set<String> value) {
        editor.putStringSet(key, value).apply();
    }

    // to save object in prefrence
//    public <T> void save(String key, T object) {
//        if (object == null) {
//            //throw new IllegalArgumentException("object is null");
//        }
//
//        if (key.equals("") || key == null) {
//            throw new IllegalArgumentException("key is empty or null");
//        }
//
//        editor.putString(key, GSON.toJson(object)).apply();
//    }

    // To get object from prefrences

//    public <T> T getObject(String key, Class<T> a) {
//
//        String gson = preferences.getString(key, null);
//        if (gson == null) {
//            return null;
//        } else {
//            try {
//                return GSON.fromJson(gson, a);
//            } catch (Exception e) {
//                throw new IllegalArgumentException("Object storaged with key "
//                        + key + " is instanceof other class");
//            }
//        }
//    }

    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    public Set<String> getStringSet(String key, Set<String> defValue) {
        return preferences.getStringSet(key, defValue);
    }

    public Map<String, ?> getAll() {
        return preferences.getAll();
    }

    public void remove() {
        editor.clear();
        editor.commit();
        editor.apply();
    }

    private static class Builder {

        private final Context context;

        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context.getApplicationContext();
        }

        /**
         * Method that creates an instance of Prefs
         *
         * @return an instance of Prefs
         */
        public Prefs build() {
            return new Prefs(context);
        }
    }

}