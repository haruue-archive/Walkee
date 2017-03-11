package moe.haruue.walkee.data.log.func;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;
import java.util.GregorianCalendar;

import moe.haruue.walkee.data.log.LogDatabase;
import rx.functions.Func1;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class InsertLogFunc implements Func1<Integer, Integer> {
    @Override
    public Integer call(Integer integer) {
        SQLiteDatabase database = LogDatabase.getDatabase();
        Calendar today = new GregorianCalendar();
        String todayDateString = today.get(Calendar.YEAR) + "-" + today.get(Calendar.MONTH) + "-" + today.get(Calendar.DAY_OF_MONTH);
        int times = queryTodayTimes(todayDateString) + 1;
        ContentValues values = new ContentValues();
        values.put(LogDatabase.COLUMN_DATE, todayDateString);
        values.put(LogDatabase.COLUMN_TIMES, times);
        database.update(
                LogDatabase.TABLE_WALK_LOG,
                values,
                LogDatabase.COLUMN_DATE + "=?",
                new String[]{todayDateString}
        );
        return times;
    }

    private int queryTodayTimes(String todayDateString) {
        SQLiteDatabase database = LogDatabase.getDatabase();
        Cursor cursor = null;
        int times = 0;
        try {
            cursor = database.query(
                    LogDatabase.TABLE_WALK_LOG,
                    new String[]{LogDatabase.COLUMN_TIMES},
                    LogDatabase.COLUMN_DATE + "=?",
                    new String[]{todayDateString},
                    null, null, null
            );
            if (cursor.moveToFirst()) {
                times = cursor.getInt(cursor.getColumnIndex(LogDatabase.COLUMN_TIMES));
            } else {
                ContentValues emptyDayValues = new ContentValues();
                emptyDayValues.put(LogDatabase.COLUMN_DATE, todayDateString);
                emptyDayValues.put(LogDatabase.COLUMN_TIMES, 0);
                database.insert(
                        LogDatabase.TABLE_WALK_LOG,
                        null,
                        emptyDayValues
                );
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return times;
    }
}
