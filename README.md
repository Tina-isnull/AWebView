# AWebView
webview封装

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
implementation 'com.github.Tina-isnull:AWebView:1.0.2'
```
使用
```
 @Override
    protected void onCreate(Bundle savedInstanceState) {
        //自定义导航条
        mTest = new ProgressViewTest(this);
        mTest.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 6));
        mTest.setDefaultColor(getResources().getColor(R.color.wsres_color_FE9949));
        //js调用android的方法
        mdata.add(new InterBean(new WebViewJS(), "android"));
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
                .setUsedDefaultProgress(true)//是否用默认的进度条
                .setProgressView(mTest)//自定义进度条
                .setList(mdata)
                .setOnTitleReceive(new onTitleReceive() {
                    @Override
                    public void onTitle(String title) {
                        mTitle.setText(title);
                    }
                })
                .setReShouldOverrideUrlLoading(new ReShouldOverrideUrlLoading() {//自定义拦截事件
                    @Override
                    public void InterceptProcess(WebView wv, String url) {
                        if (url.startsWith("wcar")) {
                            startActivity(new Intent(WebViewImlActivity.this, SecondActivity.class));
                        }
                    }
                })
                .getAWebViewWrapper();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mAWebView.canGoBack()) {
            mAWebView.goBack();//返回上个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);//退出H5界面
    }
    //照片回调
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

