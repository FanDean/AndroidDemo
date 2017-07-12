package com.fandean.zhihudaily.interfaze;

import com.android.volley.VolleyError;

/**
 * Created by fan on 17-6-12.
 */

public interface OnStringListener {
    /**
     * 请求成功时回调
     */
    void onSuccess(String result);

    /**
     * 请求失败时调用
     */
    void onError(VolleyError error);
}

