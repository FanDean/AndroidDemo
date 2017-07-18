package com.fandean.photogallery.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fandean.photogallery.service.PollService;
import com.fandean.photogallery.util.QueryPreferences;
import com.orhanobut.logger.Logger;

/**
 * 用于手机重启后，检查当前设置是否为启动服务；如果是则唤醒服务
 * Created by fan on 17-7-14.
 */

public class StartupReceiver extends BroadcastReceiver {
    private static final String TAG = "StartupReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.i(TAG + "Received broadcast intent: " + intent.getAction());

        boolean isOn = QueryPreferences.isAlarmOn(context);
        PollService.setServiceAlarm(context,isOn);
    }
}
