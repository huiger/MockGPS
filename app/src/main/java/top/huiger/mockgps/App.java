package top.huiger.mockgps;

import android.app.Application;
import android.content.Context;

import com.huige.library.HGUtils;

import org.litepal.LitePal;

/**
 * <pre>
 *  Author : huiGer
 *  Time   : 2019/4/3 0003 下午 03:53.
 *  Email  : zhihuiemail@163.com
 *  Desc   :
 * </pre>
 */
public class App extends Application{

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        HGUtils.init(this);
        LitePal.initialize(this);
    }

    public static Context getContext() {
        return mContext;
    }
}
