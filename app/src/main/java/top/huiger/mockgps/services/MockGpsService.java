package top.huiger.mockgps.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.amap.api.maps2d.model.LatLng;

import java.util.UUID;

import top.huiger.mockgps.utils.MapFixUtil;
import top.huiger.mockgps.R;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/4/3 0003 下午 06:02.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class MockGpsService extends Service {

    public static final int RunCode = 0x01;
    public static final int StopCode = 0x02;
    private String TAG = "MockGpsService";
    private LocationManager locationManager;
    private HandlerThread handlerThread;
    private Handler handler;
    private boolean isStop = true;
    //经纬度
    private LatLng mLatLng;

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //remove default network location provider
        rmNetworkProvider();

        //add a new network location provider
//        setGPSProvider();
        setNewNetworkProvider();

        //thread
        handlerThread = new HandlerThread(getUUID(), -2);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper()) {
            public void handleMessage(Message msg) {
                try {
                    Thread.sleep(333);
                    if (!isStop) {
                        setNetworkLocation();
                        sendEmptyMessage(0);
                        //broadcast to MainActivity
                        Intent intent = new Intent();
                        intent.putExtra("statusCode", RunCode);
                        intent.setAction("top.huiger.mockgps.services.MockGpsService");
                        sendBroadcast(intent);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d(TAG, "setNetworkLocation error");
                    Thread.currentThread().interrupt();
                }
            }
        };
        handler.sendEmptyMessage(0);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String channelId = "channel_01";
        String name = "channel_name";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_LOW);
            Log.i(TAG, mChannel.toString());
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
            notification = new Notification.Builder(this)
                    .setChannelId(channelId)
                    .setContentTitle("位置模拟服务已启动")
                    .setContentText("MockLocation service is running")
                    .setSmallIcon(R.mipmap.logo).build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("位置模拟服务已启动")
                    .setContentText("MockLocation service is running")
                    .setSmallIcon(R.mipmap.logo)
                    .setOngoing(true)
                    .setChannelId(channelId);//无效
            notification = notificationBuilder.build();
        }
        startForeground(1, notification);
        //

        //get location info from mainActivity
        mLatLng = intent.getParcelableExtra("latLng");
        //start to refresh location
        isStop = false;


//        return START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
//        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show();

//        DisplayToast("Mock Loction Service finish");
        isStop = true;

        handler.removeMessages(0);
        handlerThread.quit();
        rmNetworkProvider();

        stopForeground(true);

        //broadcast to MainActivity
        Intent intent = new Intent();
        intent.putExtra("statusCode", StopCode);
        intent.setAction("top.huiger.mockgps.services.MockGpsService");
        sendBroadcast(intent);

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //remove network provider
    private void rmNetworkProvider() {
        try {
            String providerStr = LocationManager.NETWORK_PROVIDER;
            if (locationManager.isProviderEnabled(providerStr)) {
                Log.d(TAG, "now remove NetworkProvider");
//                locationManager.setTestProviderEnabled(providerStr,true);
                locationManager.removeTestProvider(providerStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "rmNetworkProvider error");
        }
    }

    //set new network provider
    private void setNewNetworkProvider() {
        String providerStr = LocationManager.NETWORK_PROVIDER;
        try {
            locationManager.addTestProvider(providerStr, false, false,
                    false, false, false, false,
                    false, 1, Criteria.ACCURACY_FINE);
            Log.d(TAG, "addTestProvider[network] success");
//            locationManager.setTestProviderStatus("network", LocationProvider.AVAILABLE, null,
//                    System.currentTimeMillis());
        } catch (SecurityException e) {
            Log.d(TAG, "setNewNetworkProvider error");
        }
        if (!locationManager.isProviderEnabled(providerStr)) {
            Log.d(TAG, "now  setTestProviderEnabled[network]");
            locationManager.setTestProviderEnabled(providerStr, true);
        }
    }

    //uuid random
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    //set network location
    private void setNetworkLocation() {
        // log&lat
//        String latLngStr[] = latLngInfo.split("&");
//        LatLng latLng = new LatLng(Double.valueOf(latLngStr[1]), Double.valueOf(latLngStr[0]));
        Log.d("msg", "MockGpsService -> setNetworkLocation: 原始数据->" + mLatLng.latitude + ":" + mLatLng.longitude);
        double[] transform = MapFixUtil.toGPSPoint(mLatLng.latitude, mLatLng.longitude);
        LatLng latLng = new LatLng(transform[0], transform[1]);
        Log.d("msg", "MockGpsService -> setNetworkLocation: 纠正偏差后->" + latLng.longitude + ":" + latLng.latitude);
        String providerStr = LocationManager.NETWORK_PROVIDER;
//        String providerStr = LocationManager.GPS_PROVIDER;
        try {
            locationManager.setTestProviderLocation(providerStr, generateLocation(latLng));
            //for test
//            locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, generateLocation(latLng));
        } catch (Exception e) {
            Log.d(TAG, "setNetworkLocation error");
            e.printStackTrace();
        }
    }

    //generate a location
    public Location generateLocation(LatLng latLng) {
        Location loc = new Location(LocationManager.GPS_PROVIDER);

        loc.setAccuracy(2.0F);
        loc.setAltitude(55.0D);
        loc.setBearing(1.0F);


        Bundle bundle = new Bundle();
        bundle.putInt("satellites", 7);
        loc.setExtras(bundle);


        loc.setLatitude(latLng.latitude);
        loc.setLongitude(latLng.longitude);
        loc.setTime(System.currentTimeMillis());

        if (Build.VERSION.SDK_INT >= 17) {
            loc.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }
        return loc;
    }

    // for test: set GPS provider
    private void setGPSProvider() {
        LocationProvider provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
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
            locationManager.addTestProvider(LocationManager.GPS_PROVIDER, false, true, false,
                    false, true, true, true, 0, 5);
        }

        locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
        //新
        locationManager.setTestProviderStatus(LocationManager.GPS_PROVIDER, LocationProvider.AVAILABLE, null,
                System.currentTimeMillis());
    }

}
