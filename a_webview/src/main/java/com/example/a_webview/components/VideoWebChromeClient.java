package com.example.a_webview.components;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * 视频全屏播放
 * 记得在调用的Activity中设置 android:configChanges="keyboardHidden|orientation|screenSize"
 */
public class VideoWebChromeClient extends PhotoWebChromeClient {
    private Context mContext;
    private View customView;
    private FrameLayout fullscreenContainer;
    private CustomViewCallback customViewCallback;
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    public VideoWebChromeClient(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    @Override
    public View getVideoLoadingProgressView() {
        FrameLayout frameLayout = new FrameLayout(mContext);
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        return frameLayout;
    }

    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        showCustomView(view, callback);
    }

    @Override
    public void onHideCustomView() {
        hideCustomView();
    }


    /**
     * 视频播放全屏
     */
    private void showCustomView(View view, CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }
        ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        FrameLayout decor = (FrameLayout) ((Activity) mContext).getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(mContext);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        customViewCallback = callback;
    }

    /**
     * 隐藏视频全屏
     */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }
        ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) ((Activity) mContext).getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
    }

    /**
     * 全屏容器界面
     */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        ((Activity) mContext).getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


}
