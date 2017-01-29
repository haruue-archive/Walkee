package moe.haruue.walkee.ui.base;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatDialog;
import android.view.View;

/**
 * Base Dialog for all normal Dialog <br>
 *     extends this dialog in every dialog.
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public abstract class BaseDialog extends AppCompatDialog {

    public BaseDialog(Context context) {
        super(context);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /**
     * A simple version of {@link #findViewById(int)}
     * @param id must be a id in {@link moe.haruue.walkee.R.id}
     * @param <V> the real type of view, which will be auto casted
     * @return A casted view
     */
    @SuppressWarnings("unchecked")
    public <V extends View> V $(@IdRes int id) {
        return (V) findViewById(id);
    }

    public View getContentView() {
        return getWindow().findViewById(android.R.id.content);
    }

}
