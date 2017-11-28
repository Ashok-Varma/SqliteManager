package com.ashokvarma.sqlitemanager.sample;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.ashokvarma.sqlitemanager.SqliteDataRetriever;
import com.ashokvarma.sqlitemanager.SqliteManager;
import com.ashokvarma.sqlitemanager.sample.db.room.AppDatabase;
import com.ashokvarma.sqlitemanager.sample.db.room.utils.DatabaseInitializer;
import com.ashokvarma.sqlitemanager.sample.db.sqlite.SqliteHelper;

public class LauncherActivity extends AppCompatActivity implements View.OnClickListener {

    SqliteHelper sqliteHelper;
    SqliteDataRetriever sqliteDataRetriever;

    AppDatabase appDatabase;
    SqliteDataRetriever roomSqliteDataRetriever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        sqliteHelper = new SqliteHelper(LauncherActivity.this);
        appDatabase = AppDatabase.getDatabase(this);

        sqliteDataRetriever = new HelperSqliteDataRetriever(sqliteHelper);
        roomSqliteDataRetriever = new RoomSqliteDataRetriever(appDatabase.getOpenHelper());

        findViewById(R.id.load_test_data_button).setOnClickListener(this);
        findViewById(R.id.launch_sqlite_manager_button).setOnClickListener(this);
        findViewById(R.id.launch_sqlite_manager_room_button).setOnClickListener(this);
        findViewById(R.id.clear_data_button).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.launch_sqlite_manager_button) {
            SqliteManager.launchSqliteManager(this, sqliteDataRetriever, BuildConfig.APPLICATION_ID);
        } else if (v.getId() == R.id.launch_sqlite_manager_room_button) {
            SqliteManager.launchSqliteManager(this, roomSqliteDataRetriever, BuildConfig.APPLICATION_ID);
        } else if (v.getId() == R.id.clear_data_button) {
            sqliteHelper.clearTables();
            DatabaseInitializer.clearAsync(appDatabase);
        } else if (v.getId() == R.id.load_test_data_button) {
            sqliteHelper.populateTestData();
            DatabaseInitializer.populateAsync(appDatabase);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.launcher_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_github:
                String url = "https://github.com/Ashok-Varma/SqliteManager";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class HelperSqliteDataRetriever implements SqliteDataRetriever {
        SqliteHelper mSqliteHelper;
        SQLiteDatabase mSQLiteDatabase;

        HelperSqliteDataRetriever(SqliteHelper sqliteHelper) {
            mSqliteHelper = sqliteHelper;
            mSQLiteDatabase = mSqliteHelper.getWritableDatabase();
        }

        @Override
        public Cursor rawQuery(@NonNull String query, String[] selectionArgs) {
            if (mSQLiteDatabase == null || !mSQLiteDatabase.isOpen()) {
                mSQLiteDatabase = mSqliteHelper.getWritableDatabase();
            }
            return mSQLiteDatabase.rawQuery(query, selectionArgs);
        }

        @Override
        public String getDatabaseName() {
            return mSqliteHelper.getDatabaseName();
        }

        @Override
        public void freeResources() {
            // not good practice to open multiple database connections and close every time
        }
    }

    private static class RoomSqliteDataRetriever implements SqliteDataRetriever {
        SupportSQLiteOpenHelper mSqliteHelper;
        SupportSQLiteDatabase mSQLiteDatabase;

        RoomSqliteDataRetriever(SupportSQLiteOpenHelper sqliteHelper) {
            mSqliteHelper = sqliteHelper;
        }

        @Override
        public Cursor rawQuery(@NonNull String query, String[] selectionArgs) {
            mSQLiteDatabase = mSqliteHelper.getWritableDatabase();
            return mSQLiteDatabase.query(query, selectionArgs);
        }

        @Override
        public String getDatabaseName() {
            return mSqliteHelper.getDatabaseName();
        }

        @Override
        public void freeResources() {
            // not good practice to open multiple database connections and close every time
        }
    }
}
