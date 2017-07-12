package com.fandean.zhihudaily.homepage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.fandean.zhihudaily.R;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private MainFragment mMainFragment;
    private BookmarksFragment mBookmarksFragment;

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLyout;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        //《Android编程权威指南》中的方法
        /*
        FragmentManager fm = getSupportFragmentManager();
        if(fm.findFragmentById(R.id.layout_fragment) == null){};
        */


        //恢复fragment的状态
        if (savedInstanceState != null){
            mMainFragment = (MainFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState,"MainFragment");
            mBookmarksFragment = (BookmarksFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState,"BookmarksFragment");
        } else {
            mMainFragment = MainFragment.newInstance();
            mBookmarksFragment = BookmarksFragment.newInstance();
        }

        if (!mMainFragment.isAdded()){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_fragment, mMainFragment, "MainFragment")
                    .commit();
        }

        if (!mBookmarksFragment.isAdded()){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.layout_fragment, mBookmarksFragment, "BookmarksFragment")
                    .commit();
        }


        //实例化BookmarksPresenter
        new BookmarkPresenter(MainActivity.this, mBookmarksFragment);

        //默认显示首页内容
        showMainFragment();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLyout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,mDrawerLyout,mToolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        mDrawerLyout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
    }


    //显示MainFragment并设置Title
    private void showMainFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(mMainFragment);
        fragmentTransaction.hide(mMainFragment);
        fragmentTransaction.commit();

        mToolbar.setTitle(getResources().getString(R.string.app_name));
    }

    //显示MainFragment并设置Title
    private void showBookmarksFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(mBookmarksFragment);
        fragmentTransaction.hide(mBookmarksFragment);
        fragmentTransaction.commit();

        mToolbar.setTitle(getResources().getString(R.string.nav_bookmarks));
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMainFragment.isAdded()){
            getSupportFragmentManager().putFragment(outState,"MainFragment",mMainFragment);
        }
        if (mBookmarksFragment.isAdded()){
            getSupportFragmentManager().putFragment(outState,"BookmarksFragment",mBookmarksFragment);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //每次点击一个Menu关闭DrawerLayout
        mDrawerLyout.closeDrawer(GravityCompat.START);

        int id = item.getItemId();
        if (id == R.id.nav_home){
            showMainFragment();
        } else if (id == R.id.nav_favorites){
            showBookmarksFragment();
        } else if (id == R.id.nav_change_theme){
            //TODO MenuItem点击，更改主题
        } else if (id == R.id.nav_info){
            //TODO 设置页面
        } else if (id == R.id.nav_about){
            //TODO 关于页面
        }

        return true;
//        return false;
    }
}
