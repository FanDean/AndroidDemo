package com.fandean.zhihudaily.homepage;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fandean.zhihudaily.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private Context mContext;
    private ZhihuDailyFragment zhihuDailyFragment;
    private ZhihuDailyPresenter zhihuDailyPresenter;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mContext = getActivity();

        //Fragment状态恢复
        if (savedInstanceState != null){
            FragmentManager manager = getChildFragmentManager();
        } else {
            //创建View实例
            zhihuDailyFragment = MainFragment.newInstance();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

}
