package com.fandean.sunset;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by fan on 16-7-20.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fragment);
        setContentView(R.layout.activity_fragment); //进行了重构，P153

        FragmentManager fm = getSupportFragmentManager();
        //fragment_container是activity_crime中的FrameLayout
        //Fragment 与 Activity中的FrameLayot ？WHY？
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
//            fragment = new CrimeFragment();     //唯一不通用的地方
            fragment = createFragment();
            fm.beginTransaction()   //创建一个新的fragment事务，加入一个添加操作，然后提交该事务
                    .add(R.id.fragment_container,fragment)
                    .commit();
        }
    }
}

