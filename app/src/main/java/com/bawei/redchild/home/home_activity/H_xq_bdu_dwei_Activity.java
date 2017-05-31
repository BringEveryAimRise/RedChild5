package com.bawei.redchild.home.home_activity;

import android.Manifest;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.bawei.redchild.MainActivity;
import com.bawei.redchild.R;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.util.List;

public class H_xq_bdu_dwei_Activity extends AppCompatActivity {
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    BaiduMap mBaiduMap;
    boolean isFirstLoc = true; // 是否首次定位

    MapView mMapView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_h_xq_bdu_dwei_);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化

//动态权限的添加两个一个是位置和获取位置
        Acp.getInstance(this).request(new AcpOptions.Builder()
                        .setPermissions(
                                Manifest.permission.ACCESS_FINE_LOCATION
                                ,Manifest.permission.ACCESS_COARSE_LOCATION)
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        mLocClient = new LocationClient(H_xq_bdu_dwei_Activity.this);
                        mLocClient.registerLocationListener(myListener);
                        LocationClientOption option = new LocationClientOption();
                        option.setOpenGps(true); // 打开gps
                        option.setCoorType("bd09ll"); // 设置坐标类型
                        option.setScanSpan(1000);

                        mLocClient.setLocOption(option);

                        mLocClient.start();
                    }
                    @Override
                    public void onDenied(List<String> permissions) {
                        Toast.makeText(H_xq_bdu_dwei_Activity.this,"没权限",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            if (location.getLocType() == BDLocation.TypeGpsLocation){//

                Toast.makeText(H_xq_bdu_dwei_Activity.this,location.getAddrStr()+location.getDirection(),Toast.LENGTH_SHORT).show();

            }else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {//通过网络连接定位
                Toast.makeText(H_xq_bdu_dwei_Activity.this,location.getAddrStr()+location.getDirection(),Toast.LENGTH_SHORT).show();

            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
}
