package com.base.basepedo.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.base.basepedo.base.StepMode;
import com.base.basepedo.callback.StepCallBack;
import com.base.basepedo.config.Constant;
import com.base.basepedo.pojo.StepData;
import com.base.basepedo.utils.CountDownTimer;
import com.base.basepedo.utils.DbUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import moe.haruue.walkee.App;
import moe.haruue.walkee.config.Const;
import moe.haruue.walkee.data.log.func.InsertLogFunc;
import moe.haruue.walkee.data.permission.func.CheckPermissionFunc;
import moe.haruue.walkee.ui.floatalert.FloatAlertService;
import moe.haruue.walkee.util.ApplicationUtils;
import moe.haruue.walkee.util.LogTimestampUtils;

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class StepService extends Service implements /*SensorEventListener,*/ StepCallBack {
    private final String TAG = "StepService";
    public static final String DB_NAME_BASEPEDO = "basepedo";
    //默认为30秒进行一次存储
    private static int duration = 30000;
    private Messenger messenger = new Messenger(new MessenerHandler());
    private BroadcastReceiver mBatInfoReceiver;
    private WakeLock mWakeLock;
    private TimeCount time;
    //当天的日期
    private String CURRENTDATE = "";

    private final Set<Messenger> perStepCallbackMessengers = new HashSet<>();

    private class MessenerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.MSG_FROM_CLIENT:
                    try {
                        Messenger messenger = msg.replyTo;
                        Message replyMsg = Message.obtain(null, Constant.MSG_FROM_SERVER);
                        Bundle bundle = new Bundle();
                        bundle.putInt("step", StepMode.CURRENT_SETP);
                        replyMsg.setData(bundle);
                        messenger.send(replyMsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case Constant.MSG_INITIALIZE_PER_STEP_CALLBACK:
                    perStepCallbackMessengers.add(msg.replyTo);
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
        initBroadcastReceiver();
        startStep();
        startTimeCount();
    }

    private void initBroadcastReceiver() {
        Log.v(TAG, "initBroadcastReceiver");
        final IntentFilter filter = new IntentFilter();
        // 屏幕灭屏广播
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //日期修改
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        //关机广播
        filter.addAction(Intent.ACTION_SHUTDOWN);
        // 屏幕亮屏广播
        filter.addAction(Intent.ACTION_SCREEN_ON);
        // 屏幕解锁广播
        filter.addAction(Intent.ACTION_USER_PRESENT);
        // 当长按电源键弹出“关机”对话或者锁屏时系统会发出这个广播
        // example：有时候会用到系统对话框，权限可能很高，会覆盖在锁屏界面或者“关机”对话框之上，
        // 所以监听这个广播，当收到时就隐藏自己的对话，如点击pad右下角部分弹出的对话框
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        mBatInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                String action = intent.getAction();

                if (Intent.ACTION_SCREEN_ON.equals(action)) {
                    Log.v(TAG, "screen on");
                } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    Log.v(TAG, "screen off");
                    //改为60秒一存储
                    duration = 60000;
                } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                    Log.v(TAG, "screen unlock");
                    save();
                    //改为30秒一存储
                    duration = 30000;
                } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction())) {
                    Log.v(TAG, " receive Intent.ACTION_CLOSE_SYSTEM_DIALOGS");
                    //保存一次
                    save();
                } else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
                    Log.v(TAG, " receive ACTION_SHUTDOWN");
                    save();
                } else if (Intent.ACTION_DATE_CHANGED.equals(intent.getAction())) {
                    Log.v(TAG, " receive ACTION_DATE_CHANGED");
                    initTodayData();
                    clearStepData();
                    Log.v(TAG, "归零数据："+StepMode.CURRENT_SETP);
                    Step(StepMode.CURRENT_SETP);
                }
            }
        };
        registerReceiver(mBatInfoReceiver, filter);
    }

    private void startStep() {
        StepMode mode = new StepInPedometer(this, this);
        boolean isAvailable = mode.getStep();
        Log.v(TAG, "startStep1");
        if (!isAvailable) {
            mode = new StepInAcceleration(this, this);
            isAvailable = mode.getStep();
            if (isAvailable) {
                Log.v(TAG, "acceleration can execute!");
            }
        }
    }

    private void startTimeCount() {
        time = new TimeCount(duration, 1000);
        time.start();
    }

    @Override
    public void Step(int stepNum) {
        StepMode.CURRENT_SETP = stepNum;
        Log.v(TAG, "Step:" + stepNum);
        checkAndLog();
        callPerStepMessengers();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initTodayData();
        return START_STICKY;
    }

    private void initTodayData() {
        CURRENTDATE = getTodayDate();
        DbUtils.createDb(this, DB_NAME_BASEPEDO);
        //获取当天的数据，用于展示
        List<StepData> list = DbUtils.getQueryByWhere(StepData.class, "today", new String[]{CURRENTDATE});
        if (list.size() == 0 || list.isEmpty()) {
            StepMode.CURRENT_SETP = 0;
        } else if (list.size() == 1) {
            StepMode.CURRENT_SETP = Integer.parseInt(list.get(0).getStep());
        } else {
            Log.v(TAG, "It's wrong！");
        }
    }

    private String getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 如果计时器正常结束，则开始计步
            time.cancel();
            save();
            startTimeCount();
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }

    private void save() {
        int tempStep = StepMode.CURRENT_SETP;
        List<StepData> list = DbUtils.getQueryByWhere(StepData.class, "today", new String[]{CURRENTDATE});
        if (list.size() == 0 || list.isEmpty()) {
            StepData data = new StepData();
            data.setToday(CURRENTDATE);
            data.setStep(tempStep + "");
            DbUtils.insert(data);
        } else if (list.size() == 1) {
            StepData data = list.get(0);
            data.setStep(tempStep + "");
            DbUtils.update(data);
        } else {
        }
    }

    private void clearStepData() {
        StepMode.CURRENT_SETP = 0;
    }

    @Override
    public void onDestroy() {
        //取消前台进程
        stopForeground(true);
        DbUtils.closeDb();
        unregisterReceiver(mBatInfoReceiver);
        Intent intent = new Intent(this, StepService.class);
        startService(intent);
        super.onDestroy();
    }

    public void callPerStepMessengers() {
        Set<Messenger> unlessMessengers = new HashSet<>();
        synchronized (perStepCallbackMessengers) {
            for (Messenger m : perStepCallbackMessengers) {
                try {
                    Message perStepMsg = Message.obtain();
                    perStepMsg.what = Constant.MSG_PER_STEP;
                    m.send(perStepMsg);
                } catch (RemoteException e) {
                    Log.w(TAG, "callPerStepMessengers: callback throw a exception and will be removed", e);
                    unlessMessengers.add(m);
                }
            }
            perStepCallbackMessengers.removeAll(unlessMessengers);
        }
    }

//    private  void unlock(){
//        setLockPatternEnabled(android.provider.Settings.Secure.LOCK_PATTERN_ENABLED,false);
//    }
//
//    private void setLockPatternEnabled(String systemSettingKey, boolean enabled) {
//        //推荐使用
//        android.provider.Settings.Secure.putInt(getContentResolver(), systemSettingKey,enabled ? 1 : 0);
//    }

    public void checkAndLog() {
        LogTimestampUtils.refreshLastStepTimestamp(this);
        String packageName = ApplicationUtils.getForegroundApp(this);
        if (packageName == null || !CheckPermissionFunc.checkPermission(ApplicationUtils.getForegroundApp(this))) {
            if (!ApplicationUtils.isServiceWork(this, new ComponentName(this, FloatAlertService.class).getClassName()) && System.currentTimeMillis() - App.getInstance().unlock > Const.TIMEOUT_RELOCK_HARD) {
                FloatAlertService.start(this);
                new InsertLogFunc().call(0);
            }
        }
    }

    synchronized private PowerManager.WakeLock getLock(Context context) {
        if (mWakeLock != null) {
            if (mWakeLock.isHeld())
                mWakeLock.release();
            mWakeLock = null;
        }

        if (mWakeLock == null) {
            PowerManager mgr = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    StepService.class.getName());
            mWakeLock.setReferenceCounted(true);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            int hour = c.get(Calendar.HOUR_OF_DAY);
            if (hour >= 23 || hour <= 6) {
                mWakeLock.acquire(5000);
            } else {
                mWakeLock.acquire(300000);
            }
        }
        return (mWakeLock);
    }
}
