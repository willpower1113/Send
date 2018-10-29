package com.willpower.send.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.willpower.send.R;
import com.willpower.send.utils.LocationHelper;
import com.willpower.send.widget.HeaderBean;
import com.willpower.send.widget.ListMenuView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/10/25.
 */

public class HomeFragment extends Fragment implements LocationHelper.GlobalLocationListener {
    protected View view;

    private ListMenuView dropDownMenu;
    private TextView tvCity;

    private String citys[] = {"不限", "武汉", "北京", "上海", "成都", "广州", "深圳", "重庆", "天津", "西安", "南京", "杭州"};
    private String ages[] = {"不限", "18岁以下", "18-22岁", "23-26岁", "27-35岁", "35岁以上"};
    private String sexs[] = {"不限", "男", "女"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        dropDownMenu = view.findViewById(R.id.mMenuView);
        tvCity = view.findViewById(R.id.tv_city);
        dropDownMenu.initMenu(getData());
        LocationHelper.getInstance().addListener(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        LocationHelper.getInstance().removeListener(this);
        super.onDestroyView();
    }

    private List<HeaderBean> getData() {
        List<HeaderBean> data = new ArrayList<>();
        HeaderBean brand = new HeaderBean();
        brand.setTitle("品牌");
        brand.setmData(Arrays.asList(citys));

        HeaderBean size = new HeaderBean();
        size.setTitle("型号");
        size.setmData(Arrays.asList(ages));

        HeaderBean away = new HeaderBean();
        away.setTitle("距离");
        away.setmData(Arrays.asList(sexs));
        data.add(brand);
        data.add(size);
        data.add(away);
        return data;
    }

    @Override
    public void onLocation(AMapLocation location) {
        if (tvCity!=null){
            tvCity.setText(location.getCity());
        }
    }
}
