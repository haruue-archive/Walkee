package moe.haruue.walkee.ui.status;

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

import com.base.basepedo.config.Constant;
import com.base.basepedo.service.StepService;

import java.util.List;

import moe.haruue.walkee.BuildConfig;
import moe.haruue.walkee.config.Const;
import moe.haruue.walkee.data.log.func.QueryAllLogFunc;
import moe.haruue.walkee.ui.base.BasePresenter;
import moe.haruue.walkee.ui.base.BaseSubscriber;
import moe.haruue.walkee.ui.widget.StatisticsBarGraph;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static moe.haruue.walkee.util.ApplicationUtils.isServiceWork;

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

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (name.getClassName().equals(StepService.class.getName())) {
                Messenger initMessenger = new Messenger(service);
                Messenger replyMessenger = new Messenger(handler);
                Message initMsg = Message.obtain();
                initMsg.what = Constant.MSG_INITIALIZE_PER_STEP_CALLBACK;
                initMsg.replyTo = replyMessenger;
                try {
                    initMessenger.send(initMsg);
                } catch (RemoteException e) {
                    Log.e(TAG, "onServiceConnected: can't send per step callback init msg, connect failed", e);
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
            case MSG_BACK_STAND:
                if (System.currentTimeMillis() - lastStep > Const.TIMEOUT_BACK_STAND) {
                    fragment.setStatusStand();
                }
                handler.sendEmptyMessageDelayed(MSG_BACK_STAND, Const.INTERVAL);
                return true;
            case Constant.MSG_PER_STEP:
                lastStep = System.currentTimeMillis();
                fragment.setStatusWalk();
                return false;
        }
        return false;
    }

    @Override
    public void start() {
        handler = new Handler(Looper.getMainLooper(), this);
        handler.sendEmptyMessage(MSG_BACK_STAND);
        startServiceForStrategy();
    }

    public void stop() {
        fragment.getMainActivity().unbindService(conn);
    }

    public void requestTimeStatisticsData() {
        Observable.just(0)
                .map(new QueryAllLogFunc())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<List<StatisticsBarGraph.Item>>(TAG, "requestTimeStatisticsData") {
                    @Override
                    public void onNext(List<StatisticsBarGraph.Item> items) {
                        fragment.onLoadTimeStatisticsGraphs(items);
                    }
                });
    }

    private void startServiceForStrategy() {
        Intent intent = new Intent(fragment.getMainActivity(), StepService.class);
        if (!isServiceWork(fragment.getMainActivity(), StepService.class.getName())) {
            fragment.getMainActivity().startService(intent);
        }
        fragment.getMainActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

}
