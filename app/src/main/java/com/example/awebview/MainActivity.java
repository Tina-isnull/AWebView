package com.example.awebview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.a_webview.components.PhotoWebChromeClient;
import com.example.a_webview.utils.FileUtils;
import com.example.a_webview.utils.LogUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public static final String URLTAG = "myUrl";
    Button mBtn;
    Button mDang;
    Button mDelete;
    ImageView mPic;
    Button mBtnPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn = findViewById(R.id.btn_id);
        mDelete = findViewById(R.id.btn_id_delete);
        mDang = findViewById(R.id.dang_id);
        mPic = findViewById(R.id.img_show_pic);
        mBtnPic = findViewById(R.id.btn_take_photo);
        getApplicationContext();


        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity.this, WebViewImlActivity.class);
//                mIntent.putExtra(URLTAG, "file:///android_asset/index.html");
                mIntent.putExtra(URLTAG, "https://www.baidu.com/?tn=80035161_1_dg");
                startActivity(mIntent);
            }
        });
        mDang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity.this, WebViewImlActivity.class);
                mIntent.putExtra(URLTAG, "http://www.dangdang.com/");
                startActivity(mIntent);
            }
        });
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = getCacheDir().getAbsoluteFile();//删除缓存
                LogUtils.d("路径" + file.getPath());
                FileUtils.deleteFile(file);
            }
        });
        mBtnPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File file = new File(storageDir,"20201120_142850.jpg");
//                Uri uri = Uri.fromFile(file);
                mPic.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
//                mPic.setImageURI(uri);

            }
        });

    }

}