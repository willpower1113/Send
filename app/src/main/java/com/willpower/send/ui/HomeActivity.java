package com.willpower.send.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.willpower.send.R;
import com.willpower.send.utils.LocationHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity{
    @BindView(R.id.mHomePager)
    ViewPager mHomePager;
    @BindView(R.id.mHomeTab)
    TabLayout mHomeTab;

    private HomeFragment homeFragment;
    private MapFragment mapFragment;
    private PersonalFragment personalFragment;

    List<Fragment> fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        LocationHelper.getInstance().init(this);
        LocationHelper.getInstance().startLocation();//开启定位
        initTabLayout();
    }

    private void initTabLayout() {
        initFragment();
        TabViewPagerAdapter pagerAdapter =
                new TabViewPagerAdapter(getSupportFragmentManager(), this, fragments);
        mHomePager.setAdapter(pagerAdapter);

        mHomeTab.setupWithViewPager(mHomePager);
        for (int i = 0; i < mHomeTab.getTabCount(); i++) {
            TabLayout.Tab tab = mHomeTab.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }
        mHomeTab.addOnTabSelectedListener(tabSelectedListener);
    }

    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    static final String[] tabs = {"首页", "地图", "我的"};

    static class TabViewPagerAdapter extends FragmentPagerAdapter {

        private Context context;

        private List<Fragment> fragmentList;

        public TabViewPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList) {
            super(fm);
            this.context = context;
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        public View getTabView(int position) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_home_tab, null);
            TextView tabContent = v.findViewById(R.id.tab_content);
            tabContent.setText(tabs[position]);
            ImageView tabIcon = v.findViewById(R.id.tab_icon);
            LinearLayout tabLayout = v.findViewById(R.id.tab_layout);
            return v;
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }


    private void initFragment() {
        fragments = new ArrayList<>();
        homeFragment = new HomeFragment();
        fragments.add(homeFragment);
        mapFragment = new MapFragment();
        fragments.add(mapFragment);
        personalFragment = new PersonalFragment();
        fragments.add(personalFragment);
    }

    @Override
    protected void onDestroy() {
        LocationHelper.getInstance().onDestroy();//释放资源
        super.onDestroy();
    }
}
