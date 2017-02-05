package moe.haruue.walkee.ui.base;

import android.util.Log;
import android.widget.Toast;

import moe.haruue.walkee.App;
import moe.haruue.walkee.BuildConfig;
import rx.Subscriber;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class BaseSubscriber<T> extends Subscriber<T> {

    private String tag;
    private String prefix;
    private static final boolean DEBUG = BuildConfig.DEBUG;

    public BaseSubscriber(String tag, String prefix) {
        this.tag = tag;
        this.prefix = prefix;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (DEBUG) {
            Toast.makeText(App.getInstance(), tag + ":" + prefix + "|onError:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.wtf(tag, prefix + "|onError: ", e);
        } else {
            Log.e(tag, prefix + "|onError: ", e);
        }
    }

    @Override
    public void onNext(T t) {

    }
}
