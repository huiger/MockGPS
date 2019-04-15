package top.huiger.mockgps;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.huige.library.interfaces.OnItemClickListener;
import com.huige.library.utils.DeviceUtils;
import com.huige.library.utils.KeyboardUtils;
import com.huige.library.utils.ToastUtils;
import com.yanzhenjie.permission.Action;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import top.huiger.mockgps.adapter.AddressAdapter;
import top.huiger.mockgps.entity.LocationEntity;
import top.huiger.mockgps.gaodeLBS.GeoCoderUtil;
import top.huiger.mockgps.services.MockGpsService;
import top.huiger.mockgps.utils.MapUtils;
import top.huiger.mockgps.utils.PermissionHelper;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019-4-3 15:09:33
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 */
public class MainActivity extends AppCompatActivity implements LocationSource, AMapLocationListener {

    @Bind(R.id.mapView)
    MapView mapView;
    @Bind(R.id.map_search_address_et)
    EditText mapSearchAddressEt;
    @Bind(R.id.btn_search)
    Button btnSearch;
    @Bind(R.id.iv_menu_2)
    ImageView ivMenu2;
    @Bind(R.id.iv_menu_3)
    ImageView ivMenu3;
    @Bind(R.id.iv_menu_4)
    ImageView ivMenu4;
    @Bind(R.id.iv_menu_5)
    ImageView ivMenu5;
    @Bind(R.id.iv_menu_1)
    ImageView ivMenu1;


    OnLocationChangedListener mListener;
    private AMap mAMap;
    private AMapLocationClient mlocationClient;
    //扇形菜单按钮
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    //菜单是否展开的flag,false表示没展开
    private boolean mFlag = false;
    /**
     * 标记位置坐标
     */
    private LocationEntity markerLonLatInfo;
    /**
     * 首次定位位置
     */
    private double firstLocationLat, firstLocationLng;
    private Intent mMockLocServiceIntent;
    private boolean isMockServerStart;
    /**
     * 手动标记的marker
     */
    private Marker marker;
    /**
     * 定位小蓝点
     */
    private Marker locationMarker;
    private boolean isLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);

        PermissionHelper.getPermission(this, new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        init();
                        initListener();

                    }
                }, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
                );


        imageViews.add(ivMenu1);
        imageViews.add(ivMenu2);
        imageViews.add(ivMenu3);
        imageViews.add(ivMenu4);
        imageViews.add(ivMenu5);
    }

    private void init() {
        mAMap = mapView.getMap();
        if (mAMap != null) {
            // 缩放级别(3~19), 设置最大级别
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(mAMap.getMaxZoomLevel()));

            MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)//定位一次，且将视角移动到地图中心点。
                    .strokeColor(Color.TRANSPARENT) // 边框色
                    .radiusFillColor(Color.TRANSPARENT)// 填充色
                    .interval(1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
            mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
            //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
            mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

            // 设置定位监听
            mAMap.setLocationSource(this);
            // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            mAMap.setMyLocationEnabled(true);
            UiSettings uiSettings = mAMap.getUiSettings();
            // 显示比例尺
            uiSettings.setScaleControlsEnabled(true);
            // 隐藏缩放控件
            uiSettings.setZoomControlsEnabled(false);
        }

        mMockLocServiceIntent = new Intent(MainActivity.this, MockGpsService.class);

    }


    private void initListener() {

        mAMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {

                if (marker == null) {
                    View markerView = LayoutInflater.from(MainActivity.this).inflate(R.layout.amap_marker, mapView, false);
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(latLng)
                            .icon(BitmapDescriptorFactory.fromView(markerView));
                    marker = mAMap.addMarker(markerOptions);
                } else {
                    marker.setPosition(latLng);
                }

                GeoCoderUtil.getInstance(MainActivity.this).geoAddress(latLng, new GeoCoderUtil.GeoCoderAddressListener() {
                    @Override
                    public void onAddressResult(RegeocodeAddress regeocodeAddress, String result) {
//                        mapSearchAddressEt.setText(result);
//                        locationProvince = regeocodeAddress.getProvince();
//                        locationCity = regeocodeAddress.getCity();
//                        locationArea = regeocodeAddress.getDistrict();
//                        locationDesc = regeocodeAddress.getTownship()
//                                + regeocodeAddress.getStreetNumber().getStreet();
//                        if (regeocodeAddress.getAois().size() > 0) {
//                            locationDesc += regeocodeAddress.getAois().get(0).getAoiName();
//                        }

                        markerLonLatInfo = new LocationEntity();
                        markerLonLatInfo.setLatLng(new LatLng(latLng.latitude, latLng.longitude));
                        markerLonLatInfo.setAddress(result);
                        Log.d("msg", "MainActivity -> onAddressResult: 点击: " + markerLonLatInfo);
                    }
                });
            }
        });

    }

    @OnClick(R.id.btn_search)
    public void searchAddress(View v) {
        String key = mapSearchAddressEt.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            return;
        }
        KeyboardUtils.hideKeyBoard(v);

        PoiSearch.Query query = new PoiSearch.Query(key, "", "深圳");
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(0);//设置查询页码
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.searchPOIAsyn();


        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int rCode) {
                Log.d("msg", "AMapSearchActivity -> onPoiSearched: " + poiResult.toString());
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (poiResult != null && poiResult.getQuery() != null) {
                        if (poiResult.getPois().size() > 0) {
                            PoiItem poiItem = poiResult.getPois().get(0);
                            LatLonPoint latLonPoint = poiItem.getLatLonPoint();
                            // 移动地图
                            MapUtils.AmapMoveCamera(mAMap, latLonPoint.getLatitude(), latLonPoint.getLongitude());
                        } else {
                            ToastUtils.showToast("未搜索到相应位置");
                        }
                    } else {
                        ToastUtils.showToast("未搜索到相应位置");
                    }
                } else {
                    ToastUtils.showToast("未搜索到相应位置");
                }

            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
                Log.d("msg", "AMapSearchActivity -> onPoiItemSearched: " + poiItem.toString());
            }
        });
    }

    @OnClick({R.id.iv_menu_1, R.id.iv_menu_2, R.id.iv_menu_3, R.id.iv_menu_4, R.id.iv_menu_5})
    public void onMenuClick(View v) {
        switch (v.getId()) {
            case R.id.iv_menu_1:
                if (mFlag) {
                    hintMenuAnim();
                } else {
                    showMenuAnim();
                }
                break;
            case R.id.iv_menu_2:    // 回到初始位置
                MapUtils.AmapMoveCamera(mAMap, firstLocationLat, firstLocationLng);
                break;
            case R.id.iv_menu_3:    // 将当前位置移至标记位置
                if (isAllowMockLocation()) {
                    runMockGPS();
                } else {
                    showGetPermissionDialog();
                }
                break;
            case R.id.iv_menu_4:    // 显示常用地址
                showAddressList();
                break;
            case R.id.iv_menu_5:    //

                break;
            default:
        }


    }

    private AlertDialog mDialog;
    private List<LocationEntity> mList = new ArrayList<>();
    private AddressAdapter mAdapter;

    /**
     * 显示常用位置数据
     */
    private void showAddressList() {
        if (mDialog == null) {
            View rootView = LayoutInflater.from(this).inflate(R.layout.dialog_address_layout, null);
            mDialog = new AlertDialog.Builder(this)
                    .setView(rootView)
                    .create();
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            lp.width = (int) (DeviceUtils.getWindowWidth(this) * 0.8);
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mDialog.getWindow().setAttributes(lp);

            rootView.findViewById(R.id.iv_close_dialog).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                }
            });
            RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

//            mList.addAll(LitePal.findAll(LocationEntity.class));
            // 降序排序
            mList.addAll(LitePal.order("usedCount desc").find(LocationEntity.class));

            mAdapter = new AddressAdapter(this, mList);
            recyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position, View view) {
                    markerLonLatInfo = mList.get(position);
                    stopMockGPS();
                    runMockGPS();
                    mDialog.dismiss();
                }
            });
        } else {
            if (!mList.isEmpty()) {
                mList.clear();
            }
            // 降序排序
            mList.addAll(LitePal.order("usedCount desc").find(LocationEntity.class));
            mAdapter.notifyDataSetChanged();
        }

        mDialog.show();
    }

    /**
     * 长按Toast提示
     *
     * @param v
     */
    @OnLongClick({R.id.iv_menu_1, R.id.iv_menu_2, R.id.iv_menu_3, R.id.iv_menu_4, R.id.iv_menu_5})
    public boolean onMenuLongClick(View v) {
        switch (v.getId()) {
            case R.id.iv_menu_1:
                ToastUtils.showToast("展开/收起菜单工具");
                break;
            case R.id.iv_menu_2:
                ToastUtils.showToast("回到初始位置");
                break;
            case R.id.iv_menu_3:
                ToastUtils.showToast("设置当前为目标位置");
                break;
            case R.id.iv_menu_4:
                ToastUtils.showToast("显示常用地址");
                break;
            case R.id.iv_menu_5:
                ToastUtils.showToast("帮助");
                break;
            default:

        }
        return false;
    }


    /**
     * 隐藏扇形菜单的属性动画
     */
    private void hintMenuAnim() {
        // 100为扇形半径dp值
        int dp = 100;
        //for循环来开始小图标的出现动画
        int size = imageViews.size();
        for (int i = 1; i < size; i++) {
            AnimatorSet set = new AnimatorSet();
            double x = -Math.cos(0.5 / (size - 2) * (i - 1) * Math.PI) * DeviceUtils.dp2px(this, dp);
            double y = -Math.sin(0.5 / (size - 2) * (i - 1) * Math.PI) * DeviceUtils.dp2px(this, dp);
            set.playTogether(
                    ObjectAnimator.ofFloat(imageViews.get(i), "translationX", (float) x, (float) (x * 0.25)),
                    ObjectAnimator.ofFloat(imageViews.get(i), "translationY", (float) y, (float) (y * 0.25))
                    , ObjectAnimator.ofFloat(imageViews.get(i), "alpha", 1, 0).setDuration(2000)
            );
            set.setInterpolator(new BounceInterpolator());
            set.setDuration(500).setStartDelay(100 * i);
            set.start();
        }
        //转动加号大图标本身45°
        ObjectAnimator rotate = ObjectAnimator.ofFloat(imageViews.get(0), "rotation", 45, 0).setDuration(300);
        rotate.setInterpolator(new BounceInterpolator());
        rotate.start();
        //菜单状态置打开
        mFlag = false;
    }

    /**
     * 显示扇形菜单的属性动画
     */
    private void showMenuAnim() {
        // 100为扇形半径dp值
        int dp = 100;
        //for循环来开始小图标的出现动画
        int size = imageViews.size();
        for (int i = 1; i < size; i++) {
            AnimatorSet set = new AnimatorSet();
            double x = -Math.cos(0.5 / (size - 2) * (i - 1) * Math.PI) * DeviceUtils.dp2px(this, dp);
            double y = -Math.sin(0.5 / (size - 2) * (i - 1) * Math.PI) * DeviceUtils.dp2px(this, dp);
            set.playTogether(
                    ObjectAnimator.ofFloat(imageViews.get(i), "translationX", (float) (x * 0.25), (float) x),
                    ObjectAnimator.ofFloat(imageViews.get(i), "translationY", (float) (y * 0.25), (float) y),
                    ObjectAnimator.ofFloat(imageViews.get(i), "alpha", 0, 1).setDuration(2000)
            );
            set.setInterpolator(new BounceInterpolator());
            set.setDuration(500).setStartDelay(100 * i);
            set.start();
        }
        //转动加号大图标本身45°
        ObjectAnimator rotate = ObjectAnimator.ofFloat(imageViews.get(0), "rotation", 0, 45).setDuration(300);
        rotate.setInterpolator(new BounceInterpolator());
        rotate.start();
        //菜单状态置打开
        mFlag = true;
    }

    /**
     * 模拟位置权限是否开启
     *
     * @return
     */
    public boolean isAllowMockLocation() {
        boolean canMockPosition = false;
        if (Build.VERSION.SDK_INT <= 22) {//6.0以下
            canMockPosition = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0;
        } else {
            try {
                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);//获得LocationManager引用
                String providerStr = LocationManager.GPS_PROVIDER;
                LocationProvider provider = locationManager.getProvider(providerStr);
                if (provider != null) {
                    locationManager.addTestProvider(
                            provider.getName()
                            , provider.requiresNetwork()
                            , provider.requiresSatellite()
                            , provider.requiresCell()
                            , provider.hasMonetaryCost()
                            , provider.supportsAltitude()
                            , provider.supportsSpeed()
                            , provider.supportsBearing()
                            , provider.getPowerRequirement()
                            , provider.getAccuracy());
                } else {
                    locationManager.addTestProvider(
                            providerStr
                            , true, true, false, false, true, true, true
                            , Criteria.POWER_HIGH, Criteria.ACCURACY_FINE);
                }
                locationManager.setTestProviderEnabled(providerStr, true);
                locationManager.setTestProviderStatus(providerStr, LocationProvider.AVAILABLE, null, System.currentTimeMillis());
                // 模拟位置可用
                canMockPosition = true;
                locationManager.setTestProviderEnabled(providerStr, false);
                locationManager.removeTestProvider(providerStr);
            } catch (SecurityException e) {
                canMockPosition = false;
            }
        }
        return canMockPosition;
    }

    /**
     * 将当前位置移至标记位置
     */
    private void runMockGPS() {

        if (markerLonLatInfo == null) {
            ToastUtils.showToast("请先设置需要定位的坐标");
            return;
        }
        if (!isMockServerStart) {
            LatLng latLng = markerLonLatInfo.getLatLng();
            // 记录缓存
            markerLonLatInfo.setLastTime(System.currentTimeMillis());
            markerLonLatInfo.usedLocation();
            markerLonLatInfo.save();
            MapUtils.AmapMoveCamera(mAMap, markerLonLatInfo.getLatLng().latitude, markerLonLatInfo.getLatLng().longitude);

            Log.d("msg", "MainActivity -> runMockGPS: " + mAMap.getMapScreenMarkers().size());
            if (locationMarker != null) {
                locationMarker.setPosition(markerLonLatInfo.getLatLng());
            }

            mMockLocServiceIntent.putExtra("latLng", latLng);
            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(mMockLocServiceIntent);
            } else {
                startService(mMockLocServiceIntent);
            }
            isMockServerStart = true;
            ToastUtils.showToast("位置模拟已开启");
        } else {
            stopMockGPS();
            ToastUtils.showToast("位置模拟已结束");
        }
    }

    /**
     * 结束
     */
    private void stopMockGPS() {
        stopService(mMockLocServiceIntent);
        isMockServerStart = false;
    }

    /**
     * 提醒开启位置模拟的弹框
     */
    private void showGetPermissionDialog() {
        //判断是否开启开发者选项
//        boolean enableAdb = (Settings.Secure.getInt(getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) > 0);
//        if (!enableAdb) {
//            DisplayToast("请打先开开发者选项");
//            return;
//        }

        new AlertDialog.Builder(this)
                .setTitle("启用位置模拟")//这里是表头的内容
                .setMessage("请在开发者选项->选择模拟位置信息应用中进行设置")//这里是中间显示的具体信息
                .setPositiveButton("设置",//这个string是设置左边按钮的文字
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    ToastUtils.showToast("无法跳转到开发者选项,请先确保您的设备已处于开发者模式");
                                    e.printStackTrace();
                                }
                            }
                        })//setPositiveButton里面的onClick执行的是左边按钮
                .setNegativeButton("取消",//这个string是设置右边按钮的文字
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })//setNegativeButton里面的onClick执行的是右边的按钮的操作
                .show();
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /**
     * 定位成功
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        try {
            if(locationMarker == null) {
                locationMarker = mAMap.getMapScreenMarkers().get(0);
            }
        }catch (Exception e){

        }
        
        if (isLocation) return;
        isLocation = true;


        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            mListener.onLocationChanged(aMapLocation);
            if (firstLocationLat == 0 || firstLocationLng == 0) {
                firstLocationLat = aMapLocation.getLatitude();
                firstLocationLng = aMapLocation.getLongitude();
            }
            Log.d("msg", "MainActivity -> onLocationChanged: 高德定位->" +
                    aMapLocation.getLongitude() + ":" + aMapLocation.getLatitude());

        } else {
            ToastUtils.showToast("定位失败!");
        }
    }

    private long lastClickTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastClickTime > 2000) {
                ToastUtils.showToast("再按一次退出");
                lastClickTime = currentTime;
                return true;
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
