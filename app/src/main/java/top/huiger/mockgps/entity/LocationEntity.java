package top.huiger.mockgps.entity;

import com.amap.api.maps2d.model.LatLng;

import org.litepal.crud.LitePalSupport;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/4/12 3:02 PM.
 *  Email  : zhihuiemail@163.com
 *  Desc   : 常用地址
 * </pre>
 */
public class LocationEntity extends LitePalSupport {

    /**
     * 详细地址
     */
    private String address;

    /**
     * 纬度
     */
    private double lat;

    /**
     * 精度
     */
    private double lng;

    /**
     * 使用次数
     */
    private int usedCount;

    /**
     * 最后一次使用时间
     */
    private long lastTime;

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }

    public void setLatLng(LatLng latLng) {
        this.lat = latLng.latitude;
        this.lng = latLng.longitude;
    }

    public int getUsedCount() {
        return usedCount;
    }

    /**
     * 使用该坐标
     */
    public void usedLocation() {
        this.usedCount++;
    }
}
