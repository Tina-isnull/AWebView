# AWebView
webview封装,暂时没有jsbridge

配置

1.build.gradle(project)中添加

```
repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
```
2.build.gradle(app)中添加
```
implementation 'com.github.Tina-isnull:AWebView:1.0.5'
```
使用
```
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
//                .setReShouldOverrideUrlLoading(new ReShouldOverrideUrlLoadListener() {
//                    @Override
//                    public void interceptProcess(WebView wv, String url) {
//                       if (url.startsWith("ccc://")) {
                            //本APP内部链接跳转
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                            return true;
                        } else {
                            return false;
                        }
//                    }
//                })
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
```

