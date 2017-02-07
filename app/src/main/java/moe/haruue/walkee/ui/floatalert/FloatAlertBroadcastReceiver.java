package moe.haruue.walkee.ui.floatalert;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import moe.haruue.walkee.App;
import moe.haruue.walkee.config.Const;
import moe.haruue.walkee.data.log.func.InsertLogFunc;
import moe.haruue.walkee.data.permission.func.CheckPermissionFunc;
import moe.haruue.walkee.util.ApplicationUtils;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class FloatAlertBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = ApplicationUtils.getForegroundApp(context);
        if (packageName == null || !CheckPermissionFunc.checkPermission(ApplicationUtils.getForegroundApp(context))) {
            if (!isServiceWork(context, FloatAlertService.class.getName()) && System.currentTimeMillis() - App.getInstance().unlock > Const.TIMEOUT_RELOCK_HARD) {
                FloatAlertService.start(context);
                new InsertLogFunc().call(0);
            }
        }
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

}
