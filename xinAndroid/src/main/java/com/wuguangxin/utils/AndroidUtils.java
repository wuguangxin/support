package com.wuguangxin.utils;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StatFs;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.List;

/**
 * Android系统相关的一些工具方法
 *
 * @author wuguangxin
 */
@SuppressLint("NewApi")
public class AndroidUtils {
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final int ERROR = -1;
    private static ConnectivityManager mConnectivityManager;
    private static TelephonyManager mTelephonyManager;
    private static ActivityManager mActivityManager;
    private static WifiManager wifiManager;
    private static MemoryInfo mMemoryInfo;

	/*
    int SDK4_0 = VERSION_CODES.BASE; 					// Android 1.0		BASE
	int SDK4_1 = VERSION_CODES.BASE_1_1; 				// Android 1.1		BASE
	int SDK4_2 = VERSION_CODES.CUPCAKE; 				// Android 1.5		CUPCAKE
	int SDK4_3 = VERSION_CODES.DONUT; 					// Android 1.6		DONUT
	int SDK5 = VERSION_CODES.ECLAIR; 					// Android 2.0		ECLAIR
	int SDK6 = VERSION_CODES.ECLAIR_0_1; 				// Android 2.0.1	ECLAIR
	int SDK7 = VERSION_CODES.ECLAIR_MR1; 				// Android 2.1		ECLAIR
	int SDK8 = VERSION_CODES.FROYO; 					// Android 2.2		FROYO
	int SDK9 = VERSION_CODES.GINGERBREAD;				// Android 2.3		GINGERBREAD
	int SDK10 = VERSION_CODES.GINGERBREAD_MR1;			// Android 2.3.3	GINGERBREAD
	int SDK11 = VERSION_CODES.HONEYCOMB;				// Android 3.0		HONEYCOMB
	int SDK12 = VERSION_CODES.HONEYCOMB_MR1;			// Android 3.1		HONEYCOMB
	int SDK13 = VERSION_CODES.HONEYCOMB_MR2;			// Android 3.2		HONEYCOMB
	int SDK14 = VERSION_CODES.ICE_CREAM_SANDWICH;		// Android 4.0 		ICE_CREAM_SANDWICH
	int SDK15 = VERSION_CODES.ICE_CREAM_SANDWICH_MR1;	// Android 4.0.3	ICE_CREAM_SANDWICH
	int SDK16 = VERSION_CODES.JELLY_BEAN;				// Android 4.1		JELLY_BEAN
	int SDK17 = VERSION_CODES.JELLY_BEAN_MR1;			// Android 4.2 		JELLY_BEAN
	int SDK18 = VERSION_CODES.JELLY_BEAN_MR2;			// Android 4.3 		JELLY_BEAN
	int SDK19 = VERSION_CODES.KITKAT;					// Android 4.4 		KITKAT
	int SDK20 = 20;										// Android 4.4 		KITKAT Wear
	int SDK21 = 21;										// Android 5.0		Lollipop
	int SDK22 = 22;										// Android 5.1		Lollipop
	int SDK23 = 23;										// Android 6.0		Marshmallow
		SDKN  = N;										// Android N		Preview
	*/

    /**
     * 获取SDK版本号
     *
     * @return
     */
    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 判断SD卡是否挂载 使用Environment.getExternalStorageState() 返回sd卡的状态， Environment.MEDIA_MOUNTED 表示被挂载
     *
     * @return
     */
    public static boolean isExtStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取手机版本号
     *
     * @return
     */
    public static String isRelease() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取应用程序包名
     *
     * @return
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 判断是否已经安装SD卡
     *
     * @return
     */
    public static boolean isExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取正在运行的进程信息列表
     *
     * @return
     */
    public static List<RunningAppProcessInfo> getRunningProcessList(Context context) {
        return getActivityManager(context).getRunningAppProcesses();
    }

    /**
     * 获取正在运行的进程数量
     *
     * @return
     */
    public static int getRunningProcessSize(Context context) {
        return getRunningProcessList(context).size();
    }

    /**
     * 获取手机内存路径
     *
     * @return
     */
    public static String getInternalMemoryPath() {
        File dataDirectory = Environment.getDataDirectory();
        if (dataDirectory.exists()) {
            return dataDirectory.getPath();
        }
        return null;
    }

    /**
     * 获取扩展SD存储卡路径
     *
     * @return
     */
    public static String getExternalStoragePath() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        if (externalStorageDirectory.exists()) {
            return externalStorageDirectory.getPath();
        }
        return null;
    }

    public static ActivityManager getActivityManager(Context context) {
        if (mActivityManager == null) {
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return mActivityManager;
    }

    /**
     * 获取手机的总RAM（运存） 获取失败返回0
     *
     * @param context
     * @return
     */
    @SuppressLint("NewApi")
    public static long getTotalRAM(Context context) {
        if (getSDKCode() >= 16) {
            mMemoryInfo = new MemoryInfo();
            getActivityManager(context).getMemoryInfo(mMemoryInfo);
            return mMemoryInfo.totalMem;
        }
        // SDK16以下
        try {
            FileReader localFileReader = new FileReader("/proc/meminfo"); // /proc/meminfo系统内存信息文件
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            String lineStr = localBufferedReader.readLine();
            long totalRAM = 0;
            if (lineStr != null) {
                String[] lineStrs = lineStr.split("\\s+");
                if (lineStrs.length >= 2) {
                    String lineStrs1 = lineStrs[1];
                    if (lineStrs1 != null) {
                        totalRAM = Long.parseLong(lineStrs1) * 1024; // 获得的是KB，*1024转为byte
                    }
                }
            }
            localBufferedReader.close();
            return totalRAM;
        } catch (IOException e) {
        }
        return 0;
    }

    /**
     * 获取手机的可用RAM（运存）
     *
     * @param context
     * @return
     */
    public static long getAvailRAM(Context context) {
        mMemoryInfo = new MemoryInfo();
        getActivityManager(context).getMemoryInfo(mMemoryInfo);
        return mMemoryInfo.availMem;
    }

    /**
     * 获取手机内存总容量
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static long getTotalInternalMemorySize() {
        String path = Environment.getDataDirectory().getPath();
        StatFs stat = new StatFs(path);
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 获取手机内存剩余容量
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static long getAvailInternalMemorySize() {
        String path = Environment.getDataDirectory().getPath();
        StatFs stat = new StatFs(path);
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }

    /**
     * 获取内置存储卡总容量
     *
     * @return 如果已安装SD卡，则返回SD卡总容量，否则返回-1
     */
    @SuppressWarnings("deprecation")
    public static long getTotalExternalMemorySize() {
        if (isExistSDCard()) {
            String path = Environment.getExternalStorageDirectory().getPath();
            StatFs stat = new StatFs(path);
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        }
        return ERROR;
    }

    /**
     * 获取内置存储卡剩余容量 (可参考源码中的 Settings）
     *
     * @return 不存在则返回 -1
     */
    @SuppressWarnings("deprecation")
    public static long getAvailExternalMemorySize() {
        if (isExistSDCard()) {
            String path = Environment.getExternalStorageDirectory().getPath();
            StatFs stat = new StatFs(path); // 路径
            long availableBlocks = stat.getAvailableBlocks(); // 获取区块的个数
            long blockSize = stat.getBlockSize(); // 获取单个区块的大小
            return blockSize * availableBlocks; // 总大小
        } else {
            return ERROR;
        }
    }

    /**
     * 判断SD卡下external_sd文件夹的总大小
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static long getTotalExternal_SDMemorySize() {
        if (isExistSDCard()) {
            String path = Environment.getExternalStorageDirectory().getPath();
            File externalSD = new File(path + "/external_sd");
            if (externalSD.exists() && externalSD.isDirectory()) {
                StatFs stat = new StatFs(path + "/external_sd");
                long blockSize = stat.getBlockSize();
                long totalBlocks = stat.getBlockCount();
                long resultSize = totalBlocks * blockSize;
                if (getTotalExternalMemorySize() != -1 && getTotalExternalMemorySize() != resultSize) {
                    return resultSize;
                }
            }
        }
        return ERROR;
    }

    /**
     * 判断SD卡下external_sd文件夹的可用大小
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static long getAvailableExternal_SDMemorySize() {
        if (isExistSDCard()) {
            String path = Environment.getExternalStorageDirectory().getPath();
            File externalSD = new File(path + "/external_sd");
            if (externalSD.exists() && externalSD.isDirectory()) {
                StatFs stat = new StatFs(path + "/external_sd");
                long blockSize = stat.getBlockSize();
                long availableBlocks = stat.getAvailableBlocks();
                long resultSize = availableBlocks * blockSize;
                if (getAvailExternalMemorySize() != -1 && getAvailExternalMemorySize() != resultSize) {
                    return resultSize;
                }
            }
        }
        return ERROR;
    }

    // *********************************************************************

    /**
     * 获取设备唯一标识ID
     *
     * @return
     */
    public static String getDeviceId(Context context) {
        try {
            mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return mTelephonyManager.getDeviceId();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前应用程序版本名称
     *
     * @return 如 1.0.0
     */
    public static String getVersionName(Context context) {
        try {
            PackageInfo packInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo mPackageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return mPackageInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取SDK版本号
     *
     * @return
     */
    public static int getSDKCode() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 判断当前设备是否是模拟器。如果是模拟器返回TRUE，不是返回FALSE
     *
     * @param context
     * @return
     */
    public static boolean isMobilePhone(Context context) {
        try {
            String device_id = getDeviceId(context);
            if (device_id != null && device_id.equals("000000000000000")) {
                return false;
            }
            return !((Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 根据WifiManager获取MAC地址
     *
     * @param context
     * @return
     */
    public static String getMac(Context context) {
        String result = null;
        try {
            result = null;
            wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            result = wifiInfo.getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 通过网络接口取
     * @return
     */
    public static String getMac() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0"))
                    continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return null;
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获取本机号码，如果是双卡手机，获取的将是卡槽1的号码
     *
     * @return
     */
    public static String getPhoneNumber(Context context) {
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneString = mTelephonyManager.getLine1Number();
        if (phoneString != null) {
            return phoneString.replace("+86", "");
        }
        return null;
    }

    /**
     * 获取联系人电话 <br/>
     * 参考http://www.2cto.com/kf/201109/104686.html <br/>
     * @param cursor
     * @return
     */
    public static String getContactPhone(Context context, Cursor cursor){
        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        String phoneResult = "";
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人的电话号码的cursor;
            Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
            if (phones.moveToFirst()) {
                // 遍历所有的电话号码
                for (; !phones.isAfterLast(); phones.moveToNext()) {
                    int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String phoneNumber = phones.getString(index);
                    phoneResult = phoneNumber;
                }
                if (!phones.isClosed()) {
                    phones.close();
                }
            }
        }
        return phoneResult;
    }

    /**
     * 判断网络是否是WIFI网络
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            int type = activeNetworkInfo.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    /**
     * 得到屏幕宽度
     *
     * @return 单位:px
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 得到屏幕高度
     *
     * @return 单位:px
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 判断是否有任何可用的网络
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.isConnected();
        }
        return false;
    }

    /**
     * 判断是否有可以连接的WIFI
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo != null) {
            return wifiNetworkInfo.isConnected();
        }
        return false;
    }

    /**
     * 判断wifi的连接状态
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null) {
            return networkInfo.isConnected();
        }
        return false;
    }

    /**
     * 判断基站的连接状态
     *
     * @param context
     * @return
     */
    public static boolean isBaseStateConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null) {
            return networkInfo.isConnected();
        }
        return false;
    }

    /**
     * 复制文件
     *
     * @param sourceFile 源 {@link File}
     * @param destinationFile 目标 {@link File}
     * @throws IOException
     */
    public static void copyFile(File sourceFile, File destinationFile) throws IOException {
        FileInputStream in = new FileInputStream(sourceFile);
        FileOutputStream out = new FileOutputStream(destinationFile);
        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
        in.close();
        out.close();
    }

    /**
     * 从一个输入流复制到一个输出流（默认缓冲大小 8192 B ）
     *
     * @param input 输入流
     * @param output 输出流
     * @return
     * @throws IOException
     */
    public static int copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * 在线程池中执行一个AsyncTask Execute an {@link AsyncTask} on a thread pool.
     *
     * @param task AsyncTask
     * @param args 参数
     */
    @SafeVarargs
    @TargetApi(11)
    public static <T> void executeAsyncTask(AsyncTask<T, ?, ?> task, T... args) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, args);
        } else {
            task.execute(args);
        }
    }

    /**
     * 通过HttpURLConnection连接到指定的URL地址，返回一个InputStream，实用于下载图片等..
     */
    public static InputStream downloadUrl(String urlString) throws IOException {
        HttpURLConnection conn = buildHttpUrlConnection(urlString);
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }

    /**
     * 返回已个HttpURLConnection对象
     */
    public static HttpURLConnection buildHttpUrlConnection(String urlString) throws MalformedURLException, IOException {
        AndroidUtils.disableConnectionReuseIfNecessary();
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000); // 读取超时
        conn.setConnectTimeout(15000); //连接超时
        conn.setDoInput(true);
        conn.setRequestMethod("GET");
        return conn;
    }

    /**
     * 在Android 2.2版本之前，HttpURLConnection一直存在着一些令人厌烦的bug，
     * 比如说对一个可读的InputStream调用close()方法时，就有可能会导致连接池失效了，
     * 那么我们通常的解决办法就是直接禁用掉连接池的功能。
     */
    public static void disableConnectionReuseIfNecessary() {
        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO)) { // 小于2.2
            System.setProperty("http.keepAlive", "false");
        }
    }

    /**
     * 判断程序是否在前台运行
     * @return
     */
    public static boolean isAppOnForeground(Context context) {
        try {
            List<RunningAppProcessInfo> appList = getActivityManager(context).getRunningAppProcesses();
            if (appList == null || appList.isEmpty()) return false;
            final String packageName = context.getPackageName();
            for (RunningAppProcessInfo app : appList) {
                Logger.e("AAA", "app.processName="+ app.processName + " app.importance="+app.importance);
                if (app.processName.equals(packageName) && app.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断程序是否在后台运行
     * @return
     */
    public static boolean isAppOnBackground(Context context) {
        return !isAppOnForeground(context);
    }

    /**
     * 判断当前应用程序是否处于系统栈顶
     */
    public static boolean isTopActivity(final Context context) {
        if (context == null) return false;
        String curPackageName = context.getPackageName();
        String topPackageName = AndroidUtils.getAppTopActivityPackageName(context);
        return curPackageName.equals(topPackageName);
    }

    /**
     * 获取当前运行的APP中处于栈顶的APP包名
     */
    public static String getAppTopActivityPackageName(final Context context) {
        if (context == null) return null;
        try {
            // 该方法从Android L起限制访问，看 http://blog.csdn.net/hyhyl1990/article/details/45700447
            List<ActivityManager.RunningTaskInfo> tasks = getActivityManager(context).getRunningTasks(1);
            if (tasks != null && !tasks.isEmpty()) {
                return tasks.get(0).topActivity.getPackageName();
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 获取当前运行的APP中处于栈顶的APP包名
     */
    public static String getRunningAppList(final Context context) {
        if (context == null) return null;
        try {
            // 该方法从Android L起限制访问，看 http://blog.csdn.net/hyhyl1990/article/details/45700447
            List<ActivityManager.RunningTaskInfo> tasks = getActivityManager(context).getRunningTasks(10);
            if (tasks != null && !tasks.isEmpty()) {
                for (int i = 0; i < tasks.size(); i++) {
                    Logger.e("AndroidUtils", i + " " + tasks.get(i).topActivity.getPackageName());
                }
            }
        } catch (Exception e) {

        }
        return null;
    }


    /**
     * 让App在后台运行
     *
     * @param context
     */
    public static void setAppRunInBackground(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(intent);
    }

    /**
     * 获取当前应用程序的包名
     *
     * @param context
     * @return
     */
    public static String getAppPackageName(Context context) {
        // Android 提供了一个API以让应用程序向系统查询包名信息，使用 PackageManager 的 getPackageInfo(java.lang.String, int)方法
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.packageName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取设备ID和MAC信息（注册友盟测试设备使用）
     *
     * @param context
     * @return
     */
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = tm.getDeviceId();
            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            String mac = wifi.getConnectionInfo().getMacAddress();
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            json.put("mac", mac);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断按下的是否是返回(back)键
     *
     * @param keyCode
     * @param event
     * @return
     */
    public static boolean isPressedKeycodeBack(int keyCode, KeyEvent event) {
        return (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN);
    }

    /**
     * 是否按下了屏幕菜单(menu)键
     *
     * @param keyCode
     * @param event
     * @return
     */
    public static boolean isPressedKeycodeMenu(int keyCode, KeyEvent event) {
        return (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN);
    }

    /**
     * 软键盘是否弹出
     *
     * @param context
     * @return
     */
    public static boolean isShowSoftKey(Context context) {
        return ((Activity) context).getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
    }

    /**
     * 打开设置网络界面
     */
    public static void setNetworkMethod(final Context context) {
        //提示对话框
        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle("提示").setMessage("无法连接网络，是否进行设置？")//
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = null;
                        //判断手机系统的版本  即API大于10 就是3.0或以上版本
                        if (android.os.Build.VERSION.SDK_INT > 10) {
                            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                        } else {
                            intent = new Intent();
                            ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                            intent.setComponent(component);
                            intent.setAction("android.intent.action.VIEW");
                        }
                        context.startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 重启应用程序
     *
     * @param context
     */
    public static void restartApplication(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 卸载应用程序
     */
    public static void uninstallApplication(Context context) {
        // <action android:name="android.intent.action.DELETE" />
        // <category android:name="android.intent.category.DEFAULT" />
        // <data android:scheme="package" />
        Intent intent = new Intent();
        intent.setAction("android.intent.action.DELETE");
        // 附加的额外的参数
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }


    /**
     * 开启一个应用程序
     */
    public static void startApplication(Context context) {
        // 开启这个应用程序的第一个activity. 默认情况下 第一个activity就是具有启动能力的activity.
        String packname = context.getPackageName();
        PackageManager pm = context.getPackageManager();
        try {
            // 懒加载
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_ACTIVITIES);
            ActivityInfo[] activityinfos = packinfo.activities;
            if (activityinfos != null && activityinfos.length > 0) {
                String className = activityinfos[0].name;
                Intent intent = new Intent();
                intent.setClassName(packname, className);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "该应用程序没有界面", Toast.LENGTH_LONG).show();
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "无法启动应用.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 分享应用程序,启动系统的分享界面
     */
    public static void shareApplication(Context context) {
        // <action android:name="android.intent.action.SEND" />
        // <category android:name="android.intent.category.DEFAULT" />
        // <data android:mimeType="text/plain" />
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "推荐您使用一款软件,下载地址为:https://play.google.com/store/apps/details?id=" + context.getPackageName());
        context.startActivity(intent);
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    /**
     * 设置透明状态栏
     */
    @SuppressLint("InlinedApi")
    public static void setImmersionStatusBar(Activity activity) {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            // 透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏。注意华为和HTC等有虚拟HOME键盘的，如果使用下面这段代码，界面会被覆盖。无法操作底部TAB（建议关闭）
//			 activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 清除透明状态栏设置
     */
    @SuppressLint("InlinedApi")
    public static void clearImmersionStatusBar(Activity activity) {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 获取Manifest中配置的渠道名
     */
    public static String getChannelName(Context context, String key) {
        if (context == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }

    /**
     * 判断屏幕是否亮着
     *
     * @param context
     * @return
     */
    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        return pm != null && pm.isScreenOn();
    }

    /**
     * 是否开启了重力感应
     *
     * @param context
     * @return
     */
    public static boolean isOpenRotate(Context context) {
        int gravity = 0;
        try {
            gravity = Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return gravity == 1;
    }

    /**
     * 是否开启锁屏功能（比如手势，PIN，密码等锁屏功能）
     * @param context
     * @return
     */
    public static boolean isOpenKeyguard(Context context) {
        KeyguardManager keyguardManager =(KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguardManager == null) return false;
        return keyguardManager.isKeyguardSecure();
    }

    /**
     * 是否支持指纹识别（判断是否有硬件）
     * 给出两种方式，第一种是通过V4支持包获得兼容的对象引用，这是google推行的做法；还有就是直接使用api 23 framework中的接口获得对象引用。
     * @param context
     * @return
     */
    public static boolean isSupportFingerprint(Context context) {
        // Using the Android Support Library v4
        FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(context);
        // Using API level 23:
//        FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        if (fingerprintManager == null) return false;
        return fingerprintManager.isHardwareDetected();
    }

    /**
     * 检查设备中是否有注册过的指纹信息
     * @param context
     * @return
     */
    public static boolean hasEnrolledFingerprints(Context context) {
        FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(context);
        if (fingerprintManager == null) return false;
        return fingerprintManager.hasEnrolledFingerprints();
    }

    /**
     * 检测辅助功能是否开启<br>
     * @param mContext
     * @param serviceClass extends AccessibilityService
     * @return boolean
     */
    public static boolean isAccessibilitySettingsOn(Context mContext, Class<? extends AccessibilityService> serviceClass) {
        int accessibilityEnabled = 0;
        // TestService为对应的服务
        final String service = getPackageName(mContext) + "/" + serviceClass.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 判断服务是否启动,context上下文对象 ，className服务的name
     * @param context
     * @param serviceClass 服务类名
     * @return
     */
    public static boolean isServiceRunning(Context context, Class<? extends AccessibilityService> serviceClass) {
        if(context != null && serviceClass != null){
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> serviceList = am.getRunningServices(200);
            if (!serviceList.isEmpty()) {
                String serviceClassName = serviceClass.getSimpleName();
                for (int i = 0; i < serviceList.size(); i++) {
                    if (serviceList.get(i).service.getClassName().equals(serviceClassName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断是否已经获取该权限
     * @param context
     * @param permission 权限数组
     * @return
     */
    public static boolean isGetPermission(Context context, String... permission) {
        if (permission == null) return true;
        String packageName = context.getPackageName();
        PackageManager packageManager = context.getPackageManager();
        for (int i = 0; i < permission.length; i++) {
            if (PackageManager.PERMISSION_GRANTED != packageManager.checkPermission(permission[i], packageName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据文件路径安装APK
     * @param context
     * @param filePath 文件路径
     */
    public static void install(Context context, String filePath) {
        install(context, new File(filePath));
    }

    /**
     * 根据文件安装APK
     */
    public static void install(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 获取屏幕密度DPI（如 120 / 160 / 240）
     * @param context
     * @return 默认返回0
     */
    public static int getScreenDensityDpi(Context context) {
        try {
            return context.getResources().getDisplayMetrics().densityDpi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取屏幕密度（如1.0、1.5, 2.0）
     * @param context
     * @return 默认返回0.0
     */
    public static float getScreenDensity(Context context) {
        try {
            return context.getResources().getDisplayMetrics().density;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0f;
    }

    /**
     * 字体缩放比例
     * @param context
     * @return 默认返回0.0
     */
    public static float getScaledDensity(Context context) {
        try {
            return context.getResources().getDisplayMetrics().scaledDensity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0f;
    }


}
