package com.fandean.photogallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.fandean.photogallery.PhotoGalleryFragment.UPDATE_UI;

/**
 * Created by fan on 17-7-12.
 */

public class ThumbnailDownloader<T> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";
    //消息的what属性，用来标识该消息是进行下载请求的消息
    private static final int MESSAGE_DOWNLOAD = 0;

    private Handler mRequestHandler;
    private ConcurrentMap<T,String> mRequestMap = new ConcurrentHashMap<>();
    private Boolean mHasQuit = false;
    private Handler mResponseHandler;

    /*
        自定义事件监听
     */

    private ThumbnailDownloadListener mThumbnailDownloadListener;

    public interface ThumbnailDownloadListener<T>{
        void onThumbnailDownloaded(T target, Bitmap thumbnail);
    }

    public void setThumbnailDownloadListener(ThumbnailDownloadListener<T> listener){
        mThumbnailDownloadListener = listener;
    }


    public ThumbnailDownloader(Handler handler){
        super(TAG);
        mResponseHandler = handler;
    }

    @Override
    public boolean quit() {
        mHasQuit = true;
        return super.quit();
    }

    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD){
                    T target = (T) msg.obj;
                    Log.i(TAG,"Got a request for URL: " + mRequestMap.get(target));
                    handleRequest(target);
                }
            }
        };
    }

    private void handleRequest(final T target) {
        final String url = mRequestMap.get(target);
        if (url == null) return;
        try {
            byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory
                    .decodeByteArray(bitmapBytes,0,bitmapBytes.length);

            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    //重点理解RecyclerView的运行机制和mRequestMap的使用，还有mHasQuit的使用
                    if (mRequestMap.get(target) != url || mHasQuit)return;
                    mRequestMap.remove(target);
                    mThumbnailDownloadListener.onThumbnailDownloaded(target,bitmap);
                }
            });
        } catch (IOException e) {
            Logger.e(e,"Error downloading image",TAG);
        }
    }

    public void clearQueue(){
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
    }

    //由于还需要同时传递PhotoHolder和Bitmap，无法使用此方法
    private void sendMsgToUpdateUI(Bitmap bitmap){
        mResponseHandler.obtainMessage(UPDATE_UI,bitmap)
                .sendToTarget();
    }


    //用于在onBindViewHolder()中调用
    public void queueThumbnail(T target, String url){
        Logger.i(TAG,"Got a URL: " + url);

        if (url == null){
            mRequestMap.remove(target);
        } else {
            //更新mRequestMap
            mRequestMap.put(target,url);
            //获取消息，并发送消息到队列中
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD,target)
                    .sendToTarget();
        }
    }



}

