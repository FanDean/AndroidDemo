package com.fandean.zhihudaily.bean;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fandean.zhihudaily.MyApplication;
import com.fandean.zhihudaily.interfaze.OnStringListener;

/**
 * Created by fan on 17-6-12.
 */

public class StringModelImpl {
//    private Context mContext;

/*    public StringModelImpl(Context context){
        mContext = context;
    }*/

    public void load(String url, final OnStringListener listener){
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                listener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
            }
        });
        MyApplication.getInstance().addToRequestQueue(stringRequest);
    }
}
