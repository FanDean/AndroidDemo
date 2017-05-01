package com.fandean.androidhive.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * 这是一个单例类，用于初始化Volley的core对象
 * Created by fan on 17-4-19.
 */

public class AppController extends Application {

    private static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate(){
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance(){
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag){
        //如果没有指定tag则默认设置为TAG
        req.setTag(TextUtils.isEmpty(tag)?TAG:tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req){
        //如果没有指定tag则设置默认tag
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * 取消所有标记为tag的请求
     * @param tag
     */
    public void cancelPendingRequests(Object tag){
        if (mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }

}
