package com.wuguangxin.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import androidx.annotation.RequiresApi;

/**
 * 文件操作工具类。
 * <p>资源路径：file:///android_asset/
 * <p>Created by wuguangxin on 2014/5/5
 */
public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();
    private static WeakReference<Context> contextWeakReference;

    /**
     * 初始化
     */
    public static void init(Context context) {
        contextWeakReference = new WeakReference<>(context);
    }

    private static Context getContext() {
        if (contextWeakReference != null) {
            return contextWeakReference.get();
        }
        return null;
    }

    /**
     * 把序列化对象保存到内存中
     *
     * @param obj            序列化对象
     * @param targetFilePath 保存路径
     * @return 序列化成功返回true
     */
    public static boolean writeByFile(Object obj, String targetFilePath) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(targetFilePath);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(oos, fos);
        }
        return false;
    }

    /**
     * 从缓存文件中读取序列化对象
     *
     * @param savePath 序列化该对象时保存的路径
     * @return 反序列化成功返回true
     */
    public static Object readByFile(String savePath) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(savePath);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(ois, fis);
        }
        return null;
    }

    /**
     * 存储List对象
     *
     * @param context  程序上下文
     * @param fileName 文件名，要在系统内保持唯一
     * @param list     对象数组集合，对象必须实现 Parcelable 接口
     * @return boolean 存储成功的标志
     */
    @SuppressLint("Recycle")
    public static <T extends Parcelable> boolean writeParcelableList(Context context, String fileName, List<T> list) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            Parcel parcel = Parcel.obtain();
            parcel.writeList(list);
            byte[] data = parcel.marshall();
            fos.write(data);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(fos);
        }
        return false;
    }

    /**
     * 获取文件大小
     *
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        return getFileSizeWhile(file);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            return getFileSizeWhile(file);
//        } else {
//            ForkJoinPool forkJoinPool = new ForkJoinPool();
//            return forkJoinPool.invoke(new FileSizeFinder(file));
//        }
    }

    /**
     * 获取文件大小（递归方式）
     *
     * @param file
     * @return
     */
    private static long getFileSizeWhile(File file) {
        if (file == null) return 0L;
        Log.i(TAG, file.getPath());
        if (file.isFile()) {
            return file.length();
        }
        long totalSize = 0;
        File[] files = file.listFiles();
        if (files != null) {
            for (File child : files) {
                totalSize += getFileSize(child);
            }
        }
        return totalSize;
    }


    /**
     * 获取文件大小（线程池方式）
     *
     * @param file
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static long getFileSizePool(File file) {
        return new ForkJoinPool().invoke(new FileSizeFinder(file));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static class FileSizeFinder extends RecursiveTask<Long> {
        private static final long serialVersionUID = 8204702901559704902L;
        final File file;

        public FileSizeFinder(final File theFile) {
            file = theFile;
        }

        @Override
        public Long compute() {
            long size = 0;
            if (file.isFile()) {
                size = file.length();
            } else {
                final File[] children = file.listFiles();
                if (children != null) {
                    List<ForkJoinTask<Long>> tasks = new ArrayList<ForkJoinTask<Long>>();
                    long length;
                    for (final File child : children) {
                        if (child.isFile()) {
                            length = child.length();
                            size += length;
                            Log.i(TAG, child.getPath() + " " + (length / 1024L / 1024L) + "MB");
                        } else {
                            tasks.add(new FileSizeFinder(child));
                        }
                    }
                    for (final ForkJoinTask<Long> task : invokeAll(tasks)) {
                        size += task.join();
                    }
                }
            }
            return size;
        }
    }

    /**
     * 格式化文件大小
     *
     * @param context
     * @param size    Long类型的字节大小
     * @return
     */
    public static String formatFileSize(Context context, long size) {
        return Formatter.formatFileSize(context, size);
    }

    /**
     * 格式化文件大小
     *
     * @param context
     * @param size    Long类型的字节大小
     * @return
     */
    public static String formatShortFileSize(Context context, long size) {
        return Formatter.formatShortFileSize(context, size);
    }

    /**
     * 复制文件
     *
     * @param context
     * @param fileName
     * @param targetFile 目标文件
     */
    public static void copyAssets(Context context, String fileName, File targetFile) {
        InputStream is = null;
        try {
            is = context.getAssets().open(fileName);
            copyInputStream(is, targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(is);
        }
    }

    /**
     * 复制流
     *
     * @param is         要复制的文件InputStream
     * @param targetFile 目标文件路径
     */
    public static void copyInputStream(InputStream is, File targetFile) {
        if (is == null || targetFile == null) {
            return;
        }
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(is);
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            out = new BufferedOutputStream(new FileOutputStream(targetFile));
            byte[] bytes = new byte[1024];
            int by;
            while ((by = in.read(bytes)) != -1) {
                out.write(bytes, 0, by);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(in, is);
            close(out);
        }
    }

    /**
     * 异步把文件从Assets下复制到data/data/下
     *
     * @param context       上下文
     * @param fileName      文件名
     * @param targetFileDir 目标文件所在目录
     */
    public static void copyRawFile(Context context, int resId, String fileName, File targetFileDir) {
        File targetFile = new File(targetFileDir, fileName); // 目标文件：data/data/下的缓存文件
        InputStream is = null;
        try {
            is = context.getResources().openRawResource(resId);
            copyInputStream(is, targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(is);
        }
    }

    /**
     * 复制assets中的文件到指定目录下
     *
     * @param context
     * @param assetsFileName
     * @param targetPath
     * @return
     */
    public static void copyAssetsFile(Context context, String assetsFileName, String targetPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = context.getAssets().open(assetsFileName);
            Log.i(TAG, "in.available() = " + in.available());
            Log.i(TAG, "data/ 下的大小为 = " + new File(context.getFilesDir(), assetsFileName).length());
            out = new FileOutputStream(targetPath + File.separator + assetsFileName);
            byte[] buf = new byte[in.available()];
            int count = 0;
            while ((count = in.read(buf)) > 0) {
                out.write(buf, 0, count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(in);
            close(out);
        }
    }

    /**
     * 异步复制assets中的文件夹到指定目录下
     *
     * @param context
     * @param dirName  Assets目录下的文件夹(不能是子目录)
     * @param savePath 目标文件夹
     */
    public static void copyAssetsDir(final Context context, final String dirName, final String savePath) {
        new AsyncTask<Void, Integer, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    File saveFilePath = new File(savePath + File.separator + dirName);
                    if (!saveFilePath.exists()) {
                        saveFilePath.mkdirs();
                    }
                    String[] fileNames = context.getAssets().list(dirName);
                    InputStream is = null;
                    for (String fileName : fileNames) {
                        String name = dirName + File.separator + fileName;
                        // 如果是文件，则直接拷贝，如果是文件夹，就会抛出异常，捕捉后递归拷贝
                        try {
                            is = context.getAssets().open(name);
                            copyAssetsFile(context, name, savePath);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            close(is);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    /**
     * 删除文件夹（未测试）
     *
     * @param fileDirPath 文件夹路径
     */
    public static boolean deleteFileDir(String fileDirPath) {
        if (TextUtils.isEmpty(fileDirPath)) {
            Log.i(TAG, "fileDirPath 不能为空");
            return false;
        }
        return deleteFileDir(new File(fileDirPath));
    }

    /**
     * 删除文件夹（TODO 未测试）
     *
     * @param dirFile File文件夹路径
     */
    public static boolean deleteFileDir(File dirFile) {
        if (dirFile != null && dirFile.exists()) {
            if (dirFile.isDirectory()) {
                try {
                    File[] files = dirFile.listFiles();
                    if (files == null) {
                        return true;
                    }
                    for (File file : files) {
                        if (file.isDirectory()) {
                            deleteFileDir(file);
                        } else {
                            boolean delete = file.delete();
                        }
                    }
                    // 最后删跟目录
                    String[] list = dirFile.list();
                    if (list != null && list.length > 0) {
                        deleteFileDir(dirFile);
                    } else {
                        return dirFile.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (dirFile.isFile()) {
                return dirFile.delete();
            }
        }
        return true;
    }

    /**
     * 删除文件
     *
     * @param pathString 文件路径
     * @return
     */
    public static boolean deleteFile(String pathString) {
        return deleteFile(new File(pathString));
    }

    /**
     * 删除文件
     *
     * @param file File
     * @return
     */
    public static boolean deleteFile(File file) {
        if (file != null && file.exists()) {
            return file.delete();
        }
        return true;
    }

    /**
     * 把数据写入到文件
     *
     * @param data     数据
     * @param filePath 目标文件路径
     */
    public static boolean writeTextToFile(String data, String filePath) {
        return writeTextToFile(data, new File(filePath));
    }

    /**
     * 把数据写入到文件
     *
     * @param data       数据
     * @param targetFile 目标文件
     */
    public static boolean writeTextToFile(String data, File targetFile) {
        if (data == null) data = "";
        return writeTextToFile(data.getBytes(), targetFile);
    }

    /**
     * 把数据写入到文件
     *
     * @param data       数据
     * @param targetFile 目标文件
     */
    public static boolean writeTextToFile(byte[] data, File targetFile) {
        FileOutputStream fos = null;
        try {
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            fos = new FileOutputStream(targetFile);
            fos.write(data);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(fos);
        }
        return false;

        /*
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new StringReader(data));
            writer = new BufferedWriter(new FileWriter(targetFile));
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
                writer.flush();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(reader);
            close(writer);
        }
        return false;
        */
    }

    /**
     * 读取本地文本数据
     *
     * @param filePath 目标文件路径
     */
    public static String readTextFromFile(String filePath) {
        return readTextFromFile(new File(filePath));
    }

    /**
     * 读取本地文本数据
     *
     * @param file 目标文件
     */
    public static String readTextFromFile(File file) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(br);
        }
        return null;
    }

    /**
     * 关闭读取流Reader
     *
     * @param readers 读取流
     */
    public static void close(Reader... readers) {
        try {
            for (int i = 0; i < readers.length; i++) {
                if (readers[i] != null) {
                    readers[i].close();
                    readers[i] = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭写入流Writer
     *
     * @param writers： Writer实例对象
     */
    public static void close(Writer... writers) {
        try {
            for (int i = 0; i < writers.length; i++) {
                if (writers[i] != null) {
                    writers[i].close();
                    writers[i] = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭输入流InputStream
     *
     * @param ins 输入流
     */
    public static void close(InputStream... ins) {
        try {
            for (int i = 0; i < ins.length; i++) {
                if (ins[i] != null) {
                    ins[i].close();
                    ins[i] = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭输出流OutputStream
     *
     * @param outs 输出流
     */
    public static void close(OutputStream... outs) {
        try {
            for (int i = 0; i < outs.length; i++) {
                if (outs[i] != null) {
                    outs[i].close();
                    outs[i] = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取Access下的文本数据
     *
     * @param context
     * @param accessFileName Access下的文件名
     * @param accessFileName
     * @return
     */
    public static String readTextFromAccess(Context context, String accessFileName) {
        InputStream in = null;
        try {
            in = context.openFileInput(accessFileName);
            byte[] buf = new byte[in.available()];
            while (in.read(buf) != -1) {
            }
            return new String(buf, Charset.forName("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(in);
        }
        return null;
    }

    /**
     * 获取照片文件名 格式为：20140718_221839.png
     *
     * @return
     */
    public static String getFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyMMdd_HHmmss", Locale.CHINA);
        String dateString = format.format(new Date());
        StringBuffer sb = new StringBuffer(dateString).append(".png");
        return sb.toString();
    }

    /**
     * 把字节流写到File文件里
     *
     * @param is
     * @param targetFile
     */
    public static void copyInputStreamToFile(InputStream is, File targetFile) {
        if (is == null || targetFile == null) {
            return;
        }
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(is);
            out = new BufferedOutputStream(new FileOutputStream(targetFile));
            byte[] bytes = new byte[1024];
            int by = 0;
            while ((by = in.read(bytes)) != -1) {
                out.write(bytes, 0, by);
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(in, is);
            close(out);
        }
    }

    /**
     * 异步把文件从Assets下复制到data/data/files/ 下
     *
     * @param context
     * @param fileName   要复制的文件名
     * @param targetFile 目标文件
     */
    public static void copyAssetsFileAsync(final Context context, final String fileName, final File targetFile) {
        // 第二个参数 是执行过程中进度 的数据类型
        new AsyncTask<Void, Integer, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                copyAssets(context, fileName, targetFile);
                return null;
            }
        }.execute();
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
            outChannel.close();
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
        byte[] buffer = new byte[8192];
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
    public static <T> void executeAsyncTask(AsyncTask<T, ?, ?> task, T... args) {
        if (Build.VERSION.SDK_INT >= 11) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, args);
        } else {
            task.execute(args);
        }
    }

    /**
     * 通过HttpURLConnection连接到指定的URL地址，返回一个InputStream，实用于下载图片等..
     *
     * @param url 地址
     * @return InputStream
     * @throws IOException 异常
     */
    public static InputStream syncDownloadByUrl(String url) throws IOException {
        AndroidUtils.disableConnectionReuseIfNecessary();
        URL mUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
        conn.setReadTimeout(10000); // 读取超时
        conn.setConnectTimeout(15000); //连接超时
        conn.setDoInput(true);
        conn.setRequestMethod("GET");
        conn.connect();
        return conn.getInputStream();
    }


    /**
     * 判断一个网络URL文件是否已下载到指定的目录下
     *
     * @param path 路劲
     * @param url  网络URL
     * @return
     */
    public static boolean isExistsFile(String path, String url) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(url)) {
            return false;
        }
        String oldFileName = url.substring(url.lastIndexOf('/') + 1);

        String fileName = MD5.encode(url);
        if (oldFileName.contains(".")) {
            fileName = fileName + oldFileName.substring(oldFileName.lastIndexOf(".")); // 自定义文件名
        } else {
            fileName = fileName + ".jpg";
        }
        File file = new File(path, fileName);
        return file.exists() && file.isFile();
    }
}
