package moe.haruue.walkee.data.log.func;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import moe.haruue.walkee.data.log.LogDatabase;
import rx.functions.Func1;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class QueryLogCountWithTimeFunc implements Func1<Integer, Integer> {

    private long startTimeMillis;
    private long endTimeMillis;

    public QueryLogCountWithTimeFunc(long startTimeMillis, long endTimeMillis) {
        this.startTimeMillis = startTimeMillis;
        this.endTimeMillis = endTimeMillis;
    }

    @Override
    public Integer call(Integer integer) {
        SQLiteDatabase database = LogDatabase.getDatabase();
        Cursor cursor = database.query(
                LogDatabase.TABLE_WALK_LOG,
                new String[]{LogDatabase.COLUMN_TIME_IN_MILLIS},
                LogDatabase.COLUMN_TIME_IN_MILLIS + ">? AND" + LogDatabase.COLUMN_TIME_IN_MILLIS + "<?",
                new String[]{startTimeMillis + "", endTimeMillis + ""},
                null,
                null,
                null
        );
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


}
