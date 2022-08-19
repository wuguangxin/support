package com.wuguangxin.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ScrollView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BitmapUtils {

    /**
     * 相册路径
     */
    private static final String GALLERY_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "Camera";

    private static final String[] STORE_IMAGES = {MediaStore.Images.Thumbnails._ID,};

    /**
     * 日期格式化（格式为：20200101_105520）
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);

    private static String tempLastFileName;

    /**
     * 旋转图片.
     *
     * @param source 源图片
     * @param degree 旋转角度
     * @param flipHorizontal 是否水平填充
     * @param recycle 是否回收bitmap
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap source, int degree, boolean flipHorizontal, boolean recycle) {
        if (degree == 0 && !flipHorizontal) {
            return source;
        }
        long start = System.currentTimeMillis();
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        if (flipHorizontal) {
            matrix.postScale(-1, 1);
        }
//        Log.d(TAG, "source width: " + source.getWidth() + ", height: " + source.getHeight());
//        Log.d(TAG, "rotateBitmap: degree: " + degree);
        Bitmap rotateBitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
//        Log.d(TAG, "rotate width: " + rotateBitmap.getWidth() + ", height: " + rotateBitmap.getHeight());
        if (recycle) {
            source.recycle();
        }
//        long end = System.currentTimeMillis() - start;
//        System.out.println("ImageUtils.rotateBitmap(Bitmap source, int degree, boolean flipHorizontal, boolean recycle) 耗时：" + end + "毫秒");
        return rotateBitmap;
    }

    /**
     * 创建文件File实例对象
     *
     * @return
     */
    private static File createFile() {
//        long start = System.currentTimeMillis();
        String fileName = String.format("IMG_%s.jpg", DATE_FORMAT.format(new Date(System.currentTimeMillis())));
        if (tempLastFileName != null && tempLastFileName.equals(fileName)) {
            fileName += "_(2)";
        }
        tempLastFileName = fileName;
        File file = new File(GALLERY_PATH, fileName);

        if (!file.exists()) {
            try {
                if (file.getParentFile() != null && !file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        long end = System.currentTimeMillis() - start;
//        System.out.println("ImageUtils.createFile(）耗时：" + end + "毫秒");
        return file;
    }

    /**
     * 把 Byte 转为 File，并保存图像到相册。
     *
     * @param context 上下文
     * @param bytes bytes类型的照片数据
     */
    public static File bytesToFile(Context context, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
//        long start = System.currentTimeMillis();
        FileOutputStream os = null;
        try {
            File outFile = createFile();
            os = new FileOutputStream(outFile);
            os.write(bytes);
            os.flush();
            os.close();
            insertToDB(context, outFile.getAbsolutePath());
            return outFile;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            long end = System.currentTimeMillis() - start;
//            System.out.println("ImageUtils.bytesToFile(Context context, byte[] bytes) 耗时：" + end + "毫秒");
        }
        return null;
    }

    /**
     * 把bitmap转为file保存到相册。
     *
     * @param context 上下文
     * @param bitmap Bitmap
     */
    public static File bitmapToFile(Context context, Bitmap bitmap) {
//        long start = System.currentTimeMillis();
        File file = bitmapToFile(context, bitmap, createFile());
//        long end = System.currentTimeMillis() - start;
//        System.out.println("ImageUtils.bitmapToFile(Context context, Bitmap bitmap) 耗时：" + end + "毫秒");
        return file;
    }

    /**
     * 把Bitmap转为File图像到本地相册。
     *
     * @param context 上下文
     * @param sourceBitmap Bitmap
     * @param targetFile 目标文件
     */
    public static File bitmapToFile(Context context, Bitmap sourceBitmap, File targetFile) {
        if (context == null || sourceBitmap == null || targetFile == null) {
            return null;
        }
//        long start = System.currentTimeMillis();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);
            // 输出为png，相同取景，耗时1300毫秒，输出为jpeg则只需要250毫秒左右
            // 质量100和50，清晰度无明显差别，但相同取景下，50的120kb左右，100则需要1200kb左右。相差10倍
            // 所以一般压缩使用jpeg和50即可
            boolean success = sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            if (success) {
                insertToDB(context, targetFile.getAbsolutePath());
            }
            return targetFile;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            long end = System.currentTimeMillis() - start;
//            System.out.println("ImageUtils.bitmapToFile(Context context, Bitmap sourceBitmap, File targetFile) 耗时：" + end + "毫秒");
        }
        return null;
    }


    /**
     * 把 View 转为 Bitmap 图片
     *
     * @param view UI界面的View
     * @return
     */
    public static Bitmap viewToBitmap(View view) {
        if (view == null) {
            return null;
        }
//        long start = System.currentTimeMillis();
        int w = view.getWidth();
        int h = view.getHeight();
        if (w == 0 || h == 0) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //如果不设置canvas画布为白色，则生成透明
        canvas.drawColor(Color.WHITE);
        // view.layout(0, 0, w, h);
        view.draw(canvas);
//        long end = System.currentTimeMillis() - start;
//        System.out.println("ImageUtils.viewToBitmap(View view) 耗时：" + end + "毫秒");
        return bitmap;
    }

    /**
     * 把 View 转 File 文件保存到相册。
     */
    public static File viewToFile(View view) {
        return view == null ? null : bitmapToFile(view.getContext(), viewToBitmap(view));
    }

    /**
     * 获取scrollview的截屏
     */
    public static Bitmap scrollViewScreenShot(ScrollView scrollView) {
        if (scrollView == null) {
            return null;
        }
//        long start = System.currentTimeMillis();
        int h = 0;
        Bitmap bitmap = null;
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
        }
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
//        long end = System.currentTimeMillis() - start;
//        System.out.println("ImageUtils.scrollViewScreenShot(ScrollView scrollView) 耗时：" + end + "毫秒");
        return bitmap;
    }

    /**
     * 把二维码图片qrCodeBitmap合并到主图mainBitmap上。（仅供参考，需根据实际业务来调整相关尺寸参数）
     *
     * @param mainBitmap 主图
     * @param qrCodeBitmap 二维码图片
     * @return
     */
    public static Bitmap mergeBitmap(Bitmap mainBitmap, Bitmap qrCodeBitmap) {
//        long start = System.currentTimeMillis();
        // Bitmap bitmap = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), bitmap1.getConfig());

        // 业务场景参考：加入按比例缩放背景图到1080*2170，那么二维码的大小则为270*270
        Bitmap bitmapBG = scaleBitmap(mainBitmap, 1080, 2170);
        // 给二维码进行缩放，二维码在背景图的宽高是270x270px
        Bitmap bitmapQR = scaleBitmap(qrCodeBitmap, 270, 270);
        Canvas canvas = new Canvas(bitmapBG); // 生成画布
        canvas.drawBitmap(bitmapBG, 0, 0, null);
        canvas.drawBitmap(bitmapQR, 87, bitmapBG.getHeight() - 326, null);
        try {
            if (!bitmapQR.isRecycled()) {
                bitmapQR.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        long end = System.currentTimeMillis() - start;
//        System.out.println("ImageUtils.mergeBitmap(Bitmap bitmap1, Bitmap bitmap2) 耗时：" + end + "毫秒");
        return bitmapBG;
    }


    /**
     * 合成图片和二维码，同 {@link #mergeBitmap(Bitmap, Bitmap)}
     *
     * @param bitmap1 主图
     * @param bitmap2 二维码图
     * @return
     */
    public static Bitmap montageBitmap(Bitmap bitmap1, Bitmap bitmap2) {
        float width1 = bitmap1.getWidth();
        float newHeight2 = bitmap2.getHeight() * width1 / bitmap2.getWidth();
        Bitmap bitmapQR = scaleBitmap(bitmap2, (int) width1, (int) newHeight2);
        Bitmap bitmap = Bitmap.createBitmap(bitmap1.getWidth(), (int) (bitmap1.getHeight() + newHeight2), bitmap1.getConfig());
        Canvas canvas = new Canvas(bitmap); // 生成画布
        canvas.drawBitmap(bitmap1, 0, 0, null);
        canvas.drawBitmap(bitmapQR, 0, bitmap1.getHeight(), null);
        try {
            if (!bitmapQR.isRecycled()) {
                bitmapQR.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 根据给定的宽和高进行拉伸
     *
     * @param origin 原图
     * @param newWidth 新图的宽
     * @param newHeight 新图的高
     * @return new Bitmap
     */
    private static Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
//        long start = System.currentTimeMillis();
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
//        try {
//            if (!origin.isRecycled()) {
//                origin.recycle();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        long end = System.currentTimeMillis() - start;
//        System.out.println("ImageUtils.scaleBitmap(Bitmap origin, int newWidth, int newHeight) 耗时：" + end + "毫秒");
        return newBM;
    }

    /**
     * 把图片加入到系统数据库（刷新到相册）。
     *
     * @param context 上下文
     * @param pictureAbsolutePath 图片的绝对路径
     */
    public static void insertToDB(Context context, String pictureAbsolutePath) {
//        long start = System.currentTimeMillis();
        ContentValues values = new ContentValues();
        ContentResolver resolver = context.getContentResolver();
        values.put(MediaStore.Images.ImageColumns.DATA, pictureAbsolutePath);
        values.put(MediaStore.Images.ImageColumns.TITLE, pictureAbsolutePath.substring(pictureAbsolutePath.lastIndexOf("/") + 1));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, System.currentTimeMillis());
        }
        values.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/jpeg");
        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//        long end = System.currentTimeMillis() - start;
//        System.out.println("ImageUtils.insertToDB(Context context, String pictureAbsolutePath) 耗时：" + end + "毫秒");
    }

    /**
     * 获取最近一次拍摄的缩略图
     *
     * @param context 上下文
     * @return
     */
    public static Bitmap getLatestThumbBitmap(Context context) {
        Bitmap bitmap = null;
        // 按照时间顺序降序查询
        Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES, null, null, MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC");
        boolean first = cursor.moveToFirst();
        if (first) {
            long id = cursor.getLong(0);
            bitmap = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
//            Log.d(TAG, "bitmap width: " + bitmap.getWidth());
//            Log.d(TAG, "bitmap height: " + bitmap.getHeight());
        }
        cursor.close();
        return bitmap;
    }

    private static List<String> imageSuffixList = Arrays.asList(
            "bmp", "jpg", "jpeg", "png", "tif", "gif", "pcx", "tga", "exif", "fpx", "svg", "psd", "cdr", "pcd", "dxf", "ufo", "eps", "ai", "raw", "WMF", "webp",
            "BMP", "JPG", "JPEG", "PNG", "TIF", "GIF", "PCX", "TGA", "EXIF", "FPX", "SVG", "PSD", "CDR", "PCD", "DXF", "UFO", "EPS", "AI", "RAW", "WMF", "WEBP");

    public static boolean isImage(String url) {
        String key = url.substring(url.lastIndexOf(".") + 1);
        return imageSuffixList.contains(key);
    }

    /**
     * 获取Bitmap大小(在内存中占的大小是文件本身大小的4倍，所以计算实际大小时 /4)
     *
     * @param bitmap
     * @return
     */
    public static int getBitmapSize(Bitmap bitmap) {
        int bitmapSize = 0;
        if (bitmap != null) {
            if (Build.VERSION.SDK_INT >= 17) {
                bitmapSize = bitmap.getByteCount();
            } else {
                bitmapSize = bitmap.getRowBytes() * bitmap.getHeight(); // HC-MR1 以前
            }
        }
        return bitmapSize;
    }

    /**
     * 把图片的角设置为圆角（未测试）
     *
     * @param bitmap Bitmap图片
     * @param roundSize 圆角的大小（PX）
     * @return
     */
    public static Bitmap setBitmapRound(Bitmap bitmap, int roundSize) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(250, 0, 0, 0);
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectF, roundSize, roundSize, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
