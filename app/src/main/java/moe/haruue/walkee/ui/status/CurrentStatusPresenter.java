package moe.haruue.walkee.ui.status;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.base.basepedo.config.Constant;
import com.base.basepedo.service.StepService;

import java.util.List;

import moe.haruue.walkee.App;
import moe.haruue.walkee.BuildConfig;
import moe.haruue.walkee.config.Const;
import moe.haruue.walkee.ui.base.BasePresenter;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

class CurrentStatusPresenter implements BasePresenter, Handler.Callback {

    public static final String TAG = "CurrentStatusP";
    public static final boolean DEBUG = BuildConfig.DEBUG;

    private CurrentStatusFragment fragment;

    CurrentStatusPresenter(CurrentStatusFragment fragment) {
        this.fragment = fragment;
    }

    private Handler handler;
    private Messenger messenger;

    private long lastStep = 0;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                messenger = new Messenger(service);
                Message msg = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                msg.replyTo = new Messenger(handler);
                messenger.send(msg);
            } catch (RemoteException e) {
                if (DEBUG) {
                    Toast.makeText(fragment.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.wtf(TAG, "onServiceConnected: ", e);
                } else {
                    Log.e(TAG, "onServiceConnected: ", e);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constant.MSG_FROM_SERVER:
                int step = msg.getData().getInt("step");
                if (App.getInstance().setCurrentStep(step)) {
                    lastStep = System.currentTimeMillis();
                    fragment.setStatusWalk();
                }
                handler.sendEmptyMessageDelayed(Constant.REQUEST_SERVER, Const.INTERVAL_QUERY_STEP);
                break;
            case Constant.REQUEST_SERVER:
                try {
                    Message msg1 = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                    msg1.replyTo = new Messenger(handler);
                    messenger.send(msg1);
                    if (System.currentTimeMillis() - lastStep >= Const.TIMEOUT_BACK_STAND) {
                        fragment.setStatusStand();
                    }
                } catch (RemoteException e) {
                    if (DEBUG) {
                        Toast.makeText(fragment.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.wtf(TAG, "onServiceConnected: ", e);
                    } else {
                        Log.e(TAG, "onServiceConnected: ", e);
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public void start() {
        handler = new Handler(Looper.getMainLooper(), this);
        startServiceForStrategy();
    }

    public void stop() {
        fragment.getMainActivity().unbindService(connection);
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    /**
     * 启动service
     *
     * @param flag true-bind和start两种方式一起执行 false-只执行bind方式
     */
    private void setupService(boolean flag) {
        Intent intent = new Intent(fragment.getMainActivity(), StepService.class);
        fragment.getMainActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
        if (flag) {
            fragment.getMainActivity().startService(intent);
        }
    }

    private void startServiceForStrategy() {
        if (!isServiceWork(fragment.getMainActivity(), StepService.class.getName())) {
            setupService(true);
        } else {
            setupService(false);
        }
    }

}
