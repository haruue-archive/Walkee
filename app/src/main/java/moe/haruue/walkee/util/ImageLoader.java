package moe.haruue.walkee.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import java.io.FileInputStream;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.SafeSubscriber;
import rx.schedulers.Schedulers;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class ImageLoader {

    public static void loadLocalImage(String path, ImageView v, @DrawableRes int resWhenFailure) {
        Observable.just(path)
                .map(s -> {
                    try {
                        FileInputStream stream = new FileInputStream(path);
                        return BitmapFactory.decodeStream(stream);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SafeSubscriber<Bitmap>(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        v.setImageResource(resWhenFailure);
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        v.setImageBitmap(bitmap);
                    }
                }));
    }

}
