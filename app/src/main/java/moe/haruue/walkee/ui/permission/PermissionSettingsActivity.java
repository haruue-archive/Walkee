package moe.haruue.walkee.ui.permission;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import moe.haruue.walkee.R;
import moe.haruue.walkee.model.ApplicationCheckedInfo;
import moe.haruue.walkee.ui.base.BaseActivity;

/**
 * 权限设置
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class PermissionSettingsActivity extends BaseActivity {

    Toolbar toolbar;
    RecyclerView recycler;
    ApplicationCheckedAdapter adapter;
    PermissionSettingsPresenter presenter = new PermissionSettingsPresenter(this);
    ProgressDialog progress;
    MenuItem hideSystemApplicationSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        toolbar = $(R.id.toolbar);
        initializeToolbar();
        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.loading));
        progress.show();
        recycler = $(R.id.rv_permission_application_list);
        adapter = new ApplicationCheckedAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        presenter.start();
        adapter.setOnSwitchListener(i -> presenter.onApplicationCheckedStateChange(i));
        // Load it in {@link #onCreateOptionsMenu(Menu)}
        //presenter.requireApplicationCheckedInfoList(hideSystemApplicationSwitch.isChecked());
    }

    private void initializeToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onGetApplicationCheckedInfoList(List<ApplicationCheckedInfo> list) {
        adapter.setData(list);
        progress.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_permission_toolbar, menu);
        hideSystemApplicationSwitch = menu.findItem(R.id.item_hide_system_application);
        hideSystemApplicationSwitch.setOnMenuItemClickListener(item -> {
            item.setChecked(!item.isChecked());
            progress.show();
            presenter.requireApplicationCheckedInfoList(!item.isChecked());
            return false;
        });
        presenter.requireApplicationCheckedInfoList(!hideSystemApplicationSwitch.isChecked());
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, PermissionSettingsActivity.class);
        context.startActivity(starter);
    }

}
