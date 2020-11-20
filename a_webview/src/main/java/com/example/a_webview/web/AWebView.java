package com.example.a_webview.web;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.a_webview.components.PhotoWebChromeClient;


public class AWebView extends BaseWebView {

    public AWebView(@NonNull Context context) {
        super(context);
    }

    public AWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public AWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 权限
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //通过requestCode来识别是否同一个请求
        if (mBaseWebChromeClient instanceof PhotoWebChromeClient) {
            PhotoWebChromeClient mClient = (PhotoWebChromeClient) mBaseWebChromeClient;
            if (requestCode == 200) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mClient.mListener.showPhotoDialog(mClient);
                }
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //对相册回调的处理
        if (mBaseWebChromeClient instanceof PhotoWebChromeClient) {
            PhotoWebChromeClient mClient = (PhotoWebChromeClient) mBaseWebChromeClient;
            switch (requestCode) {
                case PhotoWebChromeClient.IMG_CHOOSER_RESULT_CODE://图库选择上传
                    Uri[] uris = new Uri[1];
                    uris[0] = intent.getData();
                    mClient.update(uris);
                    break;
                case PhotoWebChromeClient.TAKE_PIC_RESULT_CODE://拍照上传
                    Uri[] uris1 = new Uri[1];
                    uris1[0] = mClient.mImageUri;
                    mClient.update(uris1);
                    break;

            }
        }
    }
}
