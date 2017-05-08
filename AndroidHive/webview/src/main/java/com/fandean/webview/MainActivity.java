package com.fandean.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class MainActivity extends AppCompatActivity {

    private String mPostUrl = "http://api.androidhive.info/webview/index.html";
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private ImageView mImageView;
    private float mDownX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //显示返回主页的箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWebView = (WebView) findViewById(R.id.webView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mImageView = (ImageView) findViewById(R.id.backdrop);

        if (!TextUtils.isEmpty(getIntent().getStringExtra("postUrl"))){
            mPostUrl = getIntent().getStringExtra("postUrl");
        }

        initWebView();

        //初始化collapsing toolbar
        initCollapsingToolbar();

        /*
            WebView可以加载本地Web Page（HTML文件），如果要这样做请将html、css或fonts放在assets目录下
         */
        /*
        //启用JS功能
        mWebView.getSettings().setJavaScriptEnabled(true);
        //支持缩放功能
//        mWebView.getSettings().setSupportZoom(true);
        //设置内置的缩放控件
//        mWebView.getSettings().setBuiltInZoomControls(true);
        //隐藏原生的缩放控件
//        mWebView.getSettings().setDisplayZoomControls(false);

        //加载网络页面
        mWebView.loadUrl(mPostUrl);
        //加载本地html文件
//        mWebView.loadUrl("file:///android_asset/sample.html");
        //禁止显示水平滚动条
        mWebView.setHorizontalScrollBarEnabled(false);
        */

//        initWebView();
        renderPost();
    }

    private void initWebView(){
        mWebView.setWebChromeClient(new MyWebChromeClient(this));
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) return;

                if (view.getUrl().equals(url)) return;


                if (Utils.isSameDomain(mPostUrl,url)){
                    Log.d("fandean", "started中，当前URL：" + mWebView.getUrl()
                            + "\n请求的url：" + url);
                    //启动自己的Intent
                    Intent intent = new Intent(MainActivity.this,MainActivity.class);
                    intent.putExtra("postUrl",url);
                    startActivity(intent);
                } else {
                    //launch in-app browser i.e BrowserActivity
                    openInAppBarowser(url);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                /**
                 * Check for the url, if the url is from same domain
                 * open the url in the same activity as new intent
                 * else pass the url to browser activity
                 * */

                String url = request.getUrl().toString();

                if (Utils.isSameDomain(mPostUrl,url)){
                    Log.d("fandean", "请求的URL: " + request.getUrl().toString());
                    //启动自己的Intent
                    Intent intent = new Intent(MainActivity.this,MainActivity.class);
                    intent.putExtra("postUrl",url);
                    startActivity(intent);
                } else {
                    //launch in-app browser i.e BrowserActivity
                    openInAppBarowser(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("fandean","完成页面加载，URL = " + url);
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }
        });

        mWebView.clearCache(true);
        mWebView.clearHistory();
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

    private void renderPost(){
        mWebView.loadUrl(mPostUrl);
    }

    private void openInAppBarowser(String url){
        Intent intent = new Intent(MainActivity.this,BrowserActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }

    /**
     * 初始化 collapsing toolbar
     * 在滚动过程中显示或隐藏其标题
     */
    private void initCollapsingToolbar(){
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        //设置其为展开状态
        appBarLayout.setExpanded(true);

        //hiding & showing the textPostTitle when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1){
                    //更改为appBarLayout可滚动距离的最大值
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

                //已经滚动到最大值
                if (scrollRange + verticalOffset == 0){
                    collapsingToolbar.setTitle("Web View");
                    isShow = true;
                } else if (isShow){
                    collapsingToolbar.setTitle(" ");
                    isShow =false;
                }
            }
        });

        //加载toolbar头部的图片
        Glide.with(getApplicationContext()).load("http://api.androidhive.info/webview/nougat.jpg")
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView);
    }

    private class MyWebChromeClient extends WebChromeClient {
        Context context;

        public MyWebChromeClient(Context context) {
            super();
            this.context = context;
        }
    }
}
