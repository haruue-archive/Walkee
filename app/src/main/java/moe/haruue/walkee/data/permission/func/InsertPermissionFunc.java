package moe.haruue.walkee.data.permission.func;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import moe.haruue.walkee.data.permission.PermissionDatabase;
import moe.haruue.walkee.model.ApplicationCheckedInfo;
import rx.functions.Func1;

/**
 * Grant permission for a application
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class InsertPermissionFunc implements Func1<ApplicationCheckedInfo, ApplicationCheckedInfo> {
    @Override
    public ApplicationCheckedInfo call(ApplicationCheckedInfo info) {
        SQLiteDatabase database = PermissionDatabase.getDatabase();
        ContentValues values = new ContentValues();
        values.put(PermissionDatabase.COLUMN_PACKAGE_NAME, info.packageName);
        database.insert(
                PermissionDatabase.TABLE_PERMISSION,
                null,
                values
        );
        info.checked = true;
        return info;
    }
}
