package com.fandean.swiperefresh.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fandean.swiperefresh.R;

import java.util.List;

/**
 * Created by fan on 17-5-10.
 */

public class SwipeListAdapter extends BaseAdapter {
    private  List<Movie> mMovieList;
    private  Activity mActivity;
    private String[] mBgColors;
    private LayoutInflater mInflater;

    public SwipeListAdapter(Activity activity, List<Movie> movieList){
        mActivity = activity;
        mMovieList = movieList;
        mBgColors = activity.getApplicationContext()
                .getResources().getStringArray(R.array.movie_serial_bg);
    }

    @Override
    public int getCount() {
        return mMovieList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMovieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mInflater == null){
            mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null){
            //convertView，用于转换的View，用于将之前加载好的布局进行缓存，以便之后可以重用
            convertView = mInflater.inflate(R.layout.list_row,null);
        }

        //还可进一步优化，使用ViewHolder对控件实例进行缓存，
        // 并使用View的setTag()方法保存该ViewHolder，详见《第一行代码-Android》
        TextView serial = (TextView) convertView.findViewById(R.id.serial);
        TextView title = (TextView) convertView.findViewById(R.id.title);

        serial.setText(String.valueOf(mMovieList.get(position).id));
        title.setText(mMovieList.get(position).title);

        String color = mBgColors[position % mBgColors.length];
        serial.setBackgroundColor(Color.parseColor(color));
        return convertView;
    }
}
