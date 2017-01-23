package moe.haruue.walkee.ui.status;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.jude.utils.JUtils;

import moe.haruue.walkee.App;
import moe.haruue.walkee.R;
import moe.haruue.walkee.model.User;
import moe.haruue.walkee.ui.main.BaseFragmentInMainActivity;
import moe.haruue.walkee.util.ImageLoader;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class CurrentStatusFragment extends BaseFragmentInMainActivity {

    CurrentStatusPresenter presenter = new CurrentStatusPresenter(this);

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getToolbar().setTitle(R.string.app_name);
        refreshHeader();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_status, container, false);
    }

    public void refreshHeader() {
        CircularImageView avatarView = $(getView(), R.id.civ_status_header_avatar);
        CircularImageView alphaView = $(getView(), R.id.civ_status_header_avatar_alpha);
        TextView nameView = $(getView(), R.id.tv_status_header_username);
        EditText nameEdit = $(getView(), R.id.et_status_header_username_edit);
        TextInputLayout nameEditHint = $(getView(), R.id.eth_status_header_username_hint);
        User user = App.getInstance().getUser();
        String username = user.username;
        if (username == null || username.isEmpty()) {
            username = getString(R.string.click_to_fill_username);
        }
        nameView.setText(username);
        String avatar = user.avatar;
        ImageLoader.loadLocalImage(avatar, avatarView, R.drawable.default_avatar);
        nameView.setOnClickListener(v -> {
            nameEdit.setText(App.getInstance().getUser().username);
            nameEdit.requestFocus();
            nameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE
                            || actionId == EditorInfo.IME_ACTION_SEND
                            || actionId == EditorInfo.IME_ACTION_GO
                            || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                        presenter.changeUsername(nameEdit.getText().toString());
                        nameView.setVisibility(View.VISIBLE);
                        nameEditHint.setVisibility(View.INVISIBLE);
                        JUtils.closeInputMethod(getActivity());
                        return true;
                    }
                    return false;
                }
            });
            nameEdit.setOnKeyListener((v1, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    nameView.setVisibility(View.VISIBLE);
                    nameEditHint.setVisibility(View.INVISIBLE);
                    JUtils.closeInputMethod(getMainActivity());
                    return true;
                }
                return false;
            });
            nameEditHint.setVisibility(View.VISIBLE);
            nameView.setVisibility(View.INVISIBLE);
            // show ime
            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .showSoftInput(nameEdit, InputMethodManager.SHOW_IMPLICIT);
        });
        alphaView.setOnClickListener(v -> {
            // TODO: Choose Avatar here
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getMainActivity().setNavigationMenuItemChecked(R.id.item_current_status);
    }
}
