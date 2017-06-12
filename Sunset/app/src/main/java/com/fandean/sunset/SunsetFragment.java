package com.fandean.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by fan on 17-4-3.
 */

public class SunsetFragment extends android.support.v4.app.Fragment {
    private View mSceneView;
    private View mSunView;
    private View mSkyView;
    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;

    public static SunsetFragment newInstance(){
        return new SunsetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunset,container,false);
        mSceneView = view;
        mSunView = view.findViewById(R.id.sun);
        mSkyView = view.findViewById(R.id.sky);

        //获取颜色值
        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);

        mSceneView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startAnimation();
            }
        });

        return view;
    }

    /**
     * 日落动画，只需让sunView在Y轴上移动即可
     */
    private void startAnimation(){
        float sunYStart = mSunView.getTop();
        float sunYEnd = mSkyView.getHeight();

        //创建一个ObjectAnimator对象执行动画
        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView,"y",sunYStart,sunYEnd)
                .setDuration(3000);
        //添加加速效果
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        //创建另一个ObjectAnimator对象实现天空色彩变换
        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView,"backgroundColor",mBlueSkyColor,mSunsetSkyColor)
                .setDuration(3000);
        //使颜色过度更加自然
        //evaluator求值器，用于计算颜色的递增值
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView,"backgroundColor",mSunsetSkyColor,mNightSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        //使用动画集控制多个动画的执行顺序
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(heightAnimator)
                .with(sunsetSkyAnimator)
                .before(nightSkyAnimator);
       animatorSet.start();

//        heightAnimator.start();
//        sunsetSkyAnimator.start();
    }
}
