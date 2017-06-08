# Android滑动刷新ListView教程

[Android滑动刷新ListView教程](http://www.androidhive.info/2015/05/android-swipe-down-to-refresh-listview-tutorial/)

效果：
![](http://www.androidhive.info/wp-content/uploads/2015/05/android-swipe-down-to-refresh-listview-tutorial.png)



利用SwipeRefreshLayout来检测任何视图中的垂直滑动。每次下拉刷新就获取下一组（每组20部）电影并进行显示。

IMDB中评分前250的电影。

将要检测向下滑动的View作为SwipeRefreshLayout的子View即可。使用SwipeRefreshLayout.OnRefreshListener进行监听。


## 知识点

- Application
- ListView
- getSystemService()
- SwipeRefreshLayout



### 自定义Application类
[Android基础之自定义Application](http://www.jianshu.com/p/98324e5d67ae#)

[全局对象Application的使用，以及如何在任何地方得到Application全局对象](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2013/0924/1558.html)

[(译)Android Application启动流程分析](http://www.jianshu.com/p/a5532ecc8377#)


Android进程与Linux进程一样. 默认情况下, 每个apk运行在自己的Linux进程中. 另外,
默认一个进程里面只有一个线程---主线程. 这个主线程中有一个Looper实例,
通过调用Looper.loop()从Message队列里面取出Message来做相应的处理.

![APP启动流程](http://upload-images.jianshu.io/upload_images/851999-a9c2c456c9f91596.jpg)




### getSystemService()

getSystemService()是Android很重要的一个API，它是Activity的一个方法，根据传入的NAME来取得对应的Object，然后转换成相应的服务对象。


[Android操作系统服务（Context.getSystemService()](http://www.cnblogs.com/gaopeng527/p/4595901.html)


### ListView

适配器中的getView()。

还可进一步优化，使用ViewHolder对控件实例进行缓存，
并使用View的setTag()方法保存该ViewHolder，详见《第一行代码-Android》



### SwipeRefreshLayout

- 可包裹在可滚动的View上面
- 可自动识别垂直滚动手势
- 只能有一个直接子View


用法：只要在需要刷新的控件最外层加上SwipeRefreshLayout，然后他的child首先是可滚动的view，
如ScrollView或者ListView，RecyclerView

实现SwipeRefreshLayout.OnRefreshListener监听器，并覆盖onRefresh（）处理器。


This layout should be made the parent of the view that will be refreshed as a result of
the gesture and can only support one direct child（一个直接子View）. This view will also be made the target
of the gesture and will be forced to match both the width and the height supplied in this layout.
The SwipeRefreshLayout does not provide accessibility events; instead, a menu item must be
provided to allow refresh of the content wherever this gesture is used.


SwipeRefreshLayout 内部有 2 个 View，一个圆圈（mCircleView），一个内部可滚动的View（mTarget）。

参考： [SwipeRefreshLayout](https://github.com/hanks-zyh/SwipeRefreshLayout)

[Adding Swipe-to-Refresh To Your App](https://developer.android.com/training/swipe/add-swipe-interface.html#AddRefreshAction)


**viwe.post()方法**

```
// 省略若干代码
@Override
protecte onCreate(){
        //SwipeRefreshLayout继承至View，post()方法也是来至View
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                fetchMovies();
            }
        });
}
```



参考：
[不可不知的开发技巧之View.Post()](http://www.jianshu.com/p/b1d5e31e2011#)




## 在colors.xml文件中创建颜色相关的string-array

```xml
    <string-array name="movie_serial_bg">
        <item>#24c6d5</item>
        <item>#57dd86</item>
        <item>#ad7dcf</item>
        <item>#ff484d</item>
        <item>#fcba59</item>
        <item>#24c6d5</item>
    </string-array>
```


## 遇到的一个编译错误

```
java.lang.NullPointerException: Attempt to invoke virtual method
'void com.fandean.swiperefresh.app.MyApplication.addToRequestQueue(com.android.volley.Request)' on a null object reference
```
错误原因是没有把app.MyApplication指定到AndroidManifest.xml文件中。