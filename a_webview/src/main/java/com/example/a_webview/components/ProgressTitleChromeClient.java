package com.example.a_webview.components;

import android.content.Context;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.WebView;

import com.example.a_webview.inter.onProgressCountListener;
import com.example.a_webview.inter.onTitleReceiveListener;

/**
 * 进度条、标题
 */
public class ProgressTitleChromeClient extends VideoWebChromeClient {
    private onProgressCountListener onProgressCount;
    private onTitleReceiveListener onTitleReceive;

    public ProgressTitleChromeClient(Context mContext) {
        super(mContext);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if (onTitleReceive != null) {
            onTitleReceive.onTitle(title);
        }

    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (onProgressCount != null) {
            onProgressCount.onProgress(newProgress);
        }

    }

    /**
     * 用于开启定位的功能
     */
    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        callback.invoke(origin, true, false);
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    public void setOnProgressCount(onProgressCountListener onProgressCount) {
        this.onProgressCount = onProgressCount;
    }

    public void setOnTitleReceive(onTitleReceiveListener onTitleReceive) {
        this.onTitleReceive = onTitleReceive;
    }
}
