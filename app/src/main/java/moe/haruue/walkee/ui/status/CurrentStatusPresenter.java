package moe.haruue.walkee.ui.status;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.base.basepedo.config.Constant;
import com.base.basepedo.service.StepService;

import java.util.List;

import moe.haruue.walkee.BuildConfig;
import moe.haruue.walkee.config.Const;
import moe.haruue.walkee.ui.base.BasePresenter;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

class CurrentStatusPresenter implements BasePresenter, Handler.Callback {

    public static final String TAG = "CurrentStatusP";
    public static final boolean DEBUG = BuildConfig.DEBUG;

    public static final int MSG_BACK_STAND = 9347823;

    private CurrentStatusFragment fragment;

    CurrentStatusPresenter(CurrentStatusFragment fragment) {
        this.fragment = fragment;
    }

    private Handler handler;

    private long lastStep = 0;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constant.ACTION_PER_STEP.equals(intent.getAction())) {
                lastStep = System.currentTimeMillis();
                fragment.setStatusWalk();
            }
        }
    };

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_BACK_STAND:
                if (System.currentTimeMillis() - lastStep > Const.TIMEOUT_BACK_STAND) {
                    fragment.setStatusStand();
                }
                handler.sendEmptyMessageDelayed(MSG_BACK_STAND, Const.INTERVAL);
        }
        return false;
    }

    @Override
    public void start() {
        handler = new Handler(Looper.getMainLooper(), this);
        handler.sendEmptyMessage(MSG_BACK_STAND);
        startServiceForStrategy();
        fragment.getMainActivity().registerReceiver(receiver, new IntentFilter(Constant.ACTION_PER_STEP));
    }

    public void stop() {
        fragment.getMainActivity().unregisterReceiver(receiver);
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

    private void startServiceForStrategy() {
        if (!isServiceWork(fragment.getMainActivity(), StepService.class.getName())) {
            Intent intent = new Intent(fragment.getMainActivity(), StepService.class);
            fragment.getMainActivity().startService(intent);
        }
    }

}
