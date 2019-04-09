package top.huiger.mockgps.gaodeLBS;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2018/12/28 0028 上午 11:40.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class GeoCoderUtil implements GeocodeSearch.OnGeocodeSearchListener {
    private GeocodeSearch geocodeSearch;
    private GeoCoderAddressListener geoCoderAddressListener;
    private GeoCoderLatLngListener geoCoderLatLngListener;

    private static GeoCoderUtil geoCoderUtil;
    public static GeoCoderUtil getInstance(Context context) {
        if (null == geoCoderUtil) {
            geoCoderUtil = new GeoCoderUtil(context);
        }
        return geoCoderUtil;
    }

    private GeoCoderUtil(Context context) {
        geocodeSearch = new GeocodeSearch(context);
        geocodeSearch.setOnGeocodeSearchListener(this);
    }

    /**
     * 经纬度转地址描述
     **/
    public void geoAddress(LatLng latLngEntity, GeoCoderAddressListener geoCoderAddressListener) {
        if (latLngEntity == null) {
            geoCoderAddressListener.onAddressResult(null, "");
            return;
        }
        this.geoCoderAddressListener = geoCoderAddressListener;

        LatLonPoint latLonPoint = new LatLonPoint(latLngEntity.latitude, latLngEntity.longitude);
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求

    }

    /**
     * 经纬度转地址描述,同步方法
     **/
    public String geoAddress(LatLng latLngEntity) {
        if (latLngEntity == null) {
            return "";
        }
        this.geoCoderAddressListener = geoCoderAddressListener;

        LatLonPoint latLonPoint = new LatLonPoint(latLngEntity.latitude, latLngEntity.longitude);
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        try {
            RegeocodeAddress regeocodeAddress = geocodeSearch.getFromLocation(query);// 设置异步逆地理编码请求
            String formatAddress = regeocodeAddress.getFormatAddress();
            return formatAddress;
        } catch (AMapException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 地址描述转经纬度
     **/
    public void geoLatLng(String cityName, String address, GeoCoderLatLngListener geoCoderLatLngListener) {
        if (TextUtils.isEmpty(cityName) || TextUtils.isEmpty(address)) {
            geoCoderLatLngListener.onLatLngResult(null);
            return;
        }
        this.geoCoderLatLngListener = geoCoderLatLngListener;
        GeocodeQuery query = new GeocodeQuery(address, cityName);
        geocodeSearch.getFromLocationNameAsyn(query);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode != 1000 || result == null || result.getRegeocodeAddress() == null) {
            geoCoderAddressListener.onAddressResult(null,"");
            return;
        }
        RegeocodeAddress regeocodeAddress = result.getRegeocodeAddress();
        String addressDesc = regeocodeAddress.getCity()
                + regeocodeAddress.getDistrict()
                + regeocodeAddress.getTownship()
                + regeocodeAddress.getStreetNumber().getStreet();
        if (regeocodeAddress.getAois().size() > 0) {
            addressDesc += regeocodeAddress.getAois().get(0).getAoiName();
        }
        geoCoderAddressListener.onAddressResult(regeocodeAddress, addressDesc);
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {
        if (geocodeResult == null || geocodeResult.getGeocodeAddressList() == null
                || geocodeResult.getGeocodeAddressList().size() <= 0) {
            geoCoderLatLngListener.onLatLngResult(null);
            return;
        }
        GeocodeAddress address = geocodeResult.getGeocodeAddressList().get(0);
        geoCoderLatLngListener.onLatLngResult(new LatLng(address.getLatLonPoint().getLatitude(), address.getLatLonPoint().getLongitude()));

    }

    public interface GeoCoderAddressListener {
        void onAddressResult(RegeocodeAddress regeocodeAddress, String result);
    }

    public interface GeoCoderLatLngListener {
        void onLatLngResult(LatLng latLng);
    }
}
