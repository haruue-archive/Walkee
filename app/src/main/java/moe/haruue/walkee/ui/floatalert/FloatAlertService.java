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
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.base.basepedo.config.Constant;

import moe.haruue.walkee.BuildConfig;
import moe.haruue.walkee.config.Const;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class FloatAlertService extends Service implements Handler.Callback {

    public static final String TAG = "FloatAlertService";
    public static final boolean DEBUG = BuildConfig.DEBUG;

    private FloatAlertView alertView;
    private WindowManager manager;

    private Handler handler;
    private long lastStep = 0;

    public static final int MSG_CHECK_STOP = 93190719;

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
        lastStep = System.currentTimeMillis();
        registerReceiver(receiver, new IntentFilter(Constant.ACTION_PER_STEP));
        handler = new Handler(Looper.getMainLooper(), this);
        handler.sendEmptyMessage(MSG_CHECK_STOP);
    }

    private void showFloatAlert() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.x = 0;
        params.y = 0;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.TOP;
        params.format = PixelFormat.TRANSPARENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            // remove it before add it on Android 7.1.1, otherwise the view will be auto hidden by system
            // @see aosp/frameworks/base/services/core/java/com/android/server/wm/WindowManagerService.java:2102
            try {
                getWindowManager().removeView(getAlertView());
            } catch (Exception ignored) {}
        }
        try {
            if (getAlertView().getParent() == null) {
                getWindowManager().addView(getAlertView(), params);
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
                    showFloatAlert();
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
        if (alertView == null) {
            alertView = new FloatAlertView(getApplicationContext());
        }
        return alertView;
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
