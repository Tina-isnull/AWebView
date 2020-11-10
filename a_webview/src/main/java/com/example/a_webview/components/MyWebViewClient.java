package com.example.a_webview.components;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

import com.example.a_webview.inter.ReShouldOverrideUrlLoadListener;


public class MyWebViewClient extends BaseWebViewClient {
    private Context mContext;
    private ReShouldOverrideUrlLoadListener mRedefineUrl;//自定义拦截操作

    public MyWebViewClient() {
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView wv, String url) {
        if (url == null) return false;
        try {
            if (mRedefineUrl != null) {
                mRedefineUrl.interceptProcess(wv, url);
            }
            if (url.startsWith("weixin://") //微信
                    || url.startsWith("alipays://") //支付宝
                    || url.startsWith("mailto://") //邮件
                    || url.startsWith("tel://")//电话
                    || url.startsWith("dianping://")//大众点评
            ) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(intent);
                return true;
            }
        } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
            return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
        }
        if (urlCanLoad(url.toLowerCase())) {// 继续加载正常网页
            return false;
        }
        return true;
    }


    public MyWebViewClient(Builder builder) {
        mContext = builder.mContext;
        mRedefineUrl = builder.mRedefineUrl;
    }

    public static Builder createBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Context mContext;
        private ReShouldOverrideUrlLoadListener mRedefineUrl;

        public Builder setContext(Context mContext) {
            this.mContext = mContext;
            return this;
        }


        public Builder setReShouldOverrideUrlLoading(ReShouldOverrideUrlLoadListener mRedefineUrl) {
            this.mRedefineUrl = mRedefineUrl;
            return this;
        }

        public MyWebViewClient getWebViewClient() {
            return new MyWebViewClient(this);
        }
    }

    /**
     * 列举正常情况下能正常加载的网页url
     *
     * @param url
     * @return
     */
    private boolean urlCanLoad(String url) {
        return url.startsWith("http://") || url.startsWith("https://") ||
                url.startsWith("ftp://") || url.startsWith("file://");
    }
}
