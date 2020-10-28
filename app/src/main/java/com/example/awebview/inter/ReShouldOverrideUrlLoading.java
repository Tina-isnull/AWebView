package com.example.awebview.inter;

import android.webkit.WebView;

public interface ReShouldOverrideUrlLoading {
     void InterceptProcess(WebView wv, String url);
}
