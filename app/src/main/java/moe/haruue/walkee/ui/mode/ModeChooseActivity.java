package moe.haruue.walkee.ui.mode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import moe.haruue.walkee.R;
import moe.haruue.walkee.config.Const;
import moe.haruue.walkee.ui.base.BaseActivity;
import moe.haruue.walkee.util.SPUtils;

/**
 * 模式选择
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class ModeChooseActivity extends BaseActivity {

    private Toolbar toolbar;
    private TimePreferenceFragment timePreferenceFragment;
    private CardView hardModeButton;
    private ImageView hardModeCheck;
    private CardView easyModeButton;
    private ImageView easyModeCheck;

    private Listener listener = new Listener();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        toolbar = $(R.id.toolbar);
        initializeToolbar();
        timePreferenceFragment = new TimePreferenceFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_mode_time_customize_container, timePreferenceFragment).commit();
        hardModeButton = $(R.id.cv_mode_hard);
        hardModeCheck = $(R.id.iv_mode_hard_check);
        easyModeButton = $(R.id.cv_mode_easy);
        easyModeCheck = $(R.id.iv_mode_easy_check);
        hardModeButton.setOnClickListener(listener);
        easyModeButton.setOnClickListener(listener);
        refreshModeCheck();
    }

    private void initializeToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void refreshModeCheck() {
        int mode = (int) SPUtils.get(this, Const.SPKEY_MODE, Const.MODE_EASY);
        easyModeCheck.setVisibility(View.INVISIBLE);
        hardModeCheck.setVisibility(View.INVISIBLE);
        if (mode == Const.MODE_EASY) {
            easyModeCheck.setVisibility(View.VISIBLE);
        } else if (mode == Const.MODE_HARD) {
            hardModeCheck.setVisibility(View.VISIBLE);
        }

    }

    private class Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cv_mode_hard:
                    SPUtils.set(ModeChooseActivity.this, Const.SPKEY_MODE, Const.MODE_HARD);
                    refreshModeCheck();
                    break;
                case R.id.cv_mode_easy:
                    SPUtils.set(ModeChooseActivity.this, Const.SPKEY_MODE, Const.MODE_EASY);
                    refreshModeCheck();
                    break;
            }
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ModeChooseActivity.class);
        context.startActivity(starter);
    }

}
