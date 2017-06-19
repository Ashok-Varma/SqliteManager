package com.ashokvarma.sqlitemanager.sample;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
            SqliteManager.launchSqliteManager(this, sqliteDataRetriever);
        } else if (v.getId() == R.id.launch_sqlite_manager_room_button) {
            SqliteManager.launchSqliteManager(this, roomSqliteDataRetriever);
        } else if (v.getId() == R.id.clear_data_button) {
            sqliteHelper.clearTables();
            DatabaseInitializer.clearAsync(appDatabase);
        } else if (v.getId() == R.id.load_test_data_button) {
            sqliteHelper.populateTestData();
            DatabaseInitializer.populateAsync(appDatabase);
        }
    }

    private static class HelperSqliteDataRetriever implements SqliteDataRetriever {
        SqliteHelper mSqliteHelper;
        SQLiteDatabase mSQLiteDatabase;

        HelperSqliteDataRetriever(SqliteHelper sqliteHelper) {
            mSqliteHelper = sqliteHelper;
        }

        @Override
        public Cursor rawQuery(String query, String[] selectionArgs) {
            mSQLiteDatabase = mSqliteHelper.getWritableDatabase();
            return mSQLiteDatabase.rawQuery(query, selectionArgs);
        }

        @Override
        public String getDatabaseName() {
            return mSqliteHelper.getDatabaseName();
        }

        @Override
        public void freeResources() {
            if (mSQLiteDatabase != null) {
                if (mSQLiteDatabase.isOpen()) {
                    mSQLiteDatabase.close();
                }
                mSQLiteDatabase = null;
            }
        }
    }

    private static class RoomSqliteDataRetriever implements SqliteDataRetriever {
        SupportSQLiteOpenHelper mSqliteHelper;
        SupportSQLiteDatabase mSQLiteDatabase;

        RoomSqliteDataRetriever(SupportSQLiteOpenHelper sqliteHelper) {
            mSqliteHelper = sqliteHelper;
        }

        @Override
        public Cursor rawQuery(String query, String[] selectionArgs) {
            mSQLiteDatabase = mSqliteHelper.getWritableDatabase();
            return mSQLiteDatabase.query(query, selectionArgs);
        }

        @Override
        public String getDatabaseName() {
            return mSqliteHelper.getDatabaseName();
        }

        @Override
        public void freeResources() {
            /**
             * Shouldn't close db for room because Room also uses same instance of connection (which is created only once)
             *
             * if we close this :
             *
             * 1) None of the operations work with Room
             * 2) if you get the getWritableDatabase() it will return the same instance which we can't perform operations on because it was closed already
             * */


//            if (mSQLiteDatabase != null) {
//                if (mSQLiteDatabase.isOpen()) {
//                    try {
//                        mSQLiteDatabase.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                mSQLiteDatabase = null;
//            }
        }
    }
}
