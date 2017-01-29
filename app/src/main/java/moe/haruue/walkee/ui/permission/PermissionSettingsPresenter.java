package moe.haruue.walkee.ui.permission;

import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import moe.haruue.walkee.BuildConfig;
import moe.haruue.walkee.data.permission.func.CheckPermissionFunc;
import moe.haruue.walkee.data.permission.func.DeletePermissionFunc;
import moe.haruue.walkee.data.permission.func.InsertPermissionFunc;
import moe.haruue.walkee.model.ApplicationCheckedInfo;
import moe.haruue.walkee.ui.base.BasePresenter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

class PermissionSettingsPresenter implements BasePresenter {

    public static final String TAG = "PermissionSettingsP";
    public static final boolean DEBUG = BuildConfig.DEBUG;


    PermissionSettingsActivity activity;

    PermissionSettingsPresenter(PermissionSettingsActivity activity) {
        this.activity = activity;
    }

    public void requireApplicationCheckedInfoList(boolean needSystemApplication) {
        Observable.just(activity.getPackageManager())
                .map(pm -> {
                    List<ApplicationInfo> list = pm.getInstalledApplications(0);
                    if (!needSystemApplication) {
                        List<ApplicationInfo> userList = new ArrayList<>();
                        for (ApplicationInfo i: list) {
                            if ((i.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                                userList.add(i);
                            }
                        }
                        list = userList;
                    }
                    return list;
                })
                .map(infoList -> {
                    CheckPermissionFunc singleFunc = new CheckPermissionFunc();
                    List<ApplicationCheckedInfo> checkedList = new ArrayList<>();
                    for (ApplicationInfo i: infoList) {
                        checkedList.add(singleFunc.call(i));
                    }
                    Collections.sort(checkedList);
                    return checkedList;
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ApplicationCheckedInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (DEBUG) {
                            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Log.e(TAG, "requireApplicationCheckedInfoList: onError", e);
                    }

                    @Override
                    public void onNext(List<ApplicationCheckedInfo> list) {
                        activity.onGetApplicationCheckedInfoList(list);
                    }
                });
    }

    public void onApplicationCheckedStateChange(ApplicationCheckedInfo info) {
        Observable.just(info)
                .map(info1 -> {
                    if (info1.checked) {
                        return new DeletePermissionFunc().call(info1);
                    } else {
                        return new InsertPermissionFunc().call(info1);
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApplicationCheckedInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (DEBUG) {
                            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Log.e(TAG, "onApplicationCheckedStateChange: onError", e);
                    }

                    @Override
                    public void onNext(ApplicationCheckedInfo info) {

                    }
                });
    }

    @Override
    public void start() {

    }
}
