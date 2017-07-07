package com.max.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by elwinxiao on 2015/12/23.
 */
public class ImageUtils {

    /**
     * 将Bitmap对象转化为字节数组，并可指定是否回收Bitmap对象
     */
    @Deprecated
    public static byte[] bitmapToArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 将Bitmap转换成指定大小
     *
     * @param bitmap
     * @param thumbnailWidth
     * @param thumbnailHeight
     * @return
     */
    public static Bitmap createScaledBitmap(Bitmap bitmap, int thumbnailWidth, int thumbnailHeight) {
        int sourceImageWidth = bitmap.getWidth();
        int sourceImageHeight = bitmap.getHeight();

        if (sourceImageWidth > sourceImageHeight && sourceImageWidth > thumbnailWidth) {
            thumbnailHeight = (int) (0.1 * thumbnailWidth * sourceImageHeight / sourceImageWidth);
        } else if (sourceImageWidth < sourceImageHeight && sourceImageHeight > thumbnailWidth) {
            thumbnailWidth = (int) (0.1 * sourceImageWidth * thumbnailHeight / sourceImageHeight);
        }
        return Bitmap.createScaledBitmap(bitmap, thumbnailWidth, thumbnailHeight, true);
    }

    public Bitmap createScaledBitmap(String imageFilePath, int thumbnailWidth, int thumbnailHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFilePath, options);
        int sourceImageWidth = options.outWidth;
        int sourceImageHeight = options.outHeight;

        int scaling = 1;
        if (sourceImageWidth > sourceImageHeight && sourceImageWidth > thumbnailWidth) {
            scaling = (int) (0.1 * options.outWidth / thumbnailWidth);
        } else if (sourceImageWidth < sourceImageHeight && sourceImageHeight > thumbnailHeight) {
            scaling = (int) (0.1 * options.outHeight / thumbnailHeight);
        }
        if (scaling <= 0) {
            scaling = 1;
        }
        options.inSampleSize = scaling;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imageFilePath, options);
    }
}
