package com.ashokvarma.sqlitemanager;

import android.database.Cursor;
import android.support.annotation.NonNull;

/**
 * Class description
 *
 * @author ashokvarma
 * @version 1.0
 * @see SqliteDataRetriever
 * @since 17 Jun 2017
 */
class SqliteResponseRetriever {

    private SqliteDataRetriever mSqliteDataRetriever;

    SqliteResponseRetriever(SqliteDataRetriever sqliteDataRetriever) {
        this.mSqliteDataRetriever = sqliteDataRetriever;
    }

    SqliteResponse getData(@NonNull String query, String[] selectionArgs) {
        SqliteResponse sqliteResponse = new SqliteResponse();
        try {
            Cursor cursor = mSqliteDataRetriever.rawQuery(query, selectionArgs);
            sqliteResponse.setCursor(cursor);
            sqliteResponse.setQuerySuccess(true);
        } catch (Throwable throwable) {
            sqliteResponse.setThrowable(throwable);
            sqliteResponse.setQuerySuccess(false);
        }
        return sqliteResponse;
    }
}
