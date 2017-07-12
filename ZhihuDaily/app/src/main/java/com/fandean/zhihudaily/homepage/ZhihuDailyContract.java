package com.fandean.zhihudaily.homepage;

import com.fandean.zhihudaily.BasePresenter;
import com.fandean.zhihudaily.BaseView;

/**
 * Created by fan on 17-6-11.
 * 用于统一管理View和Presenter
 */

public interface ZhihuDailyContract {

    interface View extends BaseView<Presenter>{

    }

    //继承另一个接口
    interface Presenter extends BasePresenter {
        //请求数据
        void loadPosts(long date, boolean clearing);

        //刷新数据
        void refresh();

        //加载更多文章
        void loadMore(long date);

        //显示详情
        void startReading(int position);

        //随便看看
        void feelLucky();
    }
}
