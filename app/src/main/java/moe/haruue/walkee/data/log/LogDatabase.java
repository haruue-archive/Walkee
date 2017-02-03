package moe.haruue.walkee.data.log;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import moe.haruue.walkee.config.Const;

/**
 * Database to store walk log, such as the times of playing mobile while walking per day
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class LogDatabase extends SQLiteOpenHelper {

    public static final String NAME = Const.DB_NAME_LOG;
    public static final int VER = Const.DB_VER_LOG;

    private static LogDatabase database;

    public static void initialize(Context context) {
        database = new LogDatabase(context);
    }

    public static SQLiteDatabase getDatabase() {
        return database.getWritableDatabase();
    }

    public static SQLiteDatabase getDatabaseReadonly() {
        return database.getReadableDatabase();
    }

    public static final String TABLE_WALK_LOG = "walk_log";
    public static final String COLUMN_TIME_IN_MILLIS = "time_in_millis";


    private LogDatabase(Context context) {
        super(context, NAME, null, VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_WALK_LOG + " (" +
                COLUMN_TIME_IN_MILLIS + " INTEGER" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
