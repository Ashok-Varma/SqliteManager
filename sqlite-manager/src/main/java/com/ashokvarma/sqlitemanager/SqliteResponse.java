package com.ashokvarma.sqlitemanager;

import android.database.Cursor;

/**
 * Class description
 *
 * @author ashokvarma
 * @version 1.0
 * @since 17 Jun 2017
 */
class SqliteResponse {
    private Cursor cursor;
    private Throwable throwable;
    private boolean querySuccess;

    Cursor getCursor() {
        return cursor;
    }

    void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    Throwable getThrowable() {
        return throwable;
    }

    void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    boolean isQuerySuccess() {
        return querySuccess;
    }

    void setQuerySuccess(boolean querySuccess) {
        this.querySuccess = querySuccess;
    }
}
