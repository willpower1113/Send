package com.willpower.send.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.willpower.send.R;
import com.willpower.send.utils.LocationHelper;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button homeButton, mapButton;

    private HomeFragment homeFragment;
    private MapFragment mapFragment;

    List<Fragment> fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LocationHelper.getInstance().init(this);
        LocationHelper.getInstance().startLocation();//开启定位
        homeButton = findViewById(R.id.homeButton);
        mapButton = findViewById(R.id.mapButton);
        initFragment();
        homeButton.setOnClickListener(this);
        mapButton.setOnClickListener(this);
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        homeFragment = new HomeFragment();
        fragments.add(homeFragment);
        mapFragment = new MapFragment();
        fragments.add(mapFragment);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.contentPanel, homeFragment);
        transaction.add(R.id.contentPanel, mapFragment);
        transaction.commit();
        showHideFragment(0);
    }

    private void showHideFragment(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (position == 0) {
            transaction.show(homeFragment);
            transaction.hide(mapFragment);

            homeButton.setTextColor(Color.BLUE);
            mapButton.setTextColor(Color.BLACK);
        } else {
            transaction.show(mapFragment);
            transaction.hide(homeFragment);

            homeButton.setTextColor(Color.BLACK);
            mapButton.setTextColor(Color.BLUE);
        }
        transaction.commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeButton:
                showHideFragment(0);
                break;
            case R.id.mapButton:
                showHideFragment(1);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        LocationHelper.getInstance().onDestroy();//释放资源
        super.onDestroy();
    }
}
