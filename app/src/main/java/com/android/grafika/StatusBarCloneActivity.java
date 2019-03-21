package com.android.grafika;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class StatusBarCloneActivity extends Activity {

  private static final String TAG = MainActivity.TAG;

  private Context mContext;
  private WindowManager mWindowManager;
  private static final List<View> sViewList = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mContext = getApplicationContext();
    mWindowManager = getWindowManager();

    setContentView(R.layout.activity_status_bar_clone);

    if (sViewList.size() == 0) {
      addClones();
    }
  }

  private void addClones() {
    for (int i = 0; i < 15 ; i++) {
      View view = LayoutInflater.from(mContext).inflate(R.layout.status_bar_clone, null);
      add(view, i+1);
    }
  }

  public void onCleanupClick(View clickView) {
    for (View view : sViewList) {
      WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
      Log.d(TAG, "Removing view from windowmanager: " + lp.getTitle());
      mWindowManager.removeView(view);
    }

    sViewList.clear();

    finish();
  }

  // Copied from AOSP: frameworks/base/packages/SystemUI/src/com/android/systemui/statusbar/phone/StatusBarWindowManager.java
  public void add(View statusBarView, int copyNum) {
    // Now that the status bar window encompasses the sliding panel and its
    // translucent backdrop, the entire thing is made TRANSLUCENT and is
    // hardware-accelerated.
    WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
        48 * copyNum,
        48 * copyNum,
        WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, // was TYPE_STATUS_BAR, but only system can use that
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        | WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING
        | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH
        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        | WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
        PixelFormat.TRANSLUCENT);
    lp.token = new Binder();
    lp.gravity = Gravity.TOP;
    lp.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
    lp.setTitle("Clone-" + copyNum);
    lp.packageName = mContext.getPackageName();

    Log.d(TAG, "Adding view to windowmanager: " + lp.getTitle());

    mWindowManager.addView(statusBarView, lp);
    sViewList.add(statusBarView);
  }

}
