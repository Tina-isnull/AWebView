package com.example.a_webview.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.example.a_webview.bean.InterBean;
import com.example.a_webview.components.MyWebViewClient;
import com.example.a_webview.components.ProgressTitleChromeClient;
import com.example.a_webview.inter.ProgressListener;
import com.example.a_webview.inter.ReShouldOverrideUrlLoading;
import com.example.a_webview.inter.onProgressCount;
import com.example.a_webview.inter.onTitleReceive;
import com.example.a_webview.myview.ProgressView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AWebViewWrapper {
    private Context mContext;
    //自定义拦截操作
    private ReShouldOverrideUrlLoading mRedefineUrl;
    private AWebView mWebView;
    private WebChromeClient mWebChromeClient;
    private WebViewClient mWebViewClient;
    //js交互的类
    private ArrayList<InterBean> mList;
    private String mUrl;
    private Map<String, String> mHttpHeaders;
    //是否需要进度条
    private boolean isUsedDefaultProgress;
    //标题的设置回调
    private onTitleReceive onTitleReceive;
    //自定义进度条
    private View mView;


    public AWebViewWrapper(Builder mBuilder) {
        this.mContext = mBuilder.mContext;
        this.mRedefineUrl = mBuilder.mRedefineUrl;
        this.mWebView = mBuilder.mWebView;
        this.mWebChromeClient = mBuilder.mWebChromeClient;
        this.mWebViewClient = mBuilder.mWebViewClient;
        this.mList = mBuilder.mList;
        this.mUrl = mBuilder.mUrl;
        this.mHttpHeaders = mBuilder.mHttpHeaders;
        this.isUsedDefaultProgress = mBuilder.isUsedDefaultProgress;
        this.onTitleReceive = mBuilder.onTitleReceive;
        this.mView = mBuilder.mView;
        init();
    }

    @SuppressLint("JavascriptInterface")
    private void init() {
        if (mWebChromeClient != null) {
            //重新定义
            mWebView.setWebChromeClient(mWebChromeClient);
        } else {
            //用封装过的
            ProgressTitleChromeClient mClient = new ProgressTitleChromeClient(mContext);
            mWebView.setmBaseWebChromeClient(mClient);
            //设置默认的进度条
            if (isUsedDefaultProgress) {
                initProgressBar(mClient);
            }
            //设置自定义的进度条
            if (mView != null) {
                setProgressBar(mClient, mView);
            }

            //设置标题
            if (onTitleReceive != null) {
                mClient.setOnTitleReceive(onTitleReceive);
            }
        }
        if (mWebViewClient != null) {
            //重新定义
            mWebView.setWebViewClient(mWebViewClient);
        } else {
            //用封装过的
            MyWebViewClient mClient = MyWebViewClient.createBuilder()
                    .setContext(mContext)
                    .setReShouldOverrideUrlLoading(mRedefineUrl)
                    .getWebViewClient();
            mWebView.setmBaseWebViewClient(mClient);
        }
        //与js交互
        for (InterBean data : mList) {
            mWebView.addJavascriptInterface(data.object, data.tab);
        }
        if (mHttpHeaders != null && mHttpHeaders.size() != 0) {
            mWebView.loadUrl(mUrl, mHttpHeaders);
        } else {
            mWebView.loadUrl(mUrl);
        }
    }

    //设置默认进度条
    ProgressView mProgressview;

    private void initProgressBar(ProgressTitleChromeClient mclient) {
        mProgressview = new ProgressView(mContext);
        mProgressview.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 6));
        mWebView.addView(mProgressview);
        mclient.setOnProgressCount(new onProgressCount() {
            @Override
            public void onProgress(int newProgress) {
                mProgressview.setProgress(newProgress);
            }
        });
    }

    //自定义进度条
    private void setProgressBar(ProgressTitleChromeClient mclient, final View mView) {
        if(mView instanceof ProgressListener){
            mWebView.addView(mView);
            mclient.setOnProgressCount(new onProgressCount() {
                @Override
                public void onProgress(int newProgress) {
                    ((ProgressListener)mView).setProgress(newProgress);
                }
            });
        }

    }


    public static Builder createBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Context mContext;
        private ReShouldOverrideUrlLoading mRedefineUrl;
        private AWebView mWebView;
        private WebChromeClient mWebChromeClient;
        private WebViewClient mWebViewClient;
        private ArrayList<InterBean> mList = new ArrayList<>();
        private String mUrl;
        private Map<String, String> mHttpHeaders = new HashMap<String, String>();
        private boolean isUsedDefaultProgress;
        private onTitleReceive onTitleReceive;
        private View mView;

        public Builder setContext(Context mContext) {
            this.mContext = mContext;
            return this;
        }

        //是否重新定义拦截跳转的逻辑
        public Builder setReShouldOverrideUrlLoading(ReShouldOverrideUrlLoading mRedefineUrl) {
            this.mRedefineUrl = mRedefineUrl;
            return this;
        }

        public Builder setWebView(AWebView mWebView) {
            this.mWebView = mWebView;
            return this;
        }

        public Builder setWebChromeClient(WebChromeClient mWebChromeClient) {
            this.mWebChromeClient = mWebChromeClient;
            return this;
        }

        public Builder setWebViewClient(WebViewClient mWebViewClient) {
            this.mWebViewClient = mWebViewClient;
            return this;
        }

        public Builder setList(ArrayList<InterBean> mList) {
            this.mList = mList;
            return this;
        }

        public Builder setUrl(String mUrl) {
            this.mUrl = mUrl;
            return this;
        }

        public Builder addHttpHeader(final String name, final String value) {
            mHttpHeaders.put(name, value);
            return this;
        }

        public Builder setUsedDefaultProgress(Boolean usedDefaultProgress) {
            isUsedDefaultProgress = usedDefaultProgress;
            return this;
        }

        public Builder setOnTitleReceive(com.example.a_webview.inter.onTitleReceive onTitleReceive) {
            this.onTitleReceive = onTitleReceive;
            return this;
        }

        public Builder setProgressView(View mView) {
            this.mView = mView;
            return this;
        }

        public AWebViewWrapper getAWebViewWrapper() {
            return new AWebViewWrapper(this);
        }
    }

}
