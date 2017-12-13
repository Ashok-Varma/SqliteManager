package com.ashokvarma.sqlitemanager;

import android.util.SparseArray;

import java.util.List;

/**
 * Class description
 *
 * @author ashokvarma
 * @version 1.0
 * @since 17 Jun 2017
 */
class SqliteResponseData {
    private final int mColumnCount;
    private final String[] mTableColumnNames;
    private final int[] mTableColumnTypes;
    private final List<SparseArray<Object>> mColumnIndexToValuesArray;
    private final Throwable mThrowable;
    private final boolean mQuerySuccess;

    SqliteResponseData(int columnCount, String[] tableColumnNames, int[] tableColumnTypes, List<SparseArray<Object>> columnIndexToValuesArray) {
        this.mTableColumnNames = tableColumnNames;
        this.mTableColumnTypes = tableColumnTypes;
        this.mColumnCount = columnCount;
        this.mColumnIndexToValuesArray = columnIndexToValuesArray;
        this.mQuerySuccess = true;
        this.mThrowable = null;
    }

    SqliteResponseData(Throwable throwable) {
        this.mColumnCount = -1;
        this.mTableColumnNames = null;
        this.mTableColumnTypes = null;
        this.mColumnIndexToValuesArray = null;
        this.mThrowable = throwable;
        this.mQuerySuccess = false;
    }


    String[] getTableColumnNames() {
        return mTableColumnNames;
    }

    int getColumnCount() {
        return mColumnCount;
    }

    List<SparseArray<Object>> getColumnIndexToValuesArray() {
        return mColumnIndexToValuesArray;
    }

    public boolean isQuerySuccess() {
        return mQuerySuccess;
    }

    Throwable getThrowable() {
        return mThrowable;
    }
}
