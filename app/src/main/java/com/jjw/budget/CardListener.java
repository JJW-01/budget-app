package com.jjw.budget;

import android.app.Notification;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import org.json.JSONObject;
import java.util.regex.Pattern;

/** 카드 결제 알림(카드사 앱 푸시, 승인 문자)을 읽어 대기열에 넣는다. 다른 알림은 무시한다. */
public class CardListener extends NotificationListenerService {
    private static final Pattern AMOUNT = Pattern.compile("[0-9][0-9,]{2,}\\s*원");
    private static final Pattern PAY = Pattern.compile("승인|결제|일시불|할부|체크카드|신용카드|사용금액|카드.*사용");
    private static final Pattern SKIP = Pattern.compile("입금|이체 완료|송금|충전 완료|광고|이벤트|혜택|쿠폰");

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        try {
            if (sbn.getPackageName().equals(getPackageName())) return;
            Notification n = sbn.getNotification();
            if ((n.flags & Notification.FLAG_GROUP_SUMMARY) != 0) return;
            Bundle e = n.extras;
            if (e == null) return;
            CharSequence title = e.getCharSequence(Notification.EXTRA_TITLE);
            CharSequence big = e.getCharSequence(Notification.EXTRA_BIG_TEXT);
            CharSequence text = e.getCharSequence(Notification.EXTRA_TEXT);
            String body = (title == null ? "" : title + "\n") + (big != null ? big : (text == null ? "" : text));
            if (!AMOUNT.matcher(body).find() || !PAY.matcher(body).find()) return;
            if (SKIP.matcher(body).find() && !body.contains("승인")) return;
            JSONObject o = new JSONObject();
            o.put("ts", sbn.getPostTime());
            o.put("pkg", sbn.getPackageName());
            o.put("text", body);
            Pending.add(this, o);
        } catch (Exception ignored) {
        }
    }
}
