package com.fandean.photogallery.app;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;


/**
 * Created by fan on 17-7-11.
 */

public class MyApplication extends Application {
    private static String TAG = "FanDean_Logger: ";

    @Override
    public void onCreate() {
        super.onCreate();
        //Logger 2.2 的改动
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag(TAG)
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }
}
