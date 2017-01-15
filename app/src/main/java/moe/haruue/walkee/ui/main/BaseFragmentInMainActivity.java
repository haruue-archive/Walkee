package moe.haruue.walkee.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import moe.haruue.walkee.R;
import moe.haruue.walkee.ui.base.BaseFragment;

/**
 * Base Fragment for the Fragments which will be only attached to {@link MainActivity}
 *
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public abstract class BaseFragmentInMainActivity extends BaseFragment {

    private Toolbar toolbar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof MainActivity)) {
            throw new IllegalArgumentException("Fragment " + getClass().getName() + " can be only attach to " + MainActivity.class.getName());
        }
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = $(R.id.toolbar);
        getMainActivity().installToolbar(toolbar);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}
