package moe.haruue.walkee.ui.floatalert;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.base.basepedo.config.Constant;

import moe.haruue.walkee.App;
import moe.haruue.walkee.BuildConfig;
import moe.haruue.walkee.R;
import moe.haruue.walkee.config.Const;
import moe.haruue.walkee.util.SPUtils;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class FloatAlertService extends Service implements Handler.Callback {

    public static final String TAG = "FloatAlertService";
    public static final boolean DEBUG = BuildConfig.DEBUG;

    private View alertView;
    private WindowManager.LayoutParams alertParams;
    private WindowManager manager;

    private Handler handler;
    private long lastStep = 0;
    private long start = 0;
    private int mode;

    public static final int MSG_CHECK_STOP = 93190719;
    public static final int SHOW_ALERT_BAR = 0;
    public static final int SHOW_FULLSCREEN_HARD_LOCK = 1;
    public static final int SHOW_FULLSCREEN_HARD_UNLOCK = 2;
    public static final int SHOW_EMPTY = 3;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constant.ACTION_PER_STEP.equals(intent.getAction())) {
                lastStep = System.currentTimeMillis();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mode = (int) SPUtils.get(getApplicationContext(), Const.SPKEY_MODE, Const.MODE_EASY);
        Log.v(TAG, "mode=" + mode);
        start = System.currentTimeMillis();
        lastStep = System.currentTimeMillis();
        registerReceiver(receiver, new IntentFilter(Constant.ACTION_PER_STEP));
        handler = new Handler(Looper.getMainLooper(), this);
        handler.sendEmptyMessage(MSG_CHECK_STOP);
    }

    private void refreshFloatAlert() {
        try {
            getWindowManager().removeView(getAlertView());
        } catch (Exception ignored) {}
        try {
            if (getAlertView().getParent() == null) {
                getWindowManager().addView(getAlertView(), getAlertParam());
            }
        } catch (Exception ignored) {}
    }

    private void hideFloatAlert() {
        try {
            if (getAlertView().getParent() != null) {
                getWindowManager().removeView(getAlertView());
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        hideFloatAlert();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_CHECK_STOP:
                if (System.currentTimeMillis() - lastStep > Const.TIMEOUT_BACK_STAND_LONG) {
                    stopSelf();
                } else {
                    refreshFloatAlert();
                    handler.sendEmptyMessageDelayed(MSG_CHECK_STOP, Const.INTERVAL);
                }
                return true;
        }
        return false;
    }

    public WindowManager getWindowManager() {
        if (manager == null) {
            manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        }
        return manager;
    }

    public View getAlertView() {
        switch (getShow()) {
            case SHOW_ALERT_BAR:
                if (alertView == null || !(alertView instanceof FloatAlertBarView)) {
                    alertView = new FloatAlertBarView(getApplicationContext());
                }
                break;
            case SHOW_FULLSCREEN_HARD_LOCK:
                if (alertView == null || !(alertView instanceof FloatFullscreenLockView)) {
                    alertView = new FloatFullscreenLockView(getApplicationContext());
                }
                break;
            case SHOW_FULLSCREEN_HARD_UNLOCK:
                if (alertView == null || !(alertView instanceof FloatFullscreenUnlockView)) {
                    alertView = new FloatFullscreenUnlockView(getApplicationContext());
                    View unlockButton = alertView.findViewById(R.id.bt_fullscreen_unlock_unlock);
                    unlockButton.setOnClickListener(v -> App.getInstance().unlock = System.currentTimeMillis());
                }
                break;
            case SHOW_EMPTY:
                if (alertView == null) {
                    alertView = new View(getApplicationContext());
                }
                break;
        }
        return alertView;
    }

    public WindowManager.LayoutParams getAlertParam() {
        if (alertParams == null) {
            alertParams = new WindowManager.LayoutParams();
        }
        switch (getShow()) {
            case SHOW_ALERT_BAR:
                alertParams.x = 0;
                alertParams.y = 0;
                alertParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                alertParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                alertParams.gravity = Gravity.TOP;
                alertParams.format = PixelFormat.TRANSPARENT;
                alertParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
                alertParams.type = WindowManager.LayoutParams.TYPE_TOAST;
                break;
            case SHOW_FULLSCREEN_HARD_LOCK:
            case SHOW_FULLSCREEN_HARD_UNLOCK:
                alertParams.x = 0;
                alertParams.y = 0;
                alertParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                alertParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                alertParams.gravity = Gravity.CENTER;
                alertParams.format = PixelFormat.TRANSPARENT;
                alertParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
                    alertParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                } else {
                    alertParams.type = WindowManager.LayoutParams.TYPE_TOAST;
                }
                break;
            case SHOW_EMPTY:
                alertParams.x = 0;
                alertParams.y = 0;
                alertParams.height = 0;
                alertParams.width = 0;
                alertParams.gravity = Gravity.TOP;
                alertParams.format = PixelFormat.TRANSPARENT;
                alertParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
                alertParams.type = WindowManager.LayoutParams.TYPE_TOAST;
                break;
        }
        return alertParams;
    }

    public int getShow() {
        int show;
        if (mode == Const.MODE_EASY) {
            show = SHOW_ALERT_BAR;
        } else if (mode == Const.MODE_HARD && System.currentTimeMillis() - App.getInstance().unlock > Const.TIMEOUT_RELOCK_HARD) {
            if (System.currentTimeMillis() - start < Const.TIMEOUT_UNLOCK_HARD) {
                show = SHOW_FULLSCREEN_HARD_LOCK;
            } else {
                show = SHOW_FULLSCREEN_HARD_UNLOCK;
            }
        } else {
            show = SHOW_EMPTY;
        }
        Log.v(TAG, "unlock=" + App.getInstance().unlock);
        Log.v(TAG, "to=" + (System.currentTimeMillis() - App.getInstance().unlock));
        return show;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, FloatAlertService.class);
        context.startService(starter);
    }
}
