package com.fandean.swiperefresh.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.fandean.swiperefresh.R;
import com.fandean.swiperefresh.app.MyApplication;
import com.fandean.swiperefresh.helper.Movie;
import com.fandean.swiperefresh.helper.SwipeListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private String TAG = MainActivity.class.getSimpleName();
    //IMDB中评分前250的电影
    private String URL_TOP_250 = "http://api.androidhive.info/json/imdb_top_250.php?offset=";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private SwipeListAdapter mAdapter;
    private List<Movie> mMovieList;

    //该组电影的偏移量；第一组的偏移量为0，每组20部电影
    private int mOffSet = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.list_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        mMovieList = new ArrayList<>();
        mAdapter = new SwipeListAdapter(this,mMovieList);
        mListView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        //由于View的绘制流程与Activity的声明周期并不是一一对应，在onCreat()中并不能保证View已经处于正确阶段
        //而使用view.post()方法可以利用Android的异步消息处理机制来保证其包含的语句在View已经处于正确阶段
        //的情况下再在UI线程中执行。
        //关于该方法的详细介绍，见对应的Readme.md文件
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //改变刷新动画的的圆圈刷新状态；参数true表示显示刷新的圆圈
                mSwipeRefreshLayout.setRefreshing(true);
                fetchMovies();
            }
        });

    }

    /**
     * 当下拉刷新时回调该方法
     */
    @Override
    public void onRefresh(){
        fetchMovies();
    }


    /**
     * 使用http，获取电影的json
     */
    private void fetchMovies(){
        //在使用http进行连接前，显示刷新动画
        mSwipeRefreshLayout.setRefreshing(true);

        //追加偏移量到url尾部
        String url = URL_TOP_250 + mOffSet;

        // Volley's json array request object
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>(){

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG,response.toString());

                        if (response.length() > 0){

                            //正常情况下，每次返回20部电影信息
                            for (int i = 0; i< response.length(); i++){
                                try{
                                    JSONObject movieObj = response.getJSONObject(i);
                                    int rank = movieObj.getInt("rank");
                                    String title = movieObj.getString("title");
                                    Movie m = new Movie(rank,title);

                                    mMovieList.add(0,m);

                                    //updating offset value to highest value
                                    if (rank >= mOffSet){
                                        mOffSet = rank;
                                    }
                                }catch (JSONException e){
                                    Log.e(TAG,"Json parsing error: " + e.getMessage());
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                        //stopping swipe refresh,停止刷新
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Server Error: " + error.getMessage());

                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();

                //停止刷新
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

            //Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(req);
    }
}
