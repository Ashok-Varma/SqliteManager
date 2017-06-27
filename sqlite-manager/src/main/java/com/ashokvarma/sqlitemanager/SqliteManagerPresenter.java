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
    private ArrayList<String> mTableNames = new ArrayList<>();
    private static final String TABLE_FETCH_QUERY = "SELECT name _id FROM sqlite_master WHERE type ='table'";

    private boolean mIsCustomQuery;// it says if the current displayed data is from custom Query ??
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

    void unBindView(SqliteManagerView sqliteManagerView) {
        mSqliteManagerView = null;
    }

    private void changeSubTitle() {
        String databaseName = mSqliteDataRetriever.getDatabaseName();
        String appName = getApplicationName(getView().getViewContext());
        getView().setSubtitle((TextUtils.isEmpty(databaseName) ? "" : databaseName + " ") + appName);
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
                getView().informErrorToUser(R.string.sqlite_manager_please_select_a_table);
                return;
            }

            SqliteResponseData sqliteResponseData = getSqliteResponseDataForSelectedTable(selectedTableName, null, true);
            displayData(sqliteResponseData, false, true);
        }
    }

    void onAddFabClicked(@Nullable String tableName, String[] tableColumnNames) {
        if (tableName == null || TextUtils.isEmpty(tableName)) {
            getView().informErrorToUser(R.string.sqlite_manager_please_select_a_table);
            return;
        }
        getView().showAddRowDialog(tableName, tableColumnNames);
    }

    void onSortChangedOrderChanged(@Nullable String selectedTableName, @Nullable String orderBy, boolean isAscendingOrder) {
        if (mIsCustomQuery) {
            if (mPreviousCustomQuery.toUpperCase().contains("ORDER BY")) {
                getView().informErrorToUser(R.string.sqlite_manager_custom_query_contacins_order_by);
                return;
            } else {
                SqliteResponseData sqliteResponseData = getSqliteResponseDataForCustomQuery(mPreviousCustomQuery, orderBy, isAscendingOrder);
                displayData(sqliteResponseData, true, false);
            }
        } else {
            if (selectedTableName == null || TextUtils.isEmpty(selectedTableName)) {
                getView().informErrorToUser(R.string.sqlite_manager_please_select_a_table);
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
            getView().informErrorToUser(R.string.sqlite_manager_empty_custom_query);
            return;
        }
        SqliteResponseData sqliteResponseData = getSqliteResponseDataForCustomQuery(customQuery, null, true);

        mPreviousCustomQuery = customQuery;
        displayData(sqliteResponseData, true, true);
    }

    void addRow(String tableName, String[] tableColumnNames, ArrayList<String> columnValues) {
        if (tableColumnNames.length != columnValues.size()) {
            getView().informErrorToUser(R.string.sqlite_manager_table_column_length_error);
            return;
        }

        String query = "INSERT INTO ".concat(tableName).concat(" ");
        String columnNameQuery = "(";
        String columnValueQuery = "(";
        int columnIndex = 0;
        for (String currentColumnValue : columnValues) {
            String columnName = tableColumnNames[columnIndex];
            columnIndex++;
            if (!TextUtils.isEmpty(currentColumnValue)) {
                columnNameQuery = columnNameQuery.concat(columnName).concat(",");
                columnValueQuery = columnValueQuery.concat("\'").concat(currentColumnValue).concat("\'").concat(",");
            }
        }
        if (columnNameQuery.endsWith(",")) {
            columnNameQuery = columnNameQuery.substring(0, columnNameQuery.length() - 1).concat(")");
            columnValueQuery = columnValueQuery.substring(0, columnValueQuery.length() - 1).concat(")");
            query = query.concat(columnNameQuery).concat(" VALUES ").concat(columnValueQuery);
            SqliteResponseData sqliteResponseData = getSqliteResponseDataFromQuery(query, null);
            if (sqliteResponseData.isQuerySuccess()) {
                getView().informErrorToUser(R.string.sqlite_manager_add_row_success);
                onRefreshClicked(tableName);
            } else {
                if (sqliteResponseData.getThrowable() != null) {
                    getView().informErrorToUser(sqliteResponseData.getThrowable().getMessage());
                } else {
                    getView().informErrorToUser(R.string.sqlite_manager_add_row_error);
                }
            }
        } else {
            getView().informErrorToUser(R.string.sqlite_manager_all_columns_cant_be_empty);
            return;
        }
    }

    private void displayData(@NonNull SqliteResponseData sqliteResponseData, boolean isCustomQuery, boolean updateColumnNames) {
        if (sqliteResponseData.isQuerySuccess()) {
            getView().showContentView();
            this.mIsCustomQuery = isCustomQuery;// save as custom query only if this is success
            if (updateColumnNames) {
                getView().updateColumnNames(sqliteResponseData.getTableColumnNames());
            }
            getView().displayRows(sqliteResponseData.getColumnIndexToValuesArray());
        } else {
            // if custom query save old state and just show a snackbar. But other cases display error removing data
            if (sqliteResponseData.getThrowable() != null) {
                if (isCustomQuery) {
                    getView().informErrorToUser(sqliteResponseData.getThrowable().getMessage());
                } else {
                    getView().displayError(sqliteResponseData.getThrowable().getMessage());
                }
            } else {
                if (isCustomQuery) {
                    getView().informErrorToUser(R.string.sqlite_manager_error_while_fetching_data);
                } else {
                    getView().displayError(R.string.sqlite_manager_error_while_fetching_data);
                }
            }
        }

        if (mIsCustomQuery) {
            getView().setAddFABVisible(false);
        } else {
            getView().setAddFABVisible(true);
        }
    }


    private
    @NonNull
    SqliteResponseData getSqliteResponseDataForCustomQuery(@NonNull String customQuery, @Nullable String orderBy, boolean isAscendingOrder) {
        String orderByWithIncrement = orderBy == null ? null : (orderBy + (isAscendingOrder ? "" : " DESC"));
        String customQueryWithIncrement = customQuery + (orderByWithIncrement == null ? "" : " ORDER BY " + orderByWithIncrement);

        return getSqliteResponseDataFromQuery(customQueryWithIncrement, null);
    }

    private
    @NonNull
    SqliteResponseData getSqliteResponseDataForSelectedTable(@NonNull String selectedTableName, @Nullable String orderBy, boolean isAscendingOrder) {
        String orderByWithIncrement = orderBy == null ? null : (orderBy + (isAscendingOrder ? "" : " DESC"));
        String query = "SELECT * FROM " + selectedTableName + (orderByWithIncrement == null ? "" : " ORDER BY " + orderByWithIncrement);

        return getSqliteResponseDataFromQuery(query, null);
    }

    private
    @NonNull
    SqliteResponseData getSqliteResponseDataFromQuery(String query, String[] selectionArgs) {
        SqliteResponse sqliteResponse = mSqliteResponseRetriever.getData(query, selectionArgs);
        if (sqliteResponse.isQuerySuccess()) {
            try {
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
                return new SqliteResponseData(columnCount, selectedTableColumnNames, valuesArray);
            } catch (Exception exception) {
                // sometimes cursor will not be null. when there are any constraints
                return new SqliteResponseData(exception);
            } finally {
                mSqliteDataRetriever.freeResources();
            }
        } else {
            mSqliteDataRetriever.freeResources();
            return new SqliteResponseData(sqliteResponse.getThrowable());
        }
    }

    private String getApplicationName(AppCompatActivity appCompatActivity) {
        ApplicationInfo applicationInfo = appCompatActivity.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : appCompatActivity.getString(stringId);
    }
}
