package com.jjw.budget;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;

/** 카드 알림에서 읽은 내용을 앱이 열릴 때까지 모아 두는 대기열. */
public final class Pending {
    static final String PREFS = "budget";
    private static final String KEY = "pending";

    private Pending() {}

    static synchronized void add(Context c, JSONObject item) {
        SharedPreferences p = c.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        JSONArray a;
        try { a = new JSONArray(p.getString(KEY, "[]")); } catch (Exception e) { a = new JSONArray(); }
        a.put(item);
        p.edit().putString(KEY, a.toString()).apply();
    }

    static synchronized String take(Context c) {
        SharedPreferences p = c.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String s = p.getString(KEY, "[]");
        p.edit().putString(KEY, "[]").apply();
        return s;
    }
}
