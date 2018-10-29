package com.willpower.send.utils;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.willpower.send.GaoDe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2018/10/29.
 */

public class LocationHelper implements GaoDe{

    private static LocationHelper helper;

    private AMapLocationClient locationClient;

    private List<GlobalLocationListener> listeners;

    public static long INTERVAL = 5 * 1000;//定位间隔

    private Context context;

    private LocationHelper() {
    }

    public static LocationHelper getInstance() {
        if (helper == null) {
            helper = new LocationHelper();
        }
        return helper;
    }

    //缓存Context
    public void init(Context context) {
        this.context = context.getApplicationContext();
        this.listeners = new ArrayList<>();
    }

    /*
  定位回调
   */
    AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            Log.e(TAG, getLocationStr(aMapLocation));
            for (GlobalLocationListener listener :
                    listeners) {
                if (listener != null) {
                    listener.onLocation(aMapLocation);
                }
            }
        }
    };

    private void initLocation() {
        Log.e(TAG, "initLocation: ");
        locationClient = new AMapLocationClient(context);
        AMapLocationClientOption locationClientOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);
        //设置定位间隔,单位毫秒,默认为2000ms
        locationClientOption.setInterval(INTERVAL);
        // 设置是否需要显示地址信息
        locationClientOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationClientOption.setGpsFirst(true);
        //设置定位参数
        locationClient.setLocationOption(locationClientOption);
        //设置定位监听
        locationClient.setLocationListener(aMapLocationListener);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
    }

    public void startLocation() {
        if (locationClient == null) {
            initLocation();
        }
        //启动定位
        Log.e(TAG, "startLocation: ");
        locationClient.startLocation();
    }

    public void stopLocation() {
        if (locationClient != null) {
            locationClient.stopLocation();
        }
    }

    public void onDestroy() {
        if (locationClient != null) {
            if (locationClient.isStarted()) {
                stopLocation();
            }
            locationClient.onDestroy();
            locationClient = null;
        }
        if (listeners != null) {
            listeners.clear();
            listeners = null;
        }
        this.context = null;
    }

    /**
     * 根据定位结果返回定位信息的字符串
     *
     * @param location
     * @return
     */
    private static synchronized String getLocationStr(AMapLocation location) {
        if (null == location) {
            return "AMapLocation == null";
        }
        StringBuffer sb = new StringBuffer();
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if (location.getErrorCode() == 0) {
            sb.append("定位成功" + "\n");
            sb.append("定位类型: " + location.getLocationType() + "\n");
            sb.append("经    度    : " + location.getLongitude() + "\n");
            sb.append("纬    度    : " + location.getLatitude() + "\n");
            sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
            sb.append("提供者    : " + location.getProvider() + "\n");

            sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
            sb.append("角    度    : " + location.getBearing() + "\n");
            // 获取当前提供定位服务的卫星个数
            sb.append("星    数    : " + location.getSatellites() + "\n");
            sb.append("国    家    : " + location.getCountry() + "\n");
            sb.append("省            : " + location.getProvince() + "\n");
            sb.append("市            : " + location.getCity() + "\n");
            sb.append("城市编码 : " + location.getCityCode() + "\n");
            sb.append("区            : " + location.getDistrict() + "\n");
            sb.append("区域 码   : " + location.getAdCode() + "\n");
            sb.append("地    址    : " + location.getAddress() + "\n");
            sb.append("兴趣点    : " + location.getPoiName() + "\n");
            //定位完成的时间
            sb.append("定位时间: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Locale.CHINA) + "\n");
        } else {
            //定位失败
            sb.append("定位失败" + "\n");
            sb.append("错误码:" + location.getErrorCode() + "\n");
            sb.append("错误信息:" + location.getErrorInfo() + "\n");
            sb.append("错误描述:" + location.getLocationDetail() + "\n");
        }
        //定位之后的回调时间
        sb.append("回调时间: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Locale.CHINA) + "\n");
        return sb.toString();
    }


    public void addListener(GlobalLocationListener listener) {
        if (listeners != null) {
            listeners.add(listener);
        }
    }

    public void removeListener(GlobalLocationListener listener) {
        if (listeners != null && listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public interface GlobalLocationListener {
        void onLocation(AMapLocation location);
    }

}
