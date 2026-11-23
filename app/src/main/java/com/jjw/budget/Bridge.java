package com.jjw.budget;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.webkit.JavascriptInterface;

/** 웹 화면(JS)과 휴대폰 저장소·설정을 잇는 다리. 데이터는 이 휴대폰 안에만 저장된다. */
public class Bridge {
    private final Context ctx;
    private final SharedPreferences prefs;

    Bridge(Context c) {
        ctx = c.getApplicationContext();
        prefs = ctx.getSharedPreferences(Pending.PREFS, Context.MODE_PRIVATE);
    }

    @JavascriptInterface public String get(String key) { return prefs.getString("d:" + key, null); }

    @JavascriptInterface public void set(String key, String json) { prefs.edit().putString("d:" + key, json).apply(); }

    @JavascriptInterface public String takePending() { return Pending.take(ctx); }

    @JavascriptInterface public boolean notifEnabled() {
        String s = Settings.Secure.getString(ctx.getContentResolver(), "enabled_notification_listeners");
        return s != null && s.contains(ctx.getPackageName());
    }

    @JavascriptInterface public void openNotifSettings() {
        Intent i = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);
    }

    @JavascriptInterface public void copy(String text) {
        ClipboardManager cm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) cm.setPrimaryClip(ClipData.newPlainText("봉투 가계부 백업", text));
    }

    @JavascriptInterface public String allData() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (String k : prefs.getAll().keySet()) {
            if (!k.startsWith("d:")) continue;
            String v = prefs.getString(k, null);
            if (v == null) continue;
            if (!first) sb.append(',');
            first = false;
            sb.append(org.json.JSONObject.quote(k.substring(2))).append(':').append(v);
        }
        return sb.append('}').toString();
    }
}
