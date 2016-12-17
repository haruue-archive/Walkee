package moe.haruue.walkee.ui.base;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import moe.haruue.walkee.R;
import moe.haruue.walkee.util.MarginViewUtils;

/**
 * A standard activity with a drawer frame
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public abstract class AbstractDrawerActivity extends BaseActivity {

    private Toolbar toolbar;
    private AppBarLayout appbar;
    private DrawerLayout drawer;
    private NavigationView navigation;
    private FrameLayout contentView;
    private NavigationListener navigationListener = new NavigationListener();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_abstract_drawer);
        contentView = $(R.id.container);
        toolbar = $(R.id.toolbar);
        drawer = $(R.id.drawer_layout);
        navigation = $(R.id.navigation_view);
        appbar = $(R.id.app_bar_layout);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> drawer.openDrawer(GravityCompat.START));
        navigation.setNavigationItemSelectedListener(navigationListener);
        // add a disabled view for window translucent design
        if (MarginViewUtils.isNeedNavigationMargin(this)) {
            navigation.getMenu().add("").setEnabled(false);
        }
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
                    return true;
                case R.id.item_mode_choose:
                    return true;
                case R.id.item_permission_settings:
                    return true;
                case R.id.item_walk_log:
                    return true;
                case R.id.item_account_center:
                    return true;
            }
            return false;
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        contentView.removeAllViews();
        getLayoutInflater().inflate(layoutResID, contentView, true);
    }

    public void setToolbarTitle(CharSequence text) {
        toolbar.setTitle(text);
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

    public Toolbar getToolbar() {
        return toolbar;
    }

    public AppBarLayout getAppBarLayout() {
        return appbar;
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

    public void resetNavigationMenuItemChecked() {
        for (int i = 0; true; i++) {
            try {
                navigation.getMenu().getItem(i).setChecked(false);
            } catch (Exception e) {
                break;
            }
        }
    }

    @Override
    public FrameLayout getContentView() {
        return contentView;
    }
}
