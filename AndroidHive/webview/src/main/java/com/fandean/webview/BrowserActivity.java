package com.fandean.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * 该Activity处理WebView的向前或向后的导航和书签功能
 * Created by fan on 17-5-7.
 */

public class BrowserActivity extends AppCompatActivity {

    private String mUrl;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private float mDownX;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //替换ActionBar为toolbar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(" ");
        }

        mUrl = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(mUrl)){
            finish();
        }

        mWebView = (WebView) findViewById(R.id.webView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);

        initWebView();

        mWebView.loadUrl(mUrl);
    }

    private void initWebView(){
        //WebChromeClient类: 辅助WebView处理JavaScript的对话框，网站图标、网站标题等
        mWebView.setWebChromeClient(new MyWebChromeClient(this));
        //WebVewClient类: 处理各种通知和请求事件
        mWebView.setWebViewClient(new WebViewClient(){
            //打开网页时不调用系统浏览器，而是在本WebView中显示；网页上的所有加载都经过这个方法
            //(是否使用第三方浏览器) 返回true不调用 返回false调用
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
//                return super.shouldOverrideUrlLoading(view, request);//父类返回的是false

            }

            //开始载入页面时调用
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //显示进度条
                mProgressBar.setVisibility(View.VISIBLE);
                //该方法属于Activity，作用是声明菜单已经更改，应重新创建
                invalidateOptionsMenu();
            }

            //页面加载结束时调用
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
                invalidateOptionsMenu();
            }

            //加载页面的服务器出现错误时调用
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                mProgressBar.setVisibility(View.GONE);
                invalidateOptionsMenu();
            }
        });
        //清除缓存
        mWebView.clearCache(true);
        //清除历史记录
        mWebView.clearHistory();
        //支持JS
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getPointerCount() > 1){
                    //Multi touch detected(检测到多点触摸)
                    return true;
                }

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //保存触摸按下时的x坐标
                        mDownX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        //使触摸在x方向没有滑动
                        event.setLocation(mDownX,event.getY());
                        break;
                    default:
                        break;
                }
                //继续传播事件
                return false;
            }
        });
    }

    //创建选项菜单：只在第一次创建的时候调用一次
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.browser,menu);

        if (Utils.isBookmarked(this,mWebView.getUrl()))
        {
            //change icon color
            Utils.tintMenuIcon(getApplicationContext(),menu.getItem(0),R.color.colorAccent);
        }else {
            Utils.tintMenuIcon(getApplicationContext(),menu.getItem(0), android.R.color.white);
        }
        return true;
//        return super.onCreateOptionsMenu(menu);
    }

    //准备选项菜单：创建菜单之后对菜单进行修改；它会在每次按menu键时就被调用
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!mWebView.canGoBack()){//如果WebView不可返回
            menu.getItem(1).setEnabled(false);
            menu.getItem(1).getIcon().setAlpha(130);
        } else {
            menu.getItem(1).setEnabled(true);
            menu.getItem(1).getIcon().setAlpha(255);
        }

        if (!mWebView.canGoForward()){//如果WebView不可前进
            menu.getItem(2).setEnabled(false);
            menu.getItem(2).getIcon().setAlpha(130);
        } else {
            menu.getItem(2).setEnabled(true);
            menu.getItem(2).getIcon().setAlpha(255);
        }
        return true;
//        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // 系统会自动处理action bar上的Home/Up按钮点击事件

        if (item.getItemId() == android.R.id.home) finish();

        if (item.getItemId() == R.id.action_back) back();

        if (item.getItemId() == R.id.action_forward) forward();

        if (item.getItemId() == R.id.action_bookmark) {
//            Toast.makeText(BrowserActivity.this, "书签收藏功能暂未实现", Toast.LENGTH_SHORT).show();
            //添加或取消添加书签
            Utils.bookmarkUrl(this, mWebView.getUrl());

            String msg = Utils.isBookmarked(this,mWebView.getUrl())?
                    mWebView.getTitle() + " 已经收藏":
                    mWebView.getTitle() + " 已取消收藏";
            Snackbar snackbar = Snackbar.make(mCoordinatorLayout,msg,Snackbar.LENGTH_SHORT);
            snackbar.show();

            // refresh the toolbar icons, so that bookmark icon color changes
            // depending on bookmark status
            invalidateOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    //缓存中保存了相关历史记录
    private void back(){
        if (mWebView.canGoBack()) mWebView.goBack();
    }

    private void forward(){
        if (mWebView.canGoForward()) mWebView.goForward();
    }

    private class MyWebChromeClient extends WebChromeClient{
        Context mContext;

        public MyWebChromeClient(Context context){
            super();
            mContext = context;
        }
    }
}
