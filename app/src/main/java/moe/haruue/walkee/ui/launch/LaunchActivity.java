package moe.haruue.walkee.ui.launch;

import android.os.Bundle;
import android.support.annotation.Nullable;

import moe.haruue.walkee.ui.base.BaseActivity;
import moe.haruue.walkee.ui.main.MainActivity;
import moe.haruue.walkee.ui.introduce.IntroduceActivity;

/**
 * A shadow activity without interface.<br>
 *     Just for launch other activity such as {@link MainActivity} and {@link IntroduceActivity}
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.start(this);
        finish();
    }
}
