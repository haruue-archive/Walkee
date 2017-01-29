package moe.haruue.walkee.data.permission;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import moe.haruue.walkee.config.Const;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class PermissionDatabase extends SQLiteOpenHelper {

    private static PermissionDatabase database;

    public static void initialize(Context context) {
        database = new PermissionDatabase(context);
    }

    public static SQLiteDatabase getDatabase() {
        return database.getWritableDatabase();
    }

    public static SQLiteDatabase getDatabaseReadonly() {
        return database.getReadableDatabase();
    }

    public static final String TABLE_PERMISSION = "permission";
    public static final String COLUMN_PACKAGE_NAME = "package_name";

    private PermissionDatabase(Context context) {
        super(context, Const.DB_NAME_PERMISSION, null, Const.DB_VER_PERMISSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PERMISSION + " (" +
                COLUMN_PACKAGE_NAME + " TEXT" +
                " )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
