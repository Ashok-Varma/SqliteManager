package com.ashokvarma.sqlitemanager;

import android.content.Context;
import android.content.Intent;

/**
 * Class description
 *
 * @author ashokvarma
 * @version 1.0
 * @since 17 Jun 2017
 */
public class SqliteManager {

    static SqliteDataRetriever mSqliteDataRetriever;

    public static void launchSqliteManager(Context context, SqliteDataRetriever sqliteDataRetriever, String fileShareAuthority) {
        mSqliteDataRetriever = sqliteDataRetriever;
        Intent sqliteManagerIntent = new Intent(context, SqliteManagerActivity.class);
        if (fileShareAuthority != null && fileShareAuthority.trim().length() > 0) {
            sqliteManagerIntent.putExtra(SqliteManagerActivity.CSV_FILE_SHARE_AUTHORITY, fileShareAuthority);
        }
        context.startActivity(sqliteManagerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    static void clearReferences() {
        mSqliteDataRetriever = null;
    }


}
