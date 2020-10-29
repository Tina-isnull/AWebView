package com.example.awebview;

import android.webkit.JavascriptInterface;

import com.example.a_webview.utils.LogUtils;


public class WebViewJS {
    @JavascriptInterface
    public void goToSecond(String content) {
        LogUtils.d(content);
    }
}
