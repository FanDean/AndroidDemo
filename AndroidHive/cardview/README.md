# Android working with Card View and Recycler View

[Android working with Card View and Recycler View](http://www.androidhive.info/2016/05/android-working-with-card-view-and-recycler-view/)  

![图片示例](./CardView.gif)

## 使用到的知识

- CardView
- RecyclerView
- PopupMenu

在RecyclerView中响应单个CardView中的溢出菜单的点击事件；在弹出的PopupMenu中，再响应对PopupMenu的点击。


- CoordinatorLayout 在**布局文件中**较详细的做了注释
- AppBarLayout 在**布局文件中**较详细的做了注释；对其偏移量进行了监听(出现了写问题)
- CollapsingToolbarLayout 在**布局文件中**较详细的做了注释
- Toolbar

**最大的收获是**: 对CoordinatorLayout、AppBarLayout、CollapsingToolbarLayout的学习。


参考：

[ CoordinatorLayout的使用如此简单](http://blog.csdn.net/huachao1001/article/details/51554608)  

[ 玩转AppBarLayout，更酷炫的顶部栏](http://blog.csdn.net/huachao1001/article/details/51558835)  


## 补充

AppBarLayout is a vertical LinearLayout that is generally the first child inside a CoordinatorLayout and acts as a wrapper for the ToolBar in most cases. Using the ToolBar as a direct child of CoordinatorLayout would work fine but it will not be able to coordinate with other child views present. Here’s where the importance of AppBarLayout arises. It allows it’s child views to achieve the desired scrolling behavior using the param `app:layout_scrollFlags`

```
<android.support.design.widget.AppBarLayout
        android:id="@+id/appBar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:theme="@style/ThemeOverlay.AppCompat.ActionBar">

        <android.support.v7.widget.Toolbar
            android:id="@+id/toolbar"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            android:background="?attr/colorPrimary"
            app:layout_scrollFlags="scroll|enterAlways" />
    </android.support.design.widget.AppBarLayout>
```

The `app:layout_scrollFlags` attribute of the Toolbar indicates to the view how to respond. It has the following possible values.

- scroll : This flag is generally set for all views that need to scroll off-screen. Views that don’t use this flag remain static allowing the other scrollable views to slide behind it
- enterAlways : This flag ensures that any downward scroll will cause the view to become visible, enabling the quick return pattern
- enterAlwaysCollapsed : An additional flag for ‘enterAlways’ which modifies the returning view to only initially scroll back to it’s collapsed height.
- exitUntilCollapsed : This flag causes the view to be scrolled until it is collapsed (its minHeight is reached) before exiting
- snap : Upon a scroll ending, if the view is only partially visible then it will be snapped and scrolled to it’s closest edge. Hence this avoids mid-animation states of a view