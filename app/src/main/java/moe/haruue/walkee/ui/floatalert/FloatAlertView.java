package moe.haruue.walkee.ui.floatalert;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import moe.haruue.walkee.R;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class FloatAlertView extends FrameLayout {
    public FloatAlertView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.float_alert, this);
    }
}
