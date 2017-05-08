package com.fandean.webview;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;

/**
 * Created by fan on 17-5-7.
 */

public class Utils {
    public static boolean isSameDomain(String url, String url1){
        //判断两者的根域名是否相等
        boolean isSame = getRootDomainUrl(url.toLowerCase()).equals(getRootDomainUrl(url1.toLowerCase()));
        return isSame;
    }

    /**
     * 获取url的根域名。
     * 几种URL地址：
     * 1.http://www.androidhive.info/2016/12/android-working-with-webview-building-a-simple-in-app-browser/
     * 2.https://ifttt.com/
     * 3.https://translate.google.cn/
     * 4.http://www.xxx.com.cn
     * 5.http://xxx.com.cn（没有这种url吗？）
     * @param url
     * @return
     */
    private static String getRootDomainUrl(String url){
        String[] domainKeys = url.split("/")[2].split("\\.");
        int length = domainKeys.length;
        int dummy = domainKeys[0].equals("www")?1:0;
        if (length - dummy == 2){//解决了上面1、2两种url
            return domainKeys[length-2] + "." + domainKeys[length-1];
        }else {
            if (domainKeys[length -1].length() ==2){
                return domainKeys[length-3] + "." + domainKeys[length-2]+"."+domainKeys[length-1];
            }else {
                return domainKeys[length-2]+"."+domainKeys[length-1];
            }
        }
    }

    //tint：保持
    public static void tintMenuIcon(Context context, MenuItem item, int color){
        Drawable drawable = item.getIcon();
        if (drawable != null){
            //If we don't mutate the drawable, then all drawable's with this id will have a color
            //filter applied to it

//                TODO 查看笔记：
//                调用mutate()，使当前Drawable实例mutable，这个操作不可逆。
//                一个mutable的Drawable实例不会和其他Drawable实例共享它的状态（默认是共享的）。
//                当你需要修改一个从资源文件加载的Drawable实例时，mutate()方法尤其有用。
//                默认情况下，所有加载同一资源文件生成的Drawable实例都共享一个通用的状态，
//                如果你修改了其中一个Drawable实例，所有的相关Drawable实例都会发生同样的变化。
            drawable.mutate();
            //Porter-Duff模式：
            // Porter-Duff 操作是 1 组 12 项用于描述数字图像合成的基本手法。
            // 用人话讲就是：2张图要放在一张画板上，这两张图需要融合在一起，融合的方法有12个。
            // 见：http://www.jianshu.com/p/b974a2c88cd9
            // 这里是 使用单个颜色和指定Porter-Duff模式来对源像素进行染色
            drawable.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public static void bookmarkUrl(Context context, String url){
        SharedPreferences preferences = context.getSharedPreferences("androidhive_webview",0); // 0 - for private mode
        SharedPreferences.Editor editor = preferences.edit();

        //如果此url已经保存在书签中，则取消该书签
        if (preferences.getBoolean(url,false)){
            //为什么不是直接将其删除，而是将值更改为false
            editor.putBoolean(url,false);
        } else {
            editor.putBoolean(url,true);
        }
        editor.commit();
//        editor.apply();
    }

    public static boolean isBookmarked(Context context, String url){
        SharedPreferences preferences = context.getSharedPreferences("androidhive_webview",0);
        return preferences.getBoolean(url,false);
    }
}
