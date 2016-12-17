package moe.haruue.walkee.ui.base;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Base Activity for all normal Activity <br>
 *     extends this activity in every Activity.
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public abstract class BaseActivity extends AppCompatActivity {

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
