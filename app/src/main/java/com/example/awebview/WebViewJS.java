package com.example.awebview;

import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.example.awebview.utils.LogUtils;

public class WebViewJS {
    @JavascriptInterface
    public void goToSecond(String content) {
        LogUtils.d(content);
    }
}
