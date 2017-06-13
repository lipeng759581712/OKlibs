package com.max.androidutilsmodule;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {
    private static final String TAG = "ImageUtil";
    public static final String PHOTO_TEMP = "/Tencent/shouyoubao/.png";
    static final int THUMBNAIL_TARGET_SIZE = 320;
    static final int THUMBNAIL_MAX_NUM_PIXELS = 512 * 384;
    public static String getMimeType(String fileName) {
        if (fileName == null) {
            return null;
        }
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);

        return options.outMimeType;
    }

    /**
     * 返回图片文件格式
     * 
     * @param path
     * @return
     */
    public static String getImageFormat(String path) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        if (options.outWidth > 0 && options.outHeight > 0 && options.outMimeType == null) {
            return "webp"; // 假定其为webp
        }
        if (options.outMimeType != null) {
            int index = options.outMimeType.indexOf('/');
            if (index != -1) {
                return options.outMimeType.substring(index + 1);
            }
        }
        return "png";
    }

    /**
     * 按指定大小读取缓存中的文件
     * 
     * @param fileName
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getBitmapWithSize(String fileName, int width, int height) {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = true;
        // retryOptionBitmap(options,fileName,true);

        BitmapFactory.decodeFile(fileName, options);
        options.inJustDecodeBounds = false;
        int ow = options.outWidth;
        int oh = options.outHeight;

        // 计算缩放比
        int sampleSize = Math.min(ow / width, oh / height);
        if (sampleSize < 1) {
            sampleSize = 1;
        }
        options.inSampleSize = sampleSize;
        Bitmap bitmap = ImageUtil.retryOptionBitmap(options, fileName, true);
        if (bitmap == null) {
            options.inSampleSize = options.inSampleSize + 1;
            bitmap = ImageUtil.retryOptionBitmap(options, fileName);
        }
        if (bitmap == null) {
            return null;
        }
        // Bitmap bitmap = BitmapFactory.decodeFile(fileName, options);
        int bw = bitmap.getWidth();
        int bh = bitmap.getHeight();

        // 缩放图片的尺寸
        float scaleWidth = (float) width / bw; // 按固定大小缩放 sWidth 写多大就多大
        float scaleHeight = (float) height / bh; //
        if (scaleWidth > 1 && scaleHeight > 1) {
            return bitmap;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);// 产生缩放后的Bitmap对象
        Bitmap newBitmap = ImageUtil.retryMatrixBitmap(bitmap, bw, bh, matrix, true);

        if (newBitmap != bitmap) {
            bitmap.recycle();
        }
        bitmap = null;

        return newBitmap;
    }

    private static Bitmap retryMatrixBitmap(Bitmap bitmap, int width, int height, Matrix matrix, boolean retry) {
        Bitmap reBitmap = null;
        try {
            reBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        } catch (OutOfMemoryError e) {
            Log.e(ImageUtil.TAG, "catch out of mem Matrix " + retry, e);
            // handle oom
//            ExceptionManager.getInstance().handle(e);
            if (retry) {
                reBitmap = ImageUtil.retryMatrixBitmap(bitmap, width, height, matrix, false);
            }
        }
        return reBitmap;
    }

    // 失败就传原图的压缩方式
    private static Bitmap retryOptionBitmap(BitmapFactory.Options options, String fileName, boolean retry) {
        Bitmap reBitmap = null;
        try {
            reBitmap = BitmapFactory.decodeFile(fileName, options);
        } catch (OutOfMemoryError e) {
            Log.e(ImageUtil.TAG, "catch out of mem Option " + retry, e);
            // handle oom
//            ExceptionManager.getInstance().handle(e);
            if (retry) {
                reBitmap = ImageUtil.retryOptionBitmap(options, fileName, false);
            }
        }
        return reBitmap;
    }

    // 最大缩小十倍的压缩方式
    private static Bitmap retryOptionBitmap(BitmapFactory.Options options, String fileName) {

        Bitmap reBitmap = null;
        int count = 0;
        while (true) {
            if (count > 0 && options.inSampleSize > 7) {
                return reBitmap;
            }
            try {
                reBitmap = BitmapFactory.decodeFile(fileName, options);
                Log.i("QZoneUpload", "options.inSampleSize ： " + options.inSampleSize);
                return reBitmap;
            } catch (OutOfMemoryError e) {
                Log.e(ImageUtil.TAG, "catch out of mem Option small options", e);
                // handle oom
//                ExceptionManager.getInstance().handle(e);
                options.inSampleSize = options.inSampleSize + 1;
                count++;
            }
        }
        // return reBitmap;
    }

    /**
     * 获取本地图片的大小
     * 
     * @param fileName
     * @return
     */
    public static Size getBitmapSize(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);
        options.inJustDecodeBounds = false;
        return new Size(options.outWidth, options.outHeight);
    }

    /**
     * 保存Bitmap到文件
     * 
     * @param bitmap
     * @param savePath
     * @return
     */
    public static boolean bitmapToFile(Bitmap bitmap, String savePath, int quality) {

        boolean resu = false;

        File file = new File(savePath);

        BufferedOutputStream bos = null;

        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));

            if (bitmap.compress(CompressFormat.JPEG, quality, bos)) {
                bos.flush();
                resu = true;
            } else {
                resu = false;
            }

        } catch (Exception e) {
            resu = false;
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e2) {
                    resu = false;
                }
            }
        }

        return resu;

    }

    /**
     * 防止decode大图时抛出outofmemery
     * 
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    public static Bitmap safeDecodeStream(String path, int width, int height) throws IOException {
        File file = new File(path);

        int scale = 1;
        BitmapFactory.Options options = null;
        InputStream in = null;
        if (width > 0 && height > 0) {
            options = new BitmapFactory.Options();
            in = new FileInputStream(file);
            // Decode image size without loading all data into memory
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(in, null, options);
            in.close();
            int w = options.outWidth;
            int h = options.outHeight;
            if (w > width && h > height) {
                float orignialScale = w / (float) h;
                float specifiedScale = width / (float) height;

                if (orignialScale > specifiedScale) { // 原始宽高比大于指定宽高比，按高度比例取
                    scale = h / height;
                } else if (orignialScale < specifiedScale) {
                    scale = w / width;
                } else {
                    scale = 1;
                }
            }
            // Decode with inSampleSize option
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;
        }
        in = new FileInputStream(file);
        Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
        in.close();
        return bitmap;
    }

    /**
     * 从assets中获取图片
     */
    public static final Bitmap getImageFromAsset(Context context, String fileName) {
        Bitmap bitmap = null;
        AssetManager manager = null;
        InputStream inputStream = null;
        try {
            manager = context.getResources().getAssets();
            inputStream = manager.open(fileName);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e2) {
            }
        }
        return bitmap;
    }
    
    public static String getRealPathFromContentURI(Context ctx, Uri contentUri) {
        if (contentUri == null) {
            return null;
        }
        String scheme = contentUri.getScheme();
        if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            return contentUri.getPath();

        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {

            String path = null;
            String[] proj = {MediaColumns.DATA};
            Cursor cursor = null;
            try {
                cursor = ctx.getContentResolver().query(contentUri, proj, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    path = cursor.getString(cursor.getColumnIndexOrThrow(MediaColumns.DATA));
                }

            } catch (Throwable e) {
                // empty
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return path;

        } else {
            return contentUri.toString();
        }
    }
    public static BitmapFactory.Options getImageOptions(InputStream in) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, opts);
        return opts;
    }
    //
    public static class Size {
        public int width;

        public int height;

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
    
//    public static Uri convertAlbumImageToTmp(Uri uri) {
//        if(uri == null)
//            return null;
//        ParcelFileDescriptor pfdInput = null;
//        try {
//            if (uri.getScheme().equals("file")) {
//                String path = uri.getPath();
//                pfdInput = ParcelFileDescriptor.open(new File(path), ParcelFileDescriptor.MODE_READ_ONLY);
//            } else {
//                pfdInput = SCRPlugin.getInstance().getContext().getContentResolver().openFileDescriptor(uri, "r");
//            }
//        } catch (Exception e) {
//            return null;
//        }
//
//        String tmpImageFilePath = PathUtil.getStorePath(SCRPlugin.getInstance().getContext(), PHOTO_TEMP) +"image.png";
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        if(pfdInput == null){
//            return null;
//        }
//        FileDescriptor fd = pfdInput.getFileDescriptor();
//
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFileDescriptor(fd, null, options);
//
////		if (options.outWidth > CROP_MAX_WIDTH || options.outHeight > CROP_MAX_HEIGHT) {
//        options.inSampleSize = computeSampleSize(options, THUMBNAIL_TARGET_SIZE, THUMBNAIL_MAX_NUM_PIXELS);
//        options.inJustDecodeBounds = false;
//        options.inDither = false;
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        Bitmap image = BitmapFactory.decodeFileDescriptor(fd, null, options);
//        File bFile = new File(tmpImageFilePath);
//        FileOutputStream fos = null;
//        try {
//            if (bFile.exists()) {
//                boolean dflag = bFile.delete();
//            }
//            boolean cflag = bFile.createNewFile();
//            fos = new FileOutputStream(bFile);
//            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            fos.flush();
//            uri = Uri.fromFile(bFile);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (fos != null)
//                    fos.close();
//                    pfdInput.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return uri;
//    }
    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }
    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength),
                Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
