package com.example.a_webview.inter;

import android.webkit.WebView;

public interface ReShouldOverrideUrlLoading {
     void InterceptProcess(WebView wv, String url);
}
