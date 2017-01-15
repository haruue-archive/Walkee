package moe.haruue.walkee.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import moe.haruue.walkee.R;
import moe.haruue.walkee.ui.base.BaseActivity;
import moe.haruue.walkee.ui.mode.ModeChooseFragment;
import moe.haruue.walkee.ui.permission.PermissionSettingsFragment;
import moe.haruue.walkee.ui.status.CurrentStatusFragment;
import moe.haruue.walkee.ui.walklog.WalkLogFragment;
import moe.haruue.walkee.util.MarginViewUtils;

/**
 * A standard activity with a drawer frame
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class MainActivity extends BaseActivity {

    private DrawerLayout drawer;
    private NavigationView navigation;
    private FrameLayout contentView;
    private NavigationListener navigationListener = new NavigationListener();

    private CurrentStatusFragment currentStatusFragment = new CurrentStatusFragment();
    private ModeChooseFragment modeChooseFragment = new ModeChooseFragment();
    private PermissionSettingsFragment permissionSettingsFragment = new PermissionSettingsFragment();
    private WalkLogFragment walkLogFragment = new WalkLogFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        contentView = $(R.id.container);
        drawer = $(R.id.drawer_layout);
        navigation = $(R.id.navigation_view);
        navigation.setNavigationItemSelectedListener(navigationListener);
        // add a disabled view for window translucent design
        if (MarginViewUtils.isNeedNavigationMargin(this)) {
            navigation.getMenu().add("").setEnabled(false);
        }
        setFragment(currentStatusFragment);
    }

    private class NavigationListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            drawer.closeDrawer(GravityCompat.START);
            resetNavigationMenuItemChecked();
            item.setChecked(true);
            // operation
            switch (item.getItemId()) {
                case R.id.item_current_status:
                    setFragment(currentStatusFragment);
                    return true;
                case R.id.item_mode_choose:
                    setFragment(modeChooseFragment);
                    return true;
                case R.id.item_permission_settings:
                    setFragment(permissionSettingsFragment);
                    return true;
                case R.id.item_walk_log:
                    setFragment(walkLogFragment);
                    return true;
            }
            return false;
        }
    }

    public void setFragment(BaseFragmentInMainActivity fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.commit();
    }

    public void setNavigationViewHeaderUsername(CharSequence text) {
        TextView usernameTextView = (TextView) navigation.getHeaderView(0).findViewById(R.id.nh_username);
        usernameTextView.setText(text);
    }

    public ImageView getNavigationViewHeaderAvatarView() {
        return (ImageView) navigation.getHeaderView(0).findViewById(R.id.nh_avatar);
    }

    public void setNavigationViewHeaderAvatarOnClickListener(View.OnClickListener listener) {
        View header = navigation.getHeaderView(0);
        ImageView alpha = (ImageView) header.findViewById(R.id.nh_avatar_alpha);
        alpha.setOnClickListener(listener);
        if (listener == null) {
            alpha.setVisibility(View.INVISIBLE);
        } else {
            alpha.setVisibility(View.VISIBLE);
        }
    }

    public DrawerLayout getDrawerLayout() {
        return drawer;
    }

    public NavigationView getNavigationView() {
        return navigation;
    }

    public void setNavigationMenuItemChecked(@IdRes int itemId) {
        navigation.getMenu().findItem(itemId).setChecked(true);
    }

    private void resetNavigationMenuItemChecked() {
        for (int i = 0; true; i++) {
            try {
                navigation.getMenu().getItem(i).setChecked(false);
            } catch (Exception e) {
                break;
            }
        }
    }

    public void installToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> drawer.openDrawer(GravityCompat.START));
    }

    @Override
    public FrameLayout getContentView() {
        return contentView;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

}
