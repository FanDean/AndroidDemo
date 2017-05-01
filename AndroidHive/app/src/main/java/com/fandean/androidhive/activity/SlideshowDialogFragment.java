package com.fandean.androidhive.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fandean.androidhive.R;
import com.fandean.androidhive.model.Image;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlideshowDialogFragment extends DialogFragment {

    private static final String TAG = SlideshowDialogFragment.class.getSimpleName();
    private ViewPager mViewPager;
    private TextView mTVCount;
    private TextView mTVTitle;
    private TextView mTVDate;
    private ArrayList<Image> mImages;
    private int selectedPosition = 0;
    private MyViewPagerAdapter myViewPagerAdapter;


    static SlideshowDialogFragment newInstance(){
        SlideshowDialogFragment f = new SlideshowDialogFragment();
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_slide,container,false);
        mViewPager = (ViewPager)v.findViewById(R.id.viewpager);
        mTVCount = (TextView)v.findViewById(R.id.lbl_count);
        mTVTitle = (TextView)v.findViewById(R.id.title);
        mTVDate = (TextView)v.findViewById(R.id.date);

        mImages = (ArrayList<Image>) getArguments().getSerializable("images");
        selectedPosition = getArguments().getInt("position");

        Log.e(TAG, "position: " + selectedPosition);
        Log.e(TAG,"images size" + mImages.size());

        myViewPagerAdapter = new MyViewPagerAdapter();
        mViewPager.setAdapter(myViewPagerAdapter);
        mViewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }


    private void setCurrentItem(int currentItem){
        mViewPager.setCurrentItem(currentItem,false);
        displayMetaInfo(selectedPosition);
    }

    private void displayMetaInfo(int position){
        //
        mTVCount.setText((position +1) + " of " + mImages.size());

        Image image = mImages.get(position);
        mTVTitle.setText(image.getName());
        mTVDate.setText(image.getTimestamp());
    }

    //
    ViewPager.OnPageChangeListener viewPagerPageChangeListener =
            new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    displayMetaInfo(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            };

    //Adapter
    private class MyViewPagerAdapter extends PagerAdapter{
        private LayoutInflater layoutInflater;

        @Override
        public int getCount() {
            return mImages.size();
        }

        /**
         * 负责初始化指定位置的页面，并且需要返回当前页面本身
         * （其实不一定要View本身，只要是能唯一标识该页面的key都可以，
         * 不过初学者一般就先用View本身作为key就可以啦）
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater)getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container,false);
            ImageView imagePreview = (ImageView) view.findViewById(R.id.image_preview);

            Image image = mImages.get(position);

            Glide.with(getActivity())
                    .load(image.getLarge())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imagePreview);

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View)object);
        }
    }


}
