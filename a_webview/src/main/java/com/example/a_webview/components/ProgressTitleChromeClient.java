package com.example.a_webview.components;

import android.content.Context;
import android.webkit.JsPromptResult;
import android.webkit.WebView;

import com.example.a_webview.inter.onProgressCount;
import com.example.a_webview.inter.onTitleReceive;

/**
 * 进度条、标题
 */
public class ProgressTitleChromeClient extends VideoWebChromeClient {
    private onProgressCount onProgressCount;
    private onTitleReceive onTitleReceive;

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

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    public void setOnProgressCount(onProgressCount onProgressCount) {
        this.onProgressCount = onProgressCount;
    }

    public void setOnTitleReceive( onTitleReceive onTitleReceive) {
        this.onTitleReceive = onTitleReceive;
    }
}
