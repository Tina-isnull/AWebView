package com.example.a_webview.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.a_webview.web.AWebView;

/**
 * 监听Activity生命周期的方法，配合调用webView的相应方法
 */
public class WebViewLifecycle implements Application.ActivityLifecycleCallbacks {
    private Activity mActivity;
    private AWebView mWebView;

    WebViewLifecycle(Activity activity, AWebView mWebView) {
        mActivity = activity;
        this.mWebView = mWebView;
    }

    /**
     * 注册监听
     */
    void register() {
        mActivity.getApplication().registerActivityLifecycleCallbacks(this);
    }

    /**
     * 取消监听
     */
    void unregister() {
        mActivity.getApplication().unregisterActivityLifecycleCallbacks(this);
    }


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
