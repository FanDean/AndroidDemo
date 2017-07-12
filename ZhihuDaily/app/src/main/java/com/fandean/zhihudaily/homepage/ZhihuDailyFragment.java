package com.fandean.zhihudaily.homepage;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by fan on 17-6-11.
 */

public class ZhihuDailyFragment extends Fragment implements ZhihuDailyContract.View {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        return null;
    }

    @Override
    public void setPresenter(ZhihuDailyContract.Presenter presenter) {

    }

    @Override
    public void initViews(View view) {

    }

}
