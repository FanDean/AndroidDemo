package com.fandean.zhihudaily.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 检查当前网络连接状态
 * Created by fan on 17-6-12.
 */

public class NetworkState {

    //TODO 未实现实时监听网络状况
    private ConnectivityManager mManager;
    private boolean isWifiConnected = false;
    private boolean isMobileConnected = false;
    private boolean isNetWorkConnected = false;

    public NetworkState(Context context){
        mManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfoWifi = mManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo networkInfoMobile = mManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (networkInfoMobile.isConnected()) isMobileConnected = true;
        if (networkInfoWifi.isConnected()) isWifiConnected = true;
        if (isWifiConnected || isMobileConnected) isNetWorkConnected = true;
    }


    //判断是否是Wifi连接
    public  boolean isWifiConnected(){
        return isWifiConnected;
    }

    public boolean isMobileConnected(){
        return isMobileConnected;
    }

    public boolean isNetWorkConnected(){
        return isNetWorkConnected;
    }
}
