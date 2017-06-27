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
    private Cursor mCursor;
    private Throwable mThrowable;
    private boolean mQuerySuccess;

    Cursor getCursor() {
        return mCursor;
    }

    void setCursor(Cursor cursor) {
        this.mCursor = cursor;
    }

    Throwable getThrowable() {
        return mThrowable;
    }

    void setThrowable(Throwable throwable) {
        this.mThrowable = throwable;
    }

    boolean isQuerySuccess() {
        return mQuerySuccess;
    }

    void setQuerySuccess(boolean querySuccess) {
        this.mQuerySuccess = querySuccess;
    }
}
