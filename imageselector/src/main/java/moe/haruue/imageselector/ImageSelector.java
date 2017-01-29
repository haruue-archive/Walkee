package moe.haruue.imageselector;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class ImageSelector {

    static Listener listener;

    public void begin(Context context, Listener listener) {
        Intent intent = new Intent(context, ShadowActivity.class);
        ImageSelector.listener = listener;
        context.startActivity(intent);
    }


    public interface Listener {
        void onImageSelectorSuccess(Uri imageUri);
        void onImageSelectorCancel();
    }

}
