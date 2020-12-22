package com.example.a_webview.components;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.a_webview.R;
import com.example.a_webview.inter.ReShouldOverrideUrlLoadListener;
import com.example.a_webview.inter.onOpenThreeListener;
import com.example.a_webview.utils.LogUtils;


public class MyWebViewClient extends BaseWebViewClient {
    private Context mContext;
    private ReShouldOverrideUrlLoadListener mRedefineUrl;//自定义拦截操作
    private onOpenThreeListener onOpenThreeListener;

    public MyWebViewClient() {
    }

    boolean isNext;

    @Override
    public boolean shouldOverrideUrlLoading(WebView wv, final String url) {
        if (url == null) return false;

        try {
            if (mRedefineUrl != null) { //这个功能主要用于自定义一些操作，用于本app的,但是一但定义就要全部定义
                isNext = mRedefineUrl.interceptProcess(wv, url);
            }
            if (isNext) {
                return true;
            }
            if (urlCanLoad(url.toLowerCase())) {// 继续加载正常网页
                return false;
            }
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (url.startsWith("weixin://") //微信
                    || url.startsWith("alipays://") //支付宝
                    || url.startsWith("mailto://") //邮件
                    || url.startsWith("tel://")//电话
                    || url.startsWith("dianping://")//大众点评
            ) {
                mContext.startActivity(intent);
                return true;
            } else {
                String message;
                if (mContext.getPackageManager().resolveActivity(intent, 0) == null) {
                    new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle("提示")
                            .setMessage("是否允许下载？")
                            .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //实现下载功能，这里直接调用第三方打开去下载
                                    openBrowser(mContext, url);
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                    return true;
                } else {
                    if (onOpenThreeListener != null) {
                        onOpenThreeListener.showDialog(intent);//自定义弹框
                    } else {
                        message = "即将离开App，打开第三方应用";
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext).setIcon(android.R.drawable.ic_dialog_info)
                                .setTitle("提示")
                                .setMessage(message)
                                .setCancelable(false)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mContext.startActivity(intent);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        AlertDialog dialog = dialogBuilder.create();
                        dialog.show();
                        //弹框设置字体颜色
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#fe9949"));
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#333333"));
                    }
                }
            }
        } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
            if (url.startsWith("weixin://wap/pay")) {
                Toast.makeText(mContext, "请您先行下载“微信”APP", Toast.LENGTH_LONG);
            } else if (url.startsWith("alipays://platformapi/startapp")) {
                Toast.makeText(mContext, "请您先行下载“支付宝”APP", Toast.LENGTH_LONG);
            } else {
                Toast.makeText(mContext, "未安装相应的客户端", Toast.LENGTH_LONG);
            }
            LogUtils.d(e.getMessage());
            return true;//没有安装该app时，返回true，表示拦截自定义链接，但不跳转，避免弹出上面的错误页面
        }

        return true;
    }

    /**
     * 当接收到https错误时，会回调此函数，在其中可以做错误处理
     */
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();// 接受所有网站的证书
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
        private onOpenThreeListener onOpenThreeListener;

        public Builder setContext(Context mContext) {
            this.mContext = mContext;
            return this;
        }

        public Builder setOpenThreeListener(onOpenThreeListener onOpenThreeListener) {
            this.onOpenThreeListener = onOpenThreeListener;
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

    /**
     * 调用第三方浏览器打开
     *
     * @param context
     * @param url     要浏览的资源地址
     */
    public static void openBrowser(Context context, String url) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(intent, "请选择浏览器"));
        } else {
            Toast.makeText(context.getApplicationContext(), "请下载浏览器", Toast.LENGTH_SHORT).show();
        }
    }
}
