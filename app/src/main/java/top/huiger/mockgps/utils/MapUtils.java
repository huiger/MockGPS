package top.huiger.mockgps.utils;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.model.LatLng;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/4/9 7:20 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class MapUtils {

    /**
     * 将地图移动至该经纬度
     */
    public static void AmapMoveCamera(AMap aMap, double lat, double lng){
        if (aMap != null) {
//            aMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat, lng)));
            aMap.moveCamera(CameraUpdateFactory.zoomTo(aMap.getMaxZoomLevel()));
        }
    }
}
