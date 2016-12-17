package moe.haruue.walkee.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import moe.haruue.walkee.R;
import moe.haruue.walkee.ui.base.AbstractDrawerActivity;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class MainActivity extends AbstractDrawerActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbarTitle(getString(R.string.app_name));
        setNavigationMenuItemChecked(R.id.item_current_status);

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }
}
