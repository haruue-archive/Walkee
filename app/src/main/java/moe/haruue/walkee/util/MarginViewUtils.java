package moe.haruue.walkee.util;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import moe.haruue.walkee.BuildConfig;

/**
 * The utils for window translucent design of Android
 * @see <a href="http://haruue.moe/blog/2016/09/07/simple-android-window-translucent-ui-design/">android window translucent ui design</a>
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class MarginViewUtils {

    public static final String TAG = "MarginViewUtils";
    public static final boolean DEBUG = BuildConfig.DEBUG;

    public static boolean hasSoftKeys(Context context){
        boolean hasSoftwareKeys;
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            Display d = manager.getDefaultDisplay();

            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);

            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);

            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;

            hasSoftwareKeys =  (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
        } else {
            boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            hasSoftwareKeys = !hasMenuKey && !hasBackKey;
        }
        return hasSoftwareKeys;
    }

    public static boolean isScreenOrientationPortrait(Context context) {
        Configuration config = context.getResources().getConfiguration();
        return config.orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean isNeedNavigationMargin(Context context) {
        boolean result = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && MarginViewUtils.hasSoftKeys(context) && MarginViewUtils.isScreenOrientationPortrait(context);
        if (DEBUG) {
            Log.d(TAG, "isNeedNavigationMargin: " + result);
        }
        return result;
    }

    public static boolean isNeedStatusBarMargin(Context context) {
        boolean result = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (DEBUG) {
            Log.d(TAG, "isNeedStatusBarMargin: " + result);
        }
        return result;
    }

}
