package com.wuguangxin.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * 二维码生成工具类。
 * Created by wuguangxin on 16/11/15.
 */
public class ZXingUtils {


    /**
     * 创建二维码
     *
     * @param text 内容
     * @return 返回一个默认尺寸为500*500的二维码图像
     */
    public static Bitmap createQrCode(String text) {
        return createQrCode(text, 500, 500);
    }

    /**
     * 创建二维码
     * @param text 文本内容
     * @param width 二维码宽度
     * @param height 二维码高度
     * @return
     */
    public static Bitmap createQrCode(String text, int width, int height) {
        try {
            // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
            BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
            int matrixWidth = matrix.getWidth();
            int matrixHeight = matrix.getHeight();
            // 二维矩阵转为一维像素数组,也就是一直横着排列
            int[] pixels = new int[matrixWidth * matrixHeight];
            for (int y = 0; y < matrixHeight; y++) {
                for (int x = 0; x < matrixWidth; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * matrixWidth + x] = 0xff000000;
                    } else {
                        pixels[y * matrixWidth + x] = 0xffff0000;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(matrixWidth, matrixHeight, Bitmap.Config.ARGB_8888);
            // 通过像素数组生成bitmap,具体参考api
            bitmap.setPixels(pixels, 0, matrixWidth, 0, 0, matrixWidth, matrixHeight);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 创建条形码，目前不支持中文生成条形码
     *
     * @param text 内容
     * @return 返回一个尺寸为500*200的图像
     */
    public static Bitmap createBarCode(String text) {
        return createBarCode(text, 500, 200);
    }

    /**
     * 创建条形码，目前不支持中文生成条形码
     * @param text 条码内容（只能是数字、字母）
     * @param width 条码宽度
     * @param height 条码高度
     * @return
     */
    public static Bitmap createBarCode(String text, int width, int height) {
        if (isCNString(text)) {
            return null;
        }
        try {
            // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
            BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.CODE_128, width, height);
            int matrixWidth = matrix.getWidth();
            int matrixHeight = matrix.getHeight();
            int[] pixels = new int[matrixWidth * matrixHeight];
            for (int y = 0; y < matrixHeight; y++) {
                for (int x = 0; x < matrixWidth; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * matrixWidth + x] = 0xff000000;
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(matrixWidth, matrixHeight, Bitmap.Config.ARGB_8888);
            // 通过像素数组生成bitmap,具体参考api
            bitmap.setPixels(pixels, 0, matrixWidth, 0, 0, matrixWidth, matrixHeight);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断是否含有中文字符
     *
     * @param string 字符串
     * @return
     */
    public static boolean isCNString(String string) {
        for (int i = 0; i < string.length(); i++) {
            int c = string.charAt(i);
            if ((19968 <= c && c < 40623)) {
                return true;
            }
        }
        return false;
    }

}
