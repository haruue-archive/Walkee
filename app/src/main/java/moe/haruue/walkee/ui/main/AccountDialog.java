package moe.haruue.walkee.ui.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;

import java.io.File;

import moe.haruue.imageselector.ImageSelector;
import moe.haruue.walkee.App;
import moe.haruue.walkee.BuildConfig;
import moe.haruue.walkee.R;
import moe.haruue.walkee.model.User;
import moe.haruue.walkee.ui.base.BaseDialog;
import moe.haruue.walkee.util.FileUtils;
import moe.haruue.walkee.util.ImageLoader;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class AccountDialog extends BaseDialog {

    public static final boolean DEBUG = BuildConfig.DEBUG;
    public static final String TAG = "AccountDialog";

    private CircularImageView avatarView;
    private CircularImageView avatarAlpha;
    private EditText nicknameView;
    private TextView positiveButton;
    private TextView cancelButton;
    private User user;
    private Listener listener = new Listener();
    private Handler handler;
    private File tmp;
    private FinishListener finishListener;

    public AccountDialog(Context context, FinishListener l) {
        super(context, true, null);
        this.finishListener = l;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_account);
        // Initialize field
        avatarView = $(R.id.civ_dialog_account_avatar);
        avatarAlpha = $(R.id.civ_dialog_account_avatar_alpha);
        nicknameView = $(R.id.tv_dialog_account_nickname);
        positiveButton = $(R.id.bt_dialog_account_positive);
        cancelButton = $(R.id.bt_dialog_account_cancel);
        user = App.getInstance().getUser();
        handler = new Handler(Looper.getMainLooper());
        // Initialize view
        ImageLoader.loadLocalImage(user.avatar, avatarView, R.drawable.default_avatar);
        nicknameView.setText(user.username);
        // Initialize listener
        avatarAlpha.setOnClickListener(listener);
        positiveButton.setOnClickListener(listener);
        cancelButton.setOnClickListener(listener);
    }

    private class Listener implements View.OnClickListener, ImageSelector.Listener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.civ_dialog_account_avatar_alpha:
                    new ImageSelector().begin(getContext(), listener);
                    break;
                case R.id.bt_dialog_account_positive:
                    ProgressDialog progress = new ProgressDialog(getContext());
                    progress.setMessage(getContext().getString(R.string.loading));
                    progress.show();
                    new Thread(() -> {
                        App.getInstance().getUser().username = nicknameView.getText().toString();
                        if (tmp != null) {
                            File cache = new File(getContext().getFilesDir(), "avatar");
                            FileUtils.move(tmp, cache);
                            user.avatar = cache.getAbsolutePath();
                        }
                        App.getInstance().saveUser();
                        handler.post(() -> {
                            if (finishListener != null) {
                                finishListener.onEditAccountFinish();
                                finishListener = null;
                            }
                            progress.dismiss();
                        });
                    }).start();
                    dismiss();
                    break;
                case R.id.bt_dialog_account_cancel:
                    cancel();
                    break;
            }
        }

        @Override
        public void onImageSelectorSuccess(Uri imageUri) {
            tmp = new File(imageUri.getPath());
            ImageLoader.loadLocalImage(tmp.getAbsolutePath(), avatarView, R.drawable.default_avatar);
            if (DEBUG) {
                Log.d(TAG, "onImageSelectorSuccess: avatar=" + imageUri.toString());
            }
        }

        @Override
        public void onImageSelectorCancel() {
            if (DEBUG) {
                Log.i(TAG, "onImageSelectorCancel");
            }

        }
    }

    interface FinishListener {
        void onEditAccountFinish();
    }
}
