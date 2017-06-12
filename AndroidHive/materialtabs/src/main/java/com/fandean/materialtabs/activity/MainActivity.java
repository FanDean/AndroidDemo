package com.fandean.materialtabs.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.fandean.materialtabs.R;
import com.fandean.materialtabs.fragment.OneFragment;
import com.fandean.materialtabs.fragment.ThreeFragment;
import com.fandean.materialtabs.fragment.TwoFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    //标签页的图标
    private int[] tabIcons = {
            android.R.drawable.ic_menu_call,
            android.R.drawable.ic_menu_gallery,
            android.R.drawable.ic_menu_search,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        //设置图标
        setupTabIcons();
        //设置自定义的View时使用(图片在下)
//        setupCustemTabIcons();
        //设置自定义的View时使用(图片在右)
//        setupCustomRightTabIcons();
//        setupCustomTopTabIcons();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //使用固定标签页，只需添加下面三个Fragment
        adapter.addFragment(new OneFragment(), "ONE");
        adapter.addFragment(new TwoFragment(), "TWO");
        adapter.addFragment(new ThreeFragment(), "THREE");

        //使用滑动标签页时，另添加下面的Fragment

/*
        adapter.addFragment(new FourFragment(), "FOUR");
        adapter.addFragment(new FiveFragment(), "FIVE");
        adapter.addFragment(new SixFragment(), "SIX");
        adapter.addFragment(new SevenFragment(), "SEVEN");
        adapter.addFragment(new EightFragment(), "EIGHT");
        adapter.addFragment(new NineFragment(), "NINE");
        adapter.addFragment(new TenFragment(), "TEN");
*/

        mViewPager.setAdapter(adapter);
    }

    //使用固定tabs，并添加图标
    private void setupTabIcons() {
        mTabLayout.getTabAt(0).setIcon(tabIcons[0]);
        mTabLayout.getTabAt(1).setIcon(tabIcons[1]);
        mTabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    //使用固定tabs，添加自定义View (图片在下)
    private void setupCustemTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.
                from(this).inflate(R.layout.custom_tab,null);
        //在此手动设置标签文字，而在下面的适配器中仍然返回标题？？
        // 是下面的setCustomView()让适配器中的设置无效？？
        tabOne.setText("ONE");
        // 设置图标相对于文字的方向，四个参数分别代表左、上、右、下，
        // 这里的int值代表资源id，0代表该方位无图标。该方法属于TextView
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0,0,tabIcons[0]);
        //通过setCustomView()方法设置
        mTabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.
                from(this).inflate(R.layout.custom_tab,null);
        tabTwo.setText("Two");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, 0,0,tabIcons[1]);
        mTabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.
                from(this).inflate(R.layout.custom_tab,null);
        tabThree.setText("Three");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, 0,0,tabIcons[2]);
        mTabLayout.getTabAt(2).setCustomView(tabThree);
    }

    //使用固定tabs，添加自定义View (图片在右)
    private void setupCustomRightTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("ONE");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, 0, tabIcons[0], 0);
        mTabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("TWO");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, 0, tabIcons[1], 0);
        mTabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("THREE");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, 0, tabIcons[2], 0);
        mTabLayout.getTabAt(2).setCustomView(tabThree);
    }

    private void setupCustomTopTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("ONE");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_favourite, 0, 0);
        mTabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("TWO");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_call, 0, 0);
        mTabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("THREE");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_contacts, 0, 0);
        mTabLayout.getTabAt(2).setCustomView(tabThree);
    }



    class ViewPagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //如果为Tabs设置了图标，这里直接返回null，则只显示图标
            return mFragmentTitleList.get(position);
        }
    }
}
