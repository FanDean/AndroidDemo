# Android Material Design working with Tabs

[Android Material Design working with Tabs](http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/)


自定义前的效果： ![](http://www.androidhive.info/wp-content/uploads/2015/09/android-tab-layout-with-icon-and-text.png)

## Tabs
[何为Tabs](https://material.io/guidelines/components/tabs.html)

## TabLayout单独使用

TabLayout.Tab代表了Tab实例；你可以通过`newTab()`方法创建Tab，通过`setText(int)`更改tab的标题，
通过`setIcon(int)`更改tab的图标；然后通过`addTab(Tab)`进行显示。

```
TabLayout tabLayout = ...;
tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
```

单独使用时，还需设置各种监听，比如 `setOnTabSelectedListener(OnTabSelectedListener)`。

另还可在布局文件中使用`TabItem`代表Tab：

```
 <android.support.design.widget.TabLayout
         android:layout_height="wrap_content"
         android:layout_width="match_parent">

     <android.support.design.widget.TabItem
             android:text="@string/tab_text"/>

     <android.support.design.widget.TabItem
             android:icon="@drawable/ic_android"/>

 </android.support.design.widget.TabLayout>
```



当标签数量较少时，一般固定(fixed)标签位置。

TabLayout两个重要的xml属性：

- app:tabMode – This takes two values:
  - fixed – This displays all the tabs within the screen. （固定，显示所有）通常最多3个标签页。
  - scrolling – This lets the user scrolls through the tabs horizontally.（可滚动，显示部分）
- app:tabGravity – This attribute only works if app:tapMode="fixed"（固定时才可用）. This also takes two values:
  - filled – It’ll distribute the horizontal width across all the tabs.（填满）
  - center – It’ll display all the tabs in the center horizontal of the screen.（居中）


## TabLayout与ViewPager的整合

使用 TabLayout + ViewPager + Fragment 的形式可以大大简化代码。


**ViewPager切换Fragment(支持库版本)：**  

要想管理支持库版本的Fragment可以使用FragmentActivity来管理，而AppCompatActivity是继承了ragmentActivity的。

Activity 为ViewPager的FragmentPagerAdapter（为Fragment做了优化的Adapter）提供FragmentManager（fm）；适配器通过fm创建的事务切换Fragment，并在适配器的getItem()方法中返回当前显示的Fragment。


**TabLayout 与 ViewPager关联:** 

> 具体原理未知

调用TabLayout的`setupWithViewPager(ViewPager)`方法来连接到ViewPager；此时，
PagerAdapter将自动填充它的页面标题到TabLayout。而其标题可通过下面的方法设定：

```
        //PagerAdapter中的方法，返回position位置处页面的标题
        public CharSequence getPageTitle(int position) {
            //如果为Tabs设置了图标，这里直接返回null，则只显示图标
            return mFragmentTitleList.get(position);
        }
```


setupWithViewPager必须在ViewPager.setAdapter()之后调用


还可直接在布局文件中将两者整合在一起：
```
 <android.support.v4.view.ViewPager
     android:layout_width="match_parent"
     android:layout_height="match_parent">

     <android.support.design.widget.TabLayout
         android:layout_width="match_parent"
         android:layout_height="wrap_content"
         android:layout_gravity="top" />

 </android.support.v4.view.ViewPager>
```


## TabLayout使用自定义的View

为TextView指定fontFamily：先在values目录下新建fonts.xml文件，在该文件中
添加具体的字体。然后为TextView指定`fontFamily`属性。

TextView可以通过`setCompoundDrawablesWithIntrinsicBounds();`设置文字上下左右四个方向上的图片。

最后通过如下方法设置自定义的View：  `mTabLayout.getTabAt(0).setCustomView(tabOne);`

详细注释见代码。

这样自定义的View显示效果不怎么好，另一种自定义的View见参考5、6。

相对更完整的示例见参考3.

---

参考：

1. [TabLayout官方文档](https://developer.android.com/reference/android/support/design/widget/TabLayout.html)
2. [Android TabLayout in the AppBarLayout](http://www.journaldev.com/12858/android-tablayout-appbarlayout)
3. [Android TabLayout and ViewPager](http://www.journaldev.com/12958/android-tablayout-viewpager)
4. [Google Play Style Tabs using TabLayout](https://guides.codepath.com/android/google-play-style-tabs-using-tablayout)
5. [android design library提供的TabLayout的用法](https://segmentfault.com/a/1190000003500271)
6. [TabLayout使用详解](http://www.jianshu.com/p/7f79b08f5afa)




