package com.videomaker.photovideoeditorwithanimation.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.exifinterface.media.ExifInterface;
import android.util.Log;

import java.io.IOException;

public class ScalingUtilities {

    public static Bitmap ConvetrSameSize(Bitmap originalImage, Bitmap bgBitmap, int mDisplayWidth, int mDisplayHeight, float x, float y) {
        Bitmap cs = bgBitmap.copy(bgBitmap.getConfig(), true);
        Bitmap blurBitmap = FastBlur.doBlur(cs, 25, true);
        assert blurBitmap != null;
        new Canvas(blurBitmap)
                .drawBitmap(newscaleBitmap(originalImage, mDisplayWidth, mDisplayHeight, x, y), 0.0f, 0.0f, new Paint());
        return cs;
    }

    public static Bitmap scaleCenterCrop(Bitmap source, int newWidth, int newHeight) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        if (sourceWidth == newWidth && sourceHeight == newHeight) {
            return source;
        }
        float scale = Math.max(((float) newWidth) / ((float) sourceWidth), ((float) newHeight) / ((float) sourceHeight));
        float scaledWidth = scale * ((float) sourceWidth);
        float scaledHeight = scale * ((float) sourceHeight);
        float left = (((float) newWidth) - scaledWidth) / 2.0f;
        float top = (((float) newHeight) - scaledHeight) / 2.0f;
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        new Canvas(dest).drawBitmap(source, null, targetRect, null);
        return dest;
    }

    private static Bitmap newscaleBitmap(Bitmap originalImage, int width, int height, float x, float y) {
        Bitmap background = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        float originalWidth = (float) originalImage.getWidth();
        float originalHeight = (float) originalImage.getHeight();
        Canvas canvas = new Canvas(background);
        float scale = ((float) width) / originalWidth;
        float scaleY = ((float) height) / originalHeight;
        float xTranslation = 0.0f;
        float yTranslation = (((float) height) - (originalHeight * scale)) / 2.0f;
        if (yTranslation < 0.0f) {
            yTranslation = 0.0f;
            scale = ((float) height) / originalHeight;
            xTranslation = (((float) width) - (originalWidth * scaleY)) / 2.0f;
        }
        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation * x, yTranslation + y);
        Log.d("translation", "xTranslation :" + xTranslation + " yTranslation :" + yTranslation);
        transformation.preScale(scale, scale);
        canvas.drawBitmap(originalImage, transformation, new Paint());
        return background;
    }

    public static Bitmap checkBitmap(String path) {
        int orientation = 1;
        Options bounds = new Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bounds);
        Bitmap bm = BitmapFactory.decodeFile(path, new Options());
        try {
            String orientString = new ExifInterface(path).getAttribute("Orientation");
            if (orientString != null) {
                orientation = Integer.parseInt(orientString);
            }
            int rotationAngle = 0;
            if (orientation == 6) {
                rotationAngle = 90;
            }
            if (orientation == 3) {
                rotationAngle = 180;
            }
            if (orientation == 8) {
                rotationAngle = 270;
            }
            Matrix matrix = new Matrix();
            matrix.setRotate((float) rotationAngle, ((float) bm.getWidth()) / 2.0f, ((float) bm.getHeight()) / 2.0f);
            return Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
