package com.wuguangxin.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 存储管理工具类。
 * {@link #getFilesDir(Context))                        // /data/user/0/<包名>/files
 * {@link #getCacheDir(Context))                        // /data/user/0/<包名>/cache
 * {@link #getCodeCacheDir(Context));                   // /data/user/0/<包名>/code_cache
 * {@link #getExternalCacheDir(Context));               // /storage/emulated/0/Android/data/<包名>/cache
 * {@link #getExternalFilesDir(Context));               // /storage/emulated/0/Android/data/<包名>/files
 * {@link #getExternalFilesDirDCIM(Context));           // /storage/emulated/0/Android/data/<包名>/files/DCIM
 * {@link #getExternalFilesDirCache(Context));          // /storage/emulated/0/Android/data/<包名>/files/Caches
 * {@link #getExternalFilesDirMusic(Context));          // /storage/emulated/0/Android/data/<包名>/files/Music
 * {@link #getExternalFilesDirAlarms(Context));         // /storage/emulated/0/Android/data/<包名>/files/Alarms
 * {@link #getExternalFilesDirMovies(Context));         // /storage/emulated/0/Android/data/<包名>/files/Movies
 * {@link #getExternalFilesDirPictures(Context));       // /storage/emulated/0/Android/data/<包名>/files/Pictures
 * {@link #getExternalFilesDirPodcasts(Context));       // /storage/emulated/0/Android/data/<包名>/files/Podcasts
 * {@link #getExternalFilesDirDownloads(Context));      // /storage/emulated/0/Android/data/<包名>/files/Download
 * {@link #getExternalFilesDirDocuments(Context));      // /storage/emulated/0/Android/data/<包名>/files/Documents
 * {@link #getExternalFilesDirRingtones(Context));      // /storage/emulated/0/Android/data/<包名>/files/Ringtones
 * {@link #getExternalFilesDirAudioBooks(Context));     // /storage/emulated/0/Android/data/<包名>/files/Audiobooks
 * {@link #getExternalFilesDirNotifications(Context));  // /storage/emulated/0/Android/data/<包名>/files/Notifications
 * 获取存储卡各个目录：
 * {@link #hasExternalStorage())                // true
 * {@link #getRootDirectory())                  // /system
 * {@link #getDataDirectory())                  // /data
 * {@link #getDownloadCacheDirectory())         // /data/cache
 * {@link #getDirectoryPictures())              // /storage/emulated/0/Pictures
 * {@link #getDirectoryDCIM())                  // /storage/emulated/0/DCIM
 * {@link #getDirectoryMusic())                 // /storage/emulated/0/Music
 * {@link #getDirectoryAlarms())                // /storage/emulated/0/Alarms
 * {@link #getDirectoryMovies())                // /storage/emulated/0/Movies
 * {@link #getDirectoryDownloads())             // /storage/emulated/0/Download
 * {@link #getDirectoryScreenshots())           // /storage/emulated/0/Screenshots
 * {@link #getDirectoryDocuments())             // /storage/emulated/0/Documents
 * 存储空间：
 * {@link #isLowMemory(Context)}                // 是否是低内存：false
 * {@link #getTotalRAMSize(Context))            // 手机总RAM：5903380480    = 5.49GB
 * {@link #getAvailRAMSize(Context))            // 手机余RAM：1810214912    = 1.68GB
 * {@link #getInternalStorageTotalSize())       // 内置总ROM：118981873664  = 110.81GB
 * {@link #getInternalStorageAvailSize())       // 内置余ROM：3656527872    = 3.40GB
 * {@link #getExternalStorageTotalSize())       // 扩展总ROM：118981873664  = 110.81GB
 * {@link #getExternalStorageAvailSize())       // 扩展余ROM：3656527872    = 3.40GB
 * Created by wuguangxin on 2020/8/9.
 */
public class StorageUtils {

    private static final String TAG = "StorageUtils";

    /*public static void test(Context context) {
        Logger.i(TAG, "========内存区====================================");
        Logger.i(TAG, "getFilesDir()：" + getFilesDir(context));                                             // /data/user/0/<包名>/files
        Logger.i(TAG, "getCacheDir()：" + getCacheDir(context));                                             // /data/user/0/<包名>/cache
        Logger.i(TAG, "getCodeCacheDir()：" + getCodeCacheDir(context));                                     // /data/user/0/<包名>/code_cache

        Logger.i(TAG, "getExternalCacheDir()：" + getExternalCacheDir(context));                             // /storage/emulated/0/Android/data/<包名>/cache
        Logger.i(TAG, "getExternalFilesDir()：" + getExternalFilesDir(context));                             // /storage/emulated/0/Android/data/<包名>/files
        Logger.i(TAG, "getExternalFilesDirDCIM()：" + getExternalFilesDirDCIM(context));                     // /storage/emulated/0/Android/data/<包名>/files/DCIM
        Logger.i(TAG, "getExternalFilesDirCache()：" + getExternalFilesDirCache(context));                   // /storage/emulated/0/Android/data/<包名>/files/Caches
        Logger.i(TAG, "getExternalFilesDirMusic()：" + getExternalFilesDirMusic(context));                   // /storage/emulated/0/Android/data/<包名>/files/Music
        Logger.i(TAG, "getExternalFilesDirAlarms()：" + getExternalFilesDirAlarms(context));                 // /storage/emulated/0/Android/data/<包名>/files/Alarms
        Logger.i(TAG, "getExternalFilesDirMovies()：" + getExternalFilesDirMovies(context));                 // /storage/emulated/0/Android/data/<包名>/files/Movies
        Logger.i(TAG, "getExternalFilesDirPictures()：" + getExternalFilesDirPictures(context));             // /storage/emulated/0/Android/data/<包名>/files/Pictures
        Logger.i(TAG, "getExternalFilesDirPodcasts()：" + getExternalFilesDirPodcasts(context));             // /storage/emulated/0/Android/data/<包名>/files/Podcasts
        Logger.i(TAG, "getExternalFilesDirDownloads()：" + getExternalFilesDirDownloads(context));           // /storage/emulated/0/Android/data/<包名>/files/Download
        Logger.i(TAG, "getExternalFilesDirDocuments()：" + getExternalFilesDirDocuments(context));           // /storage/emulated/0/Android/data/<包名>/files/Documents
        Logger.i(TAG, "getExternalFilesDirRingtones()：" + getExternalFilesDirRingtones(context));           // /storage/emulated/0/Android/data/<包名>/files/Ringtones
        Logger.i(TAG, "getExternalFilesDirAudioBooks()：" + getExternalFilesDirAudioBooks(context));         // /storage/emulated/0/Android/data/<包名>/files/Audiobooks
        Logger.i(TAG, "getExternalFilesDirNotifications()：" + getExternalFilesDirNotifications(context));   // /storage/emulated/0/Android/data/<包名>/files/Notifications

        Logger.i(TAG, "========存储卡====================================");
        Logger.i(TAG, "hasExternalStorage()：" + hasExternalStorage());                      // true
        Logger.i(TAG, "getRootDirectory()：" + getRootDirectory());                          // /system
        Logger.i(TAG, "getDataDirectory()：" + getDataDirectory());                          // /data
        Logger.i(TAG, "getDownloadCacheDirectory()：" + getDownloadCacheDirectory());        // /data/cache
        Logger.i(TAG, "getDirectoryDCIM()：" + getDirectoryDCIM());                          // /storage/emulated/0/DCIM
        Logger.i(TAG, "getDirectoryMusic()：" + getDirectoryMusic());                        // /storage/emulated/0/Music
        Logger.i(TAG, "getDirectoryAlarms()：" + getDirectoryAlarms());                      // /storage/emulated/0/Alarms
        Logger.i(TAG, "getDirectoryMovies()：" + getDirectoryMovies());                      // /storage/emulated/0/Movies
        Logger.i(TAG, "getDirectoryPictures()：" + getDirectoryPictures());                  // /storage/emulated/0/Pictures
        Logger.i(TAG, "getDirectoryDownloads()：" + getDirectoryDownloads());                // /storage/emulated/0/Download
        Logger.i(TAG, "getDirectoryDocuments()：" + getDirectoryDocuments());                // /storage/emulated/0/Documents
        Logger.i(TAG, "getDirectoryScreenshots()：" + getDirectoryScreenshots());            // /storage/emulated/0/Screenshots

        Logger.i(TAG, "========容量====================================");
        Logger.i(TAG, "isLowMemory()：" + isLowMemory(context));                             // 是否是低内存：false
        Logger.i(TAG, "getTotalRAMSize()：" + getTotalRAMSize(context));                     // 5903380480    = 5.49GB
        Logger.i(TAG, "getAvailRAMSize()：" + getAvailRAMSize(context));                     // 1810214912    = 1.68GB
        Logger.i(TAG, "getInternalStorageTotalSize()：" + getInternalStorageTotalSize());    // 118981873664  = 110.81GB
        Logger.i(TAG, "getInternalStorageAvailSize()：" + getInternalStorageAvailSize());    // 3656527872    = 3.40GB
        Logger.i(TAG, "getExternalStorageTotalSize()：" + getExternalStorageTotalSize());    // 118981873664  = 110.81GB
        Logger.i(TAG, "getExternalStorageAvailSize()：" + getExternalStorageAvailSize());    // 3656527872    = 3.40GB


         小米8（Android 10）测试
        // ==========内存区====================================
        StorageUtils.getFilesDir()：               /data/user/0/<包名>/files
        StorageUtils.getCacheDir()：               /data/user/0/<包名>/cache
        StorageUtils.getCodeCacheDir()：           /data/user/0/<包名>/code_cache

        // ==========App====================================
        StorageUtils.getExternalCacheDir()：               /storage/emulated/0/Android/data/<包名>/cache
        StorageUtils.getExternalFilesDir()：               /storage/emulated/0/Android/data/<包名>/files
        StorageUtils.getExternalFilesDirDCIM()：           /storage/emulated/0/Android/data/<包名>/files/DCIM
        StorageUtils.getExternalFilesDirCache()：          /storage/emulated/0/Android/data/<包名>/files/Caches
        StorageUtils.getExternalFilesDirMusic()：          /storage/emulated/0/Android/data/<包名>/files/Music
        StorageUtils.getExternalFilesDirAlarms()：         /storage/emulated/0/Android/data/<包名>/files/Alarms
        StorageUtils.getExternalFilesDirMovies()：         /storage/emulated/0/Android/data/<包名>/files/Movies
        StorageUtils.getExternalFilesDirPictures()：       /storage/emulated/0/Android/data/<包名>/files/Pictures
        StorageUtils.getExternalFilesDirPodcasts()：       /storage/emulated/0/Android/data/<包名>/files/Podcasts
        StorageUtils.getExternalFilesDirDownloads()：      /storage/emulated/0/Android/data/<包名>/files/Download
        StorageUtils.getExternalFilesDirDocuments()：      /storage/emulated/0/Android/data/<包名>/files/Documents
        StorageUtils.getExternalFilesDirRingtones()：      /storage/emulated/0/Android/data/<包名>/files/Ringtones
        StorageUtils.getExternalFilesDirAudioBooks()：     /storage/emulated/0/Android/data/<包名>/files/Audiobooks
        StorageUtils.getExternalFilesDirNotifications()：  /storage/emulated/0/Android/data/<包名>/files/Notifications
        // ==========存储卡区====================================

        StorageUtils.getRootDirectory()：          /system
        StorageUtils.getDataDirectory()：          /data
        StorageUtils.getDownloadCacheDirectory()： /data/cache
        // 共享存储区
        StorageUtils.hasExternalStorage()：        true
        StorageUtils.getDirectoryPictures()：      /storage/emulated/0/Pictures
        StorageUtils.getDirectoryDCIM()：          /storage/emulated/0/DCIM
        StorageUtils.getDirectoryMusic()：         /storage/emulated/0/Music
        StorageUtils.getDirectoryAlarms()：        /storage/emulated/0/Alarms
        StorageUtils.getDirectoryMovies()：        /storage/emulated/0/Movies
        StorageUtils.getDirectoryDownloads()：     /storage/emulated/0/Download
        StorageUtils.getDirectoryScreenshots()：   /storage/emulated/0/Screenshots
        StorageUtils.getDirectoryDocuments()：     /storage/emulated/0/Documents
        // ==========存储空间====================================
        StorageUtils.getTotalRAMSize()            // 5903380480       = 5.90GB
        StorageUtils.getAvailRAMSize()            // 1943891968       = 1.94GB
        StorageUtils.getTotalMemorySize()         // 118981873664     = 118.98
        StorageUtils.getAvailMemorySize()         // 3524849664       = 3.52GB
        StorageUtils.getTotalExternalMemorySize() // 118981873664     = 118.98
        StorageUtils.getAvailExternalMemorySize() // 3524849664       = 3.52GB
    }*/

    /**
     * 获取 ActivityManager
     *
     * @param context 上下文
     * @return
     */
    public static ActivityManager getActivityManager(Context context) {
        return context == null ? null : (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
    }

    // ####### 内存区 #############################################################################


    /**
     * 获取手机内存的 cache 目录。
     * 路径：/data/user/0/<包名>/cache
     *
     * @param context 上下文
     * @return
     */
    public static File getCacheDir(Context context) {
        return context == null ? null : context.getCacheDir();
    }

    /**
     * 获取手机内存的 files 目录，需要SD读写权限。
     * 路径：/data/user/0/<包名>/files
     *
     * @param context 上下文
     * @return
     */
    public static File getFilesDir(Context context) {
        return context == null ? null : context.getFilesDir();
    }

    /**
     * 返回用于存储缓存代码的文件系统上应用程序特定缓存目录的绝对路径。
     *
     * @param context
     * @return
     */
    public static File getCodeCacheDir(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context == null ? null : context.getCodeCacheDir();
        }
        return null;
    }

    /**
     * 获取扩展存储卡中的缓存目录。存储在该目录下的数据将随应用被卸载而清除。
     * 路径：/storage/emulated/0/Android/data/<包名>/cache
     *
     * @param context 上下文
     * @return
     */
    public static File getExternalCacheDir(Context context) {
        return context == null ? null : context.getExternalCacheDir();
    }

    /**
     * 获取扩展存储卡的 files 目录。存储在该目录下的数据将随应用被卸载而清除。
     * 路径：/storage/emulated/0/Android/data/<包名>/files
     *
     * @param context 上下文
     * @return
     */
    public static File getExternalFilesDir(Context context) {
        return context == null ? null : context.getExternalFilesDir(null);
    }

    /**
     * 获取扩展存储卡的 DCIM（相册）目录。存储在该目录下的数据将随应用被卸载而清除。
     * 路径：/storage/emulated/0/Android/data/<包名>/files/DCIM
     *
     * @param context 上下文
     * @return
     */
    public static File getExternalFilesDirDCIM(Context context) {
        return context == null ? null : context.getExternalFilesDir(Environment.DIRECTORY_DCIM);
    }

    /**
     * 获取扩展存储卡的 cache（缓存） 目录，需要存储卡读写权限。
     * 路径：/storage/emulated/0/Android/data/<包名>/files/Caches
     *
     * @param context 上下文
     * @return
     */
    public static File getExternalFilesDirCache(Context context) {
        return context == null ? null : context.getExternalFilesDir("Caches");
    }

    /**
     * 获取扩展存储卡的 music 目录，需要存储卡读写权限。
     * 路径：/storage/emulated/0/Android/data/<包名>/files/Music
     *
     * @param context 上下文
     * @return
     */
    public static File getExternalFilesDirMusic(Context context) {
        return context == null ? null : context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
    }

    /**
     * 获取扩展存储卡的 Alarms(警告铃声) 目录，需要存储卡读写权限。
     * 路径：/storage/emulated/0/Android/data/<包名>/files/Alarms
     *
     * @param context 上下文
     * @return
     */
    public static File getExternalFilesDirAlarms(Context context) {
        return context == null ? null : context.getExternalFilesDir(Environment.DIRECTORY_ALARMS);
    }

    /**
     * 获取扩展存储卡的 Movies(电影) 目录，需要存储卡读写权限。
     * 路径：/storage/emulated/0/Android/data/<包名>/files/Movies
     *
     * @param context 上下文
     * @return
     */
    public static File getExternalFilesDirMovies(Context context) {
        return context == null ? null : context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
    }

    /**
     * 获取扩展存储卡的 Pictures(图片) 目录，需要存储卡读写权限。
     * 路径：/storage/emulated/0/Android/data/<包名>/files/Pictures
     *
     * @param context 上下文
     * @return
     */
    public static File getExternalFilesDirPictures(Context context) {
        return context == null ? null : context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    /**
     * 获取扩展存储卡的 Podcasts(博客) 目录，需要存储卡读写权限。
     * 路径：/storage/emulated/0/Android/data/<包名>/files/Podcasts
     *
     * @param context 上下文
     * @return
     */
    public static File getExternalFilesDirPodcasts(Context context) {
        return context == null ? null : context.getExternalFilesDir(Environment.DIRECTORY_PODCASTS);
    }

    /**
     * 获取扩展存储卡的 Download(下载) 目录，需要存储卡读写权限。
     * 路径：/storage/emulated/0/Android/data/<包名>/files/Download
     *
     * @param context 上下文
     * @return
     */
    public static File getExternalFilesDirDownloads(Context context) {
        return context == null ? null : context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
    }

    /**
     * 获取扩展存储卡的 Documents(文档) 目录，需要存储卡读写权限。
     * 路径：/storage/emulated/0/Android/data/<包名>/files/Documents
     *
     * @param context 上下文
     * @return
     */
    public static File getExternalFilesDirDocuments(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return context == null ? null : context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        }
        return null;
    }

    /**
     * 获取扩展存储卡的 Ringtones(铃声) 目录，需要存储卡读写权限。
     * 路径：/storage/emulated/0/Android/data/<包名>/files/Ringtones
     *
     * @param context 上下文
     * @return
     */
    public static File getExternalFilesDirRingtones(Context context) {
        return context == null ? null : context.getExternalFilesDir(Environment.DIRECTORY_RINGTONES);
    }

    /**
     * 获取扩展存储卡的 Audiobooks(有声读物) 目录，需要存储卡读写权限。
     * 路径：/storage/emulated/0/Android/data/<包名>/files/Audiobooks
     *
     * @param context 上下文
     * @return
     */
    public static File getExternalFilesDirAudioBooks(Context context) {
        try {
            //  注意：360N4手机会报错，所以加try catch。
            //  No static field DIRECTORY_AUDIOBOOKS of type Ljava/lang/String; in class Landroid/os/Environment;
            //  or its superclasses (declaration of 'android.os.Environment' appears in /system/framework/framework.jar)
            return context == null ? null : context.getExternalFilesDir("Audiobooks");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取扩展存储卡的 Notifications(通知铃声) 目录，需要存储卡读写权限。
     * 路径：/storage/emulated/0/Android/data/<包名>/files/Notifications
     *
     * @param context 上下文
     * @return
     */
    public static File getExternalFilesDirNotifications(Context context) {
        return context == null ? null : context.getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS);
    }

    // ####### 存储卡区 #########################################################################
    // ####### 存储卡区 #########################################################################
    // ####### 存储卡区 #########################################################################

//    Environment.getRootDirectory()            File DIR_ANDROID_ROOT           // "/system"
//    Environment.getDataDirectory()            File DIR_ANDROID_DATA           // "/data");
//    Environment.getDownloadCacheDirectory()   File DIR_DOWNLOAD_CACHE         // "/cache");

//    Environment.getStorageDirectory()         File DIR_ANDROID_STORAGE        // "/storage");   // hide
//    Environment.getExpandDirectory()          File DIR_ANDROID_EXPAND         // "/mnt/expand");// hide
//    Environment.getProductDirectory()         File DIR_PRODUCT_ROOT           // "/product");   // 系统API
//    Environment.getVendorDirectory()          File DIR_VENDOR_ROOT            // "/vendor");    // 系统API
//    Environment.getOemDirectory()             File DIR_OEM_ROOT               // "/oem");       // 系统API
//    Environment.getOdmDirectory()             File DIR_ODM_ROOT               // "/odm");       // 系统API
//    Environment.getProductServicesDirectory() File DIR_PRODUCT_SERVICES_ROOT  // "/product_services"); // 系统API

    /**
     * 判断有没有挂载存储卡。
     * Environment.getExternalStorageState()：返回sd卡的状态，
     * Environment.MEDIA_MOUNTED：表示被挂载
     */
    public static boolean hasExternalStorage() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取手机内存的 system 目录（根目录）。
     * 路径：/system
     *
     * @return
     */
    public static File getRootDirectory() {
        return Environment.getRootDirectory();
    }

    /**
     * 获取手机内存的 Data 文件目录。
     * 路径：/data
     *
     * @return
     */
    public static File getDataDirectory() {
        return Environment.getDataDirectory();
    }

    /**
     * 获取手机内存的 cache 目录。
     * 路径：/data/cache
     *
     * @return
     */
    public static File getDownloadCacheDirectory() {
        return Environment.getDownloadCacheDirectory();
    }

    /**
     * 获取扩展存储卡的主目录。未安装则返回null
     * 路径：/storage/emulated/0
     *
     * @return
     */
    private static File getExternalStorageDirectory() {
        return Environment.getExternalStorageDirectory();
    }

    /**
     * 获取扩展存储卡的【照片-Pictures】目录。
     * 路径：/storage/emulated/0/Pictures
     *
     * @return
     */
    public static File getDirectoryPictures() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    /**
     * 获取扩展存储卡的【相册-DCIM】目录。
     * 路径：/storage/emulated/0/DCIM
     *
     * @return
     */
    public static File getDirectoryDCIM() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    }

    /**
     * 获取扩展存储卡的【音乐-Music】目录。
     * 路径：/storage/emulated/0/Music
     *
     * @return
     */
    public static File getDirectoryMusic() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
    }

    /**
     * 获取扩展存储卡的【警告声-Alarms】目录。
     * 路径：/storage/emulated/0/Alarms
     *
     * @return
     */
    public static File getDirectoryAlarms() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS);
    }

    /**
     * 获取扩展存储卡的【电影-Movies】目录。
     * 路径：/storage/emulated/0/Movies
     *
     * @return
     */
    public static File getDirectoryMovies() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
    }

    /**
     * 获取扩展存储卡的【下载-Download】目录。
     * 路径：/storage/emulated/0/Download
     *
     * @return
     */
    public static File getDirectoryDownloads() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    }

    /**
     * 获取扩展存储卡的【截屏-Screenshots】目录。
     * 路径：/storage/emulated/0/Screenshots
     *
     * @return
     */
    public static File getDirectoryScreenshots() {
        // 注意：360N4手机报：No static field DIRECTORY_SCREENSHOTS, 直接写字符串
        return Environment.getExternalStoragePublicDirectory("Screenshots" /*Environment.DIRECTORY_SCREENSHOTS*/);
    }

    /**
     * 获取扩展存储卡的【文档-Documents】目录。
     * 注：为了改善用户隐私，直接访问共享/扩展存储设备被废弃。官方建议使用Context.getExternalFilesDir(String)来访问
     * 路径：/storage/emulated/0/Documents
     *
     * @return
     */
    public static File getDirectoryDocuments() {
        if (Build.VERSION.SDK_INT >= 19) {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        }
        return null;
    }

    // ####### 存储空间 #########################################################################
    // ####### 存储空间 #########################################################################
    // ####### 存储空间 #########################################################################

    /**
     * 是否是低内存
     *
     * @param context
     * @return
     */
    public static boolean isLowMemory(Context context) {
        if (context != null) {
            ActivityManager manager = getActivityManager(context);
            if (manager != null) {
                MemoryInfo memoryInfo = new MemoryInfo();
                manager.getMemoryInfo(memoryInfo);
                return memoryInfo.lowMemory;
            }
        }
        return false;
    }

    /**
     * 获取手机的运行内存（RAM）总容量。（兼容API16以下）
     *
     * @param context 上下文
     * @return 手机的总RAM（运存）
     */
    private static long getTotalRAMSize_API16Before(Context context) {
        if (Build.VERSION.SDK_INT >= 16) {
            ActivityManager manager = getActivityManager(context);
            if (manager != null) {
                MemoryInfo memoryInfo = new MemoryInfo();
                manager.getMemoryInfo(memoryInfo);
                return memoryInfo.totalMem;
            }
        } else {
            try {
                FileReader localFileReader = new FileReader("/proc/meminfo"); // /proc/meminfo系统内存信息文件
                BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
                String lineStr = localBufferedReader.readLine();
                long totalRAM = 0;
                if (lineStr != null) {
                    String[] lines = lineStr.split("\\s+");
                    if (lines.length >= 2) {
                        String line = lines[1];
                        if (line != null) {
                            totalRAM = Long.parseLong(line) * 1024; // 获得的是KB，*1024转为byte
                        }
                    }
                }
                localBufferedReader.close();
                return totalRAM;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 获取运行内存总容量-RAM（单位byte）。
     *
     * @param context 上下文
     * @return
     */
    public static long getTotalRAMSize(Context context) {
        if (context != null) {
            if (Build.VERSION.SDK_INT >= 16) {
                ActivityManager manager = getActivityManager(context);
                if (manager != null) {
                    MemoryInfo memoryInfo = new MemoryInfo();
                    manager.getMemoryInfo(memoryInfo);
                    return memoryInfo.totalMem;
                }
            } else {
                return getTotalRAMSize_API16Before(context);
            }
        }
        return 0;
    }

    /**
     * 获取运行内存剩余容量-RAM（单位byte）。
     *
     * @param context 上下文
     * @return
     */
    public static long getAvailRAMSize(Context context) {
        if (context != null) {
            ActivityManager manager = getActivityManager(context);
            if (manager != null) {
                MemoryInfo memoryInfo = new MemoryInfo();
                manager.getMemoryInfo(memoryInfo);
                return memoryInfo.availMem;
            }
        }
        return 0;
    }

    /**
     * 获取内置存储卡总容量-ROM（单位byte）
     *
     * @return
     */
    public static long getInternalStorageTotalSize() {
        StatFs stat = new StatFs(getDataDirectory().getPath());
        long totalBlocks = stat.getBlockCount(); // 获取区块的总数
        long blockSize = stat.getBlockSize();
        return totalBlocks * blockSize; // 也是 stat.getTotalBytes() 的值，只是需要API18
    }

    /**
     * 获取内置存储卡剩余容量-ROM（单位byte）
     *
     * @return
     */
    public static long getInternalStorageAvailSize() {
        StatFs stat = new StatFs(getDataDirectory().getPath());
        long availBlocks = stat.getAvailableBlocks(); // 获取区块的可用数
        long blockSize = stat.getBlockSize(); // 获取单个区块的大小
        return availBlocks * blockSize;
    }

    /**
     * 获取扩展存储卡总容量-ROM（单位byte）
     *
     * @return
     */
    public static long getExternalStorageTotalSize() {
        StatFs stat = new StatFs(getExternalStorageDirectory().getPath());
        long totalBlocks = stat.getBlockCount(); // 获取区块的总数
        long blockSize = stat.getBlockSize(); // 获取单个区块的大小
        return totalBlocks * blockSize;
    }

    /**
     * 获取扩展存储卡剩余容量-ROM（单位byte）
     *
     * @return
     */
    public static long getExternalStorageAvailSize() {
        StatFs stat = new StatFs(getExternalStorageDirectory().getPath());
        long availBlocks = stat.getAvailableBlocks(); // 获取区块的可用数
        long blockSize = stat.getBlockSize(); // 获取单个区块的大小
        return availBlocks * blockSize; // 总大小
    }
}