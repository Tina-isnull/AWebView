package com.example.awebview;

import android.Manifest;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.a_webview.components.PhotoWebChromeClient;
import com.example.a_webview.inter.onPhotoDialogListener;
import com.example.a_webview.web.AWebView;
import com.example.a_webview.web.AWebViewWrapper;
import com.example.a_webview.bean.InterBean;
import com.example.a_webview.inter.ReShouldOverrideUrlLoadListener;
import com.example.a_webview.inter.onTitleReceiveListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;


import static com.example.awebview.MainActivity.URLTAG;

//        mAWebView.loadUrl("file:///android_asset/index.html");
//        mAWebView.loadUrl("https://open.czb365.com/redirection/todo?platformType=92651662&platformCode=15110237585");
//        mAWebView.loadUrl("http://www.dangdang.com/");
public class WebViewImlActivity extends AppCompatActivity {
    TextView mTitle;
    AWebView mAWebView;
    String mUrl;
    ProgressViewTest mTest;
    Button mClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_iml);
        mAWebView = findViewById(R.id.aWebview);
        mTitle = findViewById(R.id.web_title);
        mClick = findViewById(R.id.js_click);
        mUrl = getIntent().getStringExtra(URLTAG);
        //自定义导航条
        mTest = new ProgressViewTest(this);
        mTest.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 6));
        mTest.setDefaultColor(getResources().getColor(R.color.wsres_color_FE9949));
        //js调用android的方法
        mAWebView.addJavascriptInterface(new WebViewJS(), "android");
        //android调用js方法
        mClick.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                mAWebView.evaluateJavascriptUrl("javatojscallback", new Object[]{"你管我", "哼哼哼"});
            }
        });

        AWebViewWrapper.createBuilder()
                .setContext(this)
                .setWebView(mAWebView)
                .setUrl(mUrl)
                .setIsHaveProgress(true)//设置是否有进度条
                .setProgressView(mTest)
                .setOnTitleReceive(new onTitleReceiveListener() {
                    @Override
                    public void onTitle(String title) {
                        mTitle.setText(title);
                    }
                })
                .setPhotoDialogListener(new onPhotoDialogListener() {
                    @Override
                    public void showPhotoDialog(PhotoWebChromeClient mClient) {
                        select(mClient);
                    }
                })
                .setReShouldOverrideUrlLoading(new ReShouldOverrideUrlLoadListener() {
                    @Override
                    public boolean interceptProcess(WebView wv, String url) {
                        if (url.startsWith("http://ditu.amap.com") || url.startsWith("https://ditu.amap.com")) {//高德地图
                            //拦截地图导航，不继续走下去(车生活中，车主帮需要拦截)
                            return true;
                        } else if (url.equals("https://djm-dev.wsecar.com/drivingManager/driving/order/getEDJH5PayOrderSuccess")) {
                            //e代驾支付成功，点击返回，回到订单详情已支付页面。
                            finish();
                            return true;
                        } else if (url.startsWith("wsjc://")) {
                            //本APP内部链接跳转
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .getAWebViewWrapper();

    }


    /**
     * 跳转哪个选择
     */
    public void select(final PhotoWebChromeClient mClient) {
        //拥有权限，执行操作
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mClient.cancel();
            }
        });
        dialog.setTitle("相册还是拍照");
        dialog.setNegativeButton("相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mClient.openAlbum();
            }
        });
        dialog.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    mClient.takePhoto();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        dialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mAWebView.canGoBack()) {
            mAWebView.goBack();//返回上个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);//退出H5界面
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mAWebView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAWebView.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAWebView.onDestroy();
    }
}