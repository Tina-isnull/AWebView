package com.example.a_webview.web;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.a_webview.components.PhotoWebChromeClient;

import static android.app.Activity.RESULT_OK;

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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //对相册回调的处理
        if (resultCode == RESULT_OK) {
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
}
