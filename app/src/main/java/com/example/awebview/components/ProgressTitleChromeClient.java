package com.example.awebview.components;

import android.content.Context;
import android.webkit.JsPromptResult;
import android.webkit.WebView;

import com.example.awebview.inter.onProgressCount;
import com.example.awebview.inter.onTitleReceive;

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

    public void setOnProgressCount(com.example.awebview.inter.onProgressCount onProgressCount) {
        this.onProgressCount = onProgressCount;
    }

    public void setOnTitleReceive(com.example.awebview.inter.onTitleReceive onTitleReceive) {
        this.onTitleReceive = onTitleReceive;
    }
}
