package moe.haruue.walkee.util;

import android.content.Context;

import moe.haruue.walkee.config.Const;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class LogTimestampUtils {

    public static void refreshLastLogTimestamp(Context context) {
        SPUtils.set(context, Const.SPKEY_LAST_LOG_TIMESTAMP, System.currentTimeMillis());
    }

    public static void refreshLastStepTimestamp(Context context) {
        SPUtils.set(context, Const.SPKEY_LAST_STEP_TIMESTAMP, System.currentTimeMillis());
    }

}
