package com.fandean.androidhive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fandean.androidhive.R;
import com.fandean.androidhive.model.Image;

import java.util.List;

/**
 * Created by fan on 17-4-26.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {
    private List<Image> mImages;
    private Context mContext;

    public GalleryAdapter(Context context, List<Image> images) {
        mContext = context;
        mImages = images;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Image image = mImages.get(position);

        Glide.with(mContext).load(image.getMedium())
                .thumbnail(0.5f)    //略缩图
                .crossFade()        //淡入淡出
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //disk磁盘缓存策略：Glide 缓存了原始图像，全分辨率图像和另外小版本的图像
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView thumbnail;
        public MyViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        }
    }


    public interface ClickListener{
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{
        //手势识别
        private GestureDetector mGestureDetector;
        //TODO 用于回调的接口（内部接口(也属于内部类)，可以直接这样使用）
        private GalleryAdapter.ClickListener mClickListener;

        //构造函数
        public RecyclerTouchListener(Context context,final RecyclerView recyclerView,
                                     final GalleryAdapter.ClickListener clickListener){
            this.mClickListener = clickListener;
            mGestureDetector = new GestureDetector(context,
                    new GestureDetector.SimpleOnGestureListener(){
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            Log.d("GAdapter","onSingleTapUp");
                            return true;
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {
                            Log.d("GAdapter","onLongPress");
                            View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
                            if (child != null && mClickListener != null ){
                                mClickListener.onLongClick(child,recyclerView.getChildPosition(child));
                            }
                        }
                    });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(),e.getY());
            if (child !=null && mClickListener != null && mGestureDetector.onTouchEvent(e)){
                mClickListener.onClick(child,rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
