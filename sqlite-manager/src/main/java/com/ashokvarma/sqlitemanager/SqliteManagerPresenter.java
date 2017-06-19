package com.ashokvarma.sqlitemanager;

import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Class description
 *
 * @author ashokvarma
 * @version 1.0
 * @see SqliteManagerView
 * @since 17 Jun 2017
 */
class SqliteManagerPresenter {
    private SqliteResponseRetriever mSqliteResponseRetriever;
    private SqliteDataRetriever mSqliteDataRetriever;
    private SqliteManagerView mSqliteManagerView;
    private ArrayList<String> mTableNames = new ArrayList<String>();
    private static final String TABLE_FETCH_QUERY = "SELECT name _id FROM sqlite_master WHERE type ='table'";

    private boolean mIsCustomQuery;
    private String mPreviousCustomQuery;


    SqliteManagerPresenter(SqliteDataRetriever sqliteDataRetriever) {
        mSqliteDataRetriever = sqliteDataRetriever;
        mSqliteResponseRetriever = new SqliteResponseRetriever(mSqliteDataRetriever);
    }

    public SqliteManagerView getView() {
        return mSqliteManagerView;
    }

    void bindView(SqliteManagerView sqliteManagerView) {
        mSqliteManagerView = sqliteManagerView;
        changeSubTitle();
        loadTableNames();
    }

    private void changeSubTitle() {
        String databaseName = mSqliteDataRetriever.getDatabaseName();
        String appName = getApplicationName(getView().getViewContext());
        getView().setSubtitle((TextUtils.isEmpty(databaseName) ? "" : databaseName + " ") + appName);
    }

    void unBindView(SqliteManagerView sqliteManagerView) {
        mSqliteManagerView = null;
    }

    private void loadTableNames() {
        mTableNames.clear();
        SqliteResponse sqliteResponse = mSqliteResponseRetriever.getData(TABLE_FETCH_QUERY, null);
        if (sqliteResponse.isQuerySuccess()) {
            Cursor cursor = sqliteResponse.getCursor();
            cursor.moveToFirst();
            do {
                mTableNames.add(cursor.getString(0));
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            if (sqliteResponse.getThrowable() != null) {
                getView().finishActivityWithError(sqliteResponse.getThrowable().getMessage());
            } else {
                getView().finishActivityWithError(R.string.sqlite_manager_error_while_fetching_table_names);
            }
        }
        mSqliteDataRetriever.freeResources();

        if (mTableNames.isEmpty()) {
            getView().displayError(R.string.sqlite_manager_database_has_no_tables);
        } else {
            getView().setSpinnerAdapter(mTableNames);
        }
    }

    void fetchTableData(@Nullable String selectedTableName) {
        if (selectedTableName == null || TextUtils.isEmpty(selectedTableName)) {
            getView().displayError(R.string.sqlite_manager_please_select_a_table);
            return;
        }

        SqliteResponseData sqliteResponseData = getSqliteResponseDataForSelectedTable(selectedTableName, null, true);
        displayData(sqliteResponseData, false, true);
    }

    void onRefreshClicked(@Nullable String selectedTableName) {
        if (mIsCustomQuery) {
            SqliteResponseData sqliteResponseData = getSqliteResponseDataForCustomQuery(mPreviousCustomQuery, null, true);
            displayData(sqliteResponseData, true, true);
        } else {
            if (selectedTableName == null || TextUtils.isEmpty(selectedTableName)) {
                getView().displayError(R.string.sqlite_manager_please_select_a_table);
                return;
            }

            SqliteResponseData sqliteResponseData = getSqliteResponseDataForSelectedTable(selectedTableName, null, true);
            displayData(sqliteResponseData, false, true);
        }
    }

    void onSortChangedOrderChanged(@Nullable String selectedTableName, @Nullable String orderBy, boolean isAscendingOrder) {
        if (mIsCustomQuery) {
            if (mPreviousCustomQuery.toUpperCase().contains("ORDER BY")) {
                getView().displayError(R.string.sqlite_manager_custom_query_contacins_order_by);
                return;
            } else {
                SqliteResponseData sqliteResponseData = getSqliteResponseDataForCustomQuery(mPreviousCustomQuery, orderBy, isAscendingOrder);
                displayData(sqliteResponseData, true, false);
            }
        } else {
            if (selectedTableName == null || TextUtils.isEmpty(selectedTableName)) {
                getView().displayError(R.string.sqlite_manager_please_select_a_table);
                return;
            }

            SqliteResponseData sqliteResponseData = getSqliteResponseDataForSelectedTable(selectedTableName, orderBy, isAscendingOrder);
            displayData(sqliteResponseData, false, false);
        }
    }

    void onCustomQueryClicked() {
        getView().showCustomQueryDialog(mPreviousCustomQuery);
    }

    void onCustomQuerySubmitted(String customQuery) {
        if (TextUtils.isEmpty(customQuery)) {
            getView().displayError(R.string.sqlite_manager_empty_custom_query);
            return;
        }
        SqliteResponseData sqliteResponseData = getSqliteResponseDataForCustomQuery(customQuery, null, true);

        mPreviousCustomQuery = customQuery;
        displayData(sqliteResponseData, true, true);
    }

    void displayData(@Nullable SqliteResponseData sqliteResponseData, boolean isCustomQuery, boolean updateColumnNames) {
        if (sqliteResponseData != null) {
            getView().showContentView();
            this.mIsCustomQuery = isCustomQuery;
            if (updateColumnNames) {
                getView().updateColumnNames(sqliteResponseData.getTableColumnNames());
            }
            getView().displayRows(sqliteResponseData.getColumnIndexToValuesArray());
        }
    }


    private SqliteResponseData getSqliteResponseDataForCustomQuery(@NonNull String customQuery, @Nullable String orderBy, boolean isAscendingOrder) {
        String orderByWithIncrement = orderBy == null ? null : (orderBy + (isAscendingOrder ? "" : " DESC"));
        String customQueryWithIncrement = customQuery + (orderByWithIncrement == null ? "" : " ORDER BY " + orderByWithIncrement);

        return getSqliteResponseDataFromQuery(customQueryWithIncrement, null);
    }

    private SqliteResponseData getSqliteResponseDataForSelectedTable(@NonNull String selectedTableName, @Nullable String orderBy, boolean isAscendingOrder) {
        String orderByWithIncrement = orderBy == null ? null : (orderBy + (isAscendingOrder ? "" : " DESC"));
        String query = "SELECT * FROM " + selectedTableName + (orderByWithIncrement == null ? "" : " ORDER BY " + orderByWithIncrement);

        return getSqliteResponseDataFromQuery(query, null);
    }

    private SqliteResponseData getSqliteResponseDataFromQuery(String query, String[] selectionArgs) {
        SqliteResponse sqliteResponse = mSqliteResponseRetriever.getData(query, selectionArgs);
        if (sqliteResponse.isQuerySuccess()) {
            Cursor cursor = sqliteResponse.getCursor();
            String[] selectedTableColumnNames = cursor.getColumnNames();
            int columnCount = cursor.getColumnCount();
            List<SparseArray<String>> valuesArray = new ArrayList<>(cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    SparseArray<String> columnValuePair = new SparseArray<>(columnCount);
                    for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                        columnValuePair.put(columnIndex, cursor.getString(columnIndex));
                    }
                    valuesArray.add(columnValuePair);
                } while (cursor.moveToNext());
            }

            mSqliteDataRetriever.freeResources();
            return new SqliteResponseData(columnCount, selectedTableColumnNames, valuesArray);
        } else {
            if (sqliteResponse.getThrowable() != null) {
                getView().displayError(sqliteResponse.getThrowable().getMessage());
            } else {
                getView().displayError(R.string.sqlite_manager_error_while_fetching_column_names);
            }
        }
        mSqliteDataRetriever.freeResources();
        return null;
    }

    private String getApplicationName(AppCompatActivity appCompatActivity) {
        ApplicationInfo applicationInfo = appCompatActivity.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : appCompatActivity.getString(stringId);
    }
}
