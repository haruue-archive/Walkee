package moe.haruue.walkee.data.permission.func;

import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import moe.haruue.walkee.data.permission.PermissionDatabase;
import moe.haruue.walkee.model.ApplicationCheckedInfo;
import rx.functions.Func1;

/**
 * Check if a application has permission
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class CheckPermissionFunc implements Func1<ApplicationInfo, ApplicationCheckedInfo> {
    @Override
    public ApplicationCheckedInfo call(ApplicationInfo info) {
        ApplicationCheckedInfo checked = new ApplicationCheckedInfo(info);
        checked.checked = checkPermission(info.packageName);
        return checked;
    }

    public static boolean checkPermission(String packageName) {
        SQLiteDatabase database = PermissionDatabase.getDatabaseReadonly();
        Cursor cursor = database.query(
                PermissionDatabase.TABLE_PERMISSION,
                new String[]{PermissionDatabase.COLUMN_PACKAGE_NAME},
                PermissionDatabase.COLUMN_PACKAGE_NAME + "=?",
                new String[]{packageName},
                null,
                null,
                null
        );
        boolean checked = cursor.getCount() > 0;
        cursor.close();
        return checked;
    }
}
