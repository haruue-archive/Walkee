package moe.haruue.walkee.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.jude.utils.JUtils;

import moe.haruue.walkee.util.MarginViewUtils;
import moe.haruue.walkee.util.ViewUtils;

/**
 * The view to fill in navigation bar in window translucent design of Android
 * @see <a href="http://haruue.moe/blog/2016/09/07/simple-android-window-translucent-ui-design/">android window translucent ui design</a>
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class NavigationBarMarginView extends View {
    public NavigationBarMarginView(Context context) {
        super(context);
    }

    public NavigationBarMarginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationBarMarginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isInEditMode()) {   // just show it as a 48dp rect in edit mode
            int height = ViewUtils.dip2px(getContext(), 48);
            setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), height);
            return;
        }
        if (MarginViewUtils.isNeedNavigationMargin(getContext())) {
            setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), JUtils.getNavigationBarHeight());
        } else {
            setMeasuredDimension(0, 0);
        }
    }

}
