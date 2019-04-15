package top.huiger.mockgps.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.huige.library.widget.MyToolBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import top.huiger.mockgps.R;

public class AboutActivity extends AppCompatActivity {

    @Bind(R.id.toolBar)
    MyToolBar toolBar;
    @Bind(R.id.progress)
    ProgressBar progress;
    @Bind(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        init();
        initListener();
    }

    private void init() {

        WebSettings settings = webView.getSettings();
        //配置支持domstorage
        settings.setDomStorageEnabled(true);//启用或禁用DOM缓存
        settings.setAppCacheEnabled(false);//关闭/启用应用缓存
        settings.setSupportZoom(true);//是否可以缩放，默认true
        //settings.setBuiltInZoomControls(false);//是否显示缩放按钮，默认false
        settings.setJavaScriptEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        settings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webView.loadUrl("http://htmlpreview.github.io/?https://github.com/huiger/MockGPS/blob/master/html/about.html");

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress == 100) {
                    progress.setVisibility(View.INVISIBLE);
                }else{
                    progress.setVisibility(View.VISIBLE);
                }
                progress.setProgress(newProgress);
            }
        });
    }

    private void initListener() {
        toolBar.setOnToolBarClickListener(new MyToolBar.OnToolBarClick(){
            @Override
            public void onLeftClick() {
                finish();
            }
        });
    }
}
