package com.example.a_webview.components;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.a_webview.inter.onPhotoDialogListener;

import java.io.File;
import java.io.IOException;

import static com.example.a_webview.utils.FileUtils.createImageFile;

/**
 * 相册以及拍照
 */
public class PhotoWebChromeClient extends BaseWebChromeClient {
    public static final int IMG_CHOOSER_RESULT_CODE = 2097;
    public static final int TAKE_PIC_RESULT_CODE = 2098;
    public static ValueCallback<Uri> uriValueCallback;
    public static ValueCallback<Uri[]> valueCallbacks;
    public onPhotoDialogListener mListener;
    public static Uri mImageUri = null;
    public File file = null;
    public Context mContext;

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
        valueCallbacks = filePathCallback;
        showDialog();
        return true;
    }

    //4.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        uriValueCallback = uploadMsg;
        showDialog();

    }

    // 3.0 +
    @SuppressWarnings("static-access")
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {

        uriValueCallback = uploadMsg;
        showDialog();
    }


    // Android < 3.0
    @SuppressWarnings("static-access")
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        uriValueCallback = uploadMsg;
        showDialog();
    }

    /**
     * 调起弹框
     */
    public void showDialog() {
        int hasWriteStoragePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasCameraStoragePermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);
        if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED && hasCameraStoragePermission == PackageManager.PERMISSION_GRANTED) {
            mListener.showPhotoDialog(this);
        } else {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 200);
        }
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
    public void takePhoto() throws IOException {
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Toast.makeText(mContext, "设备无摄像头", Toast.LENGTH_SHORT).show();
            return;
        }
        ((Activity) mContext).startActivityForResult(createCameraIntent(),
                TAKE_PIC_RESULT_CODE);
    }


    /**
     * 相册intent
     */
    private Intent createDefaultOpenableIntent() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        return i;

    }

    /**
     * 拍照intent
     */
    @SuppressWarnings("static-access")
    private Intent createCameraIntent() throws IOException {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.mImageUri = getFileUri();
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        return cameraIntent;
    }


    /**
     * Uri获取 支持Android7.0
     */
    private Uri getFileUri() throws IOException {
        Uri imageUri = null;
        file = createImageFile(mContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//Android版本>=7.0
            try {
                imageUri = FileProvider.getUriForFile(mContext, mContext.getPackageName().concat(".provider"), file);
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
    public void update(Uri[] uris) {

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

}
