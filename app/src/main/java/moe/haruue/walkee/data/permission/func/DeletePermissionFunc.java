package moe.haruue.walkee.data.permission.func;

import android.database.sqlite.SQLiteDatabase;

import moe.haruue.walkee.data.permission.PermissionDatabase;
import moe.haruue.walkee.model.ApplicationCheckedInfo;
import rx.functions.Func1;

/**
 * Revoke permission of a application
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class DeletePermissionFunc implements Func1<ApplicationCheckedInfo, ApplicationCheckedInfo> {
    @Override
    public ApplicationCheckedInfo call(ApplicationCheckedInfo info) {
        SQLiteDatabase database = PermissionDatabase.getDatabase();
        database.delete(
                PermissionDatabase.TABLE_PERMISSION,
                PermissionDatabase.COLUMN_PACKAGE_NAME + "=?",
                new String[]{info.packageName}
        );
        info.checked = false;
        return info;
    }
}
