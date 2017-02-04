package moe.haruue.walkee;

import android.annotation.SuppressLint;
import android.app.Application;

import com.base.basepedo.base.BasePedoApplication;
import com.jude.utils.JUtils;

import moe.haruue.walkee.config.Const;
import moe.haruue.walkee.data.log.LogDatabase;
import moe.haruue.walkee.data.permission.PermissionDatabase;
import moe.haruue.walkee.model.User;
import moe.haruue.walkee.util.SPUtils;

/**
 * {@link Application} class for whole app
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class App extends BasePedoApplication {

    @SuppressLint("StaticFieldLeak")
    private static App instance;
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        JUtils.initialize(this);
        PermissionDatabase.initialize(this);
        LogDatabase.initialize(this);
        initializeUser();
    }

    public void initializeUser() {
        String username = (String) SPUtils.get(getApplicationContext(), Const.SPKEY_USERNAME, "");
        String avatar = (String) SPUtils.get(getApplicationContext(), Const.SPKEY_USER_AVATAR, "");
        user = new User(username, avatar);
    }

    public static App getInstance() {
        return instance;
    }

    public User getUser() {
        if (user == null) {
            initializeUser();
        }
        return user;
    }

    public void saveUser() {
        SPUtils.set(getApplicationContext(), Const.SPKEY_USERNAME, getUser().username);
        SPUtils.set(getApplicationContext(), Const.SPKEY_USER_AVATAR, getUser().avatar);
    }

    private void setInstance(App instance) {
        App.instance = instance;
    }
}
