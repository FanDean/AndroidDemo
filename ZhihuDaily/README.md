# 项目笔记

## 知识点

### MVP架构

MVP架构的应用。



## 布局问题

CoordinatorLayout中NestedScrollView部分可以直接用RecyclerVeiw替代，也可正常进行协调。也可以上ListView内嵌在NestedScrollView中，但不推荐，可能会出现问题。



NestedScrollView：

 It ( NestedScrollView ) can be used as both parent or child  ScrollView(可以作为ScorllView的父或子View) . 

NestedScrollView is just like ScrollView, but it supports acting as both a nested scrolling parent and child on both new and old versions of Android. Nested scrolling is enabled by default.


正如普通的scrollview一样： 
  NestedScrollView可以使它的内部view拥有滑动效果 
  NestedScrollView 只能接受一个子view

> 一般情况下，scrollview的内部或者外部无法添加另一个scrollview


```
<ScrollView
    <LinearLayout>
        <android.support.v4.widget.NestedScrollView>
            <TextView/>
        </android.support.v4.widget.NestedScrollView>
        <include layout="@layout/cards_layout"/>
    </LinearLayout>
</ScrollView>
```

[mUrl：自动生成 Markdown 格式的链接](http://jaeger.itscoder.com/chrome%20extension/2016/09/26/chrome-extension-murl.html)


### 网络连接状况检测与监听

网络连接状况检测：

需要在Manifest文件中添加相应权限：

```
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.INTERNET"/>
```


[Android：检测网络状态&监听网络变化](http://www.jianshu.com/p/983889116526)

[android 监听网络状态的变化及实际应用](http://blog.csdn.net/gdutxiaoxu/article/details/53008266)



> 网络信号简记：
>
> 4G LTE

> 3G 联通的3G为HSDPA或HSDPAP 电信的3G为EVDO 移动3G为UMTS

> 2G 移动和联通的2G为GPRS或EGDE 电信的2G为CDMA

> 判断是联通、移动还是电信的的信号？是2G、3G、4G？


### DrawerLayout NavigationView

To use a DrawerLayout, position your primary content view as
the first child（主要内容视图作为第一个child） with width and height of `match_parent` and no `layout_gravity`。
接着再添加作为抽屉的子视图，并且设置`layout_gravity`属性(表示在左边还是右边)，其高度一般设置为`match_parent`而宽度设置为
适当宽度（但宽度不应超过 320dp）

DrawerLayout布局要求，主内容视图必须是DrawerLayout的第一个子视图。

DrawerLayout还可放在其他布局里面，比如相对布局。


如果点击BACK键，需要检测当前Drawer是否打开，如果打开则触发关闭drawer的操作。



在代码中主动展开与隐藏侧边菜单。

在点击侧边菜单选项的时候我们往往需要隐藏菜单来显示整个菜单对应的内容。
DrawerLayout.closeDrawer方法用于隐藏侧边菜单，DrawerLayout.openDrawer方法用于展开侧边菜单

每次点击一个Menu关闭DrawerLayout，方法为drawer.closeDrawer(GravityCompat.START);




drawerLayout左侧菜单（或者右侧）的展开与隐藏可以被DrawerLayout.DrawerListener的实现监听到。 ↓

**添加ActionBarDrawerToggle的效果：**

ActionBarDrawerToggle 是 DrawerLayout.DrawerListener 实现。和 NavigationDrawer 搭配使用

他还能将drawerLayout的展开和隐藏与actionbar的app 图标关联起来，当展开与隐藏的时候图标有一定的平移效果，点击图标的时候还能展开或者隐藏菜单。

[ActionBarDrawerToggle的简要介绍](http://www.nowamagic.net/academy/detail/50161533)

ActionBarDrawerToggle 的作用：

- 改变android.R.id.home返回图标。
- Drawer拉出、隐藏，带有android.R.id.home动画效果。
- 监听Drawer拉出、隐藏；

点击

![ActionBarDrawerToggle](http://upload-images.jianshu.io/upload_images/912181-2689a6c8af1b82ee.jpg?imageMogr2/auto-orient/strip)

> **Note:** android.support.v4.app.ActionBarDrawerToggle is deprecated(被弃用).
>  Always use android.support.v7.app.ActionBarDrawerToggle as a replacement.


[android官方侧滑菜单DrawerLayout详解](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2014/0925/1713.html "看来看去还是这篇好")

[创建抽屉式导航栏](https://developer.android.com/training/implementing-navigation/nav-drawer.html)

[NavigationDrawer总结](http://www.jianshu.com/p/c8cbeb7ea43a)






### 添加多密度矢量图形(Vector Asset Studio工具的使用)
[添加多密度矢量图形](https://developer.android.com/studio/write/vector-asset-studio.html)

Vector Asset: 矢量资源

Vector Asset Studio 会将矢量图形作为描述图像的 XML 文件添加到项目中。

该xml文件内容看起来像这样：

```
<vector xmlns:android="http://schemas.android.com/apk/res/android"
        android:width="24dp"
        android:height="24dp"
        android:viewportWidth="24.0"
        android:viewportHeight="24.0">
    <path
        android:fillColor="#FF000000"
        android:pathData="M19.43,12.98c0.04,-  ...  -3.5,3.5z"/>
</vector>
```


### CardView

CardView继承自FrameLayout类。
CardView是一种卡片视图，主要是以卡片形式显示内容。


```
    <!-- 默认情况，CardView是不可点击的，并且没有任何的触摸反馈效果 -->
    <!-- 实现这种行为，你必须提供一下属性:android:clickable和android:foreground。-->
    <!-- Elevation海拔： 设置阴影深度。另需注意，对低版本SDK的兼容问题（低于Android L版本） -->
```

[CardView的使用](http://www.jianshu.com/p/ae9d654599ef)

### ContentLoadingProgressBar

用于**上拉加载**更多的进度显示。

ContentLoadingProgressBar不显示问题:

ContentLoadingProgressBar需要设置style 并且在XML中布局的位置必须写在content布局的下面

[ContentLoadingProgressBar 的简单实用](http://www.qiongloo.com/archives/28.html)

[ContentLoadingProgressBar不显示问题](https://www.bbsmax.com/A/GBJrMVwKz0/)


### Fragment


#### Fragment在配置变更时的数据保存

同样与 Activity 一样，假使 Activity 的进程被终止，而您需要在重建 Activity 时恢复片段状态，
您也可以使用 Bundle 保留片段的状态。您可以在片段的 onSaveInstanceState() 回调期间保存状态，
并可在 onCreate()、onCreateView() 或 onActivityCreated() 期间恢复状态。
如需了解有关保存状态的详细信息，请参阅 Activity 文档。


见[Best tutorials for maintaining state of fragment on orientation change, restore from backstack, etc.?](https://www.reddit.com/r/androiddev/comments/3anajx/best_tutorials_for_maintaining_state_of_fragment/)
上的讨论（如何初始化...）。


**Fragment状态的持久化：**

方法一：onSaveInstanceState、onRestoreInstanceState

略

方法二：将Fragment在Activity中作为一个变量整个保存

`FragmentManager.putFragment(Bundle bundle, String key, Fragment fragment)` 是在Activity中保存Fragment的方法。
`FragmentManager.getFragment(Bundle bundle, String key)` 是在Activity中获取所保存的Frament的方法


> FragmentManager是通过Bundle去保存Fragment的。这个方法仅仅能够保存Fragment中的控件状态，
> 比如说EditText中用户已经输入的文字（注意！在这里，控件需要设置一个id，
> 否则Android将不会为我们保存控件的状态），而Fragment中需要持久化的变量依然会丢失，
> 但依然有解决办法，就是利用方法一。  (这里仅作为了解，《Android权威编程指南》中也有介绍)



本项目使用的如下代码可能并不适合保存与恢复Fragment的数据，并不适合于避免Fragment重叠问题：

```
getSupportFragmentManager().putFragment(outState,"MainFragment",mMainFragment);
```

> 采用了谷歌官方mvp的架构，发现应用在后台被回收后，打开应用界面空白，presenter为空，明明是已经走了MainActivity的onCreate等生命周期，没找到解决方案。
>   然后看到你的app，发现如果后台被回收打开应用程序就直接崩溃了，所以问问有没有解决方案或者好的思路= =，不太想使用onRestoreInstanceState之类的恢复数据，
>   希望被回收后再打开应用是重新打开

> 会不会是因为MVP架构的问题，才采用这个方法？？


#### Fragment在配置变更时避免重叠

 一个非常好的方法见《Android权威编程指南》，就是在Activity中添加Fragment时，
先看能否从现有队列中找到该Fragment，再考虑是否需要重新进行add。

```
//但如果多个Fragment都是使用同一个占位容器？？就看你的需求了，
Fragment fragment = fm.findFragmentById(R.id.XXX);
if (fragment == null){// 新建Fragment，并进行add }
```

而大神鸿祥的[方法](http://blog.csdn.net/lmj623565791/article/details/37992017)是：
通过检查onCreate的参数Bundle savedInstanceState就可以判断，当前是否发生Activity的重新创建；
只有在savedInstanceState==null时，才进行创建Fragment实例。


### Gradle加载本地jar包

com.jakewharton:butterknife 无法下载。


这种Gradle加载本地jar包的方法,

1.先讲所需要的jar包到本地,

2.在项目底下添加lib目录,将jar包仍进lib目录

3.build.gradle配置如下:
```
dependencies { compile files('lib/ojdbc-14.jar')}
```

        //为了下载Android官方架构组件，添加如下maven仓库
        maven { url 'https://maven.google.com' }

[gradle使用maven镜像仓库的方法](https://my.oschina.net/abcfy2/blog/783743)

```
    repositories {
        mavenLocal()
        maven { url "http://maven.aliyun.com/nexus/content/groups/public/" }
        jcenter()
    }
```

## RecycleView上拉加载
[RecyclerView:下拉刷新和上拉加载更多](http://www.jianshu.com/p/acbc1017f14e)    
[手把手教你实现Android RecyclerView上拉加载功能](https://my.oschina.net/ryaneLee/blog/879137)  





## ViewPager动态加载数据





## 缓存机制  

[Android缓存策略——打造最全的缓存](http://blog.csdn.net/hao54216/article/details/52413975)  
[Android学习系列(27)--App缓存管理](http://www.cnblogs.com/qianxudetianxia/archive/2012/02/20/2112128.html)  
[Android缓存机制&一个缓存框架推荐](http://blog.csdn.net/shakespeare001/article/details/51695358 "推荐")    
[Volley+LruCache+DiskLruCache的一个示例](https://github.com/saymagic/Haddock)    

这里的缓存管理的原理很简单：通过时间的设置来判断是否读取缓存还是重新下载。

两种比较常见的缓存管理方法是:数据库法和文件法。

- 数据库法：实现起来比较复杂。这种方法是在下载完数据文件后，把文件的相关信息如url，路经，下载时间，过期时间等存放到数据库，下次下载的时候根据url先从数据库中查询，如果查询到当前时间并未过期，就根据路径读取本地文件，从而实现缓存的效果。
- 文件法：使用File.lastModified()方法得到文件的最后修改时间，与当前时间判断是否过期，从而实现缓存效果。


众多的分类：  

分类一：  文字缓存和多媒体文件缓存   
分类二：  数据库法和文件法          

Android缓存分为内存缓存和文件缓存（磁盘缓存）;（那么上面两种就是属于磁盘缓存了）     

Android推出了LruCache这个内存缓存类
磁盘缓存（文件缓存）——DiskLruCache分析


> LRU，全称Least Rencetly Used，即最近最少使用，是一种非常常用的置换算法，也即淘汰最长时间未使用的对象。   
> Android 3.0（Level 11）之后，图片数据Bitmap被放置到了内存的堆区域，而堆区域的内存是由GC管理的，开发者也就不需要进行图片资源的释放工作，但这也使得图片数据的释放无法预知，增加了造成OOM的可能。因此，在Android3.1以后，Android推出了LruCache这个内存缓存类。


### LruCache内存缓存
这个类非常适合用来缓存图片，
它的主要算法原理是把最近使用的对象用强引用存储在 LinkedHashMap 中，并且把最近最少
使用的对象在缓存值达到预设定值之前从内存中移除。   

LruCache是一种内存缓存策略，但是当存在大量图片的时候，我们指定的缓存内存空间可能很快就会用完，这个时候，LruCache就会频繁的进行trimToSize()操作，不断的将最近最少使用的数据移除，当再次需要该数据时，又得从网络上重新加载。   
LruCache 自身并没有释放内存，只是 LinkedHashMap中将数据移除了，如果数据还在别的地方被引用了，还是有泄漏问题，还需要手动释放内存。   


### 磁盘缓存的目录
[缓存目录](http://www.jianshu.com/p/73f960bcdb59)  


手机自带的存储空间:  
`getCacheDir() ----- /data/user/0/xx.xxx.xx（当前包）/cache`

外部SD卡上:  
`context.getExternalCacheDir()   ---/storage/emulated/Android/data/应用包名/files`     
在使用这个方法的时候要判断外部SD卡的状态是否可用。

相同点：app卸载后，两个目录下的数据都会被清空。    
不同点：前者的路径需要root以后，用Root Explorer 文件管理器才能看到。后者的路径在手机里可以直接看到。   


### 图片缓存
我们编写的应用程序都是有一定内存限制的，程序占用了过高的内存就容易出现OOM(OutOfMemory)异常。   
压缩后的图片大小应该和用来展示它的控件大小相近，在一个很小的ImageView上显示一张超大的图片不会带来任何视觉上的好处，但却会占用我们相当多宝贵的内存，而且在性能上还可能会带来负面影响。适当压缩图片，以最佳大小显示的同时，还能防止OOM的出现。

[Android高效加载大图、多图解决方案，有效避免程序OOM](http://blog.csdn.net/guolin_blog/article/details/9316683)  

### Glide进行图片缓存
[Android图片加载框架最全解析（三），深入探究Glide的缓存机制](http://blog.csdn.net/guolin_blog/article/details/54895665)    

在缓存这一功能上，Glide又将它分成了两个模块，一个是内存缓存，一个是硬盘缓存。

默认情况下，Glide自动就是开启内存缓存的。也就是说，当我们使用Glide加载了一张图片之后，这张图片就会被缓存到内存当中，只要在它还没从内存中被清除之前，下次使用Glide再加载这张图片都会直接从内存当中读取，而不用重新从网络或硬盘上读取了，这样无疑就可以大幅度提升图片的加载效率。比方说你在一个RecyclerView当中反复上下滑动，RecyclerView中只要是Glide加载过的图片都可以直接从内存当中迅速读取并展示出来，从而大大提升了用户体验。

那么既然已经默认开启了这个功能，还有什么可讲的用法呢？只有一点，如果你有什么特殊的原因需要禁用内存缓存功能，Glide对此提供了接口：
```
Glide.with(this)
     .load(url)
     .skipMemoryCache(true)
     .into(imageView);
```
Glide内存缓存的实现自然也是使用的LruCache算法。不过除了LruCache算法之外，Glide还结合了一种弱引用的机制，共同完成了内存缓存功能。

Glide硬盘缓存：  

调用diskCacheStrategy()方法并传入DiskCacheStrategy.NONE，就可以禁用掉Glide的硬盘缓存功能了  
```
Glide.with(this)
     .load(url)
     .diskCacheStrategy(DiskCacheStrategy.NONE)
     .into(imageView);
```

这个diskCacheStrategy()方法基本上就是Glide硬盘缓存功能的一切，它可以接收四种参数：

- DiskCacheStrategy.NONE： 表示不缓存任何内容。
- DiskCacheStrategy.SOURCE： 表示只缓存原始图片。
- DiskCacheStrategy.RESULT： 表示只缓存转换过后的图片（默认选项）。
- DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。

当我们使用Glide去加载一张图片的时候，Glide默认并不会将原始图片展示出来，而是会对图片进行压缩和转换（我们会在后面学习这方面的内容）。总之就是经过种种一系列操作之后得到的图片，就叫转换过后的图片。而Glide默认情况下在硬盘缓存的就是转换过后的图片，我们通过调用diskCacheStrategy()方法则可以改变这一默认行为。

Glide是在哪里读取硬盘缓存的：  
分为两种情况：   
- decodeFromCache()方法从硬盘缓存当中读取图片。（只有缓存中不存在要读取的图片时，才会去读取原始图片）  
- getDiskCache()方法：得到硬盘缓存的文件。（Glide默认）  


原始图片的缓存写入：   


转换过后的图片缓存是怎么写入的：   
调用transform()方法来对图片进行转换，然后在writeTransformedToCache()方法中将转换过后的图片写入到硬盘缓存中。





### Retrofit 源码解读之离线缓存策略的实现

网络框架，也是至今Android网络请求中最火的一个，

Retrofit 2.0开始，底层的网络连接全都依赖于OkHttp,故要设置缓存，必须从OkHttp下手。


先要开启OkHttp缓存



### 刷新

自动刷新时间间隔、下拉刷新、（推送）


> 他们说的三种缓存: 
> 内存缓存(从内存中获取图片显示)、本地缓存(内存中没有从sd卡获取)、网络缓存(从网络下载并保存入本地和内存)；但是哪个是一级缓存、二级缓存、三级缓存，是内存、磁盘、网络吗？


