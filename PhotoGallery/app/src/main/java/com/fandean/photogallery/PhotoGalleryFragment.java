package com.fandean.photogallery;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by fan on 17-7-10.
 */

public class PhotoGalleryFragment extends Fragment {
//    @BindView(R.id.fragment_photo_gallery_recycler_view)
    private static final String TAG = "PhotoGalleryFragment";
    public static final int UPDATE_UI = 2;

    RecyclerView mPhotoRecyclerView;
    private List<GalleryItem> mItems = new ArrayList<>();
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;
    private Handler responseHandler;

    public static PhotoGalleryFragment nweInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //保留Fragment实例
        setRetainInstance(true);
        //后台获取json
        new FechItemTask().execute();

        responseHandler = new Handler();
//        handlerUpdateUI();

        //创建并启动获取图片的线程
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownloadListener(new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>() {
            @Override
            public void onThumbnailDownloaded(PhotoHolder target, Bitmap thumbnail) {
                Drawable drawable = new BitmapDrawable(getResources(),thumbnail);
                target.bindGalleryItem(drawable);
            }
        });
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
        Logger.i(TAG,"Background thread startd");
    }

    //由于还需要同时传递PhotoHolder和Bitmap，无法使用此方法
    private void handlerUpdateUI() {
        responseHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == UPDATE_UI){

                }
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        //ButterKnife无法正常工作
//        unbinder = ButterKnife.bind(this, view);

        //网格布局
        mPhotoRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_photo_gallery_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        setupAdapter();

        return view;
    }

    private void setupAdapter() {
        if (isAdded()) {//判断fragment是否已经添加到activity
            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.clearQueue();
//        mThumbnailDownloader.quit();
//        Logger.i(TAG,"Background thread destroyed");
    }

    private class FechItemTask extends AsyncTask<Void, Void, List<GalleryItem>> {

        @Override
        protected List<GalleryItem> doInBackground(Void... params) {
            return new FlickrFetchr().fetchItems();
        }

        //该方法在doInBackground方法后，并在主线程中运行
        @Override
        protected void onPostExecute(List<GalleryItem> list) {
//            super.onPostExecute(list);
            mItems = list;
            setupAdapter();
        }
    }



    private class PhotoHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.fragment_photo_gallery_image_view)
        ImageView mImageView;

        public PhotoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.fragment_photo_gallery_image_view);
        }

        public void bindGalleryItem(Drawable drawable) {
            mImageView.setImageDrawable(drawable);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> list) {
            mGalleryItems = list;
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.gallery_item,parent,false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            GalleryItem item = mGalleryItems.get(position);
            //处理title中无用的信息
            String title1 = handlerTitle(item);
            Drawable drawable = getResources().getDrawable(R.drawable.md);
            holder.bindGalleryItem(drawable);
            //放入图片
            mThumbnailDownloader.queueThumbnail(holder,item.getUrl());
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }


    private String handlerTitle(GalleryItem item) {
        String title = item.getCaption();
//            Logger.d(title);
        if (title.contains("#")){
            int i = title.indexOf('#');
            title=title.substring(0,i);
//                Logger.d("去除#号：",title);
        }
        if (title.contains(":")){
            title = title.substring(0,title.indexOf(':'));
//                Logger.d("去除:号：",title);
        }
        if (title.contains("http")){
            title = title.substring(0,title.indexOf("http"));
//                Logger.d("去除http ：", title);
        }
//            Logger.d(title);
        return title;
    }
}
