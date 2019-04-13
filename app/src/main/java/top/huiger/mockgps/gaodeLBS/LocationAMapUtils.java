package top.huiger.mockgps.gaodeLBS;

import android.Manifest;
import android.app.Activity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.yanzhenjie.permission.Action;

import java.util.List;

import top.huiger.mockgps.utils.PermissionHelper;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/12/27 0027 下午 04:36.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 高德定位
 * </pre>
 */
public class LocationAMapUtils {

    private static LocationAMapUtils mLocationAMapUtils = null;
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;

    private LocationAMapUtils() {
    }

    public static LocationAMapUtils getInstance() {
        if (mLocationAMapUtils == null) {
            synchronized (LocationAMapUtils.class) {
                if (mLocationAMapUtils == null) {
                    mLocationAMapUtils = new LocationAMapUtils();
                }
            }
        }
        return mLocationAMapUtils;
    }

    public void init(final Activity activity, final LocationAMapCallBack callBack) {
//        MapUtils.getPermission(activity, new Action<List<String>>() {
//            @Override
//            public void onAction(List<String> data) {
//                getLocation(activity, callBack);
//            }
//        }, new Action<List<String>>() {
//            @Override
//            public void onAction(List<String> data) {
//                if (AndPermission.hasAlwaysDeniedPermission(activity, data)) {
//                    SimpleDialog.showBaseDialog(activity.getString(R.string.message_permission_always_failed, "位置信息"),
//                            "取消", "前往设置", activity, null, new SimpleDialog.OnSimpleDialogClickListener() {
//                                @Override
//                                public void onClick(View v, AlertDialog dialog) {
//                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                    activity.startActivity(intent);
//                                }
//                            });
//                }
//            }
//        }, needPermissions);
        PermissionHelper.getPermission(activity, new Action<List<String>>() {
            @Override
            public void onAction(List<String> data) {
                getLocation(activity, callBack);
            }
        }, needPermissions);
    }

    /**
     * 获取定位数据
     */
    private void getLocation(Activity activity, final LocationAMapCallBack callBack) {
        //初始化定位
        mLocationClient = new AMapLocationClient(activity);
        AMapLocationClientOption clientOption = new AMapLocationClientOption();
        // 设置场景
        clientOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);

        if (null != mLocationClient) {
            mLocationClient.setLocationOption(clientOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
        mLocationClient.setLocationOption(clientOption);

        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (callBack != null) {
                    callBack.address(aMapLocation);
                }
            }
        });
    }


}
