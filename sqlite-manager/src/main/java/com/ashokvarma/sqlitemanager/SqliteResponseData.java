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
    private final List<SparseArray<String>> mColumnIndexToValuesArray;

    SqliteResponseData(int columnCount, String[] tableColumnNames, List<SparseArray<String>> columnIndexToValuesArray) {
        this.mTableColumnNames = tableColumnNames;
        this.mColumnCount = columnCount;
        this.mColumnIndexToValuesArray = columnIndexToValuesArray;
    }


    String[] getTableColumnNames() {
        return mTableColumnNames;
    }

    int getColumnCount() {
        return mColumnCount;
    }

    List<SparseArray<String>> getColumnIndexToValuesArray() {
        return mColumnIndexToValuesArray;
    }
}
