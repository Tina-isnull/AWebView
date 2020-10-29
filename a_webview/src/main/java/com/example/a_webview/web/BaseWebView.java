package com.example.a_webview.web;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.a_webview.components.BaseWebChromeClient;
import com.example.a_webview.components.BaseWebViewClient;
import com.example.a_webview.inter.LifeCycleManager;
import com.example.a_webview.utils.LogUtils;

import org.json.JSONArray;

import java.util.Arrays;


public class BaseWebView extends WebView implements LifeCycleManager {

    public BaseWebChromeClient mBaseWebChromeClient;
    public BaseWebViewClient mBaseWebViewClient;

    public BaseWebView(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    public void init() {
        initSetting();
        mBaseWebChromeClient = new BaseWebChromeClient();
        mBaseWebViewClient = new BaseWebViewClient();
        setWebChromeClient(mBaseWebChromeClient);
        setWebViewClient(mBaseWebViewClient);
    }

    /**
     * 自定义webChromeClient
     */
    public void setmBaseWebChromeClient(BaseWebChromeClient mChromeClient) {
        mBaseWebChromeClient = mChromeClient;
        setWebChromeClient(mBaseWebChromeClient);
    }

    /**
     * 自定义webClientView
     *
     * @param mClient
     */
    public void setmBaseWebViewClient(BaseWebViewClient mClient) {
        mBaseWebViewClient = mClient;
        setWebViewClient(mBaseWebViewClient);
    }

    /**
     * 初始化设置
     */
    public void initSetting() {
        WebSettings webSetting = this.getSettings();
        //允许js调用
        webSetting.setJavaScriptEnabled(true);
        //支持通过JS打开新窗口
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        /*
         * 排版适应屏幕
         * 用WebView显示图片，可使用这个参数
         * 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        //支持页面缩放
        webSetting.setSupportZoom(true);
        // 设置内置的缩放控件。若为false，则该WebView不可缩放
        webSetting.setBuiltInZoomControls(true);
        // 设置此属性，可任意比例缩放。
        webSetting.setUseWideViewPort(true);
        //设置WebView是否支持多窗口,如果为true需要实现onCreateWindow(WebView, boolean, boolean, Message)
        webSetting.setSupportMultipleWindows(false);
        //重写使用缓存的方式
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        //设置应用缓存
        webSetting.setAppCacheEnabled(true);
        String cachePath = getContext().getCacheDir().getPath(); // 把内部私有缓存目录'/data/data/包名/cache/'作为WebView的AppCache的存储路径
        webSetting.setAppCachePath(cachePath);
        //DOM存储API是否可用
        webSetting.setDomStorageEnabled(true);
        //设置应用缓存内容的最大值
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        //防止中文乱码
        webSetting.setDefaultTextEncodingName("UTF-8");
        //定位是否可用
        webSetting.setGeolocationEnabled(true);
        //网页内容的宽度是否可大于WebView控件的宽度
        webSetting.setLoadWithOverviewMode(true);
        // 保存表单数据
        webSetting.setSaveFormData(true);
        //设置是否支持插件
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //是否允许运行在一个file schema URL环境下的JavaScript访问来自其他任何来源的内容
        webSetting.setAllowUniversalAccessFromFileURLs(true);
        //是否允许运行在一个URL环境
        webSetting.setAllowFileAccessFromFileURLs(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //关闭安全浏览模式
            webSetting.setSafeBrowsingEnabled(false);
        }
        //从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //测试环境开启调试模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //todo 记得修改条件
            if (true) {
                this.setWebContentsDebuggingEnabled(true);
            }
        }
    }

    /**
     * 加载html代码
     */
    public void loadData(String content) {
        loadData(content, "text/html", "UTF-8");
    }


    /***
     * Android调用js的方法
     * @param name  方法名
     * @param mData  传参数
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void evaluateJavascriptUrl(String name, Object[] mData) {
        if (mData == null) mData = new Object[0];
        String data = new JSONArray(Arrays.asList(mData)).toString();
        String result = String.format("javascript:%s('" + "%s" + "')", name, data);
        evaluateJavascriptUrl(result);
    }

    /***
     * Android调用js的方法
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void evaluateJavascriptUrl(String script) {
        final int version = Build.VERSION.SDK_INT;
        // 因为该方法在 Android 4.4 版本才可使用，所以使用时需进行版本判断
        if (version < 18) {
            loadUrl(script);
        } else {
            BaseWebView.super.evaluateJavascript(script, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.i("tina======>", "webView的返回结果==" + value);
                }
            });
        }

    }

    /**
     * view销毁调用
     */
    @Override
    public void onDestroy() {
        try {
            if (getParent() != null) {
                ((ViewGroup) getParent()).removeView(this);// try to remove this view from its parent first
            }
            removeAllViews();
            stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            getSettings().setJavaScriptEnabled(false);
            clearHistory();
            clearView();
            destroy();
        } catch (Exception ignored) {
            LogUtils.d(ignored.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

    }



}
