package moe.haruue.walkee.ui.mode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import moe.haruue.walkee.R;
import moe.haruue.walkee.ui.base.BaseActivity;

/**
 * 模式选择
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class ModeChooseActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        toolbar = $(R.id.toolbar);
        initializeToolbar();
    }

    private void initializeToolbar() {
        toolbar.setTitle(R.string.mode_choose);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ModeChooseActivity.class);
        context.startActivity(starter);
    }

}
