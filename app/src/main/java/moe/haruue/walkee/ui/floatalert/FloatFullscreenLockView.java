package moe.haruue.walkee.ui.floatalert;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import moe.haruue.walkee.R;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class FloatFullscreenLockView extends FrameLayout {
    public FloatFullscreenLockView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.float_fullscreen_lock, this);
    }
}
