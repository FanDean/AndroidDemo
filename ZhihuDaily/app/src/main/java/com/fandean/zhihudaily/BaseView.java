package com.fandean.zhihudaily;

import android.view.View;

/**
 * Created by fan on 17-6-10.
 */

public interface BaseView<T> {
    //为View设置Presenter
    void setPresenter(T presenter);
    //初始化界面
    void initViews(View view);
}
