package com.example.a_webview.components;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.a_webview.inter.onPhotoDialogListener;

import java.io.File;

import static com.example.a_webview.utils.FileUtils.getFilePath;

/**
 * 相册以及拍照
 */
public class PhotoWebChromeClient extends BaseWebChromeClient {
    public static final int IMG_CHOOSER_RESULT_CODE = 2097;
    public static final int TAKE_PIC_RESULT_CODE = 2098;
    public static ValueCallback<Uri> uriValueCallback;
    public static ValueCallback<Uri[]> valueCallbacks;
    private onPhotoDialogListener mListener;
    public static Uri mImageUri = null;
    private Context mContext;


    @SuppressWarnings("deprecation")
    public PhotoWebChromeClient(Context mContext) {
        this.mContext = mContext;
    }

    public void setListener(onPhotoDialogListener mListener) {
        this.mListener = mListener;
    }

    //5.0+
    @Override
    public boolean onShowFileChooser(WebView webView,
                                     ValueCallback<Uri[]> filePathCallback,
                                     WebChromeClient.FileChooserParams fileChooserParams) {
        // TODO 自动生成的方法存根
        valueCallbacks = filePathCallback;
        mListener.showPhotoDialog(this);
        return true;
    }

    //4.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        uriValueCallback = uploadMsg;
        mListener.showPhotoDialog(this);

    }

    // 3.0 +
    @SuppressWarnings("static-access")
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {

        uriValueCallback = uploadMsg;
        mListener.showPhotoDialog(this);
    }


    // Android < 3.0
    @SuppressWarnings("static-access")
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        uriValueCallback = uploadMsg;
        mListener.showPhotoDialog(this);
    }


    /**
     * 打开相册
     */
    public void openAlbum() {
        ((Activity) mContext).startActivityForResult(createDefaultOpenableIntent(),
                IMG_CHOOSER_RESULT_CODE);
    }

    /**
     * 拍照
     */
    public void takePhoto() {
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Toast.makeText(mContext, "设备无摄像头", Toast.LENGTH_SHORT).show();
            return;
        }
        ((Activity) mContext).startActivityForResult(createCameraIntent(),
                TAKE_PIC_RESULT_CODE);
    }

    /**
     * 取消调用
     */
    public void cancel() {
        if (valueCallbacks != null) {
            valueCallbacks.onReceiveValue(null);
            valueCallbacks = null;
        }
        if (uriValueCallback != null) {
            uriValueCallback.onReceiveValue(null);
            uriValueCallback = null;
        }
    }

    /**
     * 跳转选择相册界面
     */
    private Intent createDefaultOpenableIntent() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        return i;

    }

    /**
     * 调用系统相机拍照
     */
    @SuppressWarnings("static-access")
    private Intent createCameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.mImageUri = getFileUri();
        cameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        return cameraIntent;
    }


    /**
     * Uri获取 支持Android7.0
     */
    private Uri getFileUri() {
        Uri imageUri = null;
        File file = getFilePath();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//Android版本>=7.0
            try {
                imageUri = FileProvider.getUriForFile((mContext),
                        "com.example.awebview.provider", file);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            imageUri = Uri.fromFile(file);
        }
        return imageUri;
    }

    /**
     * 上传
     *
     * @param uris
     */
    public static void update(Uri[] uris) {

        if (valueCallbacks != null
                && uris[0] != null) {
            valueCallbacks.onReceiveValue(uris);
            valueCallbacks = null;
        }

        if (uriValueCallback != null
                && uris[0] != null) {
            uriValueCallback.onReceiveValue(uris[0]);
            uriValueCallback = null;
        }
    }


}
