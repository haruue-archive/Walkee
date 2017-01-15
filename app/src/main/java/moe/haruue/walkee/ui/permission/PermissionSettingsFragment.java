package moe.haruue.walkee.ui.permission;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import moe.haruue.walkee.R;
import moe.haruue.walkee.ui.main.BaseFragmentInMainActivity;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class PermissionSettingsFragment extends BaseFragmentInMainActivity {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getToolbar().setTitle(R.string.permission_settings);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_permission, container, false);
    }
}
