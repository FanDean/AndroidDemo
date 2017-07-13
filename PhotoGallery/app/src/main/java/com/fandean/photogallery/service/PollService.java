package com.fandean.photogallery.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.fandean.photogallery.R;
import com.fandean.photogallery.bean.GalleryItem;
import com.fandean.photogallery.ui.PhotoGalleryActivity;
import com.fandean.photogallery.util.FlickrFetchr;
import com.fandean.photogallery.util.QueryPreferences;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by fan on 17-7-13.
 */

public class PollService extends IntentService {
    private static final String TAG = "PollService";
    private static final long POLL_INTERVAL = 1000 * 60; //60s
//    private static final long POLL_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

    public static Intent newIntent(Context context){
        return new Intent(context,PollService.class);
    }

    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (!isNetworkAvailableAndConnected()) return;
        Logger.i(TAG + "Received an intent: " + intent);


        String query = QueryPreferences.getStoredQuery(this);
        String lastResultId = QueryPreferences.getLastResultId(this);
        List<GalleryItem> items;

        if (query == null) {
            items = new FlickrFetchr().fetchRecentPhotos();
        } else {
            items = new FlickrFetchr().searchPhotos(query);
        }

        if (items.size() == 0) {
            return;
        }

        String resultId = items.get(0).getId();
        if (resultId.equals(lastResultId)) {
            Logger.i(TAG + "Got an old result: " + resultId);
        } else {
            Logger.i(TAG + "Got a new result: " + resultId);

            Resources resources = getResources();
            //构建PendingIntent，用于在点击消息后进入应用程序
            Intent i = PhotoGalleryActivity.newIntent(this);
            PendingIntent pi = PendingIntent.getActivity(this,0,i,0);

            Notification notification = new NotificationCompat.Builder(this)
                    .setTicker(resources.getString(R.string.new_pictures_title))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(resources.getString(R.string.new_pictures_title))
                    .setContentText(resources.getString(R.string.new_pictures_text))
                    //点击消息所触发的动作
                    .setContentIntent(pi)
                    //true表示，单击消息后，该消息会重消息抽屉中删除
                    .setAutoCancel(true)
                    //设置默认声音和闪光，如果是振动就需要添加相关权限
                    .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS)
                    .build();


            //从当前context中取出
            NotificationManagerCompat notificationManager =
                    NotificationManagerCompat.from(this);
            //贴出消息
            //注意第一个整型参数，它表示该消息的id，在整个应用唯一(而不是整个系统)，
            // 如果使用同一ID发送两条消息，那么第二条消息会覆盖前一条。
            notificationManager.notify(0,notification);
        }
        QueryPreferences.setLastResultId(this,resultId);
    }


    /**
     * 用于在没有Activity时，运行后台服务
     * 可以利用系统服务AlarmManager发送Intent
     * 使用PendingIntent打包intent，再利用它将intent发送给AlarmManager
     *
     * 通过判断isOn，来启动或停止服务
     */
    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = PollService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(
                context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL, pi);
        } else {
            //撤销定时器
            alarmManager.cancel(pi);
            //撤销PendingIntent
            pi.cancel();
        }
    }

    //通过检查PendingIntent是否存在，来判断定时器是否激活
    public static boolean isServiceAlarmOn(Context context){
        Intent intent = PollService.newIntent(context);
        //通过传入flag_no_create标识，来判断PendingIntent是否存在，
        // 不存在则返回null,而不是创建它(上文中传入0的情况)。
        PendingIntent pi = PendingIntent
                .getService(context,0,intent,PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    //判断网络是否连接
    private boolean isNetworkAvailableAndConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&  //是否找得到可用网络
                cm.getActiveNetworkInfo().isConnected();   //是否完全连接
        return isNetworkConnected;
    }
}
