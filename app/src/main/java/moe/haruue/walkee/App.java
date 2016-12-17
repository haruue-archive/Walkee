package moe.haruue.walkee;

import android.annotation.SuppressLint;
import android.app.Application;

import com.jude.utils.JUtils;

/**
 * {@link Application} class for whole app
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        JUtils.initialize(this);
    }

    public static App getInstance() {
        return instance;
    }

    private void setInstance(App instance) {
        App.instance = instance;
    }
}
