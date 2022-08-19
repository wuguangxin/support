package com.wuguangxin.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresPermission;

/**
 * 网络工具类。
 * Created by wuguangxin on 15/6/14.
 */
public class NetworkUtils {

    private static ConnectivityManager getConnectivityManager(Context context) {
        if (context != null) {
            return (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        return null;
    }

    /**
     * 判断是否有可用网络
     *
     * @param context 上下文
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isConnected(Context context) {
        ConnectivityManager manager = getConnectivityManager(context);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
        return false;
    }

    /**
     * 检测是否支持网络链接
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isNetSupport(Context context) {
        try {
            ConnectivityManager manager = getConnectivityManager(context);
            if (manager != null) {
                NetworkInfo[] infoArr = manager.getAllNetworkInfo();
                for (NetworkInfo info : infoArr) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断网络是否是WIFI网络
     *
     * @param context 上下文
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isWifiConnected1(Context context) {
        ConnectivityManager manager = getConnectivityManager(context);
        if (manager != null) {
             NetworkInfo info = manager.getActiveNetworkInfo();
            return info != null && info.getType() == ConnectivityManager.TYPE_WIFI;
        }
        return false;
    }


    /**
     * 判断网络是否是WIFI网络
     *
     * @param context 上下文
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager manager = getConnectivityManager(context);
        if (manager != null) {
            NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return info != null && info.isConnected();
        }
        return false;
    }


    /**
     * 判断移动网络是否可用。需要权限"ACCESS_NETWORK_STATE"。
     *
     * @param context 上下文
     * @return 基站的连接状态
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isBaseStateConnection(Context context) {
        ConnectivityManager manager = getConnectivityManager(context);
        if (manager != null) {
            NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return info != null && info.isConnected();
        }
        return false;
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager manager = getConnectivityManager(context);
        if (manager != null) {
            return manager.getActiveNetworkInfo();
        }
        return null;
    }

    /**
     * 打开系统网络设置界面
     *
     * @param context 上下文
     */
    public static void openNetworkSetting(final Context context) {
        //提示对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示").setMessage("无法连接网络，是否进行设置？")//
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        context.startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }


    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    public static String networkMessage = "网络在开小差，检查后再试吧";


    /**
     * 获取当前连接网络类型
     *
     * @param context
     * @return
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static int getNetworkType(Context context) {
        ConnectivityManager manager = getConnectivityManager(context);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null) {
                return info.getType();
            }
        }
        return -1;
    }

    /**
     * 检测是否使用3G/4G
     *
     * @param context
     * @return
     */
    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public static boolean is3Gor4G(Context context) {
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (manager != null) {
                int networkType = manager.getNetworkType();
                return networkType == TelephonyManager.NETWORK_TYPE_UMTS        // 3 3G
                        || networkType == TelephonyManager.NETWORK_TYPE_EVDO_A  // 6 API 4：中国电信的3g网络，是3G中的一种版本
                        || networkType == TelephonyManager.NETWORK_TYPE_HSDPA   // 8 API 5：3GPP中三个发展阶段：基本型HSDPA、增强型HSDPA、新空中接口。
                        || networkType == TelephonyManager.NETWORK_TYPE_HSUPA   // 9
                        || networkType == TelephonyManager.NETWORK_TYPE_HSPA    // 10
                        || networkType == TelephonyManager.NETWORK_TYPE_EVDO_B  // 12
                        || networkType == TelephonyManager.NETWORK_TYPE_LTE     // 13
                        || networkType == TelephonyManager.NETWORK_TYPE_EHRPD   // 14
                        || networkType == TelephonyManager.NETWORK_TYPE_HSPAP   // 15
                        ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 检测网络类型是否为移动数据连接
     *
     * @param networkInfo
     * @return
     */
    public static boolean isMobileNetworkInfo(NetworkInfo networkInfo) {
        return (networkInfo.getType() == 0) || (50 == networkInfo.getType());
    }


    /**
     * 检测是否开启wifi
     *
     * @param context
     * @return
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isWifiEnabled(Context context) {
        try {
            ConnectivityManager manager = getConnectivityManager(context);
            if (manager != null) {
                NetworkInfo info = manager.getActiveNetworkInfo();
                return info != null && info.getTypeName().equalsIgnoreCase("wifi");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取 WIFI 地址
     *
     * @param context 上下文
     * @return
     */
    public static String getWifiIpAddress(Context context) {
        WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (manager != null) {
            WifiInfo info = manager.getConnectionInfo();
            int ipAddress = info.getIpAddress();
            if (ipAddress == 0) return null;
            return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                    + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
        }
        return null;
    }
}
