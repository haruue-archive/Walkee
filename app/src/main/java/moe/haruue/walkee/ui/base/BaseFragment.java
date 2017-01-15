package moe.haruue.walkee.ui.base;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Base Fragment for all normal Fragment <br>
 *     extends this fragment in every Fragment.
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public abstract class BaseFragment extends Fragment {

    /**
     * A simple version of {@link View#findViewById(int)}
     * @param v the parent view to find of
     * @param id must be a id in {@link moe.haruue.walkee.R.id}
     * @param <V> the real type of view, which will be auto casted
     * @return A casted view
     */
    @SuppressWarnings("unchecked")
    public <V extends View> V $(View v, @IdRes int id) {
        return (V) v.findViewById(id);
    }

    /**
     * A simple version of {@link View#findViewById(int)}
     * @param id must be a id in {@link moe.haruue.walkee.R.id}
     * @param <V> the real type of view, which will be auto casted
     * @return A casted view
     */
    @SuppressWarnings("unchecked")
    public <V extends View> V $(@IdRes int id) {
        return (V) getView().findViewById(id);
    }

}
