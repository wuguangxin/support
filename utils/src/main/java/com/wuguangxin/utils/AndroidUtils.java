package com.wuguangxin.utils;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog.Builder;
import android.app.AppOpsManager;
import android.app.KeyguardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StatFs;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.wuguangxin.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.RequiresPermission;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

/*
                                                     正式版日期  版本号               SDK 英文名称             中文名称
int SDK0 = 0;                           			// 2008-09-22 Android Beta	    	Beta                阿童木
int SDK1 = VERSION_CODES.BASE; 					    // 2008-09-22 Android 1.0	    1	BASE                发条机器人
int SDK2 = VERSION_CODES.BASE_1_1; 				    // 2009------ Android 1.1	    2	BASE                发条机器人
int SDK3 = VERSION_CODES.CUPCAKE; 				    // 2009-04-30 Android 1.5	    3	CUPCAKE             纸杯蛋糕
int SDK4 = VERSION_CODES.DONUT; 					// 2009-09-15 Android 1.6	    4	DONUT               甜甜圈
int SDK5 = VERSION_CODES.ECLAIR; 					// 2009-10-26 Android 2.0	    5	ECLAIR              闪电泡芙、法式奶油夹心甜点、松饼
int SDK6 = VERSION_CODES.ECLAIR_0_1; 				// 2009-12-03 Android 2.0.1     6 	ECLAIR              闪电泡芙、法式奶油夹心甜点、松饼
int SDK7 = VERSION_CODES.ECLAIR_MR1; 				// 2010-01-10 Android 2.1	    7	ECLAIR              闪电泡芙、法式奶油夹心甜点、松饼
int SDK8 = VERSION_CODES.FROYO; 					// 2010-05-20 Android 2.2	    8	FROYO               冻酸奶
int SDK9 = VERSION_CODES.GINGERBREAD;				// 2010-12-07 Android 2.3	    9	GINGERBREAD         姜饼
int SDK10 = VERSION_CODES.GINGERBREAD_MR1;		    // 2011------ Android 2.3.3     10	GINGERBREAD         姜饼
int SDK11 = VERSION_CODES.HONEYCOMB;				// 2011-02-03 Android 3.0	    11	HONEYCOMB           蜂巢
int SDK12 = VERSION_CODES.HONEYCOMB_MR1;			// 2011-05-11 Android 3.1	    12	HONEYCOMB           蜂巢
int SDK13 = VERSION_CODES.HONEYCOMB_MR2;			// 2011-07-13 Android 3.2	    13	HONEYCOMB           蜂巢
int SDK14 = VERSION_CODES.ICE_CREAM_SANDWICH;		// 2011-10-19 Android 4.0 	    14	ICE_CREAM_SANDWICH  冰激凌三明治、冰淇淋三明治
int SDK15 = VERSION_CODES.ICE_CREAM_SANDWICH_MR1;	// 2011-12-17 Android 4.0.3     15	ICE_CREAM_SANDWICH  冰激凌三明治、冰淇淋三明治
int SDK16 = VERSION_CODES.JELLY_BEAN;				// 2012-06-28 Android 4.1	    16	JELLY_BEAN          果冻豆
int SDK17 = VERSION_CODES.JELLY_BEAN_MR1;			// 2012-10-30 Android 4.2 	    17	JELLY_BEAN          果冻豆
int SDK18 = VERSION_CODES.JELLY_BEAN_MR2;			// 2013-07-25 Android 4.3 	    18	JELLY_BEAN          果冻豆
int SDK19 = VERSION_CODES.KITKAT;					// 2013-09-04 Android 4.4 	    19	KITKAT              奇巧巧克力棒
int SDK20 = VERSION_CODES.KITKAT_WATCH;			    // 2013-09-04 Android 4.4W 	    20	KITKAT watches      奇巧巧克力棒
int SDK21 = VERSION_CODES.LOLLIPOP;				    // 2014-10-15 Android 5.0	    21	LOLLIPOP            棒棒糖
int SDK22 = VERSION_CODES.LOLLIPOP_MR1;			    // 2015-03-10 Android 5.1	    22	Lollipop            棒棒糖
int SDK23 = VERSION_CODES.M;						// 2015-05-25 Android 6.0	    23	Marshmallow         棉花糖
int SDK24 = VERSION_CODES.N;						// 2016-08-22 Android 7.0 	    24  Nougat              牛轧糖
int SDK25 = VERSION_CODES.N_MR1;					// 2016-12-05 Android 7.1.1	    25  Nougat              牛轧糖
int SDK26 = VERSION_CODES.O;						// 2017-08-22 Android 8.0	    26  Oreo                奥利奥
int SDK27 = VERSION_CODES.O_MR1;					// 2017-12-05 Android 8.1	    27  Oreo                奥利奥
int SDK28 = VERSION_CODES.P;						// 2018-08-07 Android 9.0       28  Pre                 派
int SDK29 = VERSION_CODES.Q;						// 2019-09-04 Android 10 	    29  Q
int SDK30 = VERSION_CODES.R;						// 2020-09-09 Android 11 	    30  R
int SDK30 = VERSION_CODES.S;						// 2021-05-19 Android 12 	    31  S
update by:2021-09-24
*/

/**
 * Android系统相关的一些工具方法
 * <p>Created by wuguangxin on 15/6/14 </p>
 */
@SuppressLint("NewApi")
public class AndroidUtils {
    private static final String TAG = "AndroidUtils";
    private static final int ERROR = -1;

    /**
     * 获取SDK版本号
     *
     * @return SDK版本号
     */
    public static int getSdkVersion() {
        return VERSION.SDK_INT;
    }

    /**
     * 判断SD卡是否挂载 使用Environment.getExternalStorageState() 返回sd卡的状态， Environment.MEDIA_MOUNTED 表示被挂载
     *
     * @return 判断SD卡是否挂载
     */
    public static boolean isExtStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取手机版本号
     *
     * @return 手机版本号
     */
    public static String getRelease() {
        return VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * 获取应用程序包名
     *
     * @return 应用程序包名
     */
    public static String getPackageName(Context context) {
        if (context == null) return null;
        return context.getPackageName();
    }

    /**
     * 判断是否已经安装SD卡
     *
     * @return 是否已经安装SD卡
     */
    public static boolean isExistSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取正在运行的进程信息列表
     *
     * @return 正在运行的进程信息列表
     */
    public static List<RunningAppProcessInfo> getRunningProcessList(Context context) {
        return getActivityManager(context).getRunningAppProcesses();
    }

    /**
     * 获取正在运行的进程数量
     *
     * @return 正在运行的进程数量
     */
    public static int getRunningProcessSize(Context context) {
        return getRunningProcessList(context).size();
    }

    /**
     * 获取手机内存路径
     *
     * @return 手机内存路径
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
     * @return 扩展SD存储卡路径
     */
    public static String getExternalStoragePath() {
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        if (externalStorageDirectory.exists()) {
            return externalStorageDirectory.getPath();
        }
        return null;
    }

    /**
     * 获取 ActivityManager
     *
     * @param context 上下文
     * @return ActivityManager
     */
    public static ActivityManager getActivityManager(Context context) {
        return (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
    }

    /**
     * 获取手机的总RAM（运存） 获取失败返回0
     *
     * @param context 上下文
     * @return 手机的总RAM（运存）
     */
    @SuppressLint("NewApi")
    public static long getTotalRAM(Context context) {
        if (getSDKCode() >= 16) {
            MemoryInfo mMemoryInfo = new MemoryInfo();
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
     * @param context 上下文
     * @return 手机的可用RAM（运存）
     */
    public static long getAvailRAM(Context context) {
        MemoryInfo mMemoryInfo = new MemoryInfo();
        getActivityManager(context).getMemoryInfo(mMemoryInfo);
        return mMemoryInfo.availMem;
    }

    /**
     * 获取手机内存总容量
     *
     * @return 手机内存总容量
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
     * @return 手机内存剩余容量
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
     * @return SD卡下external_sd文件夹的总大小
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
     * @return SD卡下external_sd文件夹的可用大小
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
     * 获取设备ID (唯一标识 IMEI)。
     * (6.0及以上系统需要动态请求权限 Manifest.permission.READ_PHONE_STATE)
     *
     * @param context 上下文
     * @return 设备ID
     */
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getDeviceId(Context context) {
        try {
            TelephonyManager manager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            return manager.getDeviceId();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前应用程序版本名称
     *
     * @return 当前应用程序版本名称。如 1.0.0
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
     * @param context context
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo packInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取SDK版本号
     *
     * @return SDK版本号
     */
    public static int getSDKCode() {
        return VERSION.SDK_INT;
    }

    /**
     * 判断当前设备是否是模拟器。如果是模拟器返回TRUE，不是返回FALSE
     *
     * @param context context
     * @return 是否是模拟器
     */
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
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
     * 根据WifiManager获取MAC地址(在6.0以上系统或无网状态下，永远是 02:00:00:00:00:00)
     *
     * @param context context
     * @return MAC地址
     * @deprecated use getMac()
     */
    @SuppressLint("HardwareIds")
    @Deprecated
    public static String getMac(Context context) {
        /*try {
            WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifi.getConnectionInfo();
            return wifiInfo.getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return getMac();
    }

    /**
     * 通过网络接口取MAC地址
     *
     * @return MAC地址
     */
    public static String getMac() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            if (networkInterfaces == null) {
                return null;
            }
            List<NetworkInterface> all = Collections.list(networkInterfaces);
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) {
                    continue;
                }

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

    // -----------------new ADD begin---------

    /**
     * 获取mac地址
     *
     * @return
     */
    public static String getMacAddress() {
        String result = "";
        String Mac = "";
//        result = DevicesUtil.callCmd("busybox ifconfig", "HWaddr");

        if (result == null) {
            return "--";
        }
        if (result.length() > 0 && result.contains("HWaddr")) {
            Mac = result.substring(result.indexOf("HWaddr") + 6, result.length() - 1);
            if (Mac.length() > 1) {
                result = Mac.toLowerCase();
            }
        }
        return result.trim();
    }

    /**
     * 获取WIFI下的网络IP地址
     *
     * @param context
     * @return
     */
    public static String getIPAddress(Context context) {
        WifiManager service = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        DhcpInfo dhcpInfo = wifi_service.getDhcpInfo(); // 从DHCP分配的信息中获取
        WifiInfo wifiinfo = service.getConnectionInfo(); // 从wifi链接信息中获取
        return Formatter.formatIpAddress(wifiinfo.getIpAddress());
    }

    //---------------NEW ADD END----------

    /**
     * 获取本机号码，如果是双卡手机，获取的将是卡槽1的号码
     *
     * @param context context
     * @return 本机号码
     */
    @RequiresPermission(anyOf = {
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.READ_PHONE_NUMBERS
    })
    @SuppressLint("HardwareIds")
    public static String getPhoneNumber(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getLine1Number();
    }

    /**
     * 获取联系人电话。
     * 需要权限：android.permission.READ_CONTACTS or android.permission.WRITE_CONTACTS
     * 参考http://www.2cto.com/kf/201109/104686.html
     *
     * @param context context
     * @param cursor  cursor
     * @return 联系人电话
     */
    public static String getContactNumber(Context context, Cursor cursor) {
        String phoneNumber = null;
        Cursor currCursor = cursor;
        try {
            if (currCursor == null) {
                String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
                ContentResolver contentResolver = context.getContentResolver();
                if (contentResolver != null) {
                    currCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, cols, null, null, null);
                }
            }
            if (currCursor != null) {
                int phoneColumn = currCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                int phoneNum = currCursor.getInt(phoneColumn);
                if (phoneNum > 0) {
                    // 获得联系人的ID号
                    int idColumn = currCursor.getColumnIndex(ContactsContract.Contacts._ID);
                    String contactId = currCursor.getString(idColumn);
                    // 获得联系人的电话号码的cursor;
                    Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                    if (phones.moveToFirst()) {
                        // 遍历所有的电话号码
                        for (; !phones.isAfterLast(); phones.moveToNext()) {
                            int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            phoneNumber = phones.getString(index);
                        }
                        if (!phones.isClosed()) {
                            phones.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (currCursor != null && !currCursor.isClosed()) {
                currCursor.close();
            }
        }
        return phoneNumber;
    }

    public static String getContactNumberByName(Context context, String contactName) {
        if (TextUtils.isEmpty(contactName)) {
            return null;
        }
        Cursor cursor = null;
        try {
            // 使用content://方案检索内容的uri
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            // 要返回的列的列表。传null将返回所有列，效率低下
            String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
            // 声明要返回哪些行的筛选器，格式为SQL WHERE子句（WHERE本身除外）。传递null将返回给定URI的所有行
            String selection = ContactsContract.PhoneLookup.DISPLAY_NAME + " = '" + contactName + "'";
            // 您可以在选择中包括？s，它将被selectionArgs中的值替换，并按它们在选择中出现的顺序排列。这些值将被绑定为字符串
            String[] selectionArgs = null;
            // 如何对行进行排序，格式为SQL order BY子句（不包括order本身）。传递null将使用默认的排序顺序，可能是无序的
            String sortOrder = null;
            // 将自己添加到 msPeers 中
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

            if (cursor == null) {
                return null;
            }
            int count = cursor.getCount();
            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);
                // 取得联系人名字
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)); // 独山县汽车站2
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); // (0854) 323 1289
                Log.e(TAG, name + ": " + number);
                if (name != null && TextUtils.equals(name, contactName)) {
                    Log.e(TAG, name + ": " + number);
                    cursor.close();
                    return number;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 根据名字模糊查询所有联系人信息
     * @param context
     * @param contactName 姓名关键字
     * @return
     */
    public static Map<String, String> queryContactByName(Context context, String contactName) {
        if (TextUtils.isEmpty(contactName)) {
            return null;
        }
        Cursor cursor = null;
        try {
            // 使用content://方案检索内容的uri
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            // 要返回的列的列表。传null将返回所有列，效率低下
            String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
            // 声明要返回哪些行的筛选器，格式为SQL WHERE子句（WHERE本身除外）。传递null将返回给定URI的所有行
            String selection = ContactsContract.PhoneLookup.DISPLAY_NAME + " like '%" + contactName + "%'";
            // 您可以在选择中包括？s，它将被selectionArgs中的值替换，并按它们在选择中出现的顺序排列。这些值将被绑定为字符串
            String[] selectionArgs = null;
            // 如何对行进行排序，格式为SQL order BY子句（不包括order本身）。传递null将使用默认的排序顺序，可能是无序的
            String sortOrder = null;
            // 将自己添加到 msPeers 中
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

            if (cursor == null) {
                return null;
            }
            Map<String, String> map = new HashMap<>();
            int count = cursor.getCount();
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    cursor.moveToPosition(i);
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)); // 张三
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); // 186 1234 5678
                    map.put(name, number);
                    Log.e(TAG, name + ": " + number);
                }
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 根据联系号码模糊查询所有联系人信息
     * @param context
     * @param contactNumber 号码关键字
     * @return
     */
    public static Map<String, String> queryContactNameByNumber(Context context, String contactNumber) {
        if (TextUtils.isEmpty(contactNumber)) {
            return null;
        }
        Cursor cursor = null;
        try {
            // 使用content://方案检索内容的uri
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            // 要返回的列的列表。传null将返回所有列，效率低下
            String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
            // 声明要返回哪些行的筛选器，格式为SQL WHERE子句（WHERE本身除外）。传递null将返回给定URI的所有行
            String selection = ContactsContract.CommonDataKinds.Phone.NUMBER + " like '%" + contactNumber + "%'";
            selection += " or " + ContactsContract.CommonDataKinds.Phone.NUMBER + " like '%" + contactNumber.replaceAll(" ", "") + "%'";

            // 您可以在选择中包括？s，它将被selectionArgs中的值替换，并按它们在选择中出现的顺序排列。这些值将被绑定为字符串
            String[] selectionArgs = null;
            // 如何对行进行排序，格式为SQL order BY子句（不包括order本身）。传递null将使用默认的排序顺序，可能是无序的
            String sortOrder = null;
            // 将自己添加到 msPeers 中
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

            if (cursor == null) {
                return null;
            }
            Map<String, String> map = new HashMap<>();
            int count = cursor.getCount();
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    cursor.moveToPosition(i);
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)); // 张三
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); // 186 1234 5678
                    map.put(name, number);
                }
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 根据联系人号码精确查找联系人姓名
     * @param context
     * @param contactNumber 联系人号码
     * @return
     */
    public static String getContactNameByNumber(Context context, String contactNumber) {
        Map<String, String> map = queryContactNameByNumber(context, contactNumber);
        contactNumber = contactNumber.replaceAll(" ", "");
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String name = entry.getKey().replaceAll(" ", "");
                String number = entry.getValue().replaceAll(" ", "");
                if (TextUtils.equals(number, contactNumber)) {
                    return name;
                }
            }
        }
        return null;
    }

    /**
     * 判断网络是否是WIFI网络
     *
     * @param context context
     * @return 是否是WIFI网络
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isWifi(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
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
     * @param context context
     * @return 单位:px
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 得到屏幕高度
     *
     * @param context context
     * @return 单位:px
     */
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 判断是否有任何可用的网络
     *
     * @param context context
     * @return 是否有任何可用的网络
     */
    @SuppressLint("MissingPermission")
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.isConnected();
        }
        return false;
    }

    /**
     * 判断是否有可以连接的WIFI
     *
     * @param context context
     * @return 是否有可以连接的WIFI
     */
    @SuppressLint("MissingPermission")
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo != null) {
            return wifiNetworkInfo.isConnected();
        }
        return false;
    }

    /**
     * 判断wifi的连接状态
     *
     * @param context context
     * @return wifi的连接状态
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isWifiConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null) {
            return networkInfo.isConnected();
        }
        return false;
    }

    /**
     * 判断基站的连接状态
     *
     * @param context context
     * @return 基站的连接状态
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isBaseStateConnection(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfo != null) {
            return networkInfo.isConnected();
        }
        return false;
    }

    /**
     * 复制文件
     *
     * @param sourceFile      源
     * @param destinationFile 目标
     * @throws IOException 异常
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
     * @param input  输入流
     * @param output 输出流
     * @return 总大小
     * @throws IOException 异常
     */
    public static int copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024 * 8];
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
        if (VERSION.SDK_INT >= VERSION_CODES.FROYO) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, args);
        } else {
            task.execute(args);
        }
    }

    /**
     * 通过HttpURLConnection连接到指定的URL地址，返回一个InputStream，实用于下载图片等..
     *
     * @param urlString 地址
     * @return InputStream
     * @throws IOException 异常
     */
    public static InputStream downloadUrl(String urlString) throws IOException {
        HttpURLConnection conn = buildHttpUrlConnection(urlString);
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }

    /**
     * 返回已个HttpURLConnection对象
     *
     * @param urlString 地址
     * @return HttpURLConnection
     * @throws IOException 异常
     */
    public static HttpURLConnection buildHttpUrlConnection(String urlString) throws IOException {
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
        if (!(VERSION.SDK_INT >= VERSION_CODES.FROYO)) { // 小于2.2
            System.setProperty("http.keepAlive", "false");
        }
    }

    /**
     * 判断程序是否在前台运行
     *
     * @param context context
     * @return 程序是否在前台运行
     */
    public static boolean isAppOnForeground(Context context) {
        try {
            List<RunningAppProcessInfo> appList = getActivityManager(context).getRunningAppProcesses();
            if (appList == null || appList.isEmpty()) return false;
            final String packageName = context.getPackageName();
            for (RunningAppProcessInfo app : appList) {
                Logger.e("AAA", "app.processName=" + app.processName + " app.importance=" + app.importance);
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
     *
     * @param context context
     * @return 程序是否在后台运行
     */
    public static boolean isAppOnBackground(Context context) {
        return !isAppOnForeground(context);
    }

    /**
     * 判断当前应用程序是否处于系统栈顶
     *
     * @param context context
     * @return 当前应用程序是否处于系统栈顶
     */
    public static boolean isTopActivity(final Context context) {
        if (context == null) return false;
        String curPackageName = context.getPackageName();
        String topPackageName = AndroidUtils.getAppTopActivityPackageName(context);
        return curPackageName.equals(topPackageName);
    }

    /**
     * 获取当前运行的APP中处于栈顶的APP包名。
     * 注：getRunningTasks() 方法从 Android L 起限制访问。
     * 看：http://blog.csdn.net/hyhyl1990/article/details/45700447
     *
     * @param context context
     * @return 当前运行的APP中处于栈顶的APP包名
     */
    public static String getAppTopActivityPackageName(final Context context) {
        if (context == null) return null;
        try {
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
     *
     * @param context context
     * @return 当前运行的APP中处于栈顶的APP包名
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
     * @param context context
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
     * @param context context
     * @return 当前应用程序的包名
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
     * @param context context
     * @return 设备ID和MAC信息
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            TelephonyManager manager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = manager.getDeviceId();
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(fstream, 1024);
                mac = in.readLine();
            } catch (IOException ignored) {
            } finally {
                try {
                    fstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检查权限是否获取
     *
     * @param context    context
     * @param permission 权限
     * @return 是否已获取
     */
    public static boolean checkPermission(Context context, String permission) {
        return PermissionUtils.checkPermission(context, permission);

        /*boolean result = false;
        if (VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (int) method.invoke(context, permission);
                result = rest == PackageManager.PERMISSION_GRANTED;
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;*/
    }


    /**
     * 判断按下的是否是返回键
     *
     * @param keyCode keyCode
     * @param event   KeyEvent
     * @return 是否是返回(back)键
     */
    public static boolean isPressedKeycodeBack(int keyCode, KeyEvent event) {
        return (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN);
    }

    /**
     * 是否按下了屏幕菜单(menu)键
     *
     * @param keyCode keyCode
     * @param event   KeyEvent
     * @return 是否按下了屏幕菜单(menu)键
     */
    public static boolean isPressedKeycodeMenu(int keyCode, KeyEvent event) {
        return (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN);
    }

    /**
     * 软键盘是否弹出
     *
     * @param context context
     * @return 软键盘是否弹出
     */
    public static boolean isShowSoftKey(Context context) {
        return ((Activity) context).getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
    }

    /**
     * 打开设置网络界面
     *
     * @param context context
     */
    public static void setNetworkMethod(Context context) {
        ObjectUtils.requireNonNull(context, "'context' cannot be null");
        Builder builder = new Builder(context);
        builder.setTitle("提示");
        builder.setMessage("无法连接网络，是否进行设置？");
        builder.setPositiveButton("设置", (dialog, which) -> context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)));
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    /**
     * 重启应用程序
     *
     * @param context context
     */
    public static void restartApplication(Context context) {
        ObjectUtils.requireNonNull(context, "'context' cannot be null");
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 卸载应用程序
     *
     * @param context context
     */
    public static void uninstallApplication(Context context) {
        // <action android:name="android.intent.action.DELETE" />
        // <category android:name="android.intent.category.DEFAULT" />
        // <data android:scheme="package" />
        Intent intent = new Intent();
        intent.setAction("android.intent.action.DELETE");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }


    /**
     * 开启一个应用程序
     *
     * @param context context
     */
    public static void startApplication(Context context) {
        // 开启这个应用程序的第一个activity. 默认情况下 第一个activity就是具有启动能力的activity.
        String packName = context.getPackageName();
        PackageManager pm = context.getPackageManager();
        try {
            // 懒加载
            PackageInfo packInfo = pm.getPackageInfo(packName, PackageManager.GET_ACTIVITIES);
            ActivityInfo[] activityInfoArr = packInfo.activities;
            if (activityInfoArr != null && activityInfoArr.length > 0) {
                String className = activityInfoArr[0].name;
                Intent intent = new Intent();
                intent.setClassName(packName, className);
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
     *
     * @param context context
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
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c;
        Object obj;
        Field field;
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
     * 设置透明状态栏。
     * >=19时:getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
     *
     * @param activity Activity
     */
    @SuppressLint("InlinedApi")
    @Deprecated
    public static void setImmersionStatusBar(Activity activity) {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            // 透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏。注意华为和HTC等有虚拟HOME键盘的，如果使用下面这段代码，界面会被覆盖。无法操作底部TAB（建议关闭）
//			 activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 设置透明状态栏
     * >=19时:
     * getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
     * >=19 且 <21时，设置：
     * //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
     * viewGroup.setClipToPadding(true);
     * //将侧边栏顶部延伸至status bar
     * viewGroup.setFitsSystemWindows(false);
     *
     * @param activity  Activity
     * @param viewGroup 窗口的跟布局
     */
    public static void setImmersionStatusBar(Activity activity, ViewGroup viewGroup) {
        if (activity == null || viewGroup == null) {
            return;
        }
        // >=19
        if (VERSION.SDK_INT >= 19) {
            // 透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏。注意华为和HTC等有虚拟HOME键盘的，如果不设置下面这段代码，虚拟键盘将覆盖APP底部界面，无法操作底部TAB
//				activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            // <21
            if (VERSION.SDK_INT < 21) {
                // 设置系统是否需要考虑 StatusBar 占据的区域来显示
                viewGroup.setFitsSystemWindows(false);
                // 是否受 StatusBar的Padding的影响，true, 则布局会往下延伸Padding，false，则占用padding的区域
                viewGroup.setClipToPadding(true);

                // android:fitsSystemWindows=""，
                // false：布局不受StatusBar的影响，可以完全的展示在StatusBar的下面。
                // true：布局不受StatusBar的影响，不会被StatusBar遮住，

                // android:clipToPadding="false"
                // false：布局不受Padding的影响，可以展示在Padding的区域。其实fitsSystemWindows就是设置一个Padding使View不会展示在StatusBar的下方，
            }
        }
    }

    /**
     * 清除透明状态栏设置
     *
     * @param activity Activity
     */
    @SuppressLint("InlinedApi")
    public static void clearImmersionStatusBar(Activity activity) {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 获取Manifest中配置的渠道名
     *
     * @param context context
     * @param key     key
     * @return 渠道名
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
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }

    /**
     * 判断屏幕是否亮着
     *
     * @param context context
     * @return 是否亮着
     */
    public static boolean isScreenOn(Context context) {
        PowerManager pm = (PowerManager) context.getApplicationContext().getSystemService(Context.POWER_SERVICE);
        return pm != null && pm.isScreenOn();
    }

    /**
     * 是否开启了重力感应
     *
     * @param context context
     * @return 是否开启了重力感应
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
     *
     * @param context context
     * @return 是否开启锁屏功能
     */
    public static boolean isOpenKeyguard(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        if (keyguardManager == null) return false;
        return keyguardManager.isKeyguardSecure();
    }

    /**
     * 是否支持指纹识别（判断是否有硬件）
     * 给出两种方式，第一种是通过V4支持包获得兼容的对象引用，这是google推行的做法；还有就是直接使用api 23 framework中的接口获得对象引用。
     *
     * @param context context
     * @return 是否支持指纹识别
     */
    @RequiresPermission(android.Manifest.permission.USE_FINGERPRINT)
    public static boolean isSupportFingerprint(Context context) {
        ObjectUtils.requireNonNull(context, "'context' cannot be null");
        if (VERSION.SDK_INT < VERSION_CODES.M) {
            FingerprintManagerCompat manager = FingerprintManagerCompat.from(context);
            return manager.hasEnrolledFingerprints();
        }
        FingerprintManager manager = (FingerprintManager) context.getApplicationContext().getSystemService(Context.FINGERPRINT_SERVICE);
        return manager.isHardwareDetected();
    }

    /**
     * 检查设备中是否有注册过的指纹信息（需要权限）
     *
     * @param context context
     * @return 是否有注册过的指纹信息
     */
    @RequiresPermission(android.Manifest.permission.USE_FINGERPRINT)
    public static boolean hasEnrolledFingerprints(Context context) {
        ObjectUtils.requireNonNull(context, "'context' not be null");
        if (VERSION.SDK_INT < VERSION_CODES.M) {
            FingerprintManagerCompat manager = FingerprintManagerCompat.from(context);
            return manager.hasEnrolledFingerprints();
        }
        FingerprintManager manager = (FingerprintManager) context.getApplicationContext().getSystemService(Context.FINGERPRINT_SERVICE);
        return manager.hasEnrolledFingerprints();
    }

    /**
     * 检测辅助功能是否开启
     *
     * @param context      Context
     * @param serviceClass extends AccessibilityService
     * @return 是否开启
     */
    public static boolean isAccessibilitySettingsOn(Context context, Class<? extends AccessibilityService> serviceClass) {
        ObjectUtils.requireNonNull(context, "'context' not be null");
        if (serviceClass != null) {
            int accessibilityEnabled = 0;
            // TestService为对应的服务
            final String service = getPackageName(context) + "/" + serviceClass.getCanonicalName();
            ContentResolver contentResolver = context.getApplicationContext().getContentResolver();
            try {
                accessibilityEnabled = Settings.Secure.getInt(contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
            if (accessibilityEnabled == 1) {
                String settingValue = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
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
        }
        return false;
    }


    /**
     * 判断服务是否启动,context上下文对象 ，className服务的name
     *
     * @param context      context
     * @param serviceClass 服务类名
     * @return 服务是否启动
     */
    public static boolean isServiceRunning(Context context, Class<? extends AccessibilityService> serviceClass) {
        ObjectUtils.requireNonNull(context, "'context' cannot be null");
        if (serviceClass != null) {
            ActivityManager am = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
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
     *
     * @param context     context
     * @param permissions 权限数组
     * @return 是否已经获取该权限
     */
    public static boolean checkPermission(Context context, String... permissions) {
        ObjectUtils.requireNonNull(context, "'context' cannot be null");
        ObjectUtils.requireNonNull(permissions, "'permissions' cannot be null");
        String packageName = context.getPackageName();
        PackageManager pm = context.getPackageManager();
        for (String permission : permissions) {
            if (pm.checkPermission(permission, packageName) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据APK文件路径安装到设备
     *
     * @param context  context
     * @param filePath 文件路径
     */
    public static void install(Context context, String filePath) {
        install(context, new File(filePath));
    }

    /**
     * 根据APK文件安装到设备
     *
     * @param context context
     * @param file    APK文件
     */
    public static void install(Context context, File file) {
        install(context, Uri.fromFile(file));
    }

    /**
     * 根据APK文件uri安装到设备
     *
     * @param context context
     * @param uri     APK文件uri
     */
    public static void install(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 获取屏幕密度DPI（如 120 / 160 / 240）
     *
     * @param context context
     * @return 获取屏幕密度DPI，默认返回0
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
     *
     * @param context context
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
     *
     * @param context context
     * @return 字体缩放比例，默认返回0.0
     */
    public static float getScaledDensity(Context context) {
        try {
            return context.getResources().getDisplayMetrics().scaledDensity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0f;
    }

    /**
     * 关闭Android P 的警告对话框
     */
    public static void closeAndroidPDialog() {
        try {
            @SuppressLint("PrivateApi, DiscouragedPrivateApi")
            Class<?> clazz = Class.forName("android.content.pm.PackageParser$Package");
            Constructor<?> declaredConstructor = clazz.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            @SuppressLint("PrivateApi")
            Class<?> clazz = Class.forName("android.app.ActivityThread");
            @SuppressLint("DiscouragedPrivateApi")
            Method declaredMethod = clazz.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            @SuppressLint("SoonBlockedPrivateApi")
            Field mHiddenApiWarningShown = clazz.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本地语言
     *
     * @param context
     * @return
     */
    public static String getLanguage(Context context) {
        try {
            if (context != null) {
                return context.getResources().getConfiguration().locale.getLanguage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置语言（App内部）
     *
     * @param context
     * @param locale
     */
    public static void setLanguageLocal(Context context, Locale locale) {
        ObjectUtils.requireNonNull(context, "context cannot be null");
        ObjectUtils.requireNonNull(locale, "locale cannot be null");
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        if (config.locale != locale) {
            DisplayMetrics display = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
            config.locale = locale; // 简体中文
            resources.updateConfiguration(config, display);
        }
    }

    /**
     * Activity 打开时动画，在Activity.startActivity()后调用
     *
     * @param activity
     */
    public static void animEnter(Activity activity) {
        if (activity == null) return;
        activity.overridePendingTransition(R.anim.xin_anim_activity_open_enter, R.anim.xin_anim_activity_open_exit);
    }

    /**
     * Activity 关闭时动画，在Activity.finish()后调用
     *
     * @param activity
     */
    public static void animClose(Activity activity) {
        if (activity == null) return;
        activity.overridePendingTransition(R.anim.xin_anim_activity_close_enter, R.anim.xin_anim_activity_close_exit);
    }

    /**
     * Lock Activity 打开时动画，在Activity.startActivity()后调用
     *
     * @param activity
     */
    public static void animEnterLock(Activity activity) {
        if (activity == null) return;
        activity.overridePendingTransition(R.anim.xin_anim_fade_in, R.anim.xin_anim_fade_out);
    }

    /**
     * Lock Activity 关闭时动画，在Activity.finish()后调用
     *
     * @param activity
     */
    public static void animCloseLock(Activity activity) {
        if (activity == null) return;
        activity.overridePendingTransition(R.anim.xin_anim_fade_in, R.anim.xin_anim_fade_out);
    }

    /**
     * Lock Activity 打开时动画，在Activity.startActivity()后调用
     *
     * @param activity
     */
    public static void animEnterLockDialog(Activity activity) {
        if (activity == null) return;
        activity.overridePendingTransition(R.anim.xin_anim_dialog_in, R.anim.xin_anim_dialog_out);
    }

    /**
     * Lock Activity 关闭时动画，在Activity.finish()后调用
     *
     * @param activity
     */
    public static void animCloseLockDialog(Activity activity) {
        if (activity == null) return;
        activity.overridePendingTransition(R.anim.xin_anim_dialog_in, R.anim.xin_anim_dialog_out);
    }

    /**
     * 判断 悬浮窗口权限是否打开
     *
     * @param context
     * @return true 允许  false禁止
     * 原文链接：https://blog.csdn.net/mzm489321926/article/details/50542065/
     */
    public static boolean checkAppOps(Context context) {
        try {
            @SuppressLint("WrongConstant")
            Object server = context.getSystemService(Context.APP_OPS_SERVICE);
            if (server == null) {
                return false;
            }
            Class<?> localClass = server.getClass();
            Class<?>[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            Object[] objects = new Object[3];
            objects[0] = 24;
            objects[1] = Binder.getCallingUid();
            objects[2] = context.getPackageName();
            int m = (int) method.invoke(server, objects);
            return m == AppOpsManager.MODE_ALLOWED /*0*/;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
