package com.fandean.photogallery.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.fandean.photogallery.service.PollService;

/**
 * 一个隐藏前台通知的通用型Fragment
 * Created by fan on 17-7-14.
 */

public abstract class VisibleFragment extends Fragment {
    private static final String TAG = "VisibleFragment";
    private BroadcastReceiver mOnshowNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getActivity(),
                    "Got a broadcast: " + intent.getAction(),
                    Toast.LENGTH_LONG)
                    .show();

            //当接收到此广播，表示当前Fragment处于可见生命周期（start-stop之间）,因此需要取消消息的发送
            //设置结果码，可以直接使用Activity的RESULT_CANCELED
            setResultCode(Activity.RESULT_CANCELED);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(PollService.ACTION_SHOW_NOTIFICATION);
        getActivity().registerReceiver(mOnshowNotification, intentFilter,
                PollService.PERM_PRIVATE, null);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mOnshowNotification);
    }


}
