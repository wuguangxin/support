package com.wuguangxin.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用工具类
 *
 * <p>Created by wuguangxin on 14/4/14 </p>
 */
public class Utils {
    private static final String TAG = "Utils";

    /**
     * 获取百分比
     *
     * @param num1 数值1
     * @param num2 数值2
     * @return 字符串
     */
    public static String fmtPercent(double num1, float num2) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0); // 保留小数位数
        if (num1 == 0.00 || num2 == 0.00) {
            return "0";
        }
        return numberFormat.format(num1 / num2 * 100);
    }

    /**
     * 获取进度
     *
     * @param num1 数值1
     * @param num2 数值2
     * @return 字符串
     */
    public static String fmtProgress(float num1, float num2) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0); // 设置精确到小数点后0位
        return numberFormat.format(num1 / num2 * 100);
    }

    /**
     * 验证密码。只能包含数字、大小写字母或者符号 实现：匹配是否有中文，如果有，则表示密码格式不正确
     *
     * @param context  上下文
     * @param password 要匹配的密码
     * @return 匹配是否通过
     */
    @Deprecated
    public static boolean matchPassword(Context context, String password) {
        String passwordUsableSign = "_@#$%";
        return password.matches("^[0-9a-zA-Z" + passwordUsableSign + "]{6,20}$");
    }

    /**
     * 弹出软键盘(貌似有BUG)
     *
     * @param context 上下文
     * @param isShow  是否显示软键盘
     */
    public static void showInputMethodKeyboard(Context context, boolean isShow) {
        if (isShow) {
            Logger.i(context, "显示软键盘");
            InputMethodManager immShow = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            immShow.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
        } else {
            Logger.i(context, "隐藏软键盘");
            InputMethodManager immHide = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            immHide.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * dip转换为px
     *
     * @param context  上下文
     * @param dipValue dip
     * @return px
     */
    public static int dip2px(Context context, float dipValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * density + 0.5f);
    }

    /**
     * dip转换为sp
     *
     * @param context 上下文
     * @param dpValue dip
     * @return sp
     */
    public static int dip2sp(Context context, float dpValue) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics()));
    }

    /**
     * px转换为dip
     *
     * @param context 上下文
     * @param pxValue px
     * @return dip
     */
    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px转换为sp
     *
     * @param context 上下文
     * @param pxValue px数值
     * @return sp
     */
    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * mm转换为px(X轴)
     *
     * @param context 上下文
     * @param value   mm数值
     * @return X轴的px值
     */
    public static float mm2pxX(Context context, float value) {
        return value * context.getResources().getDisplayMetrics().xdpi * (1.0f / 25.4f);
    }

    /**
     * mm转换为px(Y轴)
     *
     * @param context 上下文
     * @param value   mm数值
     * @return Y轴的px值
     */
    public static float mm2pxY(Context context, float value) {
        return value * context.getResources().getDisplayMetrics().ydpi * (1.0f / 25.4f);
    }

    /**
     * px转换为mm(X轴)
     *
     * @param context 上下文
     * @param value   px值
     * @return X轴的mm值
     */
    public static float px2mmX(Context context, float value) {
        return value * (1 / mm2pxX(context, 1));
    }

    /**
     * px转换为mm(Y轴)
     *
     * @param context 上下文
     * @param value   px值
     * @return Y轴的mm值
     */
    public static float px2mmY(Context context, float value) {
        return value * (1 / mm2pxY(context, 1));
    }

    /**
     * 定位光标位置到EditText文本的末尾
     *
     * @param mEditText EditText
     */
    public static void setFocusPosition(EditText mEditText) {
        if (mEditText == null) {
            return;
        }
        mEditText.requestFocus();
        Editable text = mEditText.getText();
        Selection.setSelection(text, text.length());
    }

    /**
     * 跳转界面
     *
     * @param context 上下文
     * @param clazz   跳转的目标Activity，带数据
     */
    public static void openActivity(Context context, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    /**
     * 获取两个点之间的距离
     *
     * @param event MotionEvent
     * @return 距离
     */
    @SuppressLint("NewApi")
    public static float getDistance(MotionEvent event) {
        float a = event.getX(1) - event.getX(0);
        float b = event.getY(1) - event.getY(0);
        return (float) Math.sqrt(a * a + b * b);    // 平方根(勾股定理)
    }

    /**
     * 获取两个点的中间点坐标， 第一个点的x坐标+ 上第二个点的x坐标 结果/2 就是他们的中心点， y坐标同理。
     *
     * @param event MotionEvent
     * @return 中间点坐标
     */
    @SuppressLint("NewApi")
    public static PointF getPoint(MotionEvent event) {
        float x = (event.getX(0) + event.getX(1)) / 2;
        float y = (event.getY(0) + event.getY(1)) / 2;
        return new PointF(x, y);
    }

    /**
     * 设置EditText的最大长度
     *
     * @param mEditText EditText
     * @param maxLength 最大长度
     */
    public static void setEditTextMaxLength(EditText mEditText, int maxLength) {
        if (mEditText != null) {
            mEditText.setFilters(new InputFilter[]{
                    new InputFilter.LengthFilter(maxLength)
            });
        }
    }

    /**
     * 把版本号转为数字。如1.2.5转换为010205，2.9.13转换为020913，再转换为int， 因为目前友盟的版本更新数据中并没有版本号,暂时这样做
     *
     * @param cacheVersionName 版本名称如1.2.5
     * @return 如1.2.5转换为010205
     */
    public static int versionName2Int(String cacheVersionName) {
        if (TextUtils.isEmpty(cacheVersionName)) {
            return 0;
        }
        String[] split = cacheVersionName.split("\\.");
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < split.length; i++) {
            String str = split[i];
            if (str.length() == 1) {
                stringBuffer.append("0").append(str);
            } else if (str.length() == 2) {
                stringBuffer.append(str);
            }
        }
        int parseInt = 0;
        String string = stringBuffer.toString();
        if (string != null) {
            try {
                parseInt = Integer.parseInt(string);
            } catch (NumberFormatException e) {
                Logger.i(TAG, "数字转换异常");
            }
        }
        return parseInt;
    }

    /**
     * 返回文字的长度，一个汉字算2个字符
     *
     * @param string 字符串
     * @return 长度
     */
    public static int getTextLength(String string) {
        try {
            return string.toString().getBytes("GBK").length;
        } catch (UnsupportedEncodingException e) {
            return string.length();
        }
    }

    /**
     * 重绘图片
     *
     * @param mActivity Activity
     * @param bitmap    Bitmap
     * @return Bitmap
     */
    public static Bitmap reDrawBitMap(Activity mActivity, Bitmap bitmap) {
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int rHeight = dm.heightPixels;
        int rWidth = dm.widthPixels;
//		float rHeight = dm.heightPixels / dm.density + 0.5f;
//		float rWidth = dm.widthPixels / dm.density + 0.5f;
//		int height = bitmap.getScaledHeight(dm);
//		int width = bitmap.getScaledWidth(dm);
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        float zoomScale;
        if (rWidth / rHeight > width / height) {//以高为准
            zoomScale = ((float) rHeight) / height;
        } else { //if(rWidth/rHeight<width/height)//以宽为准
            zoomScale = ((float) rWidth) / width;
        }
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(zoomScale, zoomScale);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (resizedBitmap == null) {
            return null;
        }
        return resizedBitmap;
    }

    /**
     * 冒泡排序
     *
     * @param arr int数组
     * @return 排序后的数组
     */
    public static int[] sortBubbles(int[] arr) {
        for (int x = 0; x < arr.length - 1; x++) {
            //-x:让每一次比较的元素减少。-1:避免角标越界
            for (int y = 0; y < arr.length - x - 1; y++) {
                if (arr[y] > arr[y + 1]) {
                    swap(arr, y, y + 1);
                }
            }
        }
        return arr;
    }

    /**
     * 冒泡排序的置换函数
     *
     * @param arr 原数组
     * @param a   a
     * @param b   b
     */
    private static void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    /**
     * 给1个分钟数，计算是多少小时多少分
     *
     * @param totalMinute 总分钟数，如100（分钟）
     * @return 例: 1小时40分钟
     */
    public static String getHourAndMinute(Long totalMinute) {
        if (totalMinute <= 0) {
            return "0分钟";
        }
        Long h = totalMinute / 60L;
        Long m = totalMinute % 60L;
        StringBuilder time = new StringBuilder().append(h).append("小时").append(m).append("分钟");
        return time.toString();
    }

    /**
     * 加密姓名，只显示第一个字（如吴光新=吴**，吴新=吴*）
     *
     * @param name 名字
     * @return 字符串
     */
    public static String encryptNameKeepFirst(String name) {
        if (!TextUtils.isEmpty(name)) {
            String firstStr = name.substring(1, name.length()); // 吴光新
            StringBuilder nameStr = new StringBuilder(name.substring(0, 1));
            for (int i = 0; i < firstStr.length(); i++) {
                nameStr.append("*");
            }
            return nameStr.toString();
        }
        return null;
    }

    /**
     * 加密姓名，只显示最后一个字（如吴光新=**新，吴新=*新）
     *
     * @param name 名字
     * @return 字符串
     */
    public static String encryptNameKeepLast(String name) {
        if (!TextUtils.isEmpty(name)) {
            if (name.length() < 2) {
                return "*";
            }
            String beforeStr = name.substring(0, name.length() - 1);
            StringBuilder newName = new StringBuilder();
            for (int i = 0; i < beforeStr.length(); i++) {
                newName.append("*");
            }
            newName.append(name.substring(name.length() - 1, name.length()));
            return newName.toString();
        }
        return null;
    }

    /**
     * 验证客户姓名："(([\u4E00-\u9FA5]{2,7})|([a-zA-Z]{3,15}))"
     *
     * @param name 名字
     * @return 是否匹配
     */
    public static boolean verifyUserName(String name) {
        String regx = "(([\u4E00-\u9FA5]{2,7})|([a-zA-Z]{3,15}))";
        return Pattern.matches(regx, name);
    }

    /**
     * 复制文本到剪贴板
     *
     * @param context 上下文
     * @param text    文本
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static void copyString(Context context, String text) {
        // SDK= 11以下
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
            // 得到剪贴板管理器
            android.text.ClipboardManager cmb = (android.text.ClipboardManager) context.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(text);
        } else {
            android.content.ClipboardManager cmb = (android.content.ClipboardManager) context.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(text);
        }
    }

    /**
     * 获取比某个数小的随机数
     *
     * @param bound
     * @return 随机数
     */
    public static int getRandom(int bound) {
        return new Random().nextInt(bound);
    }

    /**
     * 获取两个数之间的随机数（包含最小和最大数）
     *
     * @param min 最小的数
     * @param max 最大的数
     * @return 随机数
     */
    public static int getRandom(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    /**
     * 获取两个数之间的随机数（包含最小和最大数，并补位为最大长度）
     *
     * @param min 最小的数
     * @param max 最大的数
     * @return 例如获取0-99999 的随机数，随机到的数字是456，则返回00456
     */
    public static String getRandomString(int min, int max) { //0-99999
        int num = getRandom(min, max); // 6666
        int len = String.valueOf(max).length(); //5
        return String.format(Locale.getDefault(),"%0"+len+"d", num);
    }

    /**
     * 获取数组中的随机一个对象
     *
     * @param array 数组
     * @return Object
     */
    public static <T> T getRandom(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        int random = getRandom(array.length + 1);
        if (random > 0) {
            random -= 1;
        }
        return array[random];
    }

    /**
     * 获取List中的随机一个对象
     *
     * @param list 列表
     * @return Object
     */
    public static <T> T getRandom(List<T> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        int random = getRandom(list.size() + 1);
        if (random > 0) {
            random -= 1;
        }
        return list.get(random);
    }

    /**
     * 判断软键盘是否是弹出状态
     *
     * @param context 上下文
     * @return 软键盘是否显示
     */
    public static boolean isShowSoftKey(Context context) {
        return ((Activity) context).getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
    }

    /**
     * 从系统默认的浏览器打开网页
     *
     * @param context 上下文
     * @param url     地址
     */
    public static void openWebFromSystem(Context context, String url) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        intent.setAction(Intent.ACTION_VIEW);
        context.startActivity(intent);
    }

    /**
     * 是否包含Emoji表情
     *
     * @param string 文本
     * @return 是否包含
     */
    public static boolean isEmoji(String string) {
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }

    /**
     * 判断是否含有中文字符
     *
     * @param string 文本
     * @return 是否包含
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

    /**
     * 判断800ms内是否重复执行
     */
    private static long lastClickTime;
    public static boolean isFastClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}