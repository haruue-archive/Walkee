package moe.haruue.walkee.data.log.func;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import moe.haruue.walkee.data.log.LogDatabase;
import rx.functions.Func1;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class InsertLogFunc implements Func1<Integer, Integer> {
    @Override
    public Integer call(Integer integer) {
        SQLiteDatabase database = LogDatabase.getDatabase();
        ContentValues values = new ContentValues();
        values.put(LogDatabase.COLUMN_TIME_IN_MILLIS, System.currentTimeMillis());
        database.insert(
                LogDatabase.TABLE_WALK_LOG,
                null,
                values
        );
        return 0;
    }
}
