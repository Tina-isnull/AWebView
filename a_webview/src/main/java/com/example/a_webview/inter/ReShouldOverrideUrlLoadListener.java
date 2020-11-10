package com.example.a_webview.inter;

import android.webkit.WebView;

public interface ReShouldOverrideUrlLoadListener {
     void interceptProcess(WebView wv, String url);
}
