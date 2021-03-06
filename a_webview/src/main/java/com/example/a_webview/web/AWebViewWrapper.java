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
import com.example.a_webview.components.PhotoWebChromeClient;
import com.example.a_webview.components.ProgressTitleChromeClient;
import com.example.a_webview.inter.ProgressListener;
import com.example.a_webview.inter.ReShouldOverrideUrlLoadListener;
import com.example.a_webview.inter.onOpenThreeListener;
import com.example.a_webview.inter.onPhotoDialogListener;
import com.example.a_webview.inter.onProgressCountListener;
import com.example.a_webview.inter.onTitleReceiveListener;
import com.example.a_webview.myview.ProgressView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AWebViewWrapper {
    private Context mContext;
    //自定义拦截操作
    private ReShouldOverrideUrlLoadListener mRedefineUrl;
    private onOpenThreeListener onOpenThreeListener;
    private AWebView mWebView;
    private WebChromeClient mWebChromeClient;
    private WebViewClient mWebViewClient;

    private String mUrl;
    private Map<String, String> mHttpHeaders;
    //是否需要进度条
    private boolean isHaveProgress;
    //标题的设置回调
    private onTitleReceiveListener onTitleReceive;
    //自定义进度条
    private View mView;
    //进度条颜色
    private int mProgressColor;
    //调起照片的弹框
    private onPhotoDialogListener onPhotoDialogListener;


    public AWebViewWrapper(Builder mBuilder) {
        this.mContext = mBuilder.mContext;
        this.mRedefineUrl = mBuilder.mRedefineUrl;
        this.mWebView = mBuilder.mWebView;
        this.mWebChromeClient = mBuilder.mWebChromeClient;
        this.mWebViewClient = mBuilder.mWebViewClient;
        this.mUrl = mBuilder.mUrl;
        this.mHttpHeaders = mBuilder.mHttpHeaders;
        this.isHaveProgress = mBuilder.isHaveProgress;
        this.onTitleReceive = mBuilder.onTitleReceive;
        this.mView = mBuilder.mView;
        this.onPhotoDialogListener = mBuilder.photoDialogListener;
        this.mProgressColor = mBuilder.mProgressColor;
        this.onOpenThreeListener = mBuilder.onOpenThreeListener;
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
            if (onPhotoDialogListener != null) {//拍照功能
                ((PhotoWebChromeClient) mClient).setListener(onPhotoDialogListener);
            }
            mWebView.setmBaseWebChromeClient(mClient);
            //设置默认的进度条
            if (isHaveProgress) {
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
                    .setOpenThreeListener(onOpenThreeListener)
                    .getWebViewClient();
            mWebView.setmBaseWebViewClient(mClient);
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
        if (mProgressColor != 0) {
            mProgressview.setDefaultColor(mProgressColor);//设置进度条的颜色
        }
        mWebView.addView(mProgressview);
        mclient.setOnProgressCount(new onProgressCountListener() {
            @Override
            public void onProgress(int newProgress) {
                mProgressview.setProgress(newProgress);
            }
        });
    }

    //自定义进度条
    private void setProgressBar(ProgressTitleChromeClient mclient, final View mView) {
        if (mView instanceof ProgressListener) {
            mWebView.addView(mView);
            mclient.setOnProgressCount(new onProgressCountListener() {
                @Override
                public void onProgress(int newProgress) {
                    ((ProgressListener) mView).setProgress(newProgress);
                }
            });
        }

    }


    public static Builder createBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Context mContext;
        private ReShouldOverrideUrlLoadListener mRedefineUrl;
        private AWebView mWebView;
        private WebChromeClient mWebChromeClient;
        private WebViewClient mWebViewClient;
        private String mUrl;
        private Map<String, String> mHttpHeaders = new HashMap<String, String>();
        private boolean isHaveProgress;
        private onTitleReceiveListener onTitleReceive;
        private View mView;
        private int mProgressColor;
        private onPhotoDialogListener photoDialogListener;
        private onOpenThreeListener onOpenThreeListener;

        public Builder setPhotoDialogListener(com.example.a_webview.inter.onPhotoDialogListener photoDialogListener) {
            this.photoDialogListener = photoDialogListener;
            return this;
        }

        public Builder setContext(Context mContext) {
            this.mContext = mContext;
            return this;
        }

        //是否重新定义拦截跳转的逻辑
        public Builder setReShouldOverrideUrlLoading(ReShouldOverrideUrlLoadListener mRedefineUrl) {
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


        public Builder setUrl(String mUrl) {
            this.mUrl = mUrl;
            return this;
        }

        public Builder addHttpHeader(final String name, final String value) {
            mHttpHeaders.put(name, value);
            return this;
        }

        public Builder setIsHaveProgress(Boolean usedDefaultProgress) {
            isHaveProgress = usedDefaultProgress;
            return this;
        }

        public Builder setOnTitleReceive(onTitleReceiveListener onTitleReceive) {
            this.onTitleReceive = onTitleReceive;
            return this;
        }

        public Builder setProgressView(View mView) {
            this.mView = mView;
            return this;
        }

        public Builder setProgressColor(int mProgressColor) {
            this.mProgressColor = mProgressColor;
            return this;
        }

        public Builder setOpenThreeListener(onOpenThreeListener onOpenThreeListener) {
            this.onOpenThreeListener = onOpenThreeListener;
            return this;

        }

        public AWebViewWrapper getAWebViewWrapper() {
            return new AWebViewWrapper(this);
        }
    }

}
