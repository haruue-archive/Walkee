package moe.haruue.walkee.data.log.func;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import moe.haruue.walkee.data.log.LogDatabase;
import moe.haruue.walkee.ui.widget.StatisticsBarGraph;
import rx.functions.Func1;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public class QueryAllLogFunc implements Func1<Integer, List<StatisticsBarGraph.Item>> {
    @Override
    public List<StatisticsBarGraph.Item> call(Integer integer) {
        SQLiteDatabase database = LogDatabase.getDatabaseReadonly();
        List<StatisticsBarGraph.Item> items = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.query(
                    LogDatabase.TABLE_WALK_LOG,
                    new String[]{LogDatabase.COLUMN_DATE, LogDatabase.COLUMN_TIMES},
                    null, null, null, null, null
            );
            if (cursor.moveToFirst()) {
                do {
                    String date = cursor.getString(cursor.getColumnIndex(LogDatabase.COLUMN_DATE));
                    int times = cursor.getInt(cursor.getColumnIndex(LogDatabase.COLUMN_TIMES));
                    StatisticsBarGraph.Item item = new StatisticsBarGraph.Item(date, times);
                    items.add(item);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && cursor.isClosed()) {
                cursor.close();
            }
        }
        return items;
    }

}