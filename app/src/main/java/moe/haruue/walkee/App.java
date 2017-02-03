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
    private int currentStep = 0;

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

    /**
     * store the current step to application instance, and get whether user is walking
     * @param currentStep the current step returned from {@link com.base.basepedo.service.StepService}
     * @return is walking
     */
    public boolean setCurrentStep(int currentStep) {
        if (this.currentStep == 0) {
            this.currentStep = currentStep;
            return false;
        } else {
            if (currentStep > this.currentStep) {
                this.currentStep = currentStep;
                return true;
            } else {
                this.currentStep = currentStep;
                return false;
            }
        }
    }

    public void saveUser() {
        SPUtils.set(getApplicationContext(), Const.SPKEY_USERNAME, getUser().username);
        SPUtils.set(getApplicationContext(), Const.SPKEY_USER_AVATAR, getUser().avatar);
    }

    private void setInstance(App instance) {
        App.instance = instance;
    }
}
