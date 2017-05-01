package com.fandean.androidhive.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.fandean.androidhive.R;
import com.fandean.androidhive.adapter.GalleryAdapter;
import com.fandean.androidhive.app.AppController;
import com.fandean.androidhive.model.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 参考文章：<a href="http://www.androidhive.info/2016/04/android-glide-image-library-building-image-gallery-app/">Android Glide Image Library – Building Image Gallery App</a>
 *  RecyclerView
 *  ViewPager
 */
public class MainActivity extends AppCompatActivity {
    //Json的请求地址
    private static final String sENDPOINT = "http://api.androidhive.info/json/glide.json";
    private final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecycleView;
    private ProgressDialog mProgressDialog;
    private ArrayList<Image> mImages;
    private GalleryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecycleView = (RecyclerView) findViewById(R.id.recycler_view);

        mProgressDialog = new ProgressDialog(this);
        mImages = new ArrayList<>();
        mAdapter = new GalleryAdapter(this,mImages);

        //采用网格布局，显示两列
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2); //显示两列
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setAdapter(mAdapter);

        mRecycleView.addOnItemTouchListener
                (new GalleryAdapter.RecyclerTouchListener
                        (this,mRecycleView,
                                new GalleryAdapter.ClickListener() {
                                    @Override
                                    public void onClick(View view, int position) {
                                        Log.d(TAG,"onClick");
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("images",mImages);
                                        bundle.putInt("position",position);

                                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                                        newFragment.setArguments(bundle);
                                        newFragment.show(ft,"slideshow");
                                    }

                                    @Override
                                    public void onLongClick(View view, int position) {
                                        Log.d(TAG,"onLongClick");
                                    }
                                }));

        //获取图片
        fetchImages();

    }

    //获取图片
    private void fetchImages(){
        mProgressDialog.setMessage("Downloading json...");
        mProgressDialog.show();

        //Volley中相关方法
        JsonArrayRequest req = new JsonArrayRequest(sENDPOINT,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        //请求成功，隐藏进度条
                        mProgressDialog.hide();
                        //清除现有数据
                        mImages.clear();
                        for (int i = 0; i < response.length(); i++){
                            try{
                                JSONObject object = response.getJSONObject(i);
                                Image image = new Image();
                                image.setName(object.getString("name"));

                                JSONObject url = object.getJSONObject("url");
                                image.setSmall(url.getString("small"));
                                image.setMedium(url.getString("medium"));
                                image.setLarge(url.getString("large"));
                                image.setTimestamp(object.getString("timestamp"));

                                mImages.add(image);
                            }catch (JSONException e){
                                Log.e(TAG,"Json parsing error: " + e.getMessage());
//                                e.printStackTrace();
                            }
                        }
                        //通知适配器数据已经更改
                        mAdapter.notifyDataSetChanged();
                    }
                },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"Error: " + error.getMessage());
                mProgressDialog.hide();
            }
        });

        //将该请求添加到请求队列
        AppController.getInstance().addToRequestQueue(req);
    }
}
