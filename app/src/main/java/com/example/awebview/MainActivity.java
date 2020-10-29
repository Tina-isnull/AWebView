package com.example.awebview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.a_webview.utils.FileUtils;
import com.example.a_webview.utils.LogUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public static final String URLTAG = "myUrl";
    Button mBtn;
    Button mDang;
    Button mDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn = findViewById(R.id.btn_id);
        mDelete = findViewById(R.id.btn_id_delete);
        mDang = findViewById(R.id.dang_id);
        getApplicationContext();
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity.this, WebViewImlActivity.class);
                mIntent.putExtra(URLTAG, "file:///android_asset/index.html");
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
    }

}