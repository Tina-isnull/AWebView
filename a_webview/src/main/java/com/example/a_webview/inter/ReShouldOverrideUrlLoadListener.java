package com.example.a_webview.inter;

import android.webkit.WebView;

public interface ReShouldOverrideUrlLoadListener {
     boolean interceptProcess(WebView wv, String url);
}
