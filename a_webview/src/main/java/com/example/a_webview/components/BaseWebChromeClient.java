package com.example.a_webview.components;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class BaseWebChromeClient extends WebChromeClient {
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
//        if (newProgress == 100) {
//            progressBar.setVisibility(View.GONE);//加载完网页进度条消失
//        } else {
//            progressBar.setProgress(newProgress);//设置进度值
//            progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
//        }
    }


}
