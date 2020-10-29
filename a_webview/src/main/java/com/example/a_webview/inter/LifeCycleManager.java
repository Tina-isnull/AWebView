package com.example.a_webview.inter;

import android.content.Intent;

public interface LifeCycleManager {

    void onResume();

    void onPause();

    void onDestroy();

    void onActivityResult(final int requestCode, final int resultCode, final Intent intent);
}
