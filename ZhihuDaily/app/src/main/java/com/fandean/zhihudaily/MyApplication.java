package com.fandean.zhihudaily;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by fan on 17-6-12.
 */

public class MyApplication extends Application {
    public static final String TAG = MyApplication.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static MyApplication mInstance;

    public static synchronized MyApplication getInstance(){
        return mInstance;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;
    }

    public RequestQueue getRequestQueue(){
        if (mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    //泛型方法的书写形式，将泛型参数列表置于返回值之前
    public <T> void addToRequestQueue(Request<T> req, String tag){
        //如果没有指定tag则设置默认的tag为TAG
        req.setTag(TextUtils.isEmpty(tag)?TAG:tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req){
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * 取消所有标记为tag的请求
     */
    public void cancelPendingRequests(Object tag){
        if (mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }
}
