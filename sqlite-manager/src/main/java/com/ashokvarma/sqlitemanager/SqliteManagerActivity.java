package com.ashokvarma.sqlitemanager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SqliteManagerActivity extends AppCompatActivity implements SqliteManagerView, AdapterView.OnItemSelectedListener, ColumnNameView.ColumnHeaderSortChangeListener, View.OnClickListener, TableRecyclerAdapter.Listener {

    private SqliteManagerPresenter mSqliteManagerPresenter;
    private TableRecyclerAdapter mTableRecyclerAdapter;

    private View mSqliteManagerParent;
    private Toolbar mToolbar;

    private AppCompatSpinner mTableSelectionSpinner;

    private View mTableSelectionContainer;
    private View mCustomQueryContainer;
    private TextView mCustomQueryText;
    private ImageView mCustomQueryEdit;
    private ImageView mCustomQueryClear;

    private View mErrorLayout;
    private TextView mErrorLayoutText;

    private View mTableLayout;
    private ColumnNameView mColumnNameView;
    private RecyclerView mTableLayoutRecyclerView;

    private FloatingActionButton mSqliteManagerAddFab;

    private View mActionCustomQuery;

    public static final String CSV_FILE_SHARE_AUTHORITY = "csv_file_share_authority";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_manager);

        mSqliteManagerPresenter = new SqliteManagerPresenter(SqliteManager.mSqliteDataRetriever, getIntent().getStringExtra(CSV_FILE_SHARE_AUTHORITY));

        mSqliteManagerParent = findViewById(R.id.sqlite_manager_parent);
        mTableSelectionSpinner = (AppCompatSpinner) findViewById(R.id.sqlite_manager_table_selection_spinner);
        mErrorLayout = findViewById(R.id.sqlite_manager_error_layout);
        mTableLayout = findViewById(R.id.sqlite_manager_table_layout);
        mColumnNameView = (ColumnNameView) findViewById(R.id.sqlite_manager_table_layout_header);
        mErrorLayoutText = (TextView) findViewById(R.id.sqlite_manager_error_layout_text);
        mTableLayoutRecyclerView = (RecyclerView) findViewById(R.id.sqlite_manager_table_layout_recycler_view);
        mActionCustomQuery = findViewById(R.id.sqlite_manager_action_custom_query);
        mSqliteManagerAddFab = (FloatingActionButton) findViewById(R.id.sqlite_manager_add_fab);

        mTableSelectionContainer = findViewById(R.id.sqlite_manager_table_selection_container);
        mCustomQueryContainer = findViewById(R.id.sqlite_manager_custom_query_container);
        mCustomQueryText = (TextView) findViewById(R.id.sqlite_manager_custom_query_text);
        mCustomQueryEdit = (ImageView) findViewById(R.id.sqlite_manager_custom_query_edit);
        mCustomQueryClear = (ImageView) findViewById(R.id.sqlite_manager_custom_query_clear);

        mToolbar = (Toolbar) findViewById(R.id.sqlite_manager_toolbar);
        setSupportActionBar(mToolbar);

        mTableRecyclerAdapter = new TableRecyclerAdapter(null);
        mTableRecyclerAdapter.setListener(this);
        mTableLayoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTableLayoutRecyclerView.setAdapter(mTableRecyclerAdapter);
        mTableSelectionSpinner.setOnItemSelectedListener(this);
        mColumnNameView.setColumnHeaderSortChangeListener(this);
        mActionCustomQuery.setOnClickListener(this);
        mSqliteManagerAddFab.setOnClickListener(this);
        mCustomQueryEdit.setOnClickListener(this);
        mCustomQueryClear.setOnClickListener(this);

        mSqliteManagerPresenter.bindView(this);
    }

    @Override
    protected void onDestroy() {
        mSqliteManagerPresenter.unBindView(this);
        super.onDestroy();
    }

    @Override
    public void setSubtitle(String subtitle) {
        mToolbar.setSubtitle(subtitle);
    }

    @NonNull
    @Override
    public AppCompatActivity getViewContext() {
        return this;
    }

    @Override
    public void finishActivityWithError(@StringRes int errorMessageId) {
        finishActivityWithError(getString(errorMessageId));
    }

    @Override
    public void finishActivityWithError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        finishActivity();
    }

    @Override
    public void finishActivity() {
        SqliteManager.clearReferences();
        finish();
    }

    @Override
    public void displayError(@StringRes int errorMessageId) {
        displayError(getString(errorMessageId));
    }

    @Override
    public void displayError(String error) {
        mErrorLayout.setVisibility(View.VISIBLE);
        mTableLayout.setVisibility(View.GONE);
        mErrorLayoutText.setText(error);
    }

    @Override
    public void informErrorToUser(@StringRes int errorMessageId) {
        informErrorToUser(getString(errorMessageId));
    }

    @Override
    public void informErrorToUser(String error) {
        Snackbar.make(mSqliteManagerParent, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setAddFABVisible(boolean visible) {
        if (visible) {
            mSqliteManagerAddFab.show();
        } else {
            mSqliteManagerAddFab.hide();
        }
    }

    @Override
    public void showCustomQueryView(String customQuery) {
        mTableSelectionContainer.setVisibility(View.GONE);
        mCustomQueryContainer.setVisibility(View.VISIBLE);

        mCustomQueryText.setText(customQuery);
    }

    @Override
    public void showTableSelectionView() {
        mTableSelectionContainer.setVisibility(View.VISIBLE);
        mCustomQueryContainer.setVisibility(View.GONE);
    }

    @Override
    public void showCustomQueryDialog(String previousCustomQuery) {
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.sqlite_manager_custom_query_dialog, null);

        final TextInputEditText customQueryEditText = (TextInputEditText) dialogView.findViewById(R.id.sqlite_manager_custom_query_edit_text);
        customQueryEditText.setText(previousCustomQuery);

        new AlertDialog
                .Builder(this)
                .setView(dialogView)
                .setTitle(R.string.sqlite_manager_custom_query)
                .setMessage(R.string.sqlite_manager_custom_query_hint)
                .setPositiveButton(R.string.sqlite_manager_query, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mSqliteManagerPresenter.onCustomQuerySubmitted(customQueryEditText.getText().toString());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.sqlite_manager_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void showAddEditRowDialog(final boolean isEdit, final String tableName, final String[] tableColumnNames, final SparseArray<String> oldColumnValues) {
        LayoutInflater inflater = this.getLayoutInflater();
        final ArrayList<TextInputEditText> editTextViews = new ArrayList<>(tableColumnNames.length);

        final View dialogView = inflater.inflate(R.layout.sqlite_manager_add_edit_dialog_container, null);

        LinearLayout linearLayout = (LinearLayout) dialogView.findViewById(R.id.sqlite_manager_add_edit_dialog_container);
        Button deleteButton = (Button) dialogView.findViewById(R.id.sqlite_manager_add_edit_dialog_delete);
        Button cancelButton = (Button) dialogView.findViewById(R.id.sqlite_manager_add_edit_dialog_cancel);
        Button updateButton = (Button) dialogView.findViewById(R.id.sqlite_manager_add_edit_dialog_update);

        int index = 0;
        for (String currentColumnName : tableColumnNames) {
            View columnView = inflater.inflate(R.layout.sqlite_manager_add_edit_dialog_item, null);
            ((TextInputLayout) columnView.findViewById(R.id.sqlite_manager_add_edit_dialog_text_input_layout)).setHint(currentColumnName);
            TextInputEditText currentInputEditText = (TextInputEditText) columnView.findViewById(R.id.sqlite_manager_add_edit_dialog_edit_text);
            editTextViews.add(currentInputEditText);
            linearLayout.addView(columnView);
            if (oldColumnValues != null) {
                currentInputEditText.setText(oldColumnValues.get(index));
            }
            index++;
        }

        if (!isEdit) {
            // default params are fixed to edit case. So, need to change them only in add mode
            deleteButton.setVisibility(View.GONE);
            updateButton.setText(R.string.sqlite_manager_add);
        }

        final AlertDialog alertDialog
                = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle(isEdit ? R.string.sqlite_manager_update_row_dialog_title : R.string.sqlite_manager_add_row_dialog_title)
                .setMessage(getString(isEdit ? R.string.sqlite_manager_update_row_dialog_message : R.string.sqlite_manager_add_row_dialog_message, tableName))
                .create();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<String> columnValues = new ArrayList<>(tableColumnNames.length);
                for (TextInputEditText currentEditText : editTextViews) {
                    columnValues.add(currentEditText.getText().toString());
                }
                if (isEdit) {
                    mSqliteManagerPresenter.updateRow(tableName, tableColumnNames, oldColumnValues, columnValues);
                } else {
                    mSqliteManagerPresenter.addRow(tableName, tableColumnNames, columnValues);
                }

                alertDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSqliteManagerPresenter.deleteRow(tableName, tableColumnNames, oldColumnValues);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public void setSpinnerAdapter(ArrayList<String> tableNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tableNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTableSelectionSpinner.setAdapter(adapter);
    }

    @Override
    public void showContentView() {
        mErrorLayout.setVisibility(View.GONE);
        mTableLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateColumnNames(String[] columnNames) {
        mColumnNameView.setData(columnNames);
    }

    @Override
    public void displayRows(List<SparseArray<String>> columnIndexToValuesArray) {
        mTableRecyclerAdapter.setData(columnIndexToValuesArray);
    }

    @Override
    public void onHeaderColumnSortChanged(String columnName, boolean isAscendingOrder) {
        String selectedTableName = mTableSelectionSpinner.getSelectedItem().toString();
        mSqliteManagerPresenter.onSortChangedOrderChanged(selectedTableName, columnName, isAscendingOrder);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedTableName = mTableSelectionSpinner.getSelectedItem().toString();
        mSqliteManagerPresenter.fetchTableData(selectedTableName);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sqlite_manager_action_custom_query) {
            mSqliteManagerPresenter.onCustomQueryClicked();
        } else if (v.getId() == R.id.sqlite_manager_add_fab) {
            String selectedTableName = mTableSelectionSpinner.getSelectedItem().toString();
            mSqliteManagerPresenter.onAddFabClicked(selectedTableName, mColumnNameView.getTableColumnNames());
        } else if (v.getId() == R.id.sqlite_manager_custom_query_edit) {
            mSqliteManagerPresenter.onCustomQueryClicked();
        } else if (v.getId() == R.id.sqlite_manager_custom_query_clear) {
            String selectedTableName = mTableSelectionSpinner.getSelectedItem().toString();
            mSqliteManagerPresenter.fetchTableData(selectedTableName);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sqlite_manager_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_query_refresh) {
            String selectedTableName = mTableSelectionSpinner.getSelectedItem().toString();
            mSqliteManagerPresenter.onRefreshClicked(selectedTableName);
            return true;
        }
        if (item.getItemId() == R.id.action_export_result_as_csv) {
            String selectedTableName = mTableSelectionSpinner.getSelectedItem().toString();
            mSqliteManagerPresenter.onExportResultAsCSVClicked(selectedTableName);
            return true;
        }
        if (item.getItemId() == R.id.action_export_result_as_json) {
            mSqliteManagerPresenter.onExportResultAsJsonClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onColumnValueClicked(SparseArray<String> columnValues) {
        String selectedTableName = mTableSelectionSpinner.getSelectedItem().toString();
        mSqliteManagerPresenter.onColumnValueClicked(selectedTableName, mColumnNameView.getTableColumnNames(), columnValues);
    }
}
