package moe.haruue.walkee.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.jude.utils.JUtils;

import moe.haruue.walkee.util.MarginViewUtils;

/**
 * The view to fill in status bar in window translucent design of Android
 * @see <a href="http://haruue.moe/blog/2016/09/07/simple-android-window-translucent-ui-design/">android window translucent ui design</a>
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class StatusBarMarginView extends View {

    public StatusBarMarginView(Context context) {
        super(context);
    }

    public StatusBarMarginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusBarMarginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isInEditMode()) {   // just show it as a 24dp rect in edit mode
            final float scale = getResources().getDisplayMetrics().density;
            int height = (int) (24 * scale + 0.5f);
            setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), height);
            return;
        }
        if (MarginViewUtils.isNeedStatusBarMargin(getContext())) {
            setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), JUtils.getStatusBarHeight());
        } else {
            setMeasuredDimension(0, 0);
        }
    }

}
